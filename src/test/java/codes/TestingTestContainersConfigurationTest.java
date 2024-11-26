package codes;

import org.flywaydb.core.api.MigrationState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestingTestContainersConfigurationTest extends AbstractTestcontainersConfiguration{

    @Test
    void canStartPostgresDB() {
        System.out.println(postgreSQLContainer.getJdbcUrl());
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }

    @Test
    void doesFlywayApplyMigration(){
        // Check if all migrations have passed
        assertThat(flyway.info().all())
                .allMatch(info -> info.getState() == MigrationState.SUCCESS);
    }
}
