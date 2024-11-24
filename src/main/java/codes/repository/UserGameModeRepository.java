package codes.repository;

import codes.entity.UserGameMode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGameModeRepository extends JpaRepository<UserGameMode,Integer> {
}
