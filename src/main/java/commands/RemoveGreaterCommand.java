package commands;

import app.CollectionManager;
import modules.Movie;

import java.io.Serializable;
import java.util.Map;

public class RemoveGreaterCommand implements Command, Serializable {
    private Movie movie;

    public RemoveGreaterCommand(Movie movie) {
        this.movie = movie;
    }

    @Override
    public String execute(CollectionManager collectionManager, Map args) {
        return collectionManager.removeGreater(movie);
    }
}
