import java.util.PriorityQueue;
import java.util.Queue;

public class CollectionManager {
    private Queue<Movie> movies = new PriorityQueue<>();
    private Integer nextId = 1;

    public String add(Movie movie) {
        movie.setId(getNextId());
        movies.add(movie);
        return "Movie added successfully.";
    }

    public Integer getNextId() {
        return nextId++;
    }

    public String remove(Movie movie) {
        if (movies.remove(movie)) {
            return "Movie removed successfully.";
        } else {
            return "Movie not found.";
        }
    }

    public String show() {
        if (movies.isEmpty()) {
            return "Collection is empty.";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Movie movie : movies) {
                sb.append(movie.toString()).append("\n");
            }
            return "Collection:\n" + sb.toString();
        }
    }

    public Queue<Movie> getMovies() {
        return movies;
    }
}