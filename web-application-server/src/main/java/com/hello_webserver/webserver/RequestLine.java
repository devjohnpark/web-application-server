package com.hello_webserver.webserver;

import java.nio.file.Files;
import java.nio.file.Path;

// 요청 메서드와 URI를 담는 클래스
public class RequestLine {
    private final String method;
    private final String path;

    public RequestLine(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}

