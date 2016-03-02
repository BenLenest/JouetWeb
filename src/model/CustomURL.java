package model;

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

    /* CONSTRUCTOR ========================================================== */

    public CustomURL(String protocol, String name, int port, String path) {
        this.protocol = protocol;
        this.name = name;
        this.port = port;
        this.path = path;
        if (path.startsWith("/")) path = path.substring(1, path.length());
        this.fields = path.split("/");
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


}
