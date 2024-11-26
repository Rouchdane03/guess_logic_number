package codes.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @SequenceGenerator(
            name = "users_id_seq",
            sequenceName = "users_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "users_id_seq"
    )
    @Column(columnDefinition = "BIGSERIAL")
    private Integer id;

    @Column(nullable = false)
    private String username;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<UserGameMode> gameModes = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "user_review_id", referencedColumnName = "id")
    private Review review;

    @Column(nullable = false)
    private LocalDateTime passedAt;

    // Méthode appelée automatiquement avant la persistance
    @PrePersist
    public void setPassedAt() {
        this.passedAt = LocalDateTime.now(); // Définit la date actuelle
    }

    // Mise à jour lors d'une modification
    @PreUpdate
    public void updatePassedAt() {
        this.passedAt = LocalDateTime.now(); // Change la date passedAt à chaque mise à jour
    }
     /**
      * Constructor, getters, setters & toString methods
      */
    public User() {
    }

    public User(String username, List<UserGameMode> gameModes, Review review) {
        this.username = username;
        this.gameModes = gameModes;
        this.review = review;
    }

    public User(Integer id, String username, List<UserGameMode> gameModes, Review review, LocalDateTime passedAt) {
        this.id = id;
        this.username = username;
        this.gameModes = gameModes;
        this.review = review;
        this.passedAt = passedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<UserGameMode> getGameModes() {
        return gameModes;
    }

    public void setGameModes(List<UserGameMode> gameModes) {
        this.gameModes = gameModes;
    }


    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public LocalDateTime getPassedAt() {
        return passedAt;
    }

    public void setPassedAt(LocalDateTime passedAt) {
        this.passedAt = passedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", gameModes=" + gameModes +
                ", review=" + review +
                ", passedAt=" + passedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(gameModes, user.gameModes) && Objects.equals(review, user.review) && Objects.equals(passedAt, user.passedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, gameModes, review, passedAt);
    }
}
