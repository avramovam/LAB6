package app;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Scanner;

public class Server {
    private static final int PORT = 1236;
    private DatagramSocket socket;
    private CollectionManager collectionManager;
    private boolean running;

    public Server(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            System.err.println("Could not create socket on port " + PORT + ": " + e.getMessage());
            System.exit(1);
        }
        running = false;
    }

    public void startServer() {
        running = true;
        System.out.println("Server started on port " + PORT);

        // Create a thread to listen for the "exit" command
        Thread exitThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String command = scanner.nextLine();
                if (command.equals("exit")) {
                    stopServer();
                    break;
                }
            }
        });
        exitThread.setDaemon(true); // Set the thread as a daemon so it doesn't prevent the program from exiting
        exitThread.start();

        while (running) {
            ClientHandler clientHandler = new ClientHandler(socket, this, collectionManager);
            clientHandler.run();
        }

        System.out.println("Server stopped.");
        socket.close();
    }

    public void stopServer() {
        running = false;
        System.out.println("Stopping server...");
    }

    public static void main(String[] args) {
        CollectionManager collectionManager = new CollectionManager();
        Server server = new Server(collectionManager);
        server.startServer();
    }
}