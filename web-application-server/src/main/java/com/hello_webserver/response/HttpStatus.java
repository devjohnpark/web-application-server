package com.hello_webserver.response;

public enum HttpStatus {
    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed");

    private final int code;
    private final String codeMessage;

    HttpStatus(int code, String codeMessage) {
        this.code = code;
        this.codeMessage = codeMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return codeMessage;
    }
}
