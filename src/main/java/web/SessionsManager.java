package web;

import model.Session;

import java.util.LinkedHashMap;
import java.util.Map;

public class SessionsManager {

    /* ATTRIBUTES ========================================================== */

    private final LinkedHashMap<String, Session> sessions;

    /* INSTANCE ============================================================ */

    private SessionsManager(LinkedHashMap<String, Session> sessions) {
        this.sessions = sessions;
    }

    private static SessionsManager INSTANCE = new SessionsManager(new LinkedHashMap<>());

    public static SessionsManager getInstance() {
        return INSTANCE;
    }

    /* GETTERS ============================================================= */

    public Map<String, Session> getSessions() {
        return sessions;
    }
}