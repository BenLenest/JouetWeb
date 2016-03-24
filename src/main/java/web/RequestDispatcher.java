package web;

import com.google.gson.Gson;
import model.CustomURL;
import model.Request;
import model.Response;
import model.configuration.ConfigContainer;
import model.configuration.ConfigMethod;
import model.configuration.ConfigRequest;
import model.enums.EnumContentType;
import model.enums.EnumStatusCode;
import org.apache.commons.io.IOUtils;
import tools.JarLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.Map;

public class RequestDispatcher {

    /* CONSTANTS =========================================================== */

    private final String SUFFIX_JAR = ".jar";
    private final String SUFFIX_JS = ".js";
    private final String SUFFIX_CSS = ".css";
    private final String SUFFIX_FONT = ".ttf";
    private final String SUFFIX_IMG = ".png";
    private final String CONFIG_FILE = "configuration.json";
    private final String DIRECTORY_JS = "web/js/";
    private final String DIRECTORY_CSS = "web/css/";
    private final String DIRECTORY_FONT = "web/font/";
    private final String DIRECTORY_IMG = "web/img/";

    /* CONSTRUCTOR ========================================================= */

    public RequestDispatcher() {}

    /* PUBLIC METHODS ====================================================== */

    public Response dispatchRequest(Request request) {
        try {
            CustomURL url = request.getUrl();
            Response response = null;

            // Retrieving the proper jar to use and it's configuration file
            Map<String, URLClassLoader> classLoader = JarLoader.getInstance().getClassLoaders();
            URLClassLoader loader = classLoader.get(url.getApplicationName() + SUFFIX_JAR);
            ConfigContainer configuration = new Gson().fromJson(parseJarFileByName(loader, CONFIG_FILE), ConfigContainer.class);

            // Retrieving the proper request
            ConfigRequest requestController = configuration.findRequestControllerByName(url.getControllerName());
            if (requestController == null) return HTTPBuilder.buildErrorResponse(request, EnumStatusCode.NOT_FOUND);
            requestController.generateMethodsRegex();
            Class classToLoad = Class.forName(requestController.getController(), true, loader);

            // Check if the request is for a JavaScript or CSS file
            if (url.getQuery().contains(SUFFIX_JS) || url.getQuery().contains(SUFFIX_CSS) || url.getQuery().contains(SUFFIX_FONT) || url.getQuery().contains(SUFFIX_IMG)) {
                response = getResource(url.getQuery(), loader);
            }
            else {
                // Retrieving the proper method
                ConfigMethod requestMethod = requestController.findMethodByTypeAndUrl(request.getMethod(), url.getQuery());
                if (requestMethod == null) return HTTPBuilder.buildErrorResponse(request, EnumStatusCode.BAD_REQUEST);
                Method methodToUse = classToLoad.getDeclaredMethod(requestMethod.getName(), requestMethod.getRequestArgsTypes());

                // Retrieving the parameters values
                Object[] args = HTTPBuilder.parseParametersValues(request, requestMethod);

                // Calling the method
                Object instance = classToLoad.newInstance();
                response = (Response) methodToUse.invoke(instance, args);
            }

            response = HTTPBuilder.completeResponseHeader(request, response);
            return response;
        }
        catch (Exception e) {
            e.printStackTrace();
            return HTTPBuilder.buildErrorResponse(request, EnumStatusCode.SERVER_ERROR);
        }
    }

    /* PRIVATE METHOD ====================================================== */

    private String parseJarFileByName(URLClassLoader classLoader, String name) {
        try {
            InputStream is = classLoader.getResourceAsStream(name);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String input;
            while ((input = br.readLine()) != null) builder.append(input);
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Response getResource(String fileName, URLClassLoader loader) {
        Response response;
        byte[] content = null;
        String contentType = null;
        String path = null;
        if (fileName.contains(SUFFIX_JS)) {
            path = DIRECTORY_JS + fileName;
            contentType = EnumContentType.APPLICATION_JAVASCRIPT.value;
        }
        else if (fileName.contains(SUFFIX_CSS)) {
            path = DIRECTORY_CSS + fileName;
            contentType = EnumContentType.TEXT_CSS.value;
        }
        else if (fileName.contains(SUFFIX_FONT)) {
            path = DIRECTORY_FONT + fileName;
            contentType = EnumContentType.TEXT_CSS.value;
        }
        else if (fileName.contains(SUFFIX_IMG)) {
            path = DIRECTORY_IMG + fileName;
            contentType = EnumContentType.TEXT_CSS.value;
        }
        try { content = IOUtils.toByteArray(loader.getResourceAsStream(path)); }
        catch (IOException e) { e.printStackTrace(); }
        if (content != null) response = new Response(null, contentType, null, null, EnumStatusCode.SUCCESS.code, content);
        else response = new Response(null, contentType, null, null, EnumStatusCode.NOT_FOUND.code, null);
        return response;
    }
}
