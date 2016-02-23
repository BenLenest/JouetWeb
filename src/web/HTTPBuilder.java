package web;

import model.Request;
import tools.JarLoader;
import tools.Utils;
import model.Response;
import model.enums.Method;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * HTML Parser to parse HTTP Requests into Java objects
 */
public class HTTPBuilder {

    private static final String BASE_URL = "/myproject/hello.html";

    /* PUBLIC STATIC METHODS  ============================================== */

    /**
     *
     * @param input
     * @return Request
     * String to object Request
     */
    public static Request parseRequest(String input) {

        // attributes
        String[] lines = input.split("\n");
        for(int i=0; i < lines.length; i++){
            System.out.println(i + " : " + lines[i]);
        }
        Map<String, String> headers = parseHeaderFields(lines);

        String query;
        String protocol;
        String contentType = "";
        String content = "";
        Method method;
        String host = "";

        // parsing : method, query, protocol, host and content length
        String[] head = lines[0].split(" ");
        method = Method.findMethodByValue(head[0]);
        query = (head[1].equals("/") ? BASE_URL : head[1]);     //TODO: on devrait pas rediriger vers l'index (genre "http://truc.com/index.html") ?
        protocol = head[2];
        if (headers.containsKey(Utils.HOST)) host = headers.get(Utils.HOST);

        // parsing : content type and content
        if (headers.containsKey(Utils.CONTENT_TYPE)) {
            String type = headers.get(Utils.CONTENT_TYPE);
            if (type.contains(Utils.TEXT_PLAIN)) {
                contentType = Utils.TEXT_PLAIN;
            }
            else if (type.contains(Utils.TEXT_HTML)) {
                contentType = Utils.TEXT_HTML;
            }
            else if (type.contains(Utils.APPLICATION_JSON)) {
                contentType = Utils.APPLICATION_JSON;
            }
            content = getContentArray(lines);
        }

        /* Exemple d'appel de méthode dans un jar
        try {
            URLClassLoader classLoader = JarLoader.getInstance().getClassLoader();
            Class classToLoad = Class.forName("Controller", true, classLoader);
            java.lang.reflect.Method methodToUse = classToLoad.getDeclaredMethod("brioche");
            Object instance = classToLoad.newInstance();
            Object result = methodToUse.invoke(instance);
            System.out.println(result.toString());
        }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
        catch (InstantiationException e) { e.printStackTrace(); }
        catch (IllegalAccessException e) { e.printStackTrace(); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }
        catch (InvocationTargetException e) { e.printStackTrace(); }*/

        // return the Request object
        return new Request(query, headers, contentType, content, method, host, 0, protocol);
    }

    /**
     *
     * @param request
     * @return Response
     * @throws IOException
     * Generate response after receive a request
     */
    public static Response handleRequest(Request request) throws IOException {
        if (request != null) {
            Map<String, String> headers = request.getHeader();
            String type = "text/html";
            if(headers.containsKey("Accept"))
                type = headers.get("Accept");
            String url = request.getUrl();
            StringBuilder content = new StringBuilder();
            int size = 0;

            Map<String, String> header = new HashMap<>();
            header.put("Date", new Date().toString());
            header.put("Server", "JouetWeb");
            header.put("Last-Modified", new Date().toString());
            header.put("Connection", "Closed");

            if(url.equals("/echo")){
                if(type.contains(Utils.TEXT_HTML)){
                    content.append(HTTPBuilder.buildHTMLContent(request));
                    size = HTTPBuilder.buildHTMLContent(request).length();
                    type = Utils.TEXT_HTML;
                }else if(type.contains(Utils.APPLICATION_JSON)){
                    content.append(HTTPBuilder.buildJSONContent(request));
                    size = HTTPBuilder.buildJSONContent(request).length();
                    type = Utils.APPLICATION_JSON;
                }else if(type.contains(Utils.TEXT_PLAIN)){
                    content.append(HTTPBuilder.buildPLAINContent(request));
                    size = HTTPBuilder.buildPLAINContent(request).length();
                    type = Utils.TEXT_PLAIN;
                }
            }else{
                for (String line : Files.readAllLines(Paths.get("."+url))) {
                    content.append(line);
                    size += line.length();
                }
                if(type.contains(Utils.TEXT_HTML)) type = Utils.TEXT_HTML;
                else if(type.contains(Utils.APPLICATION_JSON)) type = Utils.APPLICATION_JSON;
                else if(type.contains(Utils.TEXT_PLAIN)) type = Utils.TEXT_PLAIN;
            }

            header.put(Utils.CONTENT_LENGTH, Integer.toString(size));
            header.put(Utils.CONTENT_TYPE, type);

            return new Response(200, url, header, request.getContentType(), content.toString());
        } else {
            System.out.println("--> The received request couldn't be parsed.");
        }
        return null;
    }

    /**
     *
     * @param resp
     * @return String
     * Response to String
     */
    public static String buildResponse(Response resp){
        StringBuilder response = new StringBuilder("HTTP/1.1 "+resp.getStatusCode()+" OK\r\n");
        Set set = resp.getHeader().entrySet();
        Iterator i = set.iterator();
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            response.append(me.getKey()).append(": ").append(me.getValue()+"\r\n");
        }
        response.append("\r\n");
        response.append(resp.getContent());
        return response.toString();
    }

    /* PRIVATE METHODS ============================================== */

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

    private static String buildHTMLContent(Request req){
        StringBuilder html = new StringBuilder("<!DOCTYPE html><html><head><title>My project</title><meta charset='utf-8'></meta></head><body>");
        html.append("<h1>Entête de la requête</h1><p>")
                .append(req.getMethod())
                .append(" ")
                .append(req.getUrl())
                .append(" ")
                .append(req.getProtocol()).append("</p><h1>Header(s) de la requête</h1>");

        Set set = req.getHeader().entrySet();
        Iterator i = set.iterator();
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            html.append("<p>").append(me.getKey()).append(": ").append(me.getValue()).append("</p>");
        }
        switch(req.getContentType()){
            case Utils.TEXT_HTML :
                System.out.println("haha");
                break;
            case Utils.APPLICATION_JSON :
                System.out.println("hoho");
                break;
            case Utils.TEXT_PLAIN :
                System.out.println("hihi");
                break;
        }
        html.append("<h1>Content de la requête</h1><p>").append(req.getContent()).append("</p>");
        html.append("</body></html>");
        return html.toString();

    }

    private static String buildJSONContent(Request req){
        JSONObject json = new JSONObject();
        try {
            json.append("Method", req.getMethod());
            json.append("Url", req.getUrl());
            json.append("Protocol", req.getProtocol());

            Set set = req.getHeader().entrySet();
            // Get an iterator
            Iterator i = set.iterator();
            // Display elements
            while(i.hasNext()) {
                Map.Entry me = (Map.Entry)i.next();
                json.append((String) me.getKey(), me.getValue());
            }
            json.append("Content", req.getContent());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    private static String buildPLAINContent(Request req){
        return "";
    }
}
