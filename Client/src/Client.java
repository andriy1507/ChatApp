import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Client extends JFrame implements ConnectionListener, ActionListener {


    private final String IP_ADDRESS = "0.0.0.0";

    private final int PORT = 8080;

    private final int WIDTH = 600;

    private final int HEIGHT = 400;

    private Connection connection;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Client::new);
    }

    private JTextArea log = new JTextArea();
    private JTextField msgField = new JTextField("");
    private JTextField nickField = new JTextField("anon");

    private Client() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setResizable(false);

        log.setEditable(false);
        log.setLineWrap(true);

        msgField.addActionListener(this);
        add(log, BorderLayout.CENTER);
        add(msgField, BorderLayout.SOUTH);
        add(nickField, BorderLayout.NORTH);

        setVisible(true);
        try {
            connection = new Connection(this, IP_ADDRESS, PORT);
        } catch (IOException e) {
            onException(connection, e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message = msgField.getText();
        if (message.equals("")) return;
        msgField.setText(null);
        connection.sendMessage(nickField.getText() + ": " + message);
    }

    @Override
    public void onConnectionReady(Connection c) {
        printMessage("Connection ready: " + c);
    }

    @Override
    public void onReceiveMessage(Connection c, String s) {
        printMessage(s);
    }

    @Override
    public void onDisconnect(Connection c) {
        printMessage("Connection closed");
    }

    @Override
    public void onException(Connection c, Exception e) {
        printMessage("Connection exception: " + e);
    }

    private synchronized void printMessage(String s) {
        SwingUtilities.invokeLater(() -> {
            log.append(s + "\n");
            log.setCaretPosition(log.getDocument().getLength());
        });
    }
}
