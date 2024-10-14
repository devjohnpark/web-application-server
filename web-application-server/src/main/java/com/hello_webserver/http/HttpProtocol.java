package com.hello_webserver.http;

import java.util.HashMap;
import java.util.Map;

public enum HttpProtocol {
    HTTP_0_9("HTTP/0.9"),
    HTTP_1_0("HTTP/1.0"),
    HTTP_1_1("HTTP/1.1"),
    HTTP_2_0("HTTP/2.0"),
    HTTP_3_0("HTTP/3.0");

    private final String protocol;
    private static final Map<String, HttpProtocol> stringToEnum = new HashMap<>();

    static {
        for (HttpProtocol httpProtocol : values()) {
            stringToEnum.put(httpProtocol.protocol, httpProtocol);
        }
    }

    HttpProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public static HttpProtocol fromString(String protocol) {
        HttpProtocol httpProtocol = stringToEnum.get(protocol);
        if (httpProtocol == null) {
            throw new IllegalArgumentException("Unknown HTTP Protocol: " + protocol);
        }
        return httpProtocol;
    }
}



