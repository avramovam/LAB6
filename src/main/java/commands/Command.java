package commands;

import app.CollectionManager;

import java.io.Serializable;
import java.util.Map;

public interface Command extends Serializable {
    String execute(CollectionManager collectionManager, Map args);
}