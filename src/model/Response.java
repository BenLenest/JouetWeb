package model;

/**
 * Class representing a Response with it's method.
 * @see model.enums.Methods
 */
public class Response extends Message {

    /* ATTRIBUTES ========================================================== */

    private String method;

    /* CONSTRUCTOR ========================================================== */

    public Response(int statusCode, String url, Header header, String method) {
        super(statusCode, url, header);
        this.method = method;
    }

    /* SETTERS ============================================================= */

    public void setMethod(String method) {
        this.method = method;
    }

    /* GETTERS ============================================================= */

    public String getMethod() {
        return method;
    }
}
