package model;

public class Response extends Message {

    /* ATTRIBUTES ========================================================== */

    private int statusCode;

    /* CONSTRUCTOR ========================================================= */

    public Response(CustomURL url, String contentType, String content, int statusCode) {
        super(url, contentType, content);
        this.statusCode = statusCode;
    }

    /* GETTERS ============================================================= */

    public int getStatusCode() {
        return statusCode;
    }

    /* SETTERS ============================================================= */

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
