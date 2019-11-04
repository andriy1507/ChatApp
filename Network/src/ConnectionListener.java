public interface ConnectionListener {

    void onConnectionReady(Connection c);

    void onReceiveMessage(Connection c, String s);

    void onDisconnect(Connection c);

    void onException(Connection c, Exception e);
}
