package com.hello_webserver.http;

import java.io.*;

import com.hello_webserver.webserver.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestParser;


// 클라이언트 요청 데이터 처리 (HttpRequest)
public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
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
        HttpRequestParser.Pair pair;
        String line = br.readLine();
        while (!line.isEmpty()) {
            pair = HttpRequestParser.parseHeader(line);
            httpHeader.addHeader(pair.key(), pair.value());
            line = br.readLine();
        }
        return httpHeader;
    }

    public HttpMethod getMethod() { return requestLine.getMethod(); }

    public String getPath() { return requestLine.getPath(); }

    public String getQueryString() { return requestLine.getQueryString(); }

    public String getParameters(String key) { return requestLine.getParameters().get(key); }

    public HttpProtocol getProtocol() { return requestLine.getProtocol(); }

    public String getHeaders(String key) { return header.getHeaders().get(key); }
}
