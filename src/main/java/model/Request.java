package model;

import model.enums.EnumMethod;

public class Request extends Message {

    /* ATTRIBUTES ========================================================== */

    private boolean valid;
    private EnumMethod method;
    private String clientIP;
    private String userAgent;

    /* CONSTRUCTOR ========================================================= */

    public Request(CustomURL url, String contentType, String content, Session session, boolean valid, EnumMethod method, String clientIP, String userAgent) {
        super(url, contentType, content, session);
        this.valid = valid;
        this.method = method;
        this.clientIP = clientIP;
        this.userAgent = userAgent;
    }

    /* GETTERS ============================================================= */

    public boolean isValid() {
        return valid;
    }

    public EnumMethod getMethod() {
        return method;
    }

    public String getClientIP() {
        return clientIP;
    }

    public String getUserAgent() {
        return userAgent;
    }
}
