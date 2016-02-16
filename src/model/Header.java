package model;

import model.enums.HeaderRequest;
import model.enums.HeaderResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the header of a message with it's possible fields.
 * @see HeaderRequest
 * @see HeaderResponse
 *
 * /!\ Ben : je n'ai pas réussi à faire une liste générique avec HeaderRequest OU HeaderResponse, donc ya 2 listes...
 *
 */
public class Header {

    /* ATTRIBUTES ========================================================== */

    private List<HeaderRequest> requestFields;
    private List<HeaderResponse> responseFields;

    /* CONSTRUCTOR ========================================================== */

    public Header(List<HeaderRequest> requestFields, List<HeaderResponse> responseFields) {
        this.requestFields = requestFields;
        this.responseFields = responseFields;
    }

    /* SETTERS ============================================================= */

    public void setRequestFields(List<HeaderRequest> requestFields) {
        this.requestFields = requestFields;
    }

    public void setResponseFields(List<HeaderResponse> responseFields) {
        this.responseFields = responseFields;
    }

    /* GETTERS ============================================================= */

    public List<HeaderRequest> getRequestFields() {
        return requestFields;
    }

    public List<HeaderResponse> getResponseFields() {
        return responseFields;
    }
}
