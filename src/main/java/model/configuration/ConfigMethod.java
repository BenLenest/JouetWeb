package model.configuration;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ConfigMethod {

    /* CONSTANTS =========================================================== */

    private final String REGEX_STRING = "[A-Za-z0-9]+";
    private final String REGEX_INT = "\\d+";
    private final String REGEX_DOUBLE = "\\d+(\\.\\d+)?";
    private final String REGEX_FLOAT = "\\d+(\\.\\d+)?";
    private final String REGEX_ARGS = "\\?";

    private final String REGEX_TYPE_STRING = "\\(string:" + REGEX_STRING + "\\)";
    private final String REGEX_TYPE_INT = "\\(int:" + REGEX_STRING + "\\)";
    private final String REGEX_TYPE_DOUBLE = "\\(double:" + REGEX_STRING + "\\)";
    private final String REGEX_TYPE_FLOAT = "\\(float:" + REGEX_STRING + "\\)";

    /* ATTRIBUTES ========================================================== */

    @SerializedName("method") private String method;
    @SerializedName("url") private String url;
    @SerializedName("name") private String name;
    @SerializedName("args") private ConfigArgs[] args;

    private String regex;
    private String[] queryParts;
    private String[] argsParts;
    private String[] regexPathParts;
    private String[] regexArgsParts;
    private Class[] urlArgsTypes;
    private Class[] postArgsTypes;

    /* CONSTRUCTOR ========================================================= */

    public ConfigMethod(String method, String url, String name, ConfigArgs[] args) {
        this.method = method;
        this.url = url;
        this.name = name;
        this.args = args;
    }

    /* GETTERS ============================================================= */

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public ConfigArgs[] getArgs() {
        return args;
    }

    public Class[] getUrlArgsTypes() {
        return urlArgsTypes;
    }

    public Class[] getPostArgsTypes() {
        return postArgsTypes;
    }

    public String getRegex() {
        return regex;
    }

    public String[] getArgsParts() {
        return argsParts;
    }

    public String[] getQueryParts() {
        return queryParts;
    }

    public String[] getRegexArgsParts() {
        return regexArgsParts;
    }

    public String[] getRegexPathParts() {
        return regexPathParts;
    }

    /* PUBLIC METHODS ====================================================== */

    public void generateUrlRegexAndTypesArray() {

        List<Class> urlTypes = new ArrayList<>();
        List<Class> postTypes = new ArrayList<>();
        String[] parts = url.split(REGEX_ARGS);
        String urlPart = parts[0];
        String argsPart = (parts.length == 2 ? parts[1] : null);

        // Building the url regex
        queryParts = urlPart.split("/");
        StringBuilder builder = new StringBuilder();
        for (int i = 0 ; i < queryParts.length ; i++) {
            String regex = getUrlTypes(queryParts[i], urlTypes);
            if (regex != null) builder.append(regex); else builder.append(queryParts[i]);
            if (i < queryParts.length - 1) builder.append("/");
        }

        // Building the GET parameters regex
        if (argsPart != null) {
            builder.append("\\?");
            argsParts = argsPart.split("&");
            for (int i = 0 ; i < argsParts.length ; i++) {
                String[] argParts = argsParts[i].substring(1, argsParts[i].length()-1).split(":");
                String regex = getUrlTypes(argsParts[i], urlTypes);
                if (regex != null) builder.append(argParts[1] + "=" + regex);
                else builder.append(argsParts[i]);
                if (i < argsParts.length - 1) builder.append("&");
            }
        }

        // Retrieving the POST parameters types
        if (args != null) for (ConfigArgs configArgs : args) getPostTypes(configArgs.getType(), postTypes);

        // Building the parameters types array for reflexion
        urlArgsTypes = new Class[urlTypes.size()];
        for (int i = 0 ; i < urlTypes.size() ; i++) urlArgsTypes[i] = urlTypes.get(i);
        postArgsTypes = new Class[postTypes.size()];
        for (int i = 0 ; i < postTypes.size() ; i++) postArgsTypes[i] = postTypes.get(i);

        // Setting the final url regex
        regex = builder.toString();
    }

    public void generateRegexParts() {
        if (regex.contains("\\?")) {
            String urlPart = regex.substring(0, regex.indexOf("\\?"));
            regexPathParts = urlPart.split("/");
            String argsPart = regex.replace(urlPart + "\\?", "");
            regexArgsParts = argsPart.substring(0).split("&");
        }
        else regexPathParts = regex.split("/");
    }

    public String getUrlTypes(String part, List<Class> types) {
        if (part.matches(REGEX_TYPE_STRING)) { types.add(String.class); return REGEX_STRING; }
        else if (part.matches(REGEX_TYPE_INT)) { types.add(int.class); return REGEX_INT; }
        else if (part.matches(REGEX_TYPE_DOUBLE)) { types.add(double.class); return REGEX_DOUBLE; }
        else if (part.matches(REGEX_TYPE_FLOAT)) { types.add(float.class); return REGEX_FLOAT; }
        return null;
    }

    public void getPostTypes(String type, List<Class> types) {
        switch (type) {
            case "int": types.add(int.class); break;
            case "string": types.add(String.class); break;
            case "double": types.add(double.class); break;
            case "float": types.add(float.class); break;
            default: break;
        }
    }

    public Class[] getRequestArgsTypes() {
        Class[] totalArray = new Class[urlArgsTypes.length + postArgsTypes.length];
        System.arraycopy(urlArgsTypes, 0, totalArray, 0, urlArgsTypes.length);
        System.arraycopy(postArgsTypes, 0, totalArray, urlArgsTypes.length, postArgsTypes.length);
        return totalArray;
    }
}
