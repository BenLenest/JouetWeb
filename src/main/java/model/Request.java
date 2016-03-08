package model;

import model.enums.EnumMethod;

public class Request extends Message {

    /* ATTRIBUTES ========================================================== */

    private boolean valid;
    private EnumMethod method;

    /* CONSTRUCTOR ========================================================= */

    public Request(CustomURL url, String contentType, String content, boolean valid, EnumMethod method) {
        super(url, contentType, content);
        this.valid = valid;
        this.method = method;
    }

    /* GETTERS ============================================================= */

    public boolean isValid() {
        return valid;
    }

    public EnumMethod getMethod() {
        return method;
    }
}