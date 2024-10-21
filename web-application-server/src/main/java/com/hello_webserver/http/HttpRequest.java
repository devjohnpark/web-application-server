package com.hello_webserver.http;

import java.io.*;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpParser;

// 클라이언트 요청 데이터 처리 (HttpRequest)
public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private RequestLine requestLine;
    private HttpHeader header;

    public HttpRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            requestLine = createRequestLine(br);
            header = createHeader(br);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private RequestLine createRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line == null) {
            throw new IllegalStateException();
        }
        return RequestLine.createFromRequestLine(line);
    }

    private HttpHeader createHeader(BufferedReader br) throws IOException {
        HttpHeader httpHeader = new HttpHeader();
        String line = br.readLine();
        while (!line.isEmpty()) {
            HttpParser.Pair pair = HttpParser.parseHeader(line);
            httpHeader.addHeader(pair.key(), pair.value());
            line = br.readLine();
        }
        return httpHeader;
    }

    public HttpMethod getMethod() { return requestLine.method(); }

    public String getPath() { return requestLine.path; }

    public String getQueryString() { return requestLine.queryString; }

    public String getParameterValue(String key) { return requestLine.parameters.get(key); }

    public HttpProtocol getProtocol() { return requestLine.protocol; }

    public String getHeaderValue(String key) { return header.getHeaders().get(key); }

    private record RequestLine(HttpMethod method, String path, String queryString, Map<String, String> parameters,
                               HttpProtocol protocol) {

        public static RequestLine createFromRequestLine(String requestLine) {
            String[] tokens = HttpParser.parseRequestLine(requestLine);
            HttpMethod method = HttpMethod.valueOf(tokens[0]);
            UrlComponents urlComponents = getUrlComponents(tokens[1]);
            HttpProtocol protocol = HttpProtocol.fromString(tokens[2]);
            return new RequestLine(method, urlComponents.path, urlComponents.queryString, urlComponents.parameters, protocol);
        }

        private static UrlComponents getUrlComponents(String url) {
            HttpParser.Pair pair = HttpParser.parseUrl(url);
            String path = pair.key();
            String queryString = pair.value();
            Map<String, String> parameters = HttpParser.parseQueryString(queryString);
            return new UrlComponents(path, queryString, parameters);
        }

        private record UrlComponents(String path, String queryString, Map<String, String> parameters) {
        }
    }
}
