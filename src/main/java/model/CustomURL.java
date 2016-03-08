package model;

import java.util.Map;

public class CustomURL {

    /* ATTRIBUTES ========================================================== */

    private String protocol;
    private String host;
    private int port;
    private String path;
    private String query;
    private String[] queryParts;
    private String[] argsParts;
    private Map<String, String> headerFields;
    private String applicationName;
    private String controllerName;

    /* CONSTRUCTOR ========================================================= */

    public CustomURL(String protocol, String host, int port, String path, String query, Map<String, String> headerFields, String applicationName, String controllerName) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
        this.headerFields = headerFields;
        this.applicationName = applicationName;
        this.controllerName = controllerName;
        this.query = query;
        String[] queryParts = (query != null ? query.split("\\?") : null);
        this.queryParts = (queryParts != null ? queryParts[0].split("/") : null);
        this.argsParts = (queryParts != null && queryParts.length == 2 ? queryParts[1].split("&") : null);
    }

    /* GETTERS ============================================================= */

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public String getQuery() {
        return query;
    }

    public String[] getQueryParts() {
        return queryParts;
    }

    public String[] getArgsParts() {
        return argsParts;
    }

    public Map<String, String> getHeaderFields() {
        return headerFields;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getControllerName() {
        return controllerName;
    }

    /* GETTERS ============================================================= */

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setQueryParts(String[] queryParts) {
        this.queryParts = queryParts;
    }

    public void setArgsParts(String[] argsParts) {
        this.argsParts = argsParts;
    }

    public void setHeaderFields(Map<String, String> headerFields) {
        this.headerFields = headerFields;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }
}
