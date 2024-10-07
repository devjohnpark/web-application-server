package com.hello_webserver.webserver;

import com.hello_webserver.request.HttpRequest;
import com.hello_webserver.request.RequestLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*; // assertThat


// RequestLine에 따른 유효값 테스트
//  GET / HTTP/1.1
//  GET /index.html HTTP/1.1
//  GET user HTTP/1.1
class HttpRequestTest {

    private HttpRequest httpRequest;

    @BeforeEach
    void setUp() {
        httpRequest = new HttpRequest(WebServer.ROOT_PATH);
    }

    @Test
    void requestLine_root() {
        // given
        String httpMethod = "GET";
        String resourcePath = "/";
        String line = String.format("%s %s HTTP/1.1\r\n", httpMethod, resourcePath);

        // when
        RequestLine requestLine = httpRequest.readRequestHeader(line);

        // then
        assertThat(requestLine.getMethod()).isEqualTo(httpMethod);
        assertThat(requestLine.getPath()).isEqualTo(resourcePath);
    }

    @Test
    void requestLine_index_html() {

        // given
        String httpMethod = "GET";
        String resourcePath = "/index.html";
        String line = String.format("%s %s HTTP/1.1\r\n", httpMethod, resourcePath);

        // when
        RequestLine requestLine = httpRequest.readRequestHeader(line);

        // then
        assertThat(requestLine.getMethod()).isEqualTo(httpMethod);
        assertThat(requestLine.getPath()).isEqualTo(resourcePath);
    }

    @Test
    void requestLine_not_root_start() {

        // given
        String httpMethod = "GET";
        String resourcePath = "user";
        String line = String.format("%s %s HTTP/1.1\r\n", httpMethod, resourcePath);

        // when
        RequestLine requestLine = httpRequest.readRequestHeader(line);

        // then
        assertThat(requestLine).isEqualTo(null);
    }
}