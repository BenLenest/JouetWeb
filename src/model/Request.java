package model;

public class Request {

    private Methods method;
    private String host;

    public Request(Methods method, String host) {
        this.method = method;
        this.host = host;
    }
}
