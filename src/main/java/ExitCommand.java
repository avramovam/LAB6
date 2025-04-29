import java.io.Serializable;
import java.util.Map;

public class ExitCommand implements Command, Serializable {
    @Override
    public String execute(CollectionManager collectionManager, Map args) {
        return "exit"; // Возвращаем строку "exit"
    }
}