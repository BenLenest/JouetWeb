package model;

import model.enums.HeaderFields;

import java.util.List;

/**
 * Class representing the header of a message with it's possible fields.
 * @see HeaderFields
 */
public class Header {

    /* ATTRIBUTES ========================================================== */

    private List<HeaderFields> fields;

    /* CONSTRUCTOR ========================================================== */

    public Header(List<HeaderFields> fields) {
        this.fields = fields;
    }

    /* SETTERS ============================================================= */

    public void setFields(List<HeaderFields> fields) {
        this.fields = fields;
    }

    /* GETTERS ============================================================= */

    public List<HeaderFields> getFields() {
        return fields;
    }
}
