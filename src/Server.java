import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int port;
    private Socket clientSocket;

    public Server(int port) {
        this.port = port;
        if (initializeServer()) startListening();
    }

    private boolean initializeServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            return true
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void startListening() {

    }
}
