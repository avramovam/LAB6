package commands;

import app.CollectionManager;
import modules.Movie;

import java.io.Serializable;
import java.util.Map;

public class UpdateCommand implements Command, Serializable {
    private int id;
    private Movie updatedMovie;

    public UpdateCommand(int id, Movie updatedMovie) {
        this.id = id;
        this.updatedMovie = updatedMovie;
    }

    @Override
    public String execute(CollectionManager collectionManager, Map args) {
        return collectionManager.update(id, updatedMovie);
    }
}
