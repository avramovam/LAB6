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
                String commandName = scanner.nextLine();

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
                    case "help":
                        command = new HelpCommand();
                        break;
                    case "exit":
                        command = new ExitCommand();
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