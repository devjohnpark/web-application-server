package com.hello_webserver.http;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {
    @Test
    void request_url_components_not_full() {
        // given
        String httpRequest = """
                    GET / HTTP/1.1
                    Host: localhost:8080
                    Connection: keep-alive
                    
                    """;
        InputStream in = new ByteArrayInputStream(httpRequest.getBytes());

        // when
        HttpRequest request = new HttpRequest(in);

        // then
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(request.getPath()).isEqualTo("/");
        assertThat(request.getQueryString()).isEqualTo("");
        assertThat(request.getParameterValue("name")).isNull();
        assertThat(request.getParameterValue("")).isNull();
        assertThat(request.getProtocol()).isEqualTo(HttpProtocol.HTTP_1_1);
        assertThat(request.getHeaderValue("Host")).isEqualTo("localhost:8080");
        assertThat(request.getHeaderValue("Connection")).isEqualTo("keep-alive");
    }

    @Test
    void request_not_url_components_full() {
        // given
        String httpRequest = """
                    GET /user/create?name=john&age=20 HTTP/1.1
                    Host: localhost:8080
                    Connection: keep-alive

                    """;
        InputStream in = new ByteArrayInputStream(httpRequest.getBytes());

        // when
        HttpRequest request = new HttpRequest(in);

        // then
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(request.getPath()).isEqualTo("/user/create");
        assertThat(request.getQueryString()).isEqualTo("name=john&age=20");
        assertThat(request.getParameterValue("name")).isEqualTo("john");
        assertThat(request.getParameterValue("age")).isEqualTo("20");
        assertThat(request.getProtocol()).isEqualTo(HttpProtocol.HTTP_1_1);
        assertThat(request.getHeaderValue("Host")).isEqualTo("localhost:8080");
        assertThat(request.getHeaderValue("Connection")).isEqualTo("keep-alive");
    }
}