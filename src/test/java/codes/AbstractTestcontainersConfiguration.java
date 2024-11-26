package codes;

import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractTestcontainersConfiguration {

	protected static Flyway flyway;

	@BeforeAll
	static void beforeAll() {
		//here we will update our datasource schema
		flyway = Flyway.configure()
				.dataSource(
						postgreSQLContainer.getJdbcUrl(),
						postgreSQLContainer.getUsername(),
						postgreSQLContainer.getPassword())
				.load();
		flyway.migrate();
	}
    //create our deletable container
	@Container
	protected static final PostgreSQLContainer<?> postgreSQLContainer =
			new PostgreSQLContainer<>("postgres:17")
					.withDatabaseName("rouchdane-repository-unit-test")
					.withUsername("rouchdane")
					.withPassword("password");

    //Setting up our datasource properties
	@DynamicPropertySource
	private static void registerDataSourceProperties(DynamicPropertyRegistry registry){
		registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
		registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
	}

	protected static final Faker faker = new Faker();

}
