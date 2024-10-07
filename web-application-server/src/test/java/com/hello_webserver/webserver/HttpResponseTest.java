package com.hello_webserver.webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DateUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*; // assertThat
import static org.junit.jupiter.api.Assertions.*; // assertThrow


// RequestLine에 따른 응답 메세지 상태 테스트
// null -> 400
// GET / HTTP/1.0 -> 200
// GET /index.html HTTP/1.0 -> 200
// GE /index.html HTTP/1.0 -> 405
// GET /user HTTP/1.0 -> 404
// POST / HTTP/1.0 -> 405
// POST /index.html HTTP/1.0 -> 405
class HttpResponseTest {
    private HttpResponse httpResponse;

    @BeforeEach
    void setUp() {
        httpResponse = new HttpResponse(WebServer.ROOT_PATH);

    }

//    @Test
//    void sendResponse() throws IOException {
//        // Given
//        String content = "Hello World";
//        String date = DateUtils.getCurrentDate();
//        String contentType = "text/html; charset=utf-8";
//        int lengthBody = content.length();
//        byte[] body = content.getBytes(StandardCharsets.UTF_8);
//        ResponseMessage responseMessage = new ResponseMessage(HttpStatus.OK, body, contentType);
//        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); // 출력한 데이터 버퍼에서 가져오기 위해서 사용
//            DataOutputStream dos = new DataOutputStream(baos);) {
//
//            // When
//            httpResponse.sendResponse(dos, responseMessage, date);
//
//            // Then
//            String actualResponse = baos.toString(StandardCharsets.UTF_8);
//            String[] expectedResponse = {
//                    String.format("HTTP/1.1 %d %s", responseMessage.getStatus().getCode(), responseMessage.getStatus().getMessage()),
//                    String.format("Date: %s", date),
//                    String.format("Content-Type: %s", responseMessage.getContentType()),
//                    String.format("Content-Length: %d", lengthBody),
//            };
//
//            String[] actualLine = actualResponse.split("\r\n");
//            for (int i = 0; i < expectedResponse.length; i++) {
//                assertThat(actualLine[i]).isEqualTo(expectedResponse[i]);
//            }
//            assertThat(actualLine[expectedResponse.length + 1]).isEqualTo(content); // "\r\n\r\n" + contentType
//        } catch (IOException e) {
//            throw new IOException(e);
//        }
//    }

    @Test
    void responseMessage_null() {
        // given
        RequestLine requestLine = null;

        // when
        ResponseMessage responseMessage = httpResponse.createResponse(requestLine);

        // then
        assertThat(responseMessage.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void responseMessage_get_root_200() {
        // given
        String httpMethod = "GET";
        String path = "/";
        RequestLine requestLine = new RequestLine(httpMethod, path);

        // when
        ResponseMessage responseMessage = httpResponse.createResponse(requestLine);

        // then
        assertThat(responseMessage.getStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void responseMessage_get_index_200() {
        // given
        String httpMethod = "GET";
        String path = "/index.html";
        RequestLine requestLine = new RequestLine(httpMethod, path);

        // when
        ResponseMessage responseMessage = httpResponse.createResponse(requestLine);

        // then
        assertThat(responseMessage.getStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void responseMessage_get_index_404() {
        // given
        String httpMethod = "GET";
        String path = "/user";
        RequestLine requestLine = new RequestLine(httpMethod, path);

        // when
        ResponseMessage responseMessage = httpResponse.createResponse(requestLine);

        // then
        assertThat(responseMessage.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void responseMessage_wrong_http_method_index_400() {
        // given
        String httpMethod = "GE";
        String path = "/index.html";
        RequestLine requestLine = new RequestLine(httpMethod, path);

        // when
        ResponseMessage responseMessage = httpResponse.createResponse(requestLine);

        // then
        assertThat(responseMessage.getStatus()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Test
    void responseMessage_post_root_405() {
        // given
        String httpMethod = "POST";
        String path = "/";
        RequestLine requestLine = new RequestLine(httpMethod, path);

        // when
        ResponseMessage responseMessage = httpResponse.createResponse(requestLine);

        // then
        assertThat(responseMessage.getStatus()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Test
    void responseMessage_post_index_405() {
        // given
        String httpMethod = "POST";
        String path = "/index.html";
        RequestLine requestLine = new RequestLine(httpMethod, path);

        // when
        ResponseMessage responseMessage = httpResponse.createResponse(requestLine);

        // then
        assertThat(responseMessage.getStatus()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    }
}