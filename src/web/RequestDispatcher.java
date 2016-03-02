package web;

import model.Request;
import model.Response;
import model.enums.EnumMethod;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import tools.JarLoader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

/**
 * Class used to dispatch a request to the proper service.
 */
public class RequestDispatcher {

    /* CONSTANTS ========================================================== */

    private final String JAR_SUFFIX = ".jar";
    private final String CONFIG_FILE = "configuration.xml";

    private final String ATTRIBUTE_METHOD = "(method)";
    private final String ATTRIBUTE_TYPE = "(type)";

    private final String TYPE_PATH = "path";
    private final String TYPE_IDENTIFIER = "identifier";

    private final String XPATH_APPLICATION = "/application";
    private final String XPATH_REQUEST = XPATH_APPLICATION + "/requests/request";
    private final String XPATH_REQUEST_URLS = XPATH_REQUEST + "/url";
    private final String XPATH_REQUEST_CONTROLLER = XPATH_REQUEST + "/controller";
    private final String XPATH_METHOD_URL_FILTER = "[url/@method='(method)' and url/@type='(type)']";
    private final String XPATH_METHODS = "/methods";
    private final String XPATH_METHOD = "/method";
    private String XPATH_METHOD_URL = XPATH_METHOD_URL_FILTER + "/url";
    private String XPATH_METHOD_NAME = XPATH_METHOD_URL_FILTER + "/name";

    /* CONSTRUCTOR ========================================================== */

    public RequestDispatcher() {
    }

    /* PUBLIC METHODS ========================================================== */

    public Response dispatchRequest(Request request) {
        try {
            // récupération des champs de l'url et préparation de la réponse
            String[] fields = request.getCustomUrl().getFields();
            Response response = HTTPBuilder.handleRequest(request);

            // récupération de service
            Map<String, URLClassLoader> classLoader = JarLoader.getInstance().getClassLoaders();
            URLClassLoader loader = classLoader.get(fields[0] + JAR_SUFFIX);
            Document configuration = parseConfigurationFile(loader);

            // préparation de la classe à instancier
            String controllerName = findControllerClassName(configuration, fields[1]);
            Class classToLoad = Class.forName(controllerName, true, loader);

            // préparation de la méthode à appeler
            String methodName = findMethodName(configuration, XPATH_APPLICATION, fields, 2, request.getMethod());
            Method methodToUse = classToLoad.getDeclaredMethod(methodName);

            // préparation des paramètres à passer à la méthode
            //TODO: parser les paramètres nécessaires dans le xml
            List args = null;

            // appel de la méthode demandée
            Object instance = classToLoad.newInstance();
            response.setContent(methodToUse.invoke(instance).toString());


            //TODO: en fonction du content-type voulu, formater la réponse par exemple sous forme de JSON (depuis les services)

            // retour de la réponse complète
            return response;
        }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    /* PRIVATE METHOD ========================================================== */

    private Document parseConfigurationFile(URLClassLoader classLoader) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            SAXReader reader = new SAXReader();
            Document document = reader.read(classLoader.findResource(CONFIG_FILE));
            return document;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String findControllerClassName(Document configuration, String controllerName) {
        List urls = configuration.selectNodes(XPATH_REQUEST_URLS);
        List controllers = configuration.selectNodes(XPATH_REQUEST_CONTROLLER);
        for (int i = 0 ; i < urls.size() ; i++) {
            String url = ((Node) urls.get(i)).getStringValue();
            if (url.equals(controllerName)) return ((Node) controllers.get(i)).getStringValue();
        }
        return null;
    }

    private String findMethodName(Document configuration, String path, String[] fields, int index, EnumMethod method) {
        StringBuilder methodName = new StringBuilder();
        List urls, methods;
        String nextPath;
        String pathQuery;
        String identifierQuery;

        // suppression de la partie paramètres dans l'URL
        if (fields[index].contains("&")) fields[index] = fields[index].substring(0, fields[index].indexOf("&") - 1);

        // recherche sur les champs de type "path"
        nextPath = path + XPATH_METHODS + XPATH_METHOD + XPATH_METHOD_URL_FILTER;
        nextPath = nextPath.replace(ATTRIBUTE_METHOD, method.name()).replace(ATTRIBUTE_TYPE, TYPE_PATH);
        pathQuery = path + XPATH_METHODS + XPATH_METHOD + XPATH_METHOD_URL;
        pathQuery = pathQuery.replace(ATTRIBUTE_METHOD, method.name()).replace(ATTRIBUTE_TYPE, TYPE_PATH);
        identifierQuery = path + XPATH_METHODS + XPATH_METHOD + XPATH_METHOD_NAME;
        identifierQuery = identifierQuery.replace(ATTRIBUTE_METHOD, method.name()).replace(ATTRIBUTE_TYPE, TYPE_PATH);
        urls = configuration.selectNodes(pathQuery);
        methods = configuration.selectNodes(identifierQuery);
        for (int i = 0; i < urls.size(); i++) {
            String url = ((Node) urls.get(i)).getStringValue();
            if (url.equals(fields[index])) {
                if (methods != null && i <= methods.size()-1) {
                    methodName.append(((Node) methods.get(i)).getStringValue());
                    if (index == fields.length - 1) return methodName.toString();
                    else return findMethodName(configuration, nextPath, fields, ++index, method);
                } else return findMethodName(configuration, nextPath, fields, ++index, method);
            }
        }

        // recherche sur les champs de type "identifier"
        nextPath = path + XPATH_METHODS + XPATH_METHOD + XPATH_METHOD_URL_FILTER;
        nextPath = nextPath.replace(ATTRIBUTE_METHOD, method.name()).replace(ATTRIBUTE_TYPE, TYPE_IDENTIFIER);
        pathQuery = path + XPATH_METHODS + XPATH_METHOD + XPATH_METHOD_URL;
        pathQuery = pathQuery.replace(ATTRIBUTE_METHOD, method.name()).replace(ATTRIBUTE_TYPE, TYPE_IDENTIFIER);
        identifierQuery = path + XPATH_METHODS + XPATH_METHOD + XPATH_METHOD_NAME;
        identifierQuery = identifierQuery.replace(ATTRIBUTE_METHOD, method.name()).replace(ATTRIBUTE_TYPE, TYPE_IDENTIFIER);
        urls = configuration.selectNodes(pathQuery);
        methods = configuration.selectNodes(identifierQuery);
        if (index == fields.length-1) return ((Node) methods.get(0)).getStringValue();
        else return findMethodName(configuration, nextPath, fields, ++index, method);
    }

    private List findMethodArgs(Document configuration) {
        return null;
    }
}