package model;

import java.util.Map;

/**
 * CustomURL Model decomposing each part of a CustomURL.
 */
public class CustomURL {

    /* ATTRIBUTES ========================================================== */

    private String protocol;
    private String name;
    private int port;
    private String path;
    private String[] fields;
    private Map<String, String> parameters;

    /* CONSTRUCTOR ========================================================== */

    public CustomURL(String protocol, String name, int port, String path, Map<String, String> parameters) {
        this.protocol = protocol;
        this.name = name;
        this.port = port;
        this.path = path;
        if (path.startsWith("/")) path = path.substring(1, path.length());
        this.fields = path.split("/");
        this.parameters = parameters;
    }

    /* SETTERS ============================================================== */

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    /* GETTERS ============================================================== */

    public String getProtocol() {
        return protocol;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public String[] getFields() {
        return fields;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
