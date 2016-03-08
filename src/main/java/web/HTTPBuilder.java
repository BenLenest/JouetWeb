package web;

import model.CustomURL;
import model.Request;
import model.Response;
import model.configuration.ConfigMethod;
import model.enums.EnumContentType;
import model.enums.EnumMethod;
import model.enums.EnumStatusCode;
import org.json.JSONObject;

import java.util.*;

public class HTTPBuilder {

    /* PUBLIC STATIC METHODS (Request) ===================================== */

    public static Request parseStringRequest(String stringRequest) {

        // parsing of the header and content parts
        int index = stringRequest.indexOf("\n\n");
        String headerPart, contentPart = null;
        headerPart = (index != -1 ? stringRequest.substring(0, index) : stringRequest);
        contentPart = (index != -1 ? stringRequest.substring(index, stringRequest.length()) : null);

        // parsing of the header detail
        CustomURL url = parseRequestUrl(headerPart);
        String contentType = url.getHeaderFields().get("Content-Type");
        if (url.getControllerName() != null) {
            EnumMethod method = EnumMethod.findMethodByValue(headerPart.split(" ")[0]);
            return new Request(url, contentType, contentPart, true, method);
        } else {
            return new Request(url, contentType, contentPart, false, null);
        }
    }

    public static CustomURL parseRequestUrl(String headerPart) {

        // parsing header fields
        Map<String, String> headerFields = new HashMap<>();
        String[] headerLines = headerPart.split("\n");
        for (int i = 1 ; i < headerLines.length ; i++) {
            String key = headerLines[i].split(": ")[0];
            String value = headerLines[i].split(": ")[1];
            headerFields.put(key, value);
        }

        // parsing main information
        String[] firstLine = headerPart.split("\n")[0].split(" ");
        String protocol = firstLine[2];
        String host = headerFields.get("Host");
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

        // Parsing request POST parameters
        if (request.getContentType().equals(EnumContentType.APPLICATION_JSON.value) && request.getContent() != null) {
            Class[] postTypes = requestMethod.getPostArgsTypes();
            JSONObject jsonObject = new JSONObject(request.getContent());
            for (int i = 0 ; i < jsonObject.names().length() ; i++) {
                String value = jsonObject.get(jsonObject.names().get(i).toString()).toString();
                if (postTypes[i] == int.class) argsList.add(Integer.valueOf(value));
                else if (postTypes[i] == String.class) argsList.add(value);
                else if (postTypes[i] == double.class) argsList.add(Double.valueOf(value));
                else if (postTypes[i] == float.class) argsList.add(Float.valueOf(value));
            }
        }

        // Building the args array
        argsValues = new Object[argsList.size()];
        for (int i = 0 ; i < argsList.size() ; i++) argsValues[i] = argsList.get(i);
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
        for (Map.Entry<String, String> entry : response.getUrl().getHeaderFields().entrySet())
            builder.append(entry.getKey() + ": " + entry.getValue() + "\r\n");
        builder.append("\r\n");
        builder.append(response.getContent());
        return builder.toString();
    }

    public static Response completeResponseHeader(Request request, Response response) {
        CustomURL responseUrl = request.getUrl();
        responseUrl.getHeaderFields().remove("Content-Length");
        responseUrl.getHeaderFields().put("Content-Length", String.valueOf(response.getContent().toString().length()));
        response.setUrl(responseUrl);
        return response;
    }

    public static Response buildErrorResponse(Request request, EnumStatusCode statusCode) {
        if (request != null) {
            return HTTPBuilder.completeResponseHeader(
                    request,
                    new Response(null,
                            EnumContentType.TEXT_HTML.value,
                            EnumStatusCode.findMessageByValue(statusCode.code),
                            statusCode.code)
            );
        } else {
            return new Response(null,
                    EnumContentType.TEXT_HTML.value,
                    EnumStatusCode.findMessageByValue(statusCode.code),
                    statusCode.code);
        }
    }
}