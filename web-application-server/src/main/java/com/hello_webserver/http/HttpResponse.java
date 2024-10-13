package com.hello_webserver.http;

import com.hello_webserver.webresources.Resource;
import com.hello_webserver.webserver.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DateUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

// 클라이언트의 응답 데이터 처리
public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final DataOutputStream dos;

    public HttpResponse(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    private void sendResponse(Response response) {
        responseHeader(response, response.getBody().length);
        responseBody(response.getBody());
    }

    public void sendError(HttpStatus status) {
        sendResponse(createErrorResponse(status, status.getDescription()));
    }

    public void sendError(HttpStatus status, String message) {
        sendResponse(createErrorResponse(status, message));
    }

    public void sendResource(Resource resource) {
        if (resource == null) {
            sendError(HttpStatus.NOT_FOUND);
            return;
        }
        sendResponse(createResponse(resource));
    }

    private Response createResponse(Resource resource) {
        return new Response(HttpStatus.OK, resource.getData(), getResponseContentType(resource.getFormat()), DateUtils.getCurrentDate());
    }

    private Response createErrorResponse(HttpStatus status, String content) {
        return new Response(status, content.getBytes(), getResponseContentType(""), DateUtils.getCurrentDate());
    }

    private String getResponseContentType(String format) {
        if (format.isEmpty()) {
            // 브라우저/모바일 분기 필요
            return "text/html; charset=UTF-8";
        } else if (format.equals(".html")) {
            return "text/html; charset=UTF-8";
        } else if (format.equals(".json")) {
            return "application/json; charset=UTF-8";
        }

        return "text/plain; charset=UTF-8";
    }

    private void responseHeader(Response response, int lengthBody) {
        try {
            // HTTP 메세지에서 문자열 줄끝을 구분하기 위해 '\r\n'을 사용
            dos.writeBytes(String.format("HTTP/1.1 %d %s\r\n", response.getStatus().getCode(), response.getStatus().getMessage()));
            dos.writeBytes(String.format("Date: %s\r\n", response.getDate()));
            dos.writeBytes(String.format("Content-Type: %s\r\n", response.getContentType()));
            dos.writeBytes(String.format( "Content-Length: %d\r\n", lengthBody));
            dos.writeBytes("\r\n"); // HTTP header 마지막줄에 body을 구분하기 위해 반드시 필요
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush(); // OS의 네트워크 스택인 TCP(socket) 버퍼에 즉시 전달 보장 (flush)
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}