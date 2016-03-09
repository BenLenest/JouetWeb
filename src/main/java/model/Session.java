package model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Session {

    /* ATTRIBUTES ========================================================== */

    private String id;
    private Map<String, String> values;
    private Date timeOutDate;

    /* CONSTRUCTOR ========================================================= */

    public Session(String id, Map<String, String> values, Date timeOutDate) {
        this.id = id;
        this.values = values;
        this.timeOutDate = timeOutDate;
    }

    public Session(String id){
        this.id = id;
        this.values = new HashMap<>();
        this.timeOutDate = null;
    }

    /* GETTERS ============================================================= */

    public String getId() {
        return id;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public Date getTimeOutDate() {
        return timeOutDate;
    }

    /* SETTERS ============================================================= */

    public void setId(String id) {
        this.id = id;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public void setTimeOutDate(Date timeOutDate) {
        this.timeOutDate = timeOutDate;
    }
}
