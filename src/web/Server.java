package web;

import model.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    /* PRIVATE METHODS ======================================================= */

    private void handleRequest(Request request) {
        if (request != null) {
            //TODO: ici l'objet Request est prêt, tu n'as plus qu'à créer la réponse à partir de ça et de le renvoyer !
        } else {
            System.out.println("--> The received request couldn't be parsed.");
        }
    }

    /* PUBLIC METHODS ======================================================= */

    public void startListening() {
        try {
            // open the client socket
            ServerSocket serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();

            // read the client request
            while ((inputLine = in.readLine()) != null) builder.append(inputLine + "\n");
            handleRequest(HTTPParser.parseRequest(builder.toString()));
            System.out.println(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
