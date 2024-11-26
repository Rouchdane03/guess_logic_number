package codes.controller;

import codes.entity.*;
import codes.exception.UserNotFoundException;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();

    private static final String userURI = "api/v1/user";

    @Test
    void addUser() {
        //⬇️the journey of adding a user of our controller class

        //Create a UserRequestBody instance
        Faker faker = new Faker();
        String username = faker.name().username()+ UUID.randomUUID();
        int givenStars = RANDOM.nextInt(1,6);
        UserRequestBody userRequestBody = new UserRequestBody(username,
                List.of(new GameModeRequestBody(GameModeEnum.HARD, Duration.ofMinutes(3))),
                new ReviewRequestBody("un message",givenStars)
        );

        //sending a post request to our api
        webTestClient.post()
                .uri(userURI+"/send")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userRequestBody),UserRequestBody.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all users to ckeck if the user we've sent is there
        List<User> allUsers = webTestClient.get()
                .uri(userURI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<User>() {
                })
                .returnResult()
                .getResponseBody();  //Faut nuancer ici c'est ResponsesBody et non RequestBody

                // now let's make sure the user we've sent is the list
                // but before let's convert the userRequestBody sent to a variable which has User type
        List<UserGameMode> listConverted = userRequestBody.gameModes().stream().map(uR->new UserGameMode(uR.gameLevelPlayed(),uR.elapsedTimeForThisLevel())).toList();
        Review reviewConverted = new Review(userRequestBody.review().message(),userRequestBody.review().givenStars());

        User expectedUser = new User(username,listConverted,reviewConverted); //ici on j'ai pris le constructeur de sans id
        //VERIFICATION
        assertThat(allUsers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id","passedAt","gameModes.id","review.id") //important car mon expectedUser n'est pas crée avec le full constructeur
                .contains(expectedUser);

        //recupation des ids nécessaire
        //--------->pour le user
        Integer idUser = allUsers
                .stream()
                .filter(u->u.getUsername().equals(username))
                .map(User::getId)
                .findFirst()
                .orElseThrow(()->new UserNotFoundException("id non trouvé"));

        //--------->pour les ids des gameModes
        List<UserGameMode> updatedListIncludingId = allUsers
                .stream()
                .filter(u->u.getUsername().equals(username))
                .map(User::getGameModes)
                .findAny()
                .orElseThrow(()->new UserNotFoundException("aucun gameModes dans ce non trouvé"));

        //----------> pour l'id du review
        Review updatedReviewIncludingId = allUsers
                .stream()
                .filter(u->u.getUsername().equals(username))
                .map(User::getReview)
                .findAny()
                .orElseThrow(()->new UserNotFoundException("aucun review de ce genre na ete trouvé"));

        //----------> pour le passedAt
        LocalDateTime passedAt = allUsers
                .stream()
                .filter(u->u.getUsername().equals(username))
                .map(User::getPassedAt)
                .findAny()
                .orElseThrow(()->new UserNotFoundException("aucun localDateTime trouvé"));;

        //important pour la requete suivante qui lui va renvoyer l'id
        expectedUser.setId(idUser);
        expectedUser.setGameModes(updatedListIncludingId);
        expectedUser.setReview(updatedReviewIncludingId);
        expectedUser.setPassedAt(passedAt);

        //getUser by this id
        webTestClient.get()
                .uri(userURI+"/{id}",idUser)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<User>() {})
                .isEqualTo(expectedUser);

    }

    @Test
    void deleteUserById() {
        //⬇️the journey of deleting a user of our controller class

        //Create a UserRequestBody instance
        Faker faker = new Faker();
        String username = faker.name().username()+ UUID.randomUUID();
        int givenStars = RANDOM.nextInt(1,6);
        UserRequestBody userRequestBody = new UserRequestBody(username,
                List.of(new GameModeRequestBody(GameModeEnum.HARD, Duration.ofMinutes(3))),
                new ReviewRequestBody("un message",givenStars)
        );

        //sending a post request to our api
        webTestClient.post()
                .uri(userURI+"/send")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userRequestBody),UserRequestBody.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all users to ckeck to get the id of the user we've sent
        List<User> allUsers = webTestClient.get()
                .uri(userURI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<User>() {
                })
                .returnResult()
                .getResponseBody();  //Faut nuancer ici c'est ResponsesBody et non RequestBody

        //recupation des ids nécessaire
        //--------->pour le user, utile pour la suite, c'est sur ce id qu'on va perform notre supression
        Integer idUser = allUsers
                .stream()
                .filter(u->u.getUsername().equals(username))
                .map(User::getId)
                .findFirst()
                .orElseThrow(()->new UserNotFoundException("id non trouvé"));


        //📌deleting our user
        webTestClient.delete()
                .uri(userURI+"/{id}",idUser)
                .exchange()
                .expectStatus()
                .isOk();

        //essayons ensuite de recuperer le user pour s'assurer qu'il a bien été supprimé
        //getUser by this id
        webTestClient.get()
                .uri(userURI+"/{id}",idUser)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();

    }

    @Test
    void updateUserById() {

        //⬇️the journey of updating a user of our controller class

        //Create a UserRequestBody instance
        Faker faker = new Faker();
        String username = faker.name().username()+ UUID.randomUUID();
        int givenStars = RANDOM.nextInt(1,6);
        UserRequestBody userRequestBody = new UserRequestBody(username,
                List.of(new GameModeRequestBody(GameModeEnum.HARD, Duration.ofMinutes(3))),
                new ReviewRequestBody("un message",givenStars)
        );

        //sending a post request to our api
        webTestClient.post()
                .uri(userURI+"/send")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userRequestBody),UserRequestBody.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all users to ckeck if the user we've sent is there
        List<User> allUsers = webTestClient.get()
                .uri(userURI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<User>() {
                })
                .returnResult()
                .getResponseBody();  //Faut nuancer ici c'est ResponsesBody et non RequestBody

        //recupation des ids nécessaire
        //--------->pour le user
        Integer idUser = allUsers
                .stream()
                .filter(u->u.getUsername().equals(username))
                .map(User::getId)
                .findFirst()
                .orElseThrow(()->new UserNotFoundException("id non trouvé"));

        UserRequestBody userToUpdate = new UserRequestBody(username,
                List.of(new GameModeRequestBody(GameModeEnum.MEDIUM, Duration.ofMinutes(3))),
                new ReviewRequestBody("rien a dire",givenStars)
        );
        //📌UPDATE THIS USER
        webTestClient.put()
                .uri(userURI+"/{id}",idUser)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userToUpdate),UserRequestBody.class)
                .exchange()
                .expectStatus()
                .isOk();

        //essayons ensuite de recuperer le user pour s'assurer qu'il a bien été modifié
        //getUser by this id
        //mais bien avant convertissons le userToUpdate en une instance typé User

        List<UserGameMode> listConverted = userToUpdate.gameModes().stream().map(uR->new UserGameMode(uR.gameLevelPlayed(),uR.elapsedTimeForThisLevel())).toList();
        Review reviewConverted = new Review(userToUpdate.review().message(),userToUpdate.review().givenStars());

        User expectedUser = new User(username,listConverted,reviewConverted);

        //important pour la requete suivante qui lui va renvoyer l'id
        expectedUser.setId(idUser);

        //finally
        webTestClient.get()
                .uri(userURI+"/{id}",idUser)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<User>() {})
                .consumeWith(response -> {
                    User actualUser = response.getResponseBody();
                    assertThat(actualUser)
                            .usingRecursiveComparison()
                            .ignoringFields("gameModes.id", "passedAt", "review.id")
                            .isEqualTo(expectedUser);
                });
    }

}
