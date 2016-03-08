package model.configuration;

import com.google.gson.annotations.SerializedName;
import model.enums.EnumMethod;

public class ConfigRequest {

    /* ATTRIBUTE =========================================================== */

    @SerializedName("url") private String url;
    @SerializedName("controller") private String controller;
    @SerializedName("methods") private ConfigMethod[] methods;

    /* CONSTRUCTOR ========================================================= */

    public ConfigRequest(String url, String controller, ConfigMethod[] methods) {
        this.url = url;
        this.controller = controller;
        this.methods = methods;
    }

    /* GETTERS ============================================================= */

    public String getUrl() {
        return url;
    }

    public String getController() {
        return controller;
    }

    /* PUBLIC METHODS ====================================================== */

    public ConfigMethod findMethodByTypeAndUrl(EnumMethod type, String query) {
        for (int i = 0 ; i < methods.length ; i++) {
            if (methods[i].getMethod().equals(type.name()) && query.matches(methods[i].getRegex()))
                return methods[i];
        }
        return null;
    }

    public void generateMethodsRegex() {
        for (ConfigMethod method : methods) {
            method.generateUrlRegexAndTypesArray();
            method.generateRegexParts();
        }
    }
}
