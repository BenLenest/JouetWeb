package model;

import java.util.Map;

/**
 * Class representing a generic message that can be sent or received.

 */
public class Message {

    /* ATTRIBUTES ========================================================== */

    private String url;
    private Map<String, String> header;
    private String contentType;
    private String content;

    /* CONSTRUCTOR ========================================================= */

    public Message(String url, Map<String, String> header, String contentType, String content) {
        this.url = url;
        this.header = header;
        this.contentType = contentType;
        this.content = content;
    }

    /* SETTERS ============================================================= */

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /* GETTERS ============================================================= */

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContent() {
        return content;
    }
}
