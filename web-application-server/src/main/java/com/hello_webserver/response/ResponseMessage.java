package com.hello_webserver.response;

public class ResponseMessage {
    private final HttpStatus status;
    private final byte[] body;
    private final String contentType;
    private final String date;

    public ResponseMessage(HttpStatus status, byte[] body, String contentType, String date) {
        this.status = status;
        this.body = body;
        this.contentType = contentType;
        this.date = date;
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

    public String getDate() {
        return date;
    }
}
