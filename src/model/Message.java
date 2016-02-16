package model;

/**
 * Class representing a generic message that can be sent or received.

 */
public class Message {

    /* ATTRIBUTES ========================================================== */

    private int statusCode;
    private String url;
    private Header header;

    /* CONSTRUCTOR ========================================================= */

    public Message(int statusCode, String url, Header header) {
        this.statusCode = statusCode;
        this.url = url;
        this.header = header;
    }

    /* SETTERS ============================================================= */

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    /* GETTERS ============================================================= */

    public int getStatusCode() {
        return statusCode;
    }

    public String getUrl() {
        return url;
    }

    public Header getHeader() {
        return header;
    }
}
