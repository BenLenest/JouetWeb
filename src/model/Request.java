package model;

import model.enums.Methods;

/**
 * Class representing a request with it's method name.
 * @see Methods
 */
public class Request extends Message {

    /* ATTRIBUTES ========================================================== */

    private Methods method;
    private String host;

    /* CONSTRUCTOR ========================================================== */

    public Request(int statusCode, String url, Header header, Methods method, String host) {
        super(statusCode, url, header);
        this.method = method;
        this.host = host;
    }

    /* SETTERS ============================================================= */

    public void setMethod(Methods method) {
        this.method = method;
    }

    public void setHost(String host) {
        this.host = host;
    }

    /* GETTERS ============================================================= */

    public Methods getMethod() {
        return method;
    }

    public String getHost() {
        return host;
    }
}
