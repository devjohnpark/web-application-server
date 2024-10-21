package com.hello_webserver.http;

public enum HttpProtocol {
    HTTP_0_9("HTTP/0.9"),
    HTTP_1_0("HTTP/1.0"),
    HTTP_1_1("HTTP/1.1"),
    HTTP_2_0("HTTP/2.0"),
    HTTP_3_0("HTTP/3.0");

    private final String version;

    HttpProtocol(String version) {
        this.version = version;
    }

    public String getVersion() {
        return this.version;
    }

    public static HttpProtocol fromString(String version) {
        for (HttpProtocol protocol : HttpProtocol.values()) {
            if (protocol.getVersion().equals(version)) {
                return protocol;
            }
        }
        throw new IllegalArgumentException();
    }
}
