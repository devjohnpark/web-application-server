package com.hello_webserver.http.request;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.hello_webserver.webresources.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hello_webserver.http.util.HttpParser;
//import com.hello_webserver.http.utils.HttpParser.Pair;

// 클라이언트 요청 데이터 처리 (HttpRequest)
public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private RequestLine requestLine;
    private HttpReqHeaders headers;
    private Map<String, String> bodyParameters;

    public HttpRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            requestLine = createRequestLine(br);
            headers = createHeader(br);
            bodyParameters = createBodyParams(br);
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

    private HttpReqHeaders createHeader(BufferedReader br) throws IOException {
        HttpReqHeaders httpHeader = new HttpReqHeaders();
        String line;
        while (!(line = br.readLine()).isEmpty()) {
            httpHeader.addHeader(line);
        }
        return httpHeader;
    }

    private Map<String, String> createBodyParams(BufferedReader br) throws IOException {
        String contentLength = headers.getHeader(HttpReqHeaders.CONTENT_LENGTH);
        String contentType = headers.getHeader(HttpReqHeaders.CONTENT_TYPE);
        int cl;
        if (contentLength != null && contentType != null && (cl = Integer.parseInt(contentLength)) > 0) {
            char[] body = new char[cl];
            br.read(body, 0, cl);
            return handleBodyParams(contentType, String.copyValueOf(body));
        }
        return new HashMap<>();
    }

    private Map<String, String> handleBodyParams(String contentType, String body) {
        if (ResourceType.URL.getContentType().equals(contentType)) {
            return HttpParser.parseQueryString(body);
        }
        return new HashMap<>();
    }

    public HttpMethod getMethod() { return requestLine.getMethod(); }

    public String getPath() { return requestLine.getPath(); }

    public String getQueryString() { return requestLine.getQueryString(); }

    public String getRequestLineParamValue(String key) { return requestLine.getParameter(key); }

    public String getBodyParamValue(String key) { return bodyParameters.get(key); }

    public HttpVersion getHttpVersion() { return requestLine.getVersion(); }

    public String getHeader(String key) { return headers.getHeader(key); }

    public String getCookie() { return headers.getHeader(HttpReqHeaders.COOKIE); }

    public String getContentType() { return headers.getHeader(HttpReqHeaders.CONTENT_TYPE); }

    public String getContentLength() { return headers.getHeader(HttpReqHeaders.CONTENT_LENGTH); }

    public String getConnection() { return headers.getHeader(HttpReqHeaders.CONNECTION); }

//    private record RequestLine(HttpMethod method, String path, String queryString, Map<String, String> parameters,
//                               HttpVersion protocol) {
//
//        public static RequestLine createFromRequestLine(String requestLine) {
//                String[] tokens = HttpParser.parseRequestLine(requestLine);
//                HttpMethod method = HttpMethod.valueOf(tokens[0]);
//                UrlComponents urlComponents = getUrlComponents(tokens[1]);
//                HttpVersion protocol = HttpVersion.fromString(tokens[2]);
//                return new RequestLine(method, urlComponents.path, urlComponents.queryString, urlComponents.parameters, protocol);
//            }
//
//        private static UrlComponents getUrlComponents(String url) {
//            Pair pair = HttpParser.parseUrl(url);
//            String path = pair.key();
//            String queryString = pair.value();
//            Map<String, String> parameters = HttpParser.parseQueryString(queryString);
//            return new UrlComponents(path, queryString, parameters);
//        }
//
//        private record UrlComponents(String path, String queryString, Map<String, String> parameters) {
//        }
//    }
}
