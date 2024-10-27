package com.hello_webserver.http.request;

import com.hello_webserver.webresources.ResourceType;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {
    private HttpRequest request;

    // 문자열 바이트 배열로 변환 -> 바이트 기반 입력 스트림의 내부 버퍼에 저장 -> 입력 스트림으로 버퍼에서 읽어오기
    private void createHttpRequest(String httpMessage) {
        InputStream in = new ByteArrayInputStream(httpMessage.getBytes(StandardCharsets.UTF_8));
        request = new HttpRequest(in);
    }

    @Test
    void request_get_url_components_not_full() {
        // given
        String httpRequestMessage = """
                    GET / HTTP/1.1
                    Connection: keep-alive
                    
                    """;

        // when
        createHttpRequest(httpRequestMessage);

        // then
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(request.getPath()).isEqualTo("/");
        assertThat(request.getQueryString()).isEqualTo("");
        assertThat(request.getRequestParameter("name")).isNull();
        assertThat(request.getRequestParameter("")).isNull();
        assertThat(request.getHttpVersion()).isEqualTo(HttpVersion.HTTP_1_1);
        assertThat(request.getHeader(HttpReqHeaders.CONNECTION)).isEqualTo(request.getConnection());
    }

    @Test
    void request_get_url_components_full() {
        // given
        String httpRequestMessage = """
                    GET /user/create?name=john%20park&age=20 HTTP/1.1
                    Connection: keep-alive

                    """;

        // when
        createHttpRequest(httpRequestMessage);

        // then
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(request.getPath()).isEqualTo("/user/create");
        assertThat(request.getQueryString()).isEqualTo("name=john%20park&age=20");
        assertThat(request.getRequestParameter("name")).isEqualTo("john park");
        assertThat(request.getRequestParameter("age")).isEqualTo("20");
        assertThat(request.getHttpVersion()).isEqualTo(HttpVersion.HTTP_1_1);
        assertThat(request.getHeader(HttpReqHeaders.CONNECTION)).isEqualTo(request.getConnection());
    }

    @Test
    void request_post_content_type_url() {

        String content = "userId=john park&password=1234";
        int contentLength = content.length();

        String httpRequestMessage = String.format("""
                    POST /user/create HTTP/1.1
                    Connection: keep-alive
                    Content-Type: %s
                    Content-Length: %d
                    
                    %s
                    """,  ResourceType.URL.getContentType(), contentLength, content);

        // when
        createHttpRequest(httpRequestMessage);

        // then
        assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(request.getPath()).isEqualTo("/user/create");
        assertThat(request.getQueryString()).isEqualTo("");
        assertThat(request.getRequestParameter("userId")).isEqualTo("john park");
        assertThat(request.getRequestParameter("password")).isEqualTo("1234");
        assertThat(request.getHttpVersion()).isEqualTo(HttpVersion.HTTP_1_1);
        assertThat(request.getHeader(HttpReqHeaders.CONNECTION)).isEqualTo(request.getConnection());
    }

    @Test
    void request_post_non_content_length_url() {

        String content = "userId=john park&password=1234";
        int contentLength = content.length();

        String httpRequestMessage = String.format("""
                    POST /user/create HTTP/1.1
                    Connection: keep-alive
                    Content-Type: %s
                    
                    %s
                    """,  ResourceType.URL.getContentType(), content);

        // when
        createHttpRequest(httpRequestMessage);

        // then
        assertThat(request.getRequestParameter("userId")).isNull();
        assertThat(request.getRequestParameter("password")).isNull();
    }

    @Test
    void request_post_invalid_content_length() {

        String content = "userId=john park&password=1234";
        int contentLength = content.length();

        String httpRequestMessage = String.format("""
                    POST /user/create HTTP/1.1
                    Connection: keep-alive
                    Content-Type: %s
                    Content-Length: %d
                    
                    %s
                    """,  ResourceType.URL.getContentType(), 0, content);

        // when
        createHttpRequest(httpRequestMessage);

        // then
        assertThat(request.getRequestParameter("userId")).isNull();
        assertThat(request.getRequestParameter("password")).isNull();
    }

    @Test
    void request_post_non_content_type() {

        String content = "userId=john park&password=1234";
        int contentLength = content.length();

        String httpRequestMessage = String.format("""
                    POST /user/create HTTP/1.1
                    Connection: keep-alive
                    Content-Length: %d
                    
                    %s
                    """, contentLength, content);

        // when
        createHttpRequest(httpRequestMessage);

        // then
        assertThat(request.getRequestParameter("userId")).isNull();
        assertThat(request.getRequestParameter("password")).isNull();
    }

    @Test
    void request_post_urlParams_bodyParams() {

        String content = "userId=john&password=1234";
        int contentLength = content.length();

        String httpRequestMessage = String.format("""
                    POST /user/create?userId=jizz&name=jimmy HTTP/1.1
                    Connection: keep-alive
                    Content-Type: %s
                    Content-Length: %d
                    
                    %s
                    """,  ResourceType.URL.getContentType(), contentLength, content);

        // when
        createHttpRequest(httpRequestMessage);

        // then
        assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(request.getPath()).isEqualTo("/user/create");
        assertThat(request.getQueryString()).isEqualTo("userId=jizz&name=jimmy");
        assertThat(request.getRequestParameter("userId")).isEqualTo("john");
        assertThat(request.getRequestParameter("name")).isEqualTo("jimmy");
        assertThat(request.getRequestParameter("password")).isEqualTo("1234");
        assertThat(request.getHttpVersion()).isEqualTo(HttpVersion.HTTP_1_1);
        assertThat(request.getHeader(HttpReqHeaders.CONNECTION)).isEqualTo(request.getConnection());
    }
}