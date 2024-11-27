package org.dochi.http.response;

import java.util.HashMap;
import java.util.Map;

/*
HTTP/1.1 200 OK
Date: Mon, 16 Oct 2024 10:00:00 GMT
Server: Apache/2.4.29 (Ubuntu)
Content-Type: text/html; charset=UTF-8
Content-Length: 3423
Connection: keep-alive
Set-Cookie: sessionId=abc123; Path=/; HttpOnly
Cache-Control: max-age=3600, must-revalidate
Last-Modified: Mon, 16 Oct 2024 09:30:00 GMT
ETag: "123456789abcdef"
Content-Encoding: gzip
 */

public class ResponseHeaders {
    private final Map<String, String> headers = new HashMap<>();

    public static final String SERVER = "Server";
    public static final String DATE = "Date";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONNECTION = "Connection";
    public static final String SET_COOKIE = "Set-Cookie";

    public void addHeader(String key, String value) {
        if (key == null || value == null || key.isEmpty()) {
            return;
        }
        headers.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
