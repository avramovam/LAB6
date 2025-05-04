package commands;

import app.CollectionManager;
import commands.Command;
import modules.MovieGenre;

import java.io.Serializable;
import java.util.Map;

public class CountGreaterThanGenreCommand implements Command, Serializable {
    private MovieGenre genre;

    public CountGreaterThanGenreCommand(MovieGenre genre) {
        this.genre = genre;
    }

    @Override
    public String execute(CollectionManager collectionManager, Map args) {
        return collectionManager.countGreaterThanGenre(genre);
    }
}