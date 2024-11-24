package codes;

import codes.controller.GameModeEnum;
import codes.entity.Review;
import codes.entity.User;
import codes.entity.UserGameMode;
import codes.repository.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class GuessLogicNumberApplication {

	private static final Random RANDOM = new Random();

	public static int GENERATED_VALUE;

	public static void main(String[] args) {
		SpringApplication.run(GuessLogicNumberApplication.class, args);
		System.out.println("the app is running");
		GENERATED_VALUE = getTheRandomNumber();
	}
	/**
	 *
	 * @return guess_number which is the random number
	 *
	 */
	private static int getTheRandomNumber(){
		int guess_number =  RANDOM.nextInt(1,100);
		System.out.println("Le nombre généré est "+guess_number);
		return guess_number;
	}

	/**
	 * adding a commandLineRunner to insert quickly an user, each time we load the app
	 */
	/*
	@Bean
	CommandLineRunner runner(UserRepository userRepository) {
		return args -> {
			Faker faker=new Faker();
			/**
			 * for gameModes list object
			 * trying to pass random enum in his gameLevelPlayed value
			 * nextInt will be either 0 or 1 or 2 car
			 * l'intervalle défini est toujours ouvert sur le bound [origin, bound[
			 * donc [0, 3[
			 * peut importe la methode nextint choisie, l'intervalle toujours ouvert sur la limite
			 */
			/*
			List<UserGameMode> gameModes = new ArrayList<>();
			List<GameModeEnum> enums = List.of(GameModeEnum.EASY,GameModeEnum.MEDIUM,GameModeEnum.HARD);
			int index = RANDOM.nextInt(enums.size());

			//for duration
			// adding random minutes in duration ofMinutes()
			Duration duration1 = Duration.ofMinutes(RANDOM.nextInt(1,1000));
			Duration duration2 = Duration.ofMinutes(RANDOM.nextInt(1,1000));

			//les enums doivent etre distinctes
			if (index==0){
				GameModeEnum gameLevelPlayed1 = enums.get(index);
				GameModeEnum gameLevelPlayed2 = enums.get(index+1);//c'est surtout à du deuxième je fais ça pour pas que les enums soient identiques(no doublon)
				gameModes.add(new UserGameMode(gameLevelPlayed1,duration1));
				gameModes.add(new UserGameMode(gameLevelPlayed2,duration2));
			}
			if (index==1){
				GameModeEnum gameLevelPlayed1 = enums.get(index);
				GameModeEnum gameLevelPlayed2 = enums.get(0); //j'ai choisi index-1 mais je peux aussi index+1 pour lui
				gameModes.add(new UserGameMode(gameLevelPlayed1,duration1));
				gameModes.add(new UserGameMode(gameLevelPlayed2,duration2));
			}
			if (index==2){
				GameModeEnum gameLevelPlayed1 = enums.get(index);
				GameModeEnum gameLevelPlayed2 = enums.get(index-1);
				gameModes.add(new UserGameMode(gameLevelPlayed1,duration1));
				gameModes.add(new UserGameMode(gameLevelPlayed2,duration2));
			}

			//for review object
			int random_stars =  RANDOM.nextInt(1,5);
			String message = "this is an example, will change soon"+ UUID.randomUUID();

			User user = new User(faker.name().username(),gameModes,new Review(message,random_stars));
			userRepository.save(user);
*/
	/*
		};

	 */
//	}
}
