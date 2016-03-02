package web;

import model.Request;
import model.Response;
import model.enums.EnumMethod;

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
    private RequestDispatcher requestDispatcher;


    /* CONSTRUCTOR ========================================================= */

    public Client(Socket cliS) {
        super();
        this.clientSocket = cliS;
        this.requestDispatcher = new RequestDispatcher();
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
            EnumMethod method;
            boolean firstLine = true;
            boolean readEnd = true;

            // MAIN LOOP
            while (true) {
                inputLine = in.readLine();
                if (firstLine && !(inputLine == null || inputLine.isEmpty())) {
                    builder = new StringBuilder();
                    method = EnumMethod.findMethodByValue(inputLine.split(" ")[0]);
                    if (method == EnumMethod.POST || method == EnumMethod.DELETE) {
                        readEnd = false;
                    }
                    firstLine = false;
                }
                else if ((inputLine == null || inputLine.isEmpty()) && !firstLine) {
                    if(readEnd) {
                        Request req = HTTPBuilder.parseRequest(builder.toString());
                        Response resp = requestDispatcher.dispatchRequest(req);
                        String responseToSend = HTTPBuilder.buildResponse(resp);
                        System.out.println("Response :\n" + responseToSend);
                        out.println(responseToSend);
                        out.flush();
                        firstLine = true;
                    }
                    else {
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
