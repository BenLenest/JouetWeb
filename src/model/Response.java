package model;

import model.enums.Method;

import java.util.Map;

/**
 * Class representing a Response with it's method.
 * @see Method
 *
 * Response example :
 *
 *   HTTP/1.1 200 OK
 *   Date: Mon, 27 Jul 2009 12:28:53 GMT
 *   Server: Apache/2.2.14 (Win32)
 *   Last-Modified: Wed, 22 Jul 2009 19:15:56 GMT
 *   Content-Length: 88
 *   Content-Type: text/html
 *   Connection: Closed
 */
public class Response extends Message {

    /* ATTRIBUTES ========================================================== */

    private int statusCode;

    /* CONSTRUCTOR ========================================================== */

    public Response(int statusCode, String url, Map<String, String> header, String contentType, String content) {
        super(url, header, contentType, content);
        this.statusCode = statusCode;
    }

    /* SETTERS ========================================================== */

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /* GETTERS ========================================================== */

    public int getStatusCode() {
        return statusCode;
    }
}
