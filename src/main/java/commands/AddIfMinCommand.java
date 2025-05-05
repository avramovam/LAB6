package commands;

import app.CollectionManager;
import modules.Movie;

import java.io.Serializable;
import java.util.Map;

public class AddIfMinCommand implements Command, Serializable {
    private Movie movie;

    public AddIfMinCommand(Movie movie) {
        this.movie = movie;
    }

    @Override
    public String execute(CollectionManager collectionManager, Map args) {
        return collectionManager.addIfMin(movie);
    }
}