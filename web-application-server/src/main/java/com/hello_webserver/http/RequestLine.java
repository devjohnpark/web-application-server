package com.hello_webserver.http;

import util.HttpParser;

import java.util.Map;

public class RequestLine {
    private final HttpMethod method;
    private final String path;
    private final String queryString;
    private final Map<String, String> parameters;
    private final HttpProtocol protocol;

    private RequestLine(HttpMethod method, String path, String queryString, Map<String, String> parameters, HttpProtocol protocol) {
        this.method = method;
        this.path = path;
        this.queryString = queryString;
        this.parameters = parameters;
        this.protocol = protocol;
    }

    public static RequestLine createRequestline(String requestLine) {
        String[] tokens = tokenizeRequestLine(requestLine);
        HttpMethod method = HttpMethod.fromString(tokens[0]);
        UrlComponents urlComponents = getUrlComponents(tokens[1]);
        HttpProtocol protocol = HttpProtocol.fromString(tokens[2]);
        return new RequestLine(method, urlComponents.path, urlComponents.queryString, urlComponents.parameters, protocol);
    }

    private static String[] tokenizeRequestLine(String requestLine) {
        String[] tokens = requestLine.split(" ");
        if (tokens.length != 3) {
            throw new IllegalStateException();
        }
        return tokens;
    }

    private static UrlComponents getUrlComponents(String url) {
        HttpParser.Pair pair = HttpParser.parseUrl(url);
        String path = pair.key();
        String queryString = pair.value();
        Map<String, String> parameters = HttpParser.parseQueryString(queryString);
        return new UrlComponents(path, queryString, parameters);
    }

    private record UrlComponents(String path, String queryString, Map<String, String> parameters) {}

    public HttpMethod getMethod() { return method; }

    public String getPath() { return path; }

    public String getQueryString() { return queryString; }

    public Map<String, String> getParameters() { return parameters; }

    public HttpProtocol getProtocol() { return protocol; }
}