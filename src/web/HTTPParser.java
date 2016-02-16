package web;

import model.Header;
import model.Request;
import model.enums.HeaderRequest;
import model.enums.Method;

import java.util.ArrayList;
import java.util.List;

/**
 * HTML Parser to parse HTTP Requests into Java objects
 */
public class HTTPParser {

    public static Request parseRequest(String input) {

        // split the request to separate each line
        String[] lines = input.split(System.getProperty("line.separator"));

        // parse the request method
        Method method = Method.findMethodByValue(lines[0].split(" ")[0]);

        // parse the header
        List<HeaderRequest> headers = new ArrayList<>();
        for (String line : lines) {
            HeaderRequest header;
            if ((header = HeaderRequest.findHeaderdByValue(line.split(":")[0])) != null) {
                headers.add(header);
            }
        }
        Header header = new Header(headers, null);

        return new Request(0, "url", header, method, "host");
    }

}
