package model;

import model.enums.Method;

import java.util.List;
import java.util.Map;

/**
 * Class representing a request with it's method name.
 * @see Method
 *
 * Request example :
 *
 * GET / HTTP/1.1
 * Host: localhost:8080
 * Connection: keep-alive
 * Authorization: Bearer 1f8ced9d-66b4-46bb-bd29-473f9e343abe
 * Cache-Control: no-cache
 * User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36
 * Postman-Token: ad4a2081-9b7f-c51e-a347-dcb8e2604d26
 * Accept: * / *
 * Accept-Encoding: gzip, deflate, sdch
 * Accept-Language: fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4
 */
public class Request extends Message {

    /* ATTRIBUTES ========================================================== */

    private Method method;
    private String host;

    /* CONSTRUCTOR ========================================================== */

    public Request(int statusCode, String url, Map<String, String> header, Method method, String host) {
        super(statusCode, url, header);
        this.method = method;
        this.host = host;
    }

    /* SETTERS ============================================================= */

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setHost(String host) {
        this.host = host;
    }

    /* GETTERS ============================================================= */

    public Method getMethod() {
        return method;
    }

    public String getHost() {
        return host;
    }
}
