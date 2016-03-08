package model;

import model.enums.EnumMethod;

public class Request extends Message {

    /* ATTRIBUTES ========================================================== */

    private boolean valid;
    private EnumMethod method;
    private Session session;

    /* CONSTRUCTOR ========================================================= */

    public Request(CustomURL url, String contentType, String content, boolean valid, EnumMethod method, Session session) {
        super(url, contentType, content);
        this.valid = valid;
        this.method = method;
        this.session = session;
    }

    /* GETTERS ============================================================= */

    public boolean isValid() {
        return valid;
    }

    public EnumMethod getMethod() {
        return method;
    }

    public Session getSession() {
        return session;
    }
}
