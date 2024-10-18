package com.hello_webserver.http;

import util.HttpParser;

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
    // Content-Type
    // Keep-Alive
    // Cookie
    // Content-Length
    private final Map<String, String> headers = new HashMap<>();

    public void addHeader(String header) {
        HttpParser.Pair pair = HttpParser.parseHeader(header);
        headers.put(pair.key(), pair.value());
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
