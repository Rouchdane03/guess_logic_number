package codes.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @SequenceGenerator(
            name = "reviews_id_seq",
            sequenceName = "reviews_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reviews_id_seq"
    )
    @Column(columnDefinition = "BIGSERIAL")
    private Integer id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private int givenStars;

    /**
     * Constructor, getters, setters & toString methods
     */
    public Review() {}

    public Review(String message, int givenStars) {
        this.message = message;
        this.givenStars = givenStars;
    }

    public Review(Integer id, String message, int givenStars) {
        this.id = id;
        this.message = message;
        this.givenStars = givenStars;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        return "Review{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", givenStars=" + givenStars +
                '}';
    }


    /**
     *Ces methodes sont importantes car elles vont servir lors de ma compraraison des listes
     *Avant le equals que j'utilisais est celui de la classe Object, lui va pas comparer les vraies valeurs, mais plutot les références en mémoire
     *Avec equals de ma classe Review, les objets seront comparés vis-a-vis de leur vraie valeur, et non leur référence en mémoire
     * ‼️attention à mettre les attributs juste en ft de l'objet à comparer.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return givenStars == review.givenStars && Objects.equals(message, review.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, givenStars);
    }
}