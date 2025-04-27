import java.io.Serializable;

public class AddCommandArgs implements Serializable {
    private Movie movie;

    public AddCommandArgs(Movie movie) {
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }
}