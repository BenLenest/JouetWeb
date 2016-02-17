package web;

import model.Request;
import model.Response;
import model.enums.HeaderRequest;
import model.enums.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTML Parser to parse HTTP Requests into Java objects
 */
public class HTTPParser {

    public final static String HOST = "Host";

    public static Request parseRequest(String input) {

        String[] lines;
        Method method;
        Map<String, String> headers;
        String host = null;

        // split the request to separate each line
        lines = input.split(System.getProperty("line.separator"));

        // parse the request method
        method = Method.findMethodByValue(lines[0].split(" ")[0]);

        // parse the header
        headers = new HashMap<>();
        String header, value;
        for (String line : lines) {
            header = line.split(":")[0];
            value = line.split(":")[1];
            if (header != null && !header.contains(" ")) {
                headers.put(header, value);
            }
        }

        // parse the host
        if (headers.keySet().contains(HOST)) {
            host = headers.get(HOST);
        }

        return new Request(0, "url", headers, method, host);
    }

    public static StringBuilder buildResponse(Response resp){
        StringBuilder response = new StringBuilder("HTTP/1.1 "+resp.getStatusCode()+" OK\n");
        //response.append()
        return response;
    }

}
