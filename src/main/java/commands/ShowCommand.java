package commands;

import app.CollectionManager;

import java.io.Serializable;
import java.util.Map;

public class ShowCommand implements Command, Serializable {
    @Override
    public String execute(CollectionManager collectionManager, Map args) {
        return collectionManager.show();
    }
}