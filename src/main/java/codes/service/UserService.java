package codes.service;

import codes.dao.UserDao;
import codes.entity.Opinion;
import codes.entity.User;
import codes.entity.UserRequestBody;
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
         //Chef if user email exists
        if(userDao.isEmailExists(userRequestBody.email())){
            throw new UserExistsException("This email already exists");
        }
        User user = new User(userRequestBody.username(),userRequestBody.email(),new Opinion(userRequestBody.opinion().message(),userRequestBody.opinion().givenStars()));
        userDao.insertUser(user);
    }

    public void removeUserById(int userId){
        //check if userId exists
        if(!userDao.isIdExists(userId)){
            throw new UserNotFoundException("user with id [%s] not found".formatted(userId));
        }
        userDao.deleteUser(userId);
    }

    public List<User> selectAllUsers(){
        return userDao.getAllUsers();
    }

    public User selectUserById(int userId){
        return userDao.getUserById(userId).orElseThrow(()->new UserNotFoundException("user with id [%s] not found".formatted(userId)));
    }

    public void changeUser(UserRequestBody userRequestBody, int userId){
        User user = selectUserById(userId);
        boolean changement = false;

        if(userRequestBody.username()!=null && !userRequestBody.username().equals(user.getUsername())){
            user.setUsername(userRequestBody.username());
            changement = true;
        }
        else if(userRequestBody.email()!=null && !userRequestBody.email().equals(user.getEmail())){
            if(userDao.isEmailExists(userRequestBody.email())){
                throw new UserExistsException("This email already exists");
            }
            user.setEmail(userRequestBody.email());
            changement = true;
        }
        else if(userRequestBody.opinion()!=null){
            if (!userRequestBody.opinion().message().equals(user.getOpinion().getMessage())){
                user.setOpinion(new Opinion(userRequestBody.opinion().message(),user.getOpinion().getGivenStars()));
                changement = true;
            }
            if (userRequestBody.opinion().givenStars()!=user.getOpinion().getGivenStars()){
                user.setOpinion(new Opinion(user.getOpinion().getMessage(),userRequestBody.opinion().givenStars()));
                changement = true;
            }
        }

        if(!changement){
            throw new UserNotChangedException("Nothing has been changed");
        }
        userDao.insertUser(user);
    }
}
