package commands;

import app.CollectionManager;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExecuteScriptCommand implements Command, Serializable {
    private String fileName;
    private static Set<String> executingScripts = new HashSet<>();

    public ExecuteScriptCommand(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String execute(CollectionManager collectionManager, Map args) {
        if (executingScripts.contains(fileName)) {
            return "Error: Recursive script execution detected. Script " + fileName + " is already being executed.";
        }

        executingScripts.add(fileName);
        String result = collectionManager.executeScript(fileName);
        executingScripts.remove(fileName);
        return result;
    }
}