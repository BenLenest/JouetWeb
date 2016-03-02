package model;

import java.util.Map;

/**
 * Class representing a generic message that can be sent or received.

 */
public class Message {

    /* ATTRIBUTES ========================================================== */

    private CustomURL customUrl;
    private Map<String, String> header;
    private String contentType;
    private String content;

    /* CONSTRUCTOR ========================================================= */

    public Message(CustomURL customUrl, Map<String, String> header, String contentType, String content) {
        this.customUrl = customUrl;
        this.header = header;
        this.contentType = contentType;
        this.content = content;
    }

    /* SETTERS ============================================================= */

    public void setCustomUrl(CustomURL customUrl) {
        this.customUrl = customUrl;
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

    public CustomURL getCustomUrl() {
        return customUrl;
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
