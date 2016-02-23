package model;

/**
 * URL Model decomposing each part of a URL.
 */
public class URL {

    /* ATTRIBUTES ========================================================== */

    private String protocol;
    private String name;
    private int port;
    private String path;

    /* CONSTRUCTOR ========================================================== */

    public URL(String protocol, String name, int port, String path) {
        this.protocol = protocol;
        this.name = name;
        this.port = port;
        this.path = path;
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
}
