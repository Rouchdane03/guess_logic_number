package codes.service;

import codes.dao.UserDao;
import codes.entity.Review;
import codes.entity.User;
import codes.entity.UserGameMode;
import codes.entity.UserRequestBody;
import codes.exception.InvalidStarsException;
import codes.exception.UserExistsException;
import codes.exception.UserNotChangedException;
import codes.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserDao userDao;

    public UserService(UserDao userDao){
        this.userDao = userDao;
    }

    public void createUser(UserRequestBody userRequestBody){
         //Chef if user username exists
        if(userDao.isUsernameExists(userRequestBody.username())){
            throw new UserExistsException("This username already exists");
        }
        if (userRequestBody.review().givenStars()>5 || userRequestBody.review().givenStars()<1){
            throw new InvalidStarsException("[%s] is outside the stars range".formatted(userRequestBody.review().givenStars()));
        }

        User user = new User(
                userRequestBody.username(),
                userRequestBody.gameModes().stream().map(element -> new UserGameMode(element.gameLevelPlayed(),element.elapsedTimeForThisLevel())).toList(),
                new Review(userRequestBody.review().message(), userRequestBody.review().givenStars())
        );
        userDao.insertUser(user);
    }

    public void removeUserById(Integer userId){
        //check if userId exists
        if(!userDao.isIdExists(userId)){
            throw new UserNotFoundException("user with id [%s] not found".formatted(userId));
        }
        userDao.deleteUser(userId);
    }

    public List<User> selectAllUsers(){
        return userDao.getAllUsers();
    }

    public User selectUserById(Integer userId){
        return userDao.getUserById(userId).orElseThrow(()->new UserNotFoundException("user with id [%s] not found".formatted(userId)));
    }

    //The big process
   // @Transactional no need
    public void changeUser(UserRequestBody userRequestBody, Integer userId){

        User user = selectUserById(userId);
        boolean changement = false;

        // ❗️⁉️FIRST IF BLOCK FOR USERNAME
        if(userRequestBody.username()!=null && !userRequestBody.username().equals(user.getUsername())){
            if(userDao.isUsernameExists(userRequestBody.username())){
                throw new UserExistsException("This username already exists");
            }
            user.setUsername(userRequestBody.username());
            changement = true;
        }

        // ❗️⁉️SECOND IF BLOCK FOR GAME MODES
        // vérifier d'abord qu'on veut pas affecté null au gameModes du user
        if(userRequestBody.gameModes()!=null){

             //conversion List<GameModeRequestBody> en List<UserGameMode> pour la comparaison
            List<UserGameMode> listeGameModesEntrant = userRequestBody.gameModes().stream().map(element -> new UserGameMode(element.gameLevelPlayed(),element.elapsedTimeForThisLevel())).toList();

            /*
            y'a une subtilité pour la suite car le set est censé supprimé aussi
             dans la base mais il va pas le faire car je pointe une opération directement sur l'enfant
            EN fait les anciens valeurs qui étaient associées à la table seront pas supprimées
            car pour le faire, il faut qu'on supprime le parent,mais nous on veut pas ça
            sinon qu'avec ça suis censé avoir des doublons dans ma table. parce qu'après tout le update
            fais un insert à la fin et dans ce cas il va rajouter d'autres lignes à la table game_modes
            ce faisant y'aura des doublons.
            Comprarer listeGameModesEntrant avec celui existant déjà à l'id spécifié
            */
            if (!listeGameModesEntrant.equals(user.getGameModes())){
                user.getGameModes().clear();
                user.getGameModes().addAll(listeGameModesEntrant);
                //user.setGameModes(listeGameModesEntrant); //hibernate comprends pas ceci
                changement = true;
            }
        }

        // ❗️⁉️THIRD IF BLOCK FOR USER REVIEW
        // vérifier d'abord qu'on veut pas affecté null au review du user
        if(userRequestBody.review()!=null){
            /*
            le user pourra modifer son message et son nbr d'étoiles tant qu'il veut
            Il faut covertir ReviewRequestBody en Review pour la comparaison
            */
            Review reviewEntrant = new Review(userRequestBody.review().message(),userRequestBody.review().givenStars());

            //Comprarer reviewEntrant avec celui existant déjà à l'id spécifié
            if (!reviewEntrant.equals(user.getReview())){
                if (reviewEntrant.getGivenStars()>5 || reviewEntrant.getGivenStars()<1)
                    throw new InvalidStarsException("[%s] is outside the stars range".formatted(reviewEntrant.getGivenStars()));
                user.setReview(reviewEntrant);
                changement = true;
            }
        }

        if(!changement){
            throw new UserNotChangedException("Nothing has been changed");
        }
        userDao.updateUser(user);
    }
}
