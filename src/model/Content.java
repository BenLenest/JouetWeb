package model;

import org.json.JSONObject;

public class Content {

    /* ATTRIBUTES ========================================================== */

    private String plainContent;
    private String htmlContent;
    private JSONObject jsonContent;

    /* CONSTRUCTOR ========================================================== */

    public Content(String plainContent, String htmlContent, JSONObject jsonContent) {
        this.plainContent = plainContent;
        this.htmlContent = htmlContent;
        this.jsonContent = jsonContent;
    }

    /* GETTERS ============================================================== */

    public String getPlainContent() {
        return plainContent;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public JSONObject getJsonContent() {
        return jsonContent;
    }
}
