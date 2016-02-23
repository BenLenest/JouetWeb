package web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class reprensenting a server which receives and send messages.
 * @see model.Message
 */
public class Server {

    /* ATTRIBUTES ========================================================== */

    private int port;
    private Socket clientSocket;

    /* CONSTRUCTOR ========================================================== */

    public Server(int port) {
        this.port = port;
    }

    /* PUBLIC METHODS ======================================================= */

    public void startListening() {
        try {
            // open the client socket
            ServerSocket serverSocket = new ServerSocket(port);
            while(true) {
                clientSocket = serverSocket.accept();
                Client client = new Client(clientSocket);
                client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}