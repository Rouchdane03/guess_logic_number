package codes.entity;

import codes.controller.GameModeEnum;
import jakarta.persistence.*;

import java.time.Duration;
import java.util.Objects;

@Entity
@Table(name = "game_modes")
public class UserGameMode {

  @Id
  @SequenceGenerator(
          name = "game_modes_id_seq",
          sequenceName = "game_modes_id_seq",
          allocationSize = 1
  )
  @GeneratedValue(
          strategy = GenerationType.SEQUENCE,
          generator = "game_modes_id_seq"
  )
  @Column(columnDefinition = "BIGSERIAL")
  private int id;

  @Enumerated(EnumType.STRING)
  @Column(name = "game_level_played", nullable = false)
  private GameModeEnum gameLevelPlayed;

  @Column(nullable = false)
  @Convert(converter = DurationToMinutesConverter.class)
  private Duration elapsedTimeForThisLevel;

  /**
   * Constructor, getters, setters & toString methods
   */

  public UserGameMode(){}

  public UserGameMode(int id, GameModeEnum gameMode, Duration elapsedTimeForThisLevel) {
    this.id = id;
    this.gameLevelPlayed = gameMode;
  }

  public UserGameMode(GameModeEnum gameMode,Duration elapsedTimeForThisLevel) {
    this.gameLevelPlayed = gameMode;
    this.elapsedTimeForThisLevel = elapsedTimeForThisLevel;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public GameModeEnum getGameLevelPlayed() {
    return gameLevelPlayed;
  }

  public void setGameLevelPlayed(GameModeEnum gameMode) {
    this.gameLevelPlayed = gameMode;
  }

  public Duration getElapsedTimeForThisLevel() {
    return elapsedTimeForThisLevel;
  }

  public void setElapsedTimeForThisLevel(Duration elapsedTimeForThisLevel) {
    this.elapsedTimeForThisLevel = elapsedTimeForThisLevel;
  }

  @Override
  public String toString() {
    return "UserGameMode{" +
            "id=" + id +
            ", gameLevelPlayed=" + gameLevelPlayed +
            ", elapsedTimeForThisLevel=" + elapsedTimeForThisLevel +
            '}';
  }

/**
  *Ces methodes sont importantes car elles vont servir lors de ma compraraison des listes
  *Avant le equals que j'utilisais est celui de la classe Object, lui va pas comparer les vraies valeurs, mais plutot les références en mémoire
  *Avec equals de ma classe UserGameMode, les objets seront comparés vis-a-vis de leur vraie valeur, et non leur référence en mémoire
  * ‼️attention à mettre les attributs juste en ft de l'objet à comparer.
 */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserGameMode that = (UserGameMode) o;
    return gameLevelPlayed == that.gameLevelPlayed && Objects.equals(elapsedTimeForThisLevel, that.elapsedTimeForThisLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gameLevelPlayed, elapsedTimeForThisLevel);
  }
}