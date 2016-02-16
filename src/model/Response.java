package model;

import model.enums.Method;

/**
 * Class representing a Response with it's method.
 * @see Method
 *
 * Response example :
 *
 * HTTP/1.0 200 OK
 * Date: Fri, 31 Dec 1999 23:59:59 GMT
 * web.Server: Apache/0.8.4
 * Content-Type: text/html
 * Content-Length: 59
 * Expires: Sat, 01 Jan 2000 00:59:59 GMT
 * Last-modified: Fri, 09 Aug 1996 14:21:40 GMT
 */
public class Response extends Message {

    /* ATTRIBUTES ========================================================== */

    private String method;

    /* CONSTRUCTOR ========================================================== */

    public Response(int statusCode, String url, Header header, String method) {
        super(statusCode, url, header);
        this.method = method;
    }

    /* SETTERS ============================================================= */

    public void setMethod(String method) {
        this.method = method;
    }

    /* GETTERS ============================================================= */

    public String getMethod() {
        return method;
    }
}
