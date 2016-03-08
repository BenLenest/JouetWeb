package web;

import model.Request;
import model.Response;
import model.enums.EnumMethod;
import model.enums.EnumStatusCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;

public class Client extends Thread {

    /* ATTRIBUTES ========================================================== */

    private Socket clientSocket;
    private RequestDispatcher requestDispatcher;

    /* CONSTRUCTOR ========================================================= */

    public Client(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.requestDispatcher = new RequestDispatcher();
    }

    /* OVERRIDEN METHOD ==================================================== */

    @Override
    public void run() {
        super.run();
        try {
            BufferedReader in  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            String inputLine;
            StringBuilder builder = new StringBuilder();
            EnumMethod method;
            boolean firstLine = true;
            boolean readEnd = true;

            while (true) {
                inputLine = in.readLine();
                if (firstLine && !(inputLine == null || inputLine.isEmpty())) {
                    builder = new StringBuilder();
                    method = EnumMethod.findMethodByValue(inputLine.split(" ")[0]);
                    if (method == EnumMethod.POST || method == EnumMethod.DELETE) readEnd = false;
                    firstLine = false;
                }
                else if ((inputLine == null || inputLine.isEmpty()) && !firstLine) {
                    if (readEnd) {
                        /* TRAITEMENT DE LA REQUETE -------------------- */
                        Request request = HTTPBuilder.parseStringRequest(builder.toString(), clientSocket.getRemoteSocketAddress());
                        Response response;
                        if (request.isValid()) response = requestDispatcher.dispatchRequest(request);
                        else response = HTTPBuilder.buildErrorResponse(request, EnumStatusCode.SERVER_ERROR);
                        String stringResponse = HTTPBuilder.buildStringResponse(response);
                        out.println(stringResponse);
                        out.flush();
                        firstLine = true;
                        /* --------------------------------------------- */
                    }
                    else readEnd = true;
                }
                if (inputLine != null) builder.append(inputLine + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


