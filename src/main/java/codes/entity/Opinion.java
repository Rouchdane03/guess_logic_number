package codes.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "opinions")
public class Opinion {
    @Id
    @SequenceGenerator(
            name = "opinions_opinion_id_seq",
            sequenceName = "opinions_opinion_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "opinions_opinion_id_seq"
    )
    @Column(name = "opinion_id",columnDefinition = "BIGSERIAL")
    private int id;

    @Column
    private String message;

    @Column
    private int givenStars;

    /**
     * Constructor, getters, setters & toString methods
     */
    public Opinion() {}

    public Opinion(String message, int givenStars) {
        this.message = message;
        this.givenStars = givenStars;
    }

    public Opinion(int id, String message, int givenStars) {
        this.id = id;
        this.message = message;
        this.givenStars = givenStars;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getGivenStars() {
        return givenStars;
    }

    public void setGivenStars(int givenStars) {
        this.givenStars = givenStars;
    }

    @Override
    public String toString() {
        return "Opinion{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", givenStars=" + givenStars +
                '}';
    }
}