package codes.service;

import codes.controller.GameModeEnum;
import codes.dao.UserDao;
import codes.entity.*;
import codes.exception.InvalidStarsException;
import codes.exception.UserExistsException;
import codes.exception.UserNotChangedException;
import codes.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService underTest;

    @Mock
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userDao);
    }

    @Test
    void createUser() {
        //Given
        String username = "rouch"+ UUID.randomUUID();
        UserRequestBody userRequestBody = new UserRequestBody(
                username,
                List.of(new GameModeRequestBody(GameModeEnum.HARD, Duration.ofMinutes(3))),
                new ReviewRequestBody("tres bien",4)
        );
       Mockito.when(userDao.isUsernameExists(username)).thenReturn(false);
        //When
       underTest.createUser(userRequestBody);
        //Then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userDao,Mockito.times(1)).insertUser(userArgumentCaptor.capture());
        User userCaptured = userArgumentCaptor.getValue();

        //convertir les valeurs typés requestbody en valeur typés user
        List<UserGameMode> convertedUserRequestBodyGameList = userRequestBody.gameModes().stream().map(element -> new UserGameMode(element.gameLevelPlayed(),element.elapsedTimeForThisLevel())).toList();
        Review convertedUserRequestBodyReview = new Review(userRequestBody.review().message(),userRequestBody.review().givenStars());

        assertThat(userCaptured.getId()).isNull();
        assertThat(userCaptured.getUsername()).isEqualTo(userRequestBody.username());
        assertThat(userCaptured.getGameModes()).isEqualTo(convertedUserRequestBodyGameList);
        assertThat(userCaptured.getReview()).isEqualTo(convertedUserRequestBodyReview);
    }

    @Test
    void createUser_ThrowsException_UserExistsException() {
        //Given
        String username = "rouch"+ UUID.randomUUID();
        UserRequestBody userRequestBody = new UserRequestBody(
                username,
                List.of(new GameModeRequestBody(GameModeEnum.HARD, Duration.ofMinutes(3))),
                new ReviewRequestBody("tres bien",4)
        );
        Mockito.when(userDao.isUsernameExists(username)).thenReturn(true);
        //When
        //Then
        assertThatThrownBy(()->underTest.createUser(userRequestBody))
                .isInstanceOf(UserExistsException.class)
                .hasMessage("This username already exists");
    }

    @Test
    void createUser_ThrowsException_InvalidStarsException() {
        //Given
        String username = "rouch"+ UUID.randomUUID();
        UserRequestBody userRequestBody = new UserRequestBody(
                username,
                List.of(new GameModeRequestBody(GameModeEnum.HARD, Duration.ofMinutes(3))),
                new ReviewRequestBody("tres bien",8)
        );
        Mockito.when(userDao.isUsernameExists(username)).thenReturn(false);
        //When
        //Then
        assertThatThrownBy(()->underTest.createUser(userRequestBody))
                .isInstanceOf(InvalidStarsException.class)
                .hasMessage("[%s] is outside the stars range".formatted(userRequestBody.review().givenStars()));
    }

    @Test
    void removeUserById() {
        //Given
        int id = 10;
        Mockito.when(userDao.isIdExists(id)).thenReturn(true);
        //When
        underTest.removeUserById(id);
        //Then
        Mockito.verify(userDao,Mockito.times(1)).deleteUser(id);
    }

    @Test
    void removeUserById_Throws_UserNotFoundException() {
        //Given
        int id = -1;
        Mockito.when(userDao.isIdExists(id)).thenReturn(false);
        //When
        //Then
        assertThatThrownBy(()->underTest.removeUserById(id))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("user with id [%s] not found".formatted(id));
    }

    @Test
    void selectAllUsers() {
        //Given: no need, methode prend rien parametre
        //When
        underTest.selectAllUsers();
        //Then
        Mockito.verify(userDao,Mockito.times(1)).getAllUsers();
    }

    @Test
    void selectUserById() {
        //Given
        int id = 10;
        String username = "rouch"+ UUID.randomUUID();
        User user = new User(
                id,
                username,
                List.of(new UserGameMode(GameModeEnum.HARD, Duration.ofMinutes(3))),
                new Review("tres bien",4),
                LocalDateTime.now()
        );
        Mockito.when(userDao.getUserById(id)).thenReturn(Optional.of(user));
        //When
        var result = underTest.selectUserById(id);
        //Then
        assertThat(result).isEqualTo(user);
        Mockito.verify(userDao,Mockito.times(1)).getUserById(id);
    }

    @Test
    void selectUserByIdThrowsException() {
        //Given
        int id = -1;
        Mockito.when(userDao.getUserById(id)).thenReturn(Optional.empty());
        //When

        //Then
        assertThatThrownBy(()-> underTest.selectUserById(id))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("user with id [%s] not found".formatted(id));
    }

    @Test
    void changeUser_Only_Username_Changed() {
        //Given
        int id = 10;
        String username = "rouch"+ UUID.randomUUID();
        User user = new User(
                id,
                username,
                List.of(new UserGameMode(GameModeEnum.HARD, Duration.ofMinutes(3))),
                new Review("tres bien",4),
                LocalDateTime.now()
        );
        Mockito.when(userDao.getUserById(id)).thenReturn(Optional.of(user));

        UserRequestBody userRequestBody = new UserRequestBody(username+"adiss",null,null);
        Mockito.when(userDao.isUsernameExists(userRequestBody.username())).thenReturn(false);
        //When
        underTest.changeUser(userRequestBody,id);
        //Then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userDao,Mockito.times(1)).updateUser(userArgumentCaptor.capture());
        User userInserer = userArgumentCaptor.getValue();

        //le resultat est evalué selon le fait seul le username changera et prendra celui du requesBody
        //mais les autres conserveront la valeur du user initial.
        assertThat(userInserer.getId()).isNotNull();
        assertThat(userInserer.getUsername()).isEqualTo(userRequestBody.username());//Only here
        assertThat(userInserer.getGameModes()).isEqualTo(user.getGameModes());
        assertThat(userInserer.getReview()).isEqualTo(user.getReview());


    }

    @Test
    void changeUser_User_Username_AlreadyExists_Throws_UserNotChangedException() {
        //Given
        int id = 10;
        String username = "rouch"+ UUID.randomUUID();
        User user = new User(
                id,
                username,
                List.of(new UserGameMode(GameModeEnum.HARD, Duration.ofMinutes(3))),
                new Review("tres bien",4),
                LocalDateTime.now()
        );
        Mockito.when(userDao.getUserById(id)).thenReturn(Optional.of(user));

        UserRequestBody userRequestBody = new UserRequestBody(username+"adiss",null,null);
        Mockito.when(userDao.isUsernameExists(userRequestBody.username())).thenReturn(true);
        //When
        //Then
        assertThatThrownBy(()->underTest.changeUser(userRequestBody,id))
                .isInstanceOf(UserExistsException.class)
                .hasMessage("This username already exists");

    }

//ici j'ai pas pu faire List.of pour intialiser une liste de user game mode car quand il doit faire clear dans mon
//servie, il marche pas. C'est une liste immutable, inchangeable après l'initialisation.
    @Test
    void changeUser_Only_GameModesList_Changed() {
        //Given
        int id = 10;
        String username = "rouch"+ UUID.randomUUID();
        List<UserGameMode> list = new ArrayList<>();
        list.add(new UserGameMode(GameModeEnum.HARD, Duration.ofMinutes(3)));
        User user = new User(
                id,
                username,
                list,
                new Review("tres bien",4),
                LocalDateTime.now()
        );
        Mockito.when(userDao.getUserById(id)).thenReturn(Optional.of(user));
        UserRequestBody userRequestBody = new UserRequestBody(null, List.of(new GameModeRequestBody(GameModeEnum.MEDIUM, Duration.ofMinutes(4))), null);

        //When
         underTest.changeUser(userRequestBody,id);
        //Then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userDao,Mockito.times(1)).updateUser(userArgumentCaptor.capture());
        User userAInserer = userArgumentCaptor.getValue();

        //le resultat est evalué selon le fait seul le GameModesList changera et prendra celui du requesBody
        //mais les autres conserveront la valeur du user initial.
        assertThat(userAInserer.getId()).isNotNull();
        assertThat(userAInserer.getUsername()).isEqualTo(user.getUsername());

        //conversion List<GameModeRequestBody> en List<UserGameMode> pour la comparaison
        List<UserGameMode> listeGameModesEntrant = userRequestBody.gameModes().stream().map(element -> new UserGameMode(element.gameLevelPlayed(),element.elapsedTimeForThisLevel())).toList();
        assertThat(userAInserer.getGameModes()).isEqualTo(listeGameModesEntrant);
        assertThat(userAInserer.getReview()).isEqualTo(user.getReview());
    }

    @Test
    void changeUser_Only_Review_Changed() {
        //Given
        int id = 10;
        String username = "rouch"+ UUID.randomUUID();
        User user = new User(
                id,
                username,
                List.of(new UserGameMode(GameModeEnum.HARD, Duration.ofMinutes(3))),
                new Review("tres bien",4),
                LocalDateTime.now()
        );
        Mockito.when(userDao.getUserById(id)).thenReturn(Optional.of(user));

        UserRequestBody userRequestBody = new UserRequestBody(null,null,new ReviewRequestBody("new message",5));
        //When
        underTest.changeUser(userRequestBody,id);
        //Then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userDao,Mockito.times(1)).updateUser(userArgumentCaptor.capture());
        User userInserer = userArgumentCaptor.getValue();

        //le resultat est evalué selon le fait seul le review changera et prendra celui du requesBody
        //mais les autres conserveront la valeur du user initial.
        assertThat(userInserer.getId()).isNotNull();
        assertThat(userInserer.getUsername()).isEqualTo(user.getUsername());
        assertThat(userInserer.getGameModes()).isEqualTo(user.getGameModes());
        //conversion ReviewRequestBody en Review pour la comparaison
        Review review = new Review(userRequestBody.review().message(),userRequestBody.review().givenStars());
        assertThat(userInserer.getReview()).isEqualTo(review);//Only here
    }

    @Test
    void changeUser_Only_Review_Changed_Throws_InvalidStarsException() {
        //Given
        int id = 10;
        String username = "rouch"+ UUID.randomUUID();
        User user = new User(
                id,
                username,
                List.of(new UserGameMode(GameModeEnum.HARD, Duration.ofMinutes(3))),
                new Review("tres bien",4),
                LocalDateTime.now()
        );
        Mockito.when(userDao.getUserById(id)).thenReturn(Optional.of(user));
        UserRequestBody userRequestBody = new UserRequestBody(null,null,new ReviewRequestBody("new message",8));
        //conversion ReviewRequestBody en Review pour la comparaison
        Review review = new Review(userRequestBody.review().message(),userRequestBody.review().givenStars());
        //When
        //Then
        assertThatThrownBy(()-> underTest.changeUser(userRequestBody,id))
                .isInstanceOf(InvalidStarsException.class)
                .hasMessage("[%s] is outside the stars range".formatted(review.getGivenStars()));
    }

    @Test
    void changeUser_Nothing_Changed() {
        //Given
        int id = 10;
        String username = "rouch"+ UUID.randomUUID();
        User user = new User(
                id,
                username,
                List.of(new UserGameMode(GameModeEnum.HARD, Duration.ofMinutes(3))),
                new Review("tres bien",4),
                LocalDateTime.now()
        );
        Mockito.when(userDao.getUserById(id)).thenReturn(Optional.of(user));
        UserRequestBody userRequestBody = new UserRequestBody(username,List.of(new GameModeRequestBody(GameModeEnum.HARD, Duration.ofMinutes(3))),new ReviewRequestBody("tres bien",4));
        //When
        //Then
        assertThatThrownBy(()-> underTest.changeUser(userRequestBody,id))
                .isInstanceOf(UserNotChangedException.class)
                .hasMessage("Nothing has been changed");
    }

    @Test
    void changeUser_All_Things_Changed() {
        //Given
        int id = 10;
        String username = "rouch"+ UUID.randomUUID();
        List<UserGameMode> list = new ArrayList<>();
        list.add(new UserGameMode(GameModeEnum.HARD, Duration.ofMinutes(3)));
        User user = new User(
                id,
                username,
                list,
                new Review("tres bien",4),
                LocalDateTime.now()
        );
        Mockito.when(userDao.getUserById(id)).thenReturn(Optional.of(user));
        UserRequestBody userRequestBody = new UserRequestBody(username+"adissa", List.of(new GameModeRequestBody(GameModeEnum.MEDIUM, Duration.ofMinutes(4))), new ReviewRequestBody("nouveau message",1));

        //When
        underTest.changeUser(userRequestBody,id);
        //Then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userDao,Mockito.times(1)).updateUser(userArgumentCaptor.capture());
        User userAInserer = userArgumentCaptor.getValue();

        //le resultat est evalué selon le fait seul le GameModesList changera et prendra celui du requesBody
        //mais les autres conserveront la valeur du user initial.
        assertThat(userAInserer.getId()).isNotNull();
        assertThat(userAInserer.getUsername()).isEqualTo(userRequestBody.username());

        //conversion List<GameModeRequestBody> en List<UserGameMode> pour la comparaison
        List<UserGameMode> listeGameModesEntrant = userRequestBody.gameModes().stream().map(element -> new UserGameMode(element.gameLevelPlayed(),element.elapsedTimeForThisLevel())).toList();
        assertThat(userAInserer.getGameModes()).isEqualTo(listeGameModesEntrant);

        //conversion ReviewRequestBody en Review pour la comparaison
        Review review = new Review(userRequestBody.review().message(),userRequestBody.review().givenStars());
        assertThat(userAInserer.getReview()).isEqualTo(review);
    }
}