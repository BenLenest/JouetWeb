package model;

import model.enums.ContentType;

import java.util.List;
import java.util.Map;

/**
 * Class representing a generic message that can be sent or received.

 */
public class Message {

    /* ATTRIBUTES ========================================================== */

    private String url;
    private Map<String, String> header;
    private ContentType contentType;
    private Content content;

    /* CONSTRUCTOR ========================================================= */

    public Message(String url, Map<String, String> header, ContentType contentType, Content content) {
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

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    /* GETTERS ============================================================= */

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public Content getContent() {
        return content;
    }
}
