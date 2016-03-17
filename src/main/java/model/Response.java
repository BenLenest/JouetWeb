package model;

public class Response extends Message {

    /* ATTRIBUTES ========================================================== */

    private int statusCode;
    private byte[] byteContent;

    /* CONSTRUCTOR ========================================================= */

    public Response(CustomURL url, String contentType, String content, Session session, int statusCode, byte[] byteContent) {
        super(url, contentType, content, session);
        this.statusCode = statusCode;
        this.byteContent = byteContent;
    }

    /* GETTERS ============================================================= */

    public int getStatusCode() {
        return statusCode;
    }

    public byte[] getByteContent() {
        return byteContent;
    }

    /* SETTERS ============================================================= */

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setByteContent(byte[] byteContent) {
        this.byteContent = byteContent;
    }
}
