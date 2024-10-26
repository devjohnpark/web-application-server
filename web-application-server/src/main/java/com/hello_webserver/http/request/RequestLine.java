package com.hello_webserver.http.request;

import com.hello_webserver.http.util.HttpParser;
import com.hello_webserver.http.util.HttpParser.Pair;

import java.util.Map;

public class RequestLine {
    private final HttpMethod method;
    private final String path;
    private final String queryString;
    private final HttpVersion version;

    private RequestLine(HttpMethod method, String path, String queryString, HttpVersion version) {
        this.method = method;
        this.path = path;
        this.queryString = queryString;
        this.version = version;
    }

    public static RequestLine createFromRequestLine(String requestLine) {
        String[] tokens = HttpParser.parseRequestLine(requestLine);
        HttpMethod method = HttpMethod.valueOf(tokens[0]);
        UrlComponents urlComponents = getUrlComponents(tokens[1]);
        HttpVersion version = HttpVersion.fromString(tokens[2]);
        return new RequestLine(method, urlComponents.path, urlComponents.queryString, version);
    }

    private static UrlComponents getUrlComponents(String url) {
        Pair pair = HttpParser.parseUrl(url);
        String path = pair.key();
        String queryString = pair.value();
        Map<String, String> parameters = HttpParser.parseQueryString(queryString);
        return new UrlComponents(path, queryString);
    }

    private record UrlComponents(String path, String queryString) {}

    public HttpMethod getMethod() { return method; }

    public String getPath() { return path; }

    public String getQueryString() { return queryString; }

    public HttpVersion getVersion() { return version; }
}
