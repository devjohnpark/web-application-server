package com.hello_webserver.http.request;

import java.io.*;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 클라이언트 요청 데이터 처리 (HttpRequest)
public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private RequestLine requestLine;
    private final HttpReqHeaders headers = new HttpReqHeaders();
    private final RequestParameters parameters = new RequestParameters();
    private String body = null;

    public HttpRequest(InputStream in) {
        try {
            processHttpRequest(new BufferedReader(new InputStreamReader(in)));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void processHttpRequest(BufferedReader br) throws IOException {
        requestLine = setRequestLine(br);
        setHeaders(br);
        setBody(br);
        setParameters(br);
    }

    private RequestLine setRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line == null) {
            throw new IllegalStateException();
        }
        return RequestLine.createFromRequestLine(line);
    }

    private void setHeaders(BufferedReader br) throws IOException {
        String line;
        while (!(line = br.readLine()).isEmpty()) {
            headers.addHeader(line);
        }
    }

    private void setBody(BufferedReader br) throws IOException {
        String contentLength = getContentLength();
        String contentType = getContentType();
        int cl;
        if (contentLength != null && contentType != null && (cl = Integer.parseInt(contentLength)) > 0) {
            char[] body = new char[cl];
            br.read(body, 0, cl);
            this.body = String.copyValueOf(body);
        }
    }

    private void setParameters(BufferedReader br) throws IOException {
        parameters.addRequestLineParameters(requestLine.getQueryString());
        parameters.addBodyParameters(body, getContentType());
    }

    public HttpMethod getMethod() { return requestLine.getMethod(); }

    public String getPath() { return requestLine.getPath(); }

    public String getQueryString() { return requestLine.getQueryString(); }

    public String getRequestParameter(String key) { return parameters.getParameter(key); }

    public HttpVersion getHttpVersion() { return requestLine.getVersion(); }

    public String getHeader(String key) { return headers.getHeader(key); }

    public String getCookie() { return headers.getCookie(); }

    public String getContentType() { return headers.getContentType(); }

    public String getContentLength() { return headers.getContentLength(); }

    public String getConnection() { return headers.getConnection(); }
}
