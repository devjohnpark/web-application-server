package com.hello_webserver.http;

import com.hello_webserver.webserver.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DateFormatter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

// 1. / -> /index.html 클라이언트가 요청한 url 변경되었으므로 redirect 할수도 있지만, 사용자 관점에서는 홈페이지이므로 필요없다..
// 2. sendResource -> Resource 타입으로 전달하면 jsonㅇ
// 3. 응답할 데이터 준비 객체로 다루는게 좋다. (Tomcat에서는 resource에서 getContent 메서드로 byte[] 타입으로 받아서 보낸다.)
// 4. resource를 null을 반환 않받도록 패턴 개선
// 4. 응답할 데이터를 헤더에 적재 (클라에서 요청한 동일한 프로토콜로 전송. 즉, HttpRequest에서 가져와야한다.)


// hierarchy



// 응답 데이터 처리 객체 (HttpResponse)
//      헤더 저장 객체(HttpHeader) <- 응답 메세지 객체(ResponseMessage)
//      요청 처리 객체(HttpRequest)의 헤더에서 필요한 값 받는다. (로그인 유무 cookie, session 등)
//      응답 메세지 객체 (ResponseMessage)
//          status (판단은 api handler에서)
//          contentType
//          date
//          body content
// ResourceHandler으로부터 Resource를 받아온다.?
//

// 클라이언트의 응답 데이터 처리

// Inner class로 필드 감쌀지 고민
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