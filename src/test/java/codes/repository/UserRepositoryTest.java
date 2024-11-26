package codes.repository;

import codes.AbstractTestcontainersConfiguration;
import codes.controller.GameModeEnum;
import codes.entity.Review;
import codes.entity.User;
import codes.entity.UserGameMode;
import codes.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest extends AbstractTestcontainersConfiguration {

    @Autowired
    private UserRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsUserByUsername() {
        //Given
        String username = faker.name().username()+ "rouch"+ UUID.randomUUID();
        User user = new User(
                username,
                 List.of(new UserGameMode(GameModeEnum.HARD, Duration.ofMinutes(3))),
                 new Review("tres bien",4)
                );
        underTest.save(user);
        //When
        var result = underTest.existsUserByUsername(username);
        //Then
        assertThat(result).isTrue();
    }

    @Test
    void existsUserByUsernameFailsWhenUsernameDoesNotExistsInDb() {
        //Given
        String username = faker.name().username()+ "rouch"+ UUID.randomUUID();
        //When
        var result = underTest.existsUserByUsername(username);
        //Then
        assertThat(result).isFalse();
    }


    @Test
    void existsUserById() {
        //Given
        String username = faker.name().username()+ "rouch"+ UUID.randomUUID();
        User user = new User(
                username,
                List.of(new UserGameMode(GameModeEnum.HARD, Duration.ofMinutes(3))),
                new Review("tres bien",4)
        );
        underTest.save(user);
        //When
        Integer recupId = underTest.findAll()
                .stream()
                .filter(u-> u.getUsername().equals(username))
                .map(User::getId)
                .findFirst()
                .orElseThrow(()-> new UserNotFoundException("id do not exist"));
        var result = underTest.existsUserById(recupId);
        //Then
        assertThat(result).isTrue();
    }

    @Test
    void existsUserByIdFailsWhenIdNotExist() {
        //Given
        int id = -1;
        //When
        var result = underTest.existsUserById(id);
        //Then
        assertThat(result).isFalse();
    }


}