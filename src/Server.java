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
        if (initializeServer()) startListening();
    }

    /* PRIVATE METHOD ======================================================= */

    private boolean initializeServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void startListening() {

    }
}
