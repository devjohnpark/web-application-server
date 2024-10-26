package com.hello_webserver.http.request;

import com.hello_webserver.http.util.HttpParser;
import com.hello_webserver.http.util.HttpParser.Pair;

import java.util.Map;

public class RequestLine {
    private final HttpMethod method;
    private final String path;
    private final String queryString;
    private final Map<String, String> parameters;
    private final HttpVersion version;

    private RequestLine(HttpMethod method, String path, String queryString, Map<String, String> parameters, HttpVersion version) {
        this.method = method;
        this.path = path;
        this.queryString = queryString;
        this.parameters = parameters;
        this.version = version;
    }

    public static RequestLine createFromRequestLine(String requestLine) {
        String[] tokens = HttpParser.parseRequestLine(requestLine);
        HttpMethod method = HttpMethod.valueOf(tokens[0]);
        UrlComponents urlComponents = getUrlComponents(tokens[1]);
        HttpVersion version = HttpVersion.fromString(tokens[2]);
        return new RequestLine(method, urlComponents.path, urlComponents.queryString, urlComponents.parameters, version);
    }

    private static UrlComponents getUrlComponents(String url) {
        Pair pair = HttpParser.parseUrl(url);
        String path = pair.key();
        String queryString = pair.value();
        Map<String, String> parameters = HttpParser.parseQueryString(queryString);
        return new UrlComponents(path, queryString, parameters);
    }

    private record UrlComponents(String path, String queryString, Map<String, String> parameters) {}

    public HttpMethod getMethod() { return method; }

    public String getPath() { return path; }

    public String getQueryString() { return queryString; }

    public String getParameter(String key) { return parameters.get(key); }

    public HttpVersion getVersion() { return version; }
}
