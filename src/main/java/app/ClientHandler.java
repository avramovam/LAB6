package app;

import commands.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientHandler implements Runnable {

    private DatagramSocket socket;
    private Server server;
    private CollectionManager collectionManager;
    private DatagramPacket packet;

    public ClientHandler(DatagramSocket socket, Server server, CollectionManager collectionManager) {
        this.socket = socket;
        this.server = server;
        this.collectionManager = collectionManager;
        this.packet = receivePacket();
    }

    private DatagramPacket receivePacket() {
        byte[] buffer = new byte[65535];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            System.err.println("Error receiving packet: " + e.getMessage());
        }
        return packet;
    }

    @Override
    public void run() {
        try {
            InetAddress address = packet.getAddress();
            int port = packet.getPort();

            System.out.println("Received packet from: " + address.getHostAddress() + ":" + port);

            // Десериализуем полученный объект
            ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object commandObject = ois.readObject();

            System.out.println("Received object: " + commandObject);

            String response;

            if (commandObject instanceof AddCommand) {
                System.out.println("Received Commands.AddCommand");
                AddCommand command = (AddCommand) commandObject;
                response = command.execute(collectionManager, null);
                System.out.println("Response from execute: " + response);  // Add this line
            } else if (commandObject instanceof ShowCommand) {
                System.out.println("Received Commands.ShowCommand");
                ShowCommand command = (ShowCommand) commandObject;
                response = command.execute(collectionManager, null);
            } else if (commandObject instanceof HelpCommand) {
                System.out.println("Received Commands.HelpCommand");
                HelpCommand command = (HelpCommand) commandObject;
                response = command.execute(collectionManager, null);
            } else if (commandObject instanceof ExitCommand) {
                System.out.println("Received Commands.ExitCommand");
                ExitCommand command = (ExitCommand) commandObject;
                response = command.execute(collectionManager, null);
            } else if (commandObject instanceof RemoveByIdCommand) {
                System.out.println("Received RemoveCommand");
                RemoveByIdCommand command = (RemoveByIdCommand) commandObject;
                response = command.execute(collectionManager, null);
            } else if (commandObject instanceof UpdateCommand) {
                System.out.println("Received Commands.UpdateCommand");
                UpdateCommand command = (UpdateCommand) commandObject;
                response = command.execute(collectionManager, null);

            } else if (commandObject instanceof RemoveHeadCommand) {
                System.out.println("Received Commands.RemoveHeadCommand");
                RemoveHeadCommand command = (RemoveHeadCommand) commandObject;
                response = command.execute(collectionManager, null);

            } else if (commandObject instanceof CountGreaterThanGenreCommand){
                System.out.println("Received Commands.CountGreaterThanGenreCommand");
                CountGreaterThanGenreCommand command = (CountGreaterThanGenreCommand) commandObject;
                response = command.execute(collectionManager, null);
            }
            else {
                response = "Unknown command object received.";
            }

            // Сериализуем ответ
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(response);
            System.out.println("Serialized response: " + response);  // Add this line

            byte[] responseData = baos.toByteArray();
            DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, address, port);
            socket.send(responsePacket);
            System.out.println("Sent response to client");  // Add this line

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error in client handler: " + e.getMessage());
        }
    }
}