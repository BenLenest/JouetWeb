package web;

import model.Session;

import java.util.HashMap;
import java.util.Map;

public class SessionsManager {

    /* ATTRIBUTES ========================================================== */

    private final Map<String, Session> sessions;

    /* INSTANCE ============================================================ */

    private SessionsManager(Map<String, Session> sessions) {
        this.sessions = sessions;
    }

    private static SessionsManager INSTANCE = new SessionsManager(new HashMap<>());

    public static SessionsManager getInstance() {
        return INSTANCE;
    }

    /* GETTERS ============================================================= */

    public Map<String, Session> getSessions() {
        return sessions;
    }
}
