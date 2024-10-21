package com.hello_webserver.http;

import java.util.HashMap;
import java.util.Map;

/*
HTTP Request Message Header
GET / HTTP/1.1
Host: www.example.com
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3
Accept: text/html,application/xhtml+xml,application/xml;q=0.9
Accept-Language: en-US,en;q=0.5
Accept-Encoding: gzip, deflate, br
Connection: keep-alive
Cookie: sessionId=abc123
Cache-Control: no-cache
Referer: https://www.google.com/
 */

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


/*
Not Incude Content-Length -> 411 Length Required
 */




public class HttpHeader {
    private final Map<String, String> headers = new HashMap<>();

    public static final String HOST = "Host";
    public static final String USER_AGENT = "User-Agent";
    public static final String ACCEPT = "Accept";
    public static final String CONNECTION = "Connection";
    public static final String COOKIE = "Cookie";

    public static final String SERVER = "Server";
    public static final String DATE = "Date";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String SET_COOKIE = "Set-Cookie";

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
