package web;

import com.google.gson.Gson;
import model.CustomURL;
import model.Request;
import model.Response;
import model.configuration.ConfigContainer;
import model.configuration.ConfigMethod;
import model.configuration.ConfigRequest;
import model.enums.EnumStatusCode;
import tools.JarLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.Map;

public class RequestDispatcher {

    /* CONSTANTS =========================================================== */

    private final String JAR_SUFFIX = ".jar";
    private final String CONFIG_FILE = "configuration.json";

    /* CONSTRUCTOR ========================================================= */

    public RequestDispatcher() {}

    /* PUBLIC METHODS ====================================================== */

    public Response dispatchRequest(Request request) {
        try {
            CustomURL url = request.getUrl();

            // Retrieving the proper jar to use and it's configuration file
            Map<String, URLClassLoader> classLoader = JarLoader.getInstance().getClassLoaders();
            URLClassLoader loader = classLoader.get(url.getApplicationName() + JAR_SUFFIX);
            ConfigContainer configuration = new Gson().fromJson(parseConfigurationFile(loader), ConfigContainer.class);

            // Retrieving the proper request
            ConfigRequest requestController = configuration.findRequestControllerByName(url.getControllerName());
            if (requestController == null) return HTTPBuilder.buildErrorResponse(request, EnumStatusCode.NOT_FOUND);
            requestController.generateMethodsRegex();
            Class classToLoad = Class.forName(requestController.getController(), true, loader);

            // Retrieving the proper method
            ConfigMethod requestMethod = requestController.findMethodByTypeAndUrl(request.getMethod(), url.getQuery());
            if (requestMethod == null) return HTTPBuilder.buildErrorResponse(request, EnumStatusCode.BAD_REQUEST);
            Method methodToUse = classToLoad.getDeclaredMethod(requestMethod.getName(), requestMethod.getRequestArgsTypes());

            // Retrieving the parameters values
            Object[] args = HTTPBuilder.parseParametersValues(request, requestMethod);

            // Calling the method
            Object instance = classToLoad.newInstance();
            Response response = (Response) methodToUse.invoke(instance, args);

            return HTTPBuilder.completeResponseHeader(request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
            return HTTPBuilder.buildErrorResponse(request, EnumStatusCode.SERVER_ERROR);
        }
    }

    /* PRIVATE METHOD ====================================================== */

    private String parseConfigurationFile(URLClassLoader classLoader) {
        try {
            InputStream is = classLoader.getResourceAsStream(CONFIG_FILE);
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
}
