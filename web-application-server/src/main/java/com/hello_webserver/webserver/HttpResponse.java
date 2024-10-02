package com.hello_webserver.webserver;

public class HttpResponse {
    private final HttpStatus status;
    private final byte[] body;

    public HttpResponse(HttpStatus status, byte[] body) {
        this.status = status;
        this.body = body;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public byte[] getBody() {
        return body;
    }
}