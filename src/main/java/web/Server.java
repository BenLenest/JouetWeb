package web;

import java.io.IOException;
import java.net.ServerSocket;

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
            System.out.println("\nNEW CLIENT :\n");
            while (true) new Client(serverSocket.accept()).run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
