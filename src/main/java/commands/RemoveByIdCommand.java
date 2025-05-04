package commands;

import app.CollectionManager;

import java.io.Serializable;
import java.util.Map;

public class RemoveByIdCommand implements Command, Serializable {
    private int id;

    public RemoveByIdCommand(int id) {
        this.id = id;
    }

    @Override
    public String execute(CollectionManager collectionManager, Map args) {
        return collectionManager.removeById(id);
    }
}