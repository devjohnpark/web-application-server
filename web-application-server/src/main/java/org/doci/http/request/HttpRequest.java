package org.doci.http.request;

import java.io.*;
import java.nio.charset.StandardCharsets;

import org.doci.webresources.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 클라이언트 요청 데이터 처리 (HttpRequest)
public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private BufferedReader br;
    private RequestLine requestLine;
    private final HttpReqHeaders headers = new HttpReqHeaders();
    private final RequestParameters parameters = new RequestParameters();

    public HttpRequest(InputStream in) {
        try {
            this.br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            processHttpRequest();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void processHttpRequest() throws IOException {
        requestLine = createRequestLine();
        setHeaders();
        setParameters();
    }

    private RequestLine createRequestLine() throws IOException {
        String line = br.readLine();
        if (line == null) {
            throw new IllegalStateException();
        }
        return RequestLine.createFromRequestLine(line);
    }

    private void setHeaders() throws IOException {
        String line;
        while (!(line = br.readLine()).isEmpty()) {
            headers.addHeader(line);
        }
    }

    private void setParameters() throws IOException {
        parameters.addRequestLineParameters(requestLine.getQueryString());
        if (ResourceType.URL.getMimeType().equals(headers.getContentType())) {
            parameters.addBodyParameters(readAllBodyAsString());
        }
    }

    public String readAllBodyAsString() throws IOException {
        int contentLength = headers.getContentLength();
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public byte[] readAllBodyAsBytes() throws IOException {
        return readAllBodyAsString().getBytes();
    }

    public BufferedReader getBufferedReader() { return br; }

    public HttpMethod getMethod() { return requestLine.getMethod(); }

    public String getPath() { return requestLine.getPath(); }

    public String getRequestParameter(String key) { return parameters.getParameter(key); }

    public HttpVersion getHttpVersion() { return requestLine.getVersion(); }

    public String getHeader(String key) { return headers.getHeader(key); }

    public String getCookie() { return headers.getCookie(); }

    public String getContentType() { return headers.getContentType(); }

    public int getContentLength() { return headers.getContentLength(); }

    public String getConnection() { return headers.getConnection(); }
}
