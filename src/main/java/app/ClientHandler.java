package app;

import commands.*;

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
import java.util.Set;

public class ClientHandler implements Runnable {
    private DatagramChannel channel;
    private Selector selector;
    private CollectionManager collectionManager;
    private static final int BUFFER_SIZE = 65535;

    public ClientHandler(DatagramChannel channel, Selector selector, CollectionManager collectionManager) {
        this.channel = channel;
        this.selector = selector;
        this.collectionManager = collectionManager;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int readyChannels = selector.select();
                if (readyChannels == 0) continue;

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (key.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                        InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(buffer);

                        if (clientAddress != null) {
                            buffer.flip();
                            byte[] data = new byte[buffer.remaining()];
                            buffer.get(data);

                            ByteArrayInputStream bais = new ByteArrayInputStream(data);
                            ObjectInputStream ois = new ObjectInputStream(bais);
                            Command command = (Command) ois.readObject();

                            String response = command.execute(collectionManager, null);

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ObjectOutputStream oos = new ObjectOutputStream(baos);
                            oos.writeObject(response);
                            byte[] responseData = baos.toByteArray();

                            ByteBuffer responseBuffer = ByteBuffer.wrap(responseData);
                            channel.send(responseBuffer, clientAddress);
                        }
                    }

                    keyIterator.remove();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("ClientHandler error: " + e.getMessage());
        }
    }
}