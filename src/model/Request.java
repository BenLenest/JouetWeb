package model;

import model.enums.EnumMethod;

import java.util.Map;

/**
 * Class representing a request with it's method name.
 * @see EnumMethod
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

    private EnumMethod method;
    private String host;
    private int port;
    private String protocol;

    /* CONSTRUCTOR ========================================================== */

    public Request(CustomURL customUrl, Map<String, String> header, String contentType, String content, EnumMethod method, String host, int port, String protocol) {
        super(customUrl, header, contentType, content);
        this.method = method;
        this.host = host;
        this.port = port;
        this.protocol = protocol;
    }

    /* SETTERS ============================================================= */

    public void setMethod(EnumMethod method) {
        this.method = method;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }


    /* GETTERS ============================================================= */

    public EnumMethod getMethod() {
        return method;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }
}
