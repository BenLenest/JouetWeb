package web;

import model.Request;
import model.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Implementation of a client socket.
 */
public class Client extends Thread {

    /* ATTRIBUTES ========================================================== */

    private Socket clientSocket;


    /* CONSTRUCTOR ========================================================= */

    public Client(Socket cliS){
        super();
        this.clientSocket = cliS;
    }

    /* PUBLIC METHODS ====================================================== */

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            String inputLine;
            StringBuilder builder = new StringBuilder();

            // MAIN LOOP
            while ((inputLine = in.readLine()) != null){
                if(inputLine.isEmpty()){
                    Response resp = HTTPBuilder.handleRequest(HTTPBuilder.parseRequest(builder.toString()));
                    String responseToSend = HTTPBuilder.buildResponse(resp);
                    System.out.println(responseToSend);
                    out.println(responseToSend);
                    out.flush();
                }else{
                    builder.append(inputLine).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
