package app;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.channels.SelectionKey;
import java.util.Scanner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;

public class Server {
    private static final int PORT = 1236;

    public static void main(String[] args) {
        try {
            DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(PORT));

            Selector selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);

            CollectionManager collectionManager = new CollectionManager();

            ClientHandler clientHandler = new ClientHandler(channel, selector, collectionManager);
            new Thread(clientHandler).start();

            System.out.println("Server started on port " + PORT);
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}