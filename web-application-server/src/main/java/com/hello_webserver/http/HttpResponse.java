package com.hello_webserver.http;

import com.hello_webserver.webserver.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DateFormatter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;


public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private final DataOutputStream dos;
    private final HttpHeader headers = new HttpHeader(); // 특정 값에 키 맵핑시켜서 저장
    private HttpProtocol httpProtocol = HttpProtocol.HTTP_1_1;
    private HttpStatus status = HttpStatus.OK;
    private byte[] body = null;

    public HttpResponse(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void setProtocol(HttpProtocol protocol) {
        this.httpProtocol = protocol;
    }

    public HttpResponse setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public HttpResponse setContentType(String contentType) {
        this.headers.addHeader(HttpHeader.CONTENT_TYPE, contentType);
        return this;
    }

    public HttpResponse setBody(byte[] body) {
        this.body = body;
        this.headers.addHeader(HttpHeader.CONTENT_LENGTH, String.valueOf(body.length));
        return this;
    }

    private void setDefaultHeaders() {
        this.headers.addHeader(HttpHeader.SERVER, "John Park's Web Server");
        this.headers.addHeader(HttpHeader.DATE, DateFormatter.getCurrentDate());
    }

    private void writeStatusLine() throws IOException {
        dos.writeBytes(String.format("%s %d %s\r\n", httpProtocol.getVersion(), status.getCode(), status.getMessage()));
    }

    private void writeHeaders() throws IOException {
        Set<String> keys = headers.getHeaders().keySet();
        for (String key: keys) {
            dos.writeBytes(key + ": " + headers.getHeaders().get(key) + "\r\n");
        }
        dos.writeBytes("\r\n");
    }

    public void send() {
        setDefaultHeaders();
        try {
            writeStatusLine();
            writeHeaders();
            writeBody();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void sendError(HttpStatus status) {
        setStatus(status);
        send();
    }

    public void sendError(HttpStatus status, String errMessage) {
        setStatus(status);
        setBody(errMessage.getBytes());
        send();
    }

    private void writeBody() throws IOException {
        if (body != null) {
            dos.write(body, 0, body.length);
        }
        dos.writeBytes("\r\n");
        dos.flush(); // OS의 네트워크 스택인 TCP(socket) 버퍼에 즉시 전달 보장 (flush)
    }
}