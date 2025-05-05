package commands;

import app.CollectionManager;

import java.io.Serializable;
import java.util.Map;

public class CheckIdCommand implements Command, Serializable {
    private int id;

    public CheckIdCommand(int id) {
        this.id = id;
    }

    @Override
    public String execute(CollectionManager collectionManager, Map args) {
        return collectionManager.checkIdExists(id) ? "true" : "false";
    }
}