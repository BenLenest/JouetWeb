package web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    /* ATTRIBUTES ========================================================== */

    private int port;

    /* CONSTRUCTOR ========================================================= */

    public Server(int port) {
        this.port = port;
    }

    /* PUBLIC METHODS ====================================================== */

    public void startListening() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Client client = new Client(clientSocket);
                client.run();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
