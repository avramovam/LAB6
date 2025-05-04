package commands;

import app.CollectionManager;

import java.io.Serializable;
import java.util.Map;

public class HelpCommand implements Command, Serializable {
    @Override
    public String execute(CollectionManager collectionManager, Map args) {
        return "help : вывести справку по доступным командам\n" +
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add {element} : добавить новый элемент в коллекцию\n" +
                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "remove_by_id id : удалить элемент из коллекции по его id\n" +
                "clear : очистить коллекцию\n" +
                "save : сохранить коллекцию в файл\n" +
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "exit : завершить программу (без сохранения в файл)\n" +
                "remove_first : удалить первый элемент из коллекции\n" +
                "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" +
                "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
                "history : вывести последние 15 команд (без их аргументов)\n" +
                "sum_of_oscars_count : вывести сумму значений поля oscarsCount для всех элементов коллекции\n" +
                "count_by_genre genre : вывести количество элементов, значение поля genre которых равно заданному\n" +
                "filter_greater_than_director director : вывести элементы, значение поля director которых больше заданного";
    }
}