package model;

import java.util.Date;
import java.util.Map;

public class Session {

    /* CONSTANTS ========================================================== */

    public final static String SESSION_TOKEN = "sessionToken";
    public final static String SESSION_PATH = "path";
    public final static String SESSION_EXPIRES = "expires";
    public final static String SESSION_EXPIRES_DATE_FORMAT = "EEE, dd-MMM-yy HH:mm:ss z";

    /* ATTRIBUTES ========================================================== */

    private String key;
    private Map<String, String> values;
    private Date expires;

    /* CONSTRUCTOR ========================================================= */

    public Session(String key, Map<String, String> values){
        this.key = key;
        this.values = values;
        this.expires = null;
    }

    public Session(String key, Map<String, String> values, Date expires) {
        this(key, values);
        this.expires = expires;
    }

    /* GETTERS ============================================================= */

    public String getKey() {
        return key;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public Date getExpires() {
        return expires;
    }

    /* SETTERS ============================================================= */

    public void setKey(String key) {
        this.key = key;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }
}
