import web.Server;

public class Main {

    public static void main(String[] args) {

        int port = 8080;

        // Launching the server
        Server server = new Server(port);
        server.startListening();
        System.out.println("Server launched and listening...");
    }

}
