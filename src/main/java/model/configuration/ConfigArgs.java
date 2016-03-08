package model.configuration;

import com.google.gson.annotations.SerializedName;

public class ConfigArgs {

    /* ATTRIBUTES ========================================================== */

    @SerializedName("type") private String type;
    @SerializedName("name") private String name;

    /* CONSTRUCTOR ========================================================= */

    public ConfigArgs(String type, String name) {
        this.type = type;
        this.name = name;
    }

    /* GETTERS ============================================================= */

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
