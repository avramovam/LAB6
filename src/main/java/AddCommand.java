import java.io.Serializable;
import java.util.Map;

public class AddCommand implements Command, Serializable {
    private AddCommandArgs args;

    public AddCommand(AddCommandArgs args) {
        this.args = args;
    }

    public AddCommandArgs getArgs() {
        return args;
    }

    @Override
    public String execute(CollectionManager collectionManager, Map unusedArgs) {
        // Извлеките movie из args
        Movie movie = args.getMovie();
        return collectionManager.add(movie); // или другой метод
    }
}