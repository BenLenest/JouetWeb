import web.Server;

/**
 * Main class of the project.
 * Instantiates a server which listens to incoming requests and returns responses.
 */
public class Main {

    public static void main(String[] args) {

        int port = 8081;

        // web.Server instantiation
        System.out.println("#1 - Server instantiation");
        Server server = new Server(port);

        // Start listening
        System.out.println("#2 - Server starts listening on port " + port + " :\n");
        server.startListening();
    }
}
