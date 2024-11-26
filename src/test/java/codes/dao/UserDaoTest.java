package codes.dao;

import codes.controller.GameModeEnum;
import codes.entity.Review;
import codes.entity.User;
import codes.entity.UserGameMode;
import codes.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
public class UserDaoTest {

    private UserDao underTest;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        underTest = new UserDao(userRepository);
    }

    @Test
    void insertUser() {
        //Given
        String username = "rouch"+ UUID.randomUUID();
        User user = new User(
                username,
                List.of(new UserGameMode(GameModeEnum.HARD, Duration.ofMinutes(3))),
                new Review("tres bien",4)
        );
        //When
        underTest.insertUser(user);
        //Then
        Mockito.verify(userRepository,Mockito.times(1)).save(user);
    }

    @Test
    void getUserById() {
        //Given
        Integer id = 1;
        //When
        underTest.getUserById(id);
        //Then
        Mockito.verify(userRepository,Mockito.times(1)).findById(id);
    }

    @Test
    void getAllUsers() {
        //➡️On a juste à vérifier que les methodes ont éte invoquées
        //Given : ❌pas besoin car la méthode ne prend rien en paramètre
        //When
        underTest.getAllUsers();
        //Then
        Mockito.verify(userRepository,Mockito.times(1)).findAll();
    }

    @Test
    void updateUser() {
        //Given
        String username = "rouch"+ UUID.randomUUID();
        User userUpdated = new User(
                username,
                List.of(new UserGameMode(GameModeEnum.HARD, Duration.ofMinutes(3))),
                new Review("tres bien",4)
        );
        //When
        underTest.insertUser(userUpdated);
        //Then
        Mockito.verify(userRepository,Mockito.times(1)).save(userUpdated);
    }

    @Test
    void deleteUser() {
        //Given
        Integer id = 1;
        //When
       underTest.deleteUser(id);
        //Then
        Mockito.verify(userRepository,Mockito.times(1)).deleteById(id);
    }

    @Test
    void isIdExists() {
        //Given
        Integer id = 1;
        //When
       underTest.isIdExists(id);
        //Then
        Mockito.verify(userRepository,Mockito.times(1)).existsUserById(id);
    }

    @Test
    void isUsernameExists() {
        //Given
        String username = "rouch"+ UUID.randomUUID();
        //When
        underTest.isUsernameExists(username);
        //Then
        Mockito.verify(userRepository,Mockito.times(1)).existsUserByUsername(username);
    }
}