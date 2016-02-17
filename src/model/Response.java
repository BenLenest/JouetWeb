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

    private String content;

    /* CONSTRUCTOR ========================================================== */

    public Response(int statusCode, String url, Header header, String content) {
        super(statusCode, url, header);
        this.content = content;
    }

    /* SETTERS ============================================================= */

    public void setContent(String content) {
        this.content = content;
    }

    /* GETTERS ============================================================= */

    public String getContent() {
        return content;
    }
}
