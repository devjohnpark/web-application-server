package com.hello_webserver.response;

import com.hello_webserver.webserver.RequestHandler;
import com.hello_webserver.request.RequestLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DateUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// 클라이언트의 응답 데이터 처리
public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final String webAppDir;

    public HttpResponse(String webAppDir) {
        this.webAppDir = webAppDir;
    }

    public ResponseMessage createResponse(RequestLine requestLine) {
        // 400: 유효한 http method와 유효한 url 검증 실패
        if (!isValidRequest(requestLine)) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "The server cannot or will not process the request.");
        }

        // GET Method
        if (isGetMethod(requestLine)) {
            return handleGetRequest(requestLine);
        }

        // 405
        return createErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, "The request method is known by the server but is not supported by the target resource.");
    }

    public void sendResponse(DataOutputStream dos, ResponseMessage responseMessage) {
        responseHeader(dos, responseMessage, responseMessage.getBody().length);
        responseBody(dos, responseMessage.getBody());
    }

    private ResponseMessage handleGetRequest(RequestLine requestLine) {
        Resource resource = readResource(setIfRootPath(requestLine.getPath()));

        // 404
        if (resource == null) {
            return createErrorResponse(HttpStatus.NOT_FOUND, "The requested resource could not be found.");
        }

        // 200
        return new ResponseMessage(HttpStatus.OK, resource.getData(), getResponseContentType(resource.getFormat()), DateUtils.getCurrentDate());
    }

    private boolean isValidRequest(RequestLine requestLine) {
        return requestLine != null;
    }

    private boolean isGetMethod(RequestLine requestLine) {
        return "GET".equals(requestLine.getMethod());
    }

    private ResponseMessage createErrorResponse(HttpStatus status, String content) {
        return new ResponseMessage(status, content.getBytes(), getResponseContentType(""), DateUtils.getCurrentDate());
    }

    // 각 리소스 포맷마다 ResourceReader 필요
    private Resource readResource(String filePath) {
        if (filePath.endsWith(".html")) {
            return new Resource(readFile(filePath), ".html");
        }
        return null;
    }

    private byte[] readFile(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(webAppDir + filePath));
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    private String setIfRootPath(String filePath) {
        if (filePath.equals("/")) { return "/index.html"; }
        return filePath;
    }

    private String getResponseContentType(String format) {
        if (format.isEmpty()) {
            // 브라우저/모바일 분기 필요
            return "text/html; charset=utf-8";
        } else if (format.equals(".html")) {
            return "text/html; charset=utf-8";
        } else if (format.equals(".json")) {
            return "application/json; charset=utf-8";
        }

        return "text/plain; charset=utf-8";
    }

    private void responseHeader(DataOutputStream dos, ResponseMessage responseMessage, int lengthBody) {
        try {
            // HTTP 메세지에서 문자열 줄끝을 구분하기 위해 '\r\n'을 사용
            dos.writeBytes(String.format("HTTP/1.1 %d %s\r\n", responseMessage.getStatus().getCode(), responseMessage.getStatus().getMessage()));
            dos.writeBytes(String.format("Date: %s\r\n", responseMessage.getDate()));
            dos.writeBytes(String.format("Content-Type: %s\r\n", responseMessage.getContentType()));
            dos.writeBytes(String.format( "Content-Length: %d\r\n", lengthBody));
            dos.writeBytes("\r\n"); // HTTP header 마지막줄에 body을 구분하기 위해 반드시 필요
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush(); // OS의 네트워크 스택인 TCP(socket) 버퍼에 즉시 전달 보장 (flush)
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static class Resource {
        private final byte[] data;
        private final String format;

        public Resource(byte[] data, String format) {
            this.data = data;
            this.format = format;
        }

        public byte[] getData() {
            return data;
        }

        public String getFormat() {
            return format;
        }
    }
}