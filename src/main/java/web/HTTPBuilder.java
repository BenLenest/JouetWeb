package web;

import model.CustomURL;
import model.Request;
import model.Response;
import model.Session;
import model.configuration.ConfigMethod;
import model.enums.EnumContentType;
import model.enums.EnumHeaderFields;
import model.enums.EnumMethod;
import model.enums.EnumStatusCode;
import org.json.JSONObject;

import java.io.File;
import java.net.SocketAddress;
import java.util.*;

public class HTTPBuilder {

    /* PUBLIC STATIC METHODS (Request) ===================================== */

    public static Request parseStringRequest(String stringRequest, SocketAddress ip) {

        // parsing of the header and content parts
        stringRequest = stringRequest.replace("\r", "");
        int index = stringRequest.indexOf("\n\n");
        String headerPart, contentPart = null;
        headerPart = (index != -1 ? stringRequest.substring(0, index) : stringRequest);
        contentPart = (index != -1 ? stringRequest.substring(index, stringRequest.length()) : null);

        // Secure against favicon request
        if (headerPart.split("\n")[0].contains("favicon")) return null;

        // parsing of the header detail
        CustomURL url = parseRequestUrl(headerPart);
        String contentType = url.getHeaderFields().get(EnumHeaderFields.CONTENT_TYPE.value);
        if (url.getControllerName() != null) {
            EnumMethod method = EnumMethod.findMethodByValue(headerPart.split(" ")[0]);
            Session session = parseRequestSession(url, ip, url.getApplicationName());
            return new Request(url, contentType, contentPart, session, true, method);
        } else {
            return new Request(url, contentType, contentPart, null, false, null);
        }
    }

    public static CustomURL parseRequestUrl(String headerPart) {

        // parsing header fields
        Map<String, String> headerFields = new HashMap<>();
        String[] headerLines = headerPart.split("\n");
        for (int i = 1 ; i < headerLines.length ; i++) {
            String[] parts = headerLines[i].split(": ");
            if (parts.length == 2) {
                String key = parts[0];
                String value = parts[1];
                headerFields.put(key, value);
            }
        }

        // parsing main information
        String[] firstLine = headerPart.split("\n")[0].split(" ");
        String protocol = firstLine[2];
        String host = headerFields.get(EnumHeaderFields.HOST.value);
        int port = 0; //TODO: récupérer le bon port
        String path = firstLine[1];
        String[] pathParts = path.split("/");

        if (pathParts.length > 2) {
            String applicationName = pathParts[1];
            String controllerName = pathParts[2];
            StringBuilder builder = new StringBuilder();
            for (int i = 3; i < pathParts.length; i++) {
                builder.append(pathParts[i]);
                if (i < pathParts.length - 1) builder.append("/");
            }
            return new CustomURL(protocol, host, port, path, builder.toString(), headerFields, applicationName, controllerName);
        } else {
            return new CustomURL(protocol, host, port, path, null, headerFields, null, null);
        }
    }

    public static Session parseRequestSession(CustomURL url, SocketAddress ip, String applicationName) {
        String cookie = url.getHeaderFields().get(EnumHeaderFields.COOKIE.value);
        Map<String, String> values = cookie != null ? parseCookie(cookie) : new LinkedHashMap<>();
        String key = values.size() > 0 ? values.get(Session.SESSION_TOKEN) : null;
        if (key != null) {
            return SessionsManager.getInstance().getSessions().get(key);
        } else {
            key = (ip.toString() + url.getHeaderFields().get(EnumHeaderFields.USER_AGENT.value) + applicationName).replace(";", "").replace(" ", "");
            values.put(Session.SESSION_TOKEN, key);
            values.put(Session.SESSION_PATH, File.separator + url.getApplicationName() + File.separator + url.getControllerName());
            //values.put(Session.SESSION_PATH, File.separator + url.getApplicationName() + File.separator + url.getControllerName());
            //values.put(Session.SESSION_EXPIRES, new SimpleDateFormat(Session.SESSION_EXPIRES_DATE_FORMAT).format(new Date()).toString());
            if (SessionsManager.getInstance().getSessions().containsKey(key)) SessionsManager.getInstance().getSessions().remove(key);
            SessionsManager.getInstance().getSessions().put(key, new Session(key, values));
            return SessionsManager.getInstance().getSessions().get(key);
        }
    }

    private static Map<String, String> parseCookie(String cookie){
        Map<String, String> values = new LinkedHashMap<>();
        String[] cookieValues = cookie.split("; ");
        for (int i = 0 ; i < cookieValues.length ; i++) {
            String[] parts = cookieValues[i].split("=");
            values.put(parts[0], parts[1]);
        }
        return values;
    }

    public static Object[] parseParametersValues(Request request, ConfigMethod requestMethod) {

        // Preparing the array to return
        Object[] argsValues;
        List<Object> argsList = new ArrayList<>();

        // Retrieving the url parts
        String[] queryPathParts = request.getUrl().getQueryParts();
        String[] queryArgsParts = request.getUrl().getArgsParts();

        String[] configPathParts = requestMethod.getQueryParts();
        String[] configArgsParts = requestMethod.getArgsParts();

        String[] regexPathParts = requestMethod.getRegexPathParts();
        String[] regexArgsParts = requestMethod.getRegexArgsParts();

        // Parsing request path parameters
        for (int i = 0 ; i < queryPathParts.length ; i++) {
            if (!queryPathParts[i].equals(regexPathParts[i]) && queryPathParts[i].matches(regexPathParts[i])) {
                String value = queryPathParts[i];
                String type = configPathParts[i].substring(1, configPathParts[i].indexOf(":"));
                findArgType(argsList, type, value);
            }
        }

        // Parsing request args parameters
        if (queryArgsParts != null) {
            for (int i = 0; i < queryArgsParts.length; i++) {
                String type = configArgsParts[i].substring(1, configArgsParts[i].indexOf(":"));
                String value = queryArgsParts[i].substring(queryArgsParts[i].indexOf("=")+1, queryArgsParts[i].length());
                findArgType(argsList, type, value);
            }
        }

        if (request.getContent() != null && !request.getContent().equals("") && !request.getContent().equals("\n\n")) {

            // Parsing request POST parameters - JSON
            if (request.getContentType().matches(EnumContentType.APPLICATION_JSON.value)) {
                Class[] postTypes = requestMethod.getPostArgsTypes();
                JSONObject jsonObject = new JSONObject(request.getContent());
                for (int i = 0; i < jsonObject.names().length(); i++) {
                    String value = jsonObject.get(jsonObject.names().get(i).toString()).toString();
                    if (postTypes[i] == int.class) argsList.add(Integer.valueOf(value));
                    else if (postTypes[i] == String.class) argsList.add(value);
                    else if (postTypes[i] == double.class) argsList.add(Double.valueOf(value));
                    else if (postTypes[i] == float.class) argsList.add(Float.valueOf(value));
                }
            }

            // Parsing request POST parameters - HTML
            if (request.getContentType().matches(EnumContentType.TEXT_HTML.value)) {
                Class[] postTypes = requestMethod.getPostArgsTypes();
                String[] parameters = request.getContent().split("&");
                for (int i = 0; i < parameters.length; i++) {
                    String value = parameters[i].split("=")[1];
                    if (postTypes[i] == int.class) argsList.add(Integer.valueOf(value));
                    else if (postTypes[i] == String.class) argsList.add(value);
                    else if (postTypes[i] == double.class) argsList.add(Double.valueOf(value));
                    else if (postTypes[i] == float.class) argsList.add(Float.valueOf(value));
                }
            }

        }

        // Building the args array
        argsValues = new Object[argsList.size()+1];
        argsValues[0] = request.getSession();
        for (int i = 0 ; i < argsList.size() ; i++) argsValues[i+1] = argsList.get(i);
        return argsValues;
    }

    private static void findArgType(List<Object> argsList, String type, String value) {
        switch (type) {
            case "int": argsList.add(Integer.valueOf(value)); break;
            case "string": argsList.add(value); break;
            case "double": argsList.add(Double.valueOf(value)); break;
            case "float": argsList.add(Float.valueOf(value)); break;
            default: break;
        }
    }

    /* PUBLIC STATIC METHODS (Response) ==================================== */

    public static String buildStringResponse(Response response) {
        StringBuilder builder = new StringBuilder(response.getUrl().getProtocol() + " "
                + response.getStatusCode() + " "
                + EnumStatusCode.findNameByValue(response.getStatusCode()) + "\r\n");
        for (Map.Entry<String, String> entry : response.getUrl().getHeaderFields().entrySet()) {
            if (!entry.getKey().equals(EnumHeaderFields.COOKIE.value) && !entry.getKey().equals(EnumHeaderFields.SET_COOKIE.value))
                builder.append(entry.getKey() + ": " + entry.getValue() + "\r\n");
        }
        builder.append(EnumHeaderFields.SET_COOKIE.value + ":");
        for (Map.Entry<String, String> entry : response.getSession().getValues().entrySet()) {
            builder.append(" " + entry.getKey() + "=" + entry.getValue() + ";");
        }
        builder.append("\r\n\r\n");
        builder.append(response.getContent());
        return builder.toString();
    }

    public static Response completeResponseHeader(Request request, Response response) {
        CustomURL responseUrl = request.getUrl();
        responseUrl.getHeaderFields().remove(EnumHeaderFields.CONTENT_LENGTH.value);
        responseUrl.getHeaderFields().put(EnumHeaderFields.CONTENT_LENGTH.value, String.valueOf(response.getContent().toString().length()));
        if (request.getSession()!=null) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : request.getSession().getValues().entrySet())
                builder.append(entry.getKey() + "=" + entry.getValue() + "; ");
            builder.replace(builder.length()-1, builder.length(), "");
            responseUrl.getHeaderFields().put(EnumHeaderFields.SET_COOKIE.value, builder.toString());
        }
        response.setUrl(responseUrl);
        updateSessionFromResponse(response);
        return response;
    }

    public static Response buildErrorResponse(Request request, EnumStatusCode statusCode) {
        if (request != null) {
            return HTTPBuilder.completeResponseHeader(
                    request,
                    new Response(null,
                            EnumContentType.TEXT_HTML.value,
                            EnumStatusCode.findMessageByValue(statusCode.code),
                            request.getSession(),
                            statusCode.code)
            );
        } else {
            return new Response(null,
                    EnumContentType.TEXT_HTML.value,
                    EnumStatusCode.findMessageByValue(statusCode.code),
                    request.getSession(),
                    statusCode.code);
        }
    }

    public static void updateSessionFromResponse(Response response) {
        if (response.getSession() != null) {
            Session newSession = new Session(response.getSession().getKey(), new LinkedHashMap<>());
            for (Map.Entry<String, String> entry : response.getSession().getValues().entrySet()) {
                if (!entry.getKey().equals(Session.SESSION_EXPIRES) && !entry.getKey().equals(Session.SESSION_PATH))
                    newSession.getValues().put(entry.getKey(), entry.getValue());
            }
            if (response.getSession().getValues().containsKey(Session.SESSION_EXPIRES))
                newSession.getValues().put(Session.SESSION_EXPIRES, response.getSession().getValues().get(Session.SESSION_EXPIRES));
            if (response.getSession().getValues().containsKey(Session.SESSION_PATH))
                newSession.getValues().put(Session.SESSION_PATH, response.getSession().getValues().get(Session.SESSION_PATH));
            response.setSession(newSession);
            if (SessionsManager.getInstance().getSessions().containsKey(response.getSession().getKey())) {
                SessionsManager.getInstance().getSessions().remove(response.getSession().getKey());
                SessionsManager.getInstance().getSessions().put(response.getSession().getKey(), response.getSession());
            }
        }
    }
}