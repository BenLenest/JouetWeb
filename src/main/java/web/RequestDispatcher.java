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
import tools.JarLoader;

import java.io.BufferedReader;
import java.io.File;
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
    private final String CONFIG_FILE = "configuration.json";
    private final String DIRECTORY_JS = "web/js/";
    private final String DIRECTORY_CSS = "web/css/";

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

            // Check if the request is for a JavaScript or CSS
            if (url.getQuery().contains(SUFFIX_JS) || url.getQuery().contains(SUFFIX_CSS)) {
                response = getJsOrCssContent(url.getQuery(), loader);
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

            return HTTPBuilder.completeResponseHeader(request, response);
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

    private Response getJsOrCssContent(String fileName, URLClassLoader loader) {
        Response response = null;
        String content = null;
        String contentType = null;
        if (fileName.contains(SUFFIX_JS)) {
            content = parseJarFileByName(loader, DIRECTORY_JS + fileName);
            contentType = EnumContentType.APPLICATION_JAVASCRIPT.value;
        }
        else if (fileName.contains(SUFFIX_CSS)) {
            content = parseJarFileByName(loader, DIRECTORY_CSS + fileName);
            contentType = EnumContentType.TEXT_CSS.value;
        }
        if (content != null) response = new Response(null, contentType, content, null, EnumStatusCode.SUCCESS.code);
        else response = new Response(null, contentType, null, null, EnumStatusCode.NOT_FOUND.code);
        return response;
    }
}
