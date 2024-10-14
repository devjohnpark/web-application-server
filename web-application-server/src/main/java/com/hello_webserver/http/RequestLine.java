package com.hello_webserver.http;

import util.HttpParser;

public class RequestLine {
    private final HttpMethod method;
    private final String path;
    private final String queryString;
    private final HttpProtocol protocol;

    private RequestLine(HttpMethod method, String path, String queryString, HttpProtocol protocol) {
        this.method = method;
        this.path = path;
        this.queryString = queryString;
        this.protocol = protocol;
    }

    public static RequestLine createRequestline(String requestLine) {
        String[] tokens = tokenizeRequestLine(requestLine);
        HttpMethod method = HttpMethod.fromString(tokens[0]);
        UrlComponents urlComponents = parseUrl(tokens[1]);
        HttpProtocol protocol = HttpProtocol.fromString(tokens[2]);

        return new RequestLine(method, urlComponents.path(), urlComponents.queryString(), protocol);
    }

    private static String[] tokenizeRequestLine(String requestLine) {
        String[] tokens = requestLine.split(" ");
        if (tokens.length != 3) {
            throw new IllegalArgumentException("Invalid request line format: " + requestLine);
        }
        return tokens;
    }

    private static UrlComponents parseUrl(String url) {
        int queryStringIndex = url.indexOf('?');
        if (queryStringIndex > 0) {
            String path = url.substring(0, queryStringIndex);
            String queryString = url.substring(queryStringIndex + 1);
            return new UrlComponents(path, queryString);
        }
        return new UrlComponents(url, "");
    }

    private record UrlComponents(String path, String queryString) {}

    public HttpMethod getMethod() { return method; }

    public String getPath() { return path; }

    public String getQueryString() { return queryString; }

    public HttpProtocol getProtocol() { return protocol; }
}