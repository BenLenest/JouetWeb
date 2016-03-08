package model.configuration;

import com.google.gson.annotations.SerializedName;

public class ConfigContainer {

    /* ATTRIBUTES ========================================================== */

    @SerializedName("requests") private ConfigRequest[] requests;

    /* CONSTRUCTOR ========================================================= */

    public ConfigContainer(ConfigRequest[] requests) {
        this.requests = requests;
    }

    /* PUBLIC METHODS ====================================================== */

    public ConfigRequest findRequestControllerByName(String name) {
        for (int i = 0 ; i < requests.length ; i++) if (requests[i].getUrl().equals(name)) return requests[i];
        return null;
    }
}
