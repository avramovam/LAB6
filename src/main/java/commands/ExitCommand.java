package commands;

import app.CollectionManager;

import java.io.Serializable;
import java.util.Map;

public class ExitCommand implements Command, Serializable {
    @Override
    public String execute(CollectionManager collectionManager, Map args) {
        collectionManager.save();
        return "exit"; // Возвращаем строку "exit"
    }
}