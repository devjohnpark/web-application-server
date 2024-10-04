package com.hello_webserver.webserver;

public class ResponseMessage {
    private final HttpStatus status;
    private final byte[] body;
    private final String contentType;

    public ResponseMessage(HttpStatus status, byte[] body, String contentType) {
        this.status = status;
        this.body = body;
        this.contentType = contentType;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public byte[] getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }
}
