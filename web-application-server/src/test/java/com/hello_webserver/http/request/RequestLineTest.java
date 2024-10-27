package com.hello_webserver.http.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class RequestLineTest {

    @Test
    void createRequestLine_only_path() {
        RequestLine requestLine = RequestLine.createFromRequestLine("GET / HTTP/1.1");
        assertThat(requestLine.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(requestLine.getPath()).isEqualTo("/");
        assertThat(requestLine.getVersion()).isEqualTo(HttpVersion.HTTP_1_1);
    }

    @Test
    void createRequestLine_with_querystring() {
        RequestLine requestLine = RequestLine.createFromRequestLine("GET /user/create?name=john%park&age=20 HTTP/1.1");
        assertThat(requestLine.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(requestLine.getPath()).isEqualTo("/user/create");
        assertThat(requestLine.getQueryString()).isEqualTo("name=john%park&age=20");
        assertThat(requestLine.getVersion()).isEqualTo(HttpVersion.HTTP_1_1);
    }
}