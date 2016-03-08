package web;

import model.Request;
import model.Response;

/**
 * Class used to dispatch a request to the proper service.
 */
public class RequestDispatcher {

    /* CONSTANTS =========================================================== */

    /* CONSTRUCTOR ========================================================= */

    public RequestDispatcher() {
    }

    /* PUBLIC METHODS ====================================================== */

    public Response dispatchRequest(String stringRequest) {
        System.out.println(stringRequest);
        Request request = HTTPBuilder.parseRequest(stringRequest);
        return null;
    }

}
