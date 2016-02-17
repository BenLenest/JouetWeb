package web;

import model.Request;
import model.Response;

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

    private Response handleRequest(Request request) {
        StringBuilder sb = new StringBuilder("<html>\n");
        sb.append("<body>\n<h1>Hello, World!</h1>\n</body>\n</html>");
        return new Response(request.getStatusCode(), request.getUrl(), request.getHeader(), sb.toString());
    }

    /* PUBLIC METHODS ======================================================= */

    public void startListening() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            Response resp = null;

            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine + "\n");
                if (inputLine.equals("")) {
                    resp = handleRequest(HTTPParser.parseRequest(builder.toString()));
                    //System.out.println(builder.toString());
                    System.out.println(HTTPParser.buildResponse(resp));
                    builder.delete(0, builder.length());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
