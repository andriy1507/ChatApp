import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server implements ConnectionListener {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        new Server();
    }

    private ArrayList<Connection> connections = new ArrayList<>();

    private Server() {
        System.out.println("***SERVER STARTED***");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println(serverSocket.getLocalSocketAddress());
            while (true) {
                try {
                    new Connection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("Connection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendToAll(String s) {
        for (Connection c : connections) c.sendMessage(s);
    }

    @Override
    public synchronized void onConnectionReady(Connection c) {
        System.out.println(c.toString());
        connections.add(c);
        sendToAll("Client connected: " + c);
    }

    @Override
    public synchronized void onReceiveMessage(Connection c, String s) {
        sendToAll(s);
    }

    @Override
    public synchronized void onDisconnect(Connection c) {
        connections.remove(c);
        sendToAll("Client disconnected: " + c);
    }

    @Override
    public synchronized void onException(Connection c, Exception e) {
        System.out.println("Connection exception: " + e);
    }
}
