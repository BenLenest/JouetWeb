package model;

import java.util.Map;

public class Message {

    /* CONSTANTS =========================================================== */

    public final static String TEXT_PLAIN = "text/plain";
    public final static String TEXT_HTML = "text/html";
    public final static String APPLICATION_JSON = "application/json";

    /* ATTRIBUTES ========================================================== */

    private CustomURL url;
    private String contentType;
    private String content;

    /* CONSTRUCTOR ========================================================= */

    public Message(CustomURL url, String contentType, String content) {
        this.url = url;
        this.contentType = (contentType != null ? contentType : TEXT_HTML);
        this.content = (content != null ? content : "");
    }

    /* GETTERS ============================================================= */

    public CustomURL getUrl() {
        return url;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContent() {
        return content;
    }

    /* SETTERS ============================================================= */

    public void setUrl(CustomURL url) {
        this.url = url;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
