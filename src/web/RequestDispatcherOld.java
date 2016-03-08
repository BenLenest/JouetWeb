package web;

import model.Request;
import model.Response;
import model.enums.EnumMethod;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import tools.JarLoader;
import tools.Utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class used to dispatch a request to the proper service.
 */
public class RequestDispatcherOld {

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
    private final String XPATH_PARAMETERS = "/parameters/parameter";
    private String XPATH_METHOD_URL = XPATH_METHOD_URL_FILTER + "/url";
    private String XPATH_METHOD_NAME = XPATH_METHOD_URL_FILTER + "/name";

    /* CONSTRUCTOR ========================================================== */

    public RequestDispatcherOld() {
    }

    /* PUBLIC METHODS ========================================================== */

    public Response dispatchRequest(Request request) {
        try {
            // récupération des champs de l'url et préparation de la réponse
            String[] fields = request.getCustomUrl().getFields();
            //Response response = HTTPBuilder.completeResponse(request);

            // récupération de service
            Map<String, URLClassLoader> classLoader = JarLoader.getInstance().getClassLoaders();
            URLClassLoader loader = classLoader.get(fields[0] + JAR_SUFFIX);
            Document configuration = parseConfigurationFile(loader);

            // préparation de la classe à instancier
            String controllerName = findControllerClassName(configuration, fields[1]);
            Class classToLoad = Class.forName(controllerName, true, loader);

            // préparation de la méthode à appeler
            String path = findMethodPath(configuration, XPATH_APPLICATION, fields, 2, request.getMethod());
            String methodName = path.substring(path.lastIndexOf("/")+1, path.length());
            Map<String, String> argsTypes = findMethodArgs(configuration, path, methodName);
            List args = generateArgsList(argsTypes, request.getCustomUrl().getParameters());
            Method methodToUse = classToLoad.getDeclaredMethod(methodName);

            Response response = new Response(Utils.HTML_STATUS_NOT_FOUND, null, null, null, null);
            if (classToLoad != null && methodToUse != null) {
                // appel de la méthode demandée
                Object instance = classToLoad.newInstance();
                response = (Response) methodToUse.invoke(instance, args);
            }

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

    private String findMethodPath(Document configuration, String path, String[] fields, int index, EnumMethod method) {
        StringBuilder methodName = new StringBuilder();
        List urls, methods;
        String nextPath, pathQuery, identifierQuery;

        // recherche sur les champs de type "path"
        nextPath = path + XPATH_METHODS + XPATH_METHOD + XPATH_METHOD_URL_FILTER.replace(ATTRIBUTE_METHOD, method.name()).replace(ATTRIBUTE_TYPE, TYPE_PATH);
        pathQuery = path + XPATH_METHODS + XPATH_METHOD + XPATH_METHOD_URL.replace(ATTRIBUTE_METHOD, method.name()).replace(ATTRIBUTE_TYPE, TYPE_PATH);
        identifierQuery = path + XPATH_METHODS + XPATH_METHOD + XPATH_METHOD_NAME.replace(ATTRIBUTE_METHOD, method.name()).replace(ATTRIBUTE_TYPE, TYPE_PATH);

        urls = configuration.selectNodes(pathQuery);
        methods = configuration.selectNodes(identifierQuery);
        for (int i = 0; i < urls.size(); i++) {
            String url = ((Node) urls.get(i)).getStringValue();
            if (url.equals(fields[index])) {
                if (methods != null && i <= methods.size()-1) {
                    methodName.append(((Node) methods.get(i)).getStringValue());
                    if (index == fields.length - 1) return nextPath + "/" + methodName.toString();
                    else return findMethodPath(configuration, nextPath, fields, ++index, method);
                } else return findMethodPath(configuration, nextPath, fields, ++index, method);
            }
        }

        // recherche sur les champs de type "identifier"
        nextPath = path + XPATH_METHODS + XPATH_METHOD + XPATH_METHOD_URL_FILTER.replace(ATTRIBUTE_METHOD, method.name()).replace(ATTRIBUTE_TYPE, TYPE_IDENTIFIER);
        pathQuery = path + XPATH_METHODS + XPATH_METHOD + XPATH_METHOD_URL.replace(ATTRIBUTE_METHOD, method.name()).replace(ATTRIBUTE_TYPE, TYPE_IDENTIFIER);
        identifierQuery = path + XPATH_METHODS + XPATH_METHOD + XPATH_METHOD_NAME.replace(ATTRIBUTE_METHOD, method.name()).replace(ATTRIBUTE_TYPE, TYPE_IDENTIFIER);

        urls = configuration.selectNodes(pathQuery);
        methods = configuration.selectNodes(identifierQuery);
        if (index == fields.length-1) return nextPath + ((Node) methods.get(0)).getStringValue();
        else return findMethodPath(configuration, nextPath, fields, ++index, method);
    }

    private Map<String, String> findMethodArgs(Document configuration, String path, String methodName) {
        Map<String, String> methodArgs = new HashMap<>();
        path = path.substring(0, path.lastIndexOf("/"));
        path += "[name='" + methodName + "']";
        List parameters = configuration.selectNodes(path + XPATH_PARAMETERS);
        for (int i = 0 ; i < parameters.size() ; i++) {
            Node node = (Node) parameters.get(i);
            String paramName = node.getStringValue();
            String paramtype = ((Element) node).attributeValue("type");
            methodArgs.put(paramName, paramtype);
        }
        return methodArgs;
    }

    private List generateArgsList(Map<String, String> argsTypes, Map<String, String> urlArgs) {
        List args = new ArrayList<>();
        for (Map.Entry<String, String> entry : argsTypes.entrySet()) {
            if (urlArgs.containsKey(entry.getKey())) {
                //WARNING: la liste est incomplète
                String arg = urlArgs.get(entry.getKey());
                switch (entry.getValue()) {
                    case "int":
                        args.add(Integer.valueOf(arg));
                        break;
                    case "string":
                        args.add(arg);
                        break;
                    case "boolean":
                        args.add(arg.equals("true") ? true : false);
                        break;
                    case "double":
                        args.add(Double.valueOf(arg));
                        break;
                    case "float":
                        args.add(Float.valueOf(arg));
                        break;
                    default:
                        break;
                }
            }
        }
        return args;
    }
}