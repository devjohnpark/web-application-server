package com.hello_webserver.http;

import java.util.HashMap;
import java.util.Map;

public enum HttpMethod {
    GET, POST, PUT, PATCH, DELETE;

    private static final Map<String, HttpMethod> stringToEnum = new HashMap<>();

    static {
        for (HttpMethod method : values()) {
            stringToEnum.put(method.name(), method);
        }
    }

    public static HttpMethod fromString(String method) {
        HttpMethod httpMethod = stringToEnum.get(method.toUpperCase());
        if (httpMethod == null) {
            throw new IllegalArgumentException();
        }
        return httpMethod;
    }
}



//public enum HttpMethod {
//    GET("GET"), POST("POST"), PUT("PUT"), PATCH("PATCH"), DELETE("DELETE");
//
//    private final String method;
//
//    HttpMethod(String method) {
//        this.method = method;
//    }
//
//    public String getMethod() {
//        return method;
//    }
//}
