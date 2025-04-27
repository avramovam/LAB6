import java.io.Serializable;
import java.util.Map;

public class ExitCommand implements Command, Serializable {
    @Override
    public String execute(CollectionManager collectionManager, Map args) {
        System.exit(0);
        return "Exiting program...";
    }
}