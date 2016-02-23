package web;

import model.Content;
import model.Request;
import model.enums.ContentType;
import model.Response;
import model.enums.Method;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * HTML Parser to parse HTTP Requests into Java objects
 */
public class HTTPBuilder {

    /* CONSTANTS ========================================================== */

    public final static String HOST = "Host";
    public final static String CONTENT_TYPE = "Content-Type";
    public final static String CONTENT_LENGTH = "Content-Length";

    /* PUBLIC STATIC METHODS  ============================================== */

    public static Request parseRequest(String input) {

        // attributes
        System.out.println(input);
        String[] lines = input.split("\n");
        Map<String, String> headers = parseHeaderFields(lines);

        String query = null;
        ContentType contentType = null;
        Content content = null;
        Method method = null;
        String host = null;
        int contentLength = 0;

        // parsing : method, query, host and content length
        method = Method.findMethodByValue(lines[0].split(" ")[0]);
        query = lines[0].split(" ")[1];
        if (headers.containsKey(HOST)) host = headers.get(HOST);
        if (headers.containsKey(CONTENT_LENGTH)) contentLength = Integer.parseInt(headers.get(CONTENT_LENGTH));

        // parsing : content type and content
        if (headers.containsKey(CONTENT_TYPE)) {
            String type = headers.get(CONTENT_TYPE);
            if (type.contains(ContentType.TEXT_PLAIN.getValue())) {
                contentType = ContentType.TEXT_PLAIN;
                content = new Content(getContentArray(lines), null, null);
            }
            else if (type.contains(ContentType.TEXT_HTML.getValue())) {
                contentType = ContentType.TEXT_HTML;
                content = new Content(null, getContentArray(lines), null);
            }
            else if (type.contains(ContentType.APPLICATION_JSON.getValue())) {
                contentType = ContentType.APPLICATION_JSON;
                content = new Content(null, null, parseApplicationJson(getContentArray(lines)));
            }
        }

        if(headers.containsKey("Accept")) System.out.println(headers.get("Accept"));

        // return the Request object
        return new Request(query, headers, contentType, content, method, host, 0);
    }

    private static String getContentArray(String[] lines) {
        int index = 0;
        for (int i = 0 ; i < lines.length ; i++) {
            if (lines[i].equals("")) {
                index = i+1;
                break;
            }
        }
        StringBuilder builder = new StringBuilder();
        for (int i = index ; i < lines.length; i++) {
            builder.append(lines[i]);
        }
        return builder.toString();
    }

    private static Map<String, String> parseHeaderFields(String[] lines) {
        Map<String, String> headers = new HashMap<>();
        for (int i = 1 ; i < lines.length ; i++) {
            if (lines[i].equals("") || lines[i].split(": ").length < 2) break;
            headers.put(lines[i].split(": ")[0], lines[i].split(": ")[1]);
        }
        return headers;
    }

    private static JSONObject parseApplicationJson(String contentText) {
        try {
            return new JSONObject(contentText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String buildResponse(Response resp){
        StringBuilder response = new StringBuilder("HTTP/1.1 "+resp.getStatusCode()+" OK\r\n");
        Set set = resp.getHeader().entrySet();
        // Get an iterator
        Iterator i = set.iterator();
        // Display elements
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            response.append(me.getKey()).append(": ").append(me.getValue()+"\r\n");
        }
        response.append("\r\n");
        response.append(resp.getContent().getHtmlContent());
        return response.toString();
    }
}
