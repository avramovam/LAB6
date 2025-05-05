package app;

import app.MovieFactory;
import commands.*;
import modules.Movie;
import modules.MovieGenre;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class UDPClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1236;  // Изменил порт
    private static final int BUFFER_SIZE = 65535;

    public static void main(String[] args) {
        try {
            // Создаем DatagramChannel
            DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false); // Устанавливаем неблокирующий режим
            InetSocketAddress serverAddress = new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT);

            // Создаем Selector
            Selector selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Enter command: ");
                String commandLine = scanner.nextLine();
                String[] commandParts = commandLine.split(" ", 2);
                String commandName = commandParts[0];
                String commandArgs = commandParts.length > 1 ? commandParts[1] : "";

                // Создаем объект команды (пока просто имя команды)
                Object command = null;

                switch (commandName) {
                    case "add":
                        // Используем MovieFactory для создания объекта Movie
                        Movie movie = MovieFactory.createMovie();

                        // Создаем объект AddCommandArgs
                        AddCommandArgs addCommandArgs = new AddCommandArgs(movie);

                        // Создаем объект AddCommand
                        command = new AddCommand(addCommandArgs);
                        break;
                    case "show":
                        command = new ShowCommand();
                        break;
                    case "remove_by_id":
                        try {
                            int id = Integer.parseInt(commandArgs);
                            command = new RemoveByIdCommand(id);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid ID format. Please enter a valid number.");
                            continue;
                        }
                        break;
                    case "update":
                        try {
                            String[] updateParts = commandArgs.split(" ", 2);
                            int updateId = Integer.parseInt(updateParts[0]);
                            Movie updatedMovie = MovieFactory.createMovie();
                            command = new UpdateCommand(updateId, updatedMovie);
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            System.out.println("Invalid command format. Please use 'update id'.");
                            continue;
                        }
                        break;
                    case "remove_head":
                        command = new RemoveHeadCommand();
                        break;
                    case "help":
                        command = new HelpCommand();
                        break;
                    case "exit":
                        command = new ExitCommand();
                        break;
                    case "count_greater_than_genre":
                        try {
                            MovieGenre genre = MovieGenre.valueOf(commandArgs.toUpperCase());
                            command = new CountGreaterThanGenreCommand(genre);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid genre. Please enter a valid genre.");
                            continue;
                        }
                        break;
                    case "add_if_min":
                        Movie newMovie = MovieFactory.createMovie();
                        command = new AddIfMinCommand(newMovie);
                        break;

                    case "remove_greater":
                        Movie greaterMovie = MovieFactory.createMovie();
                        command = new RemoveGreaterCommand(greaterMovie);
                        break;
                    case "min_by_coordinates":
                        command = new MinByCoordinatesCommand();
                        break;
                    case "max_by_id":
                        command = new MaxByIdCommand();
                        break;
                    case "info":
                        command = new InfoCommand();
                        break;
                    default:
                        System.out.println("Unknown command.");
                        continue; // Пропускаем отправку на сервер
                }

                if (command == null) {
                    continue; // Пропускаем отправку на сервер, если команда не создана
                }

                // Сериализуем объект команды
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(command);
                byte[] data = baos.toByteArray();

                // Отправляем данные на сервер
                ByteBuffer buffer = ByteBuffer.wrap(data);
                channel.send(buffer, serverAddress);

                System.out.println("Command sent to server...");

                // Ждем ответа от сервера с таймаутом
                boolean receivedResponse = false;
                int attempts = 0;
                int maxAttempts = 3;

                while (!receivedResponse && attempts < maxAttempts) {
                    attempts++;
                    System.out.println("Waiting for response (attempt " + attempts + ")...");

                    // ждем, пока канал станет готовым для чтения или истечет таймаут
                    int readyChannels = selector.select(5000); // ждем 5 секунд

                    if (readyChannels == 0) {
                        System.out.println("Server is not responding. Retrying...");
                        continue; // Повторяем попытку отправки команды
                    }

                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = keys.iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();

                        if (key.isReadable()) {
                            ByteBuffer receiveBuffer = ByteBuffer.allocate(BUFFER_SIZE);
                            InetSocketAddress responseAddress = (InetSocketAddress) channel.receive(receiveBuffer);

                            if (responseAddress != null) {
                                // Десериализуем ответ
                                receiveBuffer.flip();
                                byte[] responseData = new byte[receiveBuffer.remaining()];
                                receiveBuffer.get(responseData);

                                ByteArrayInputStream bais = new ByteArrayInputStream(responseData);
                                ObjectInputStream ois = new ObjectInputStream(bais);
                                String response = (String) ois.readObject();

                                System.out.println("Response from server: " + response);
                                receivedResponse = true;  // Помечаем, что ответ получен

                                // Проверяем, пришла ли команда "exit"
                                if (response.equals("exit")) {
                                    break; // Выходим из цикла обработки команд
                                }

                            } else {
                                System.out.println("No response from server.");
                            }
                        }
                        keyIterator.remove();
                    }
                }

                if (!receivedResponse) {
                    System.out.println("Server is not responding. Please try again later.");
                }

                if (commandName.equalsIgnoreCase("exit")) {
                    break; // Выходим из внешнего цикла, если клиент ввел "exit"
                }

                // Если получили команду "exit" от сервера, то выходим из цикла
                if (receivedResponse && commandName.equals("exit")) {
                    break;
                }
            }

            channel.close();
            selector.close();
            System.out.println("Client stopped.");

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}