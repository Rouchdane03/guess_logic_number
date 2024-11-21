package codes.entity;

import jakarta.persistence.*;

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
    private int id;

    @Column
    private String username;

    @Column
    private String email;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_opinion_id", referencedColumnName = "opinion_id")
    private Opinion opinion;

    /**
     * Constructor, getters, setters & toString methods
     */
    public User() {}

    public User(String username, String email, Opinion opinion) {
        this.username = username;
        this.email = email;
        this.opinion = opinion;
    }

    public User(int id, String username, String email, Opinion opinion) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.opinion = opinion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Opinion getOpinion() {
        return opinion;
    }

    public void setOpinion(Opinion opinion) {
        this.opinion = opinion;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", opinion=" + opinion +
                '}';
    }
}
