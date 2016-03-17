package web;

import model.Request;
import model.Response;
import model.enums.EnumStatusCode;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {

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
        try {
            // Attributes
            //PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            OutputStream out = clientSocket.getOutputStream();
            InputStream is = clientSocket.getInputStream();
            byte[] buffer = new byte[1024];
            int read;
            StringBuilder builder = new StringBuilder();

            // Reading the request
            while((read = is.read(buffer)) != -1) {
                String output = new String(buffer, 0, read);
                builder.append(output);
                if (is.available() <= 0) break;
            }

            // Printing and parsing the request
            System.out.println("REQUEST :");
            System.out.println(builder.toString());
            Request request = HTTPBuilder.parseStringRequest(builder.toString(), clientSocket.getLocalSocketAddress());
            if (request != null) {
                Response response;

                // Preparing the response
                if (request.isValid()) response = requestDispatcher.dispatchRequest(request);
                else response = HTTPBuilder.buildErrorResponse(request, EnumStatusCode.SERVER_ERROR);

                // Printing and sending the response
                String stringResponse = HTTPBuilder.buildStringResponse(response);
                System.out.println("\n\nRESPONSE :");
                System.out.println(stringResponse);
                //out.println(stringResponse);
                out.write(response.getByteContent());
                out.flush();
            }

            // Closing the client socket
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
