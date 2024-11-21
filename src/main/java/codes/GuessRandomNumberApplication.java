package codes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;

@SpringBootApplication
public class GuessRandomNumberApplication {

	private static final Random RANDOM = new Random();

	public static int GENERATED_VALUE;

	public static void main(String[] args) {
		SpringApplication.run(GuessRandomNumberApplication.class, args);
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
}
