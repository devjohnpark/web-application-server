package com.hello_webserver.webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*; // assertThat
import static org.junit.jupiter.api.Assertions.*; // assertThrow


// RequestLine에 따른 응답 메세지 상태 테스트
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
        httpResponse = new HttpResponse("webapp");
    }

    @Test
    void validResponseMessage_get_root_200() {
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
    void validResponseMessage_get_index_200() {
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
    void validResponseMessage_get_index_404() {
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
    void validResponseMessage_wrong_http_method_index_400() {
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
    void validResponseMessage_post_root_405() {
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
    void validResponseMessage_post_index_405() {
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