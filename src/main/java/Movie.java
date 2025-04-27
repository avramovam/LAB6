import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Movie implements Serializable, Comparable<Movie> {
    private Integer id; // Поле не может быть null, Значение этого поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; // Поле не может быть null
    private java.time.LocalDateTime creationDate; // Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int oscarsCount; // Значение поля должно быть больше 0
    private Integer length; // Поле может быть null, Значение поля должно быть больше 0
    private MovieGenre genre; // Поле может быть null
    private MpaaRating mpaaRating; // Поле может быть null
    private Person director; // Поле может быть null

    public Movie() {
        this.creationDate = LocalDateTime.now();
    }

    @Override
    public int compareTo(Movie other) {
        if (this.name.length() != other.name.length()) {
            return Integer.compare(this.name.length(), other.name.length());
        } else {
            return Long.compare(this.length, other.length);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public int getOscarsCount() {
        return oscarsCount;
    }

    public void setOscarsCount(int oscarsCount) {
        this.oscarsCount = oscarsCount;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public MovieGenre getGenre() {
        return genre;
    }

    public void setGenre(MovieGenre genre) {
        this.genre = genre;
    }

    public MpaaRating getMpaaRating() {
        return mpaaRating;
    }

    public void setMpaaRating(MpaaRating mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public Person getDirector() {
        return director;
    }

    public void setDirector(Person director) {
        this.director = director;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return oscarsCount == movie.oscarsCount && id.equals(movie.id) && name.equals(movie.name) && coordinates.equals(movie.coordinates) && creationDate.equals(movie.creationDate) && Objects.equals(length, movie.length) && genre == movie.genre && mpaaRating == movie.mpaaRating && director.equals(movie.director);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, oscarsCount, length, genre, mpaaRating, director);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", oscarsCount=" + oscarsCount +
                ", length=" + length +
                ", genre=" + genre +
                ", mpaaRating=" + mpaaRating +
                ", director=" + director +
                '}';
    }
}