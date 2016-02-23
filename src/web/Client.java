package web;

import model.Response;
import model.enums.Method;
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
            System.out.println("NEW SOCKET");
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            String inputLine;
            StringBuilder builder = new StringBuilder();
            Method method = null;
            boolean firstLine = true;
            boolean readEnd = true;

            // MAIN LOOP
            while(true){
                inputLine = in.readLine();
                if(firstLine && !(inputLine == null || inputLine.isEmpty())){
                    builder = new StringBuilder();
                    method = Method.findMethodByValue(inputLine.split(" ")[0]);
                    if (method == Method.POST || method == Method.DELETE) {
                        readEnd = false;
                    }
                    firstLine = false;
                }else if ((inputLine == null || inputLine.isEmpty()) && !firstLine) {
                    if(readEnd) {
                        Response resp = HTTPBuilder.handleRequest(HTTPBuilder.parseRequest(builder.toString()));
                        String responseToSend = HTTPBuilder.buildResponse(resp);
                        System.out.println(responseToSend);
                        out.println(responseToSend);
                        out.flush();
                        firstLine = true;
                    }else{
                        readEnd = true;
                    }
                }
                if(inputLine != null) {
                    builder.append(inputLine).append("\n");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
