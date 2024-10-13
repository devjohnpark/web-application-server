package com.hello_webserver.http;

// 요청 메서드와 URI를 담는 클래스
public class RequestLine {
    private final HttpMethod method;
    private final String path;
    private final HttpProtocol protocol;

    public RequestLine(HttpMethod method, String path, HttpProtocol protocol) {
        this.method = method;
        this.path = path;
        this.protocol = protocol;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public HttpProtocol getProtocol() {
        return protocol;
    }
}

