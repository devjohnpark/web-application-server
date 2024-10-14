package com.hello_webserver.http;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import com.hello_webserver.webserver.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



// 클라이언트 요청 데이터 처리 (HttpRequest)
public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private RequestLine requestLine = null;


    public HttpRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            requestLine = RequestLine.createRequestline(readRequestLine(br));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String readRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line == null) {
            throw new IllegalStateException("Request line is null");
        }
        return line;
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public HttpProtocol getProtocol() {
        return requestLine.getProtocol();
    }
}
