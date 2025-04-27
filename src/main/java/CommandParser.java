import java.util.HashMap;
import java.util.Map;

public class CommandParser {

    public static CommandParser.ParsedCommand parse(String commandString) {
        String[] parts = commandString.split(" ");
        String commandName = parts[0];

        Map<String, String> args = new HashMap<>();
        for (int i = 1; i < parts.length; i++) {
            String[] argParts = parts[i].split("=");
            if (argParts.length == 2) {
                args.put(argParts[0], argParts[1]);
            }
        }

        return new CommandParser.ParsedCommand(commandName, args);
    }

    public static class ParsedCommand {
        private String commandName;
        private Map<String, String> args;

        public ParsedCommand(String commandName, Map<String, String> args) {
            this.commandName = commandName;
            this.args = args;
        }

        public String getCommandName() {
            return commandName;
        }

        public Map<String, String> getArgs() {
            return args;
        }
    }
}