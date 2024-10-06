package com.hello_webserver.webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.net.SocketImpl;

import static org.assertj.core.api.Assertions.*; // assertThat
import static org.junit.jupiter.api.Assertions.*; // assertThrow

// RequestLine에 따른 유효값 테스트
//  GET / HTTP/1.1
//  GET /index.html HTTP/1.1
//  GET user HTTP/1.1
class HttpRequestTest {

    private HttpRequest httpRequest;

    @BeforeEach
    void setUp() {
        httpRequest = new HttpRequest("webapp");
    }

    @Test
    void validRequestLine_root() {
        // given
        String httpMethod = "GET";
        String resourcePath = "/";
        BufferedReader br = new BufferedReader(new StringReader(String.format("%s %s HTTP/1.1\r\n", httpMethod, resourcePath)));

        // when
        RequestLine requestLine = httpRequest.readRequestHeader(br);

        // then
        assertThat(requestLine.getMethod()).isEqualTo(httpMethod);
        assertThat(requestLine.getPath()).isEqualTo(resourcePath);
    }

    @Test
    void validRequestLine() {

        // given
        String httpMethod = "GET";
        String resourcePath = "/index.html";
        BufferedReader br = new BufferedReader(new StringReader(String.format("%s %s HTTP/1.1\r\n", httpMethod, resourcePath)));

        // when
        RequestLine requestLine = httpRequest.readRequestHeader(br);

        // then
        assertThat(requestLine.getMethod()).isEqualTo(httpMethod);
        assertThat(requestLine.getPath()).isEqualTo(resourcePath);
    }

    @Test
    void invalidRequestLine_not_root_start() {

        // given
        String httpMethod = "GET";
        String resourcePath = "user";
        BufferedReader br = new BufferedReader(new StringReader(String.format("%s %s HTTP/1.1\r\n", httpMethod, resourcePath)));

        // when
        RequestLine requestLine = httpRequest.readRequestHeader(br);

        // then
        assertThat(requestLine).isEqualTo(null);
    }
}