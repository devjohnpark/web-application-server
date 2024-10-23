package com.hello_webserver.http;

import com.hello_webserver.webserver.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DateFormatter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.util.Set;


public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private final DataOutputStream dos;
    private final StatusLine statusLine = new StatusLine(HttpProtocol.HTTP_1_1, HttpStatus.OK);
    private final HttpHeader headers = new HttpHeader();
    private byte[] body = null;

    public HttpResponse(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    private void setDefaultHeaders() {
        this.headers.addHeader(HttpHeader.SERVER, "John Park's Web Server");
        this.headers.addHeader(HttpHeader.DATE, DateFormatter.getCurrentDate());
    }

    public HttpResponse setConnection(boolean isConnection) {
        if (isConnection) {
            this.headers.addHeader(HttpHeader.CONNECTION, "keep-alive");
        } else {
            this.headers.addHeader(HttpHeader.CONNECTION, "close");
        }
        return this;
    }

    public HttpResponse setProtocol(HttpProtocol protocol) {
        this.statusLine.protocol = protocol;
        return this;
    }

    public HttpResponse setStatus(HttpStatus status) {
        this.statusLine.status = status;
        return this;
    }

    public HttpResponse setContentType(String contentType) {
        this.headers.addHeader(HttpHeader.CONTENT_TYPE, contentType);
        return this;
    }

    public HttpResponse setCookie(boolean isLogin) {
        this.headers.addHeader(HttpHeader.SET_COOKIE, String.valueOf(isLogin));
        return this;
    }

    public HttpResponse setBody(byte[] body) {
        this.body = body;
        this.headers.addHeader(HttpHeader.CONTENT_LENGTH, String.valueOf(body.length));
        return this;
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

    private void writeStatusLine() throws IOException {
        dos.writeBytes(statusLine.toString());
    }

    private void writeHeaders() throws IOException {
        Set<String> keys = headers.getHeaders().keySet();
        for (String key: keys) {
            dos.writeBytes(key + ": " + headers.getHeaders().get(key) + "\r\n");
        }
        dos.writeBytes("\r\n");
    }

    private void writeBody() throws IOException {
        if (body != null) {
            dos.write(body, 0, body.length);
        }
        dos.writeBytes("\r\n");
        dos.flush(); // OS의 네트워크 스택인 TCP(socket) 버퍼에 즉시 전달 보장 (flush)
    }

    // StatusLine 객체는 HttpResponse 객체에 의존하지 않으므로 static으로 선언
    private static class StatusLine {
        private HttpProtocol protocol;
        private HttpStatus status;

        public StatusLine(HttpProtocol protocol, HttpStatus status) {
            this.protocol = protocol;
            this.status = status;
        }

        @Override
        public String toString() {
            return String.format("%s %d %s\r\n", protocol.getVersion(), status.getCode(), status.getMessage());
        }
    }
}