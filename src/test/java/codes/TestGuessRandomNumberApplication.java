package codes;

import org.springframework.boot.SpringApplication;

public class TestGuessRandomNumberApplication {

	public static void main(String[] args) {
		SpringApplication.from(GuessRandomNumberApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
