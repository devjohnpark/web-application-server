package org.doci.http.request;

import java.io.*;
import java.nio.charset.StandardCharsets;

import org.doci.webresources.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 클라이언트 요청 데이터 처리 (HttpRequest)
public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private InputStream in;
    private RequestLine requestLine;
    private final HttpReqHeaders headers = new HttpReqHeaders();
    private final RequestParameters parameters = new RequestParameters();

    public HttpRequest(InputStream in) {
        try {
            this.in = in;
            processHttpRequest(new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void processHttpRequest(BufferedReader br) throws IOException {
        requestLine = createRequestLine(br);
        setHeaders(br);
        setParameters(br);
    }

    private RequestLine createRequestLine(BufferedReader br) throws IOException {
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

    private void setParameters(BufferedReader br) throws IOException {
        parameters.addRequestLineParameters(requestLine.getQueryString());
        if (ResourceType.URL.getMimeType().equals(headers.getContentType())) {
            parameters.addBodyParameters(readAllBodyAsString(br, headers.getContentLength()));
        }
    }

    public String readAllBodyAsString(BufferedReader br, String contentLength) throws IOException {
        int cl = contentLength == null ? 0 : Integer.parseInt(contentLength);
        char[] body = new char[cl];
        br.read(body, 0, cl);
        return String.copyValueOf(body);
    }

    public byte[] readAllBodyAsBytes(String contentLength) throws IOException {
        int cl = contentLength == null ? 0 : Integer.parseInt(contentLength);
        byte[] body = new byte[cl];
        in.read(body, 0, cl);
        return body;
    }

    public InputStream getInputStream() { return in; }

    public HttpMethod getMethod() { return requestLine.getMethod(); }

    public String getPath() { return requestLine.getPath(); }

    public String getRequestParameter(String key) { return parameters.getParameter(key); }

    public HttpVersion getHttpVersion() { return requestLine.getVersion(); }

    public String getHeader(String key) { return headers.getHeader(key); }

    public String getCookie() { return headers.getCookie(); }

    public String getContentType() { return headers.getContentType(); }

    public String getContentLength() { return headers.getContentLength(); }

    public String getConnection() { return headers.getConnection(); }
}
