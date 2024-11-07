package org.doci.http.request;

import org.doci.webresources.ResourceType;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {
    private HttpRequest httpRequest;

    // 문자열 바이트 배열로 변환 -> 바이트 기반 입력 스트림의 내부 버퍼에 저장 -> 입력 스트림으로 버퍼에서 읽어오기
    private void createHttpRequest(String httpMessage) {
        InputStream in = new ByteArrayInputStream(httpMessage.getBytes(StandardCharsets.UTF_8));
        httpRequest = new HttpRequest(in);
    }

    @Test
    void get_url_only_path() {
        // given
        String httpRequestMessage = """
                    GET / HTTP/1.1
                    Connection: keep-alive
                    
                    """;

        // when
        createHttpRequest(httpRequestMessage);

        // then
        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getPath()).isEqualTo("/");
        assertThat(httpRequest.getRequestParameter("name")).isNull();
        assertThat(httpRequest.getRequestParameter("")).isNull();
        assertThat(httpRequest.getHttpVersion()).isEqualTo(HttpVersion.HTTP_1_1);
        assertThat(httpRequest.getHeader(RequestHeaders.CONNECTION)).isEqualTo(httpRequest.getConnection());
    }

    @Test
    void get_url_querystring() {
        // given
        String httpRequestMessage = """
                    GET /user/create?name=john%20park&age=20 HTTP/1.1
                    Connection: keep-alive

                    """;

        // when
        createHttpRequest(httpRequestMessage);

        // then
        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getPath()).isEqualTo("/user/create");
        assertThat(httpRequest.getRequestParameter("name")).isEqualTo("john park");
        assertThat(httpRequest.getRequestParameter("age")).isEqualTo("20");
        assertThat(httpRequest.getHttpVersion()).isEqualTo(HttpVersion.HTTP_1_1);
        assertThat(httpRequest.getHeader(RequestHeaders.CONNECTION)).isEqualTo(httpRequest.getConnection());
    }

    @Test
    void post() {
        // given
        String content = "userId=john park&password=1234";
        int contentLength = content.length();

        String httpRequestMessage = String.format("""
                    POST /user/create HTTP/1.1
                    Connection: keep-alive
                    Content-Type: %s
                    Content-Length: %d
                    
                    %s
                    
                    """,  ResourceType.URL.getMimeType(), contentLength, content);

        // when
        createHttpRequest(httpRequestMessage);

        // then
        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(httpRequest.getPath()).isEqualTo("/user/create");
        assertThat(httpRequest.getRequestParameter("userId")).isEqualTo("john park");
        assertThat(httpRequest.getRequestParameter("password")).isEqualTo("1234");
        assertThat(httpRequest.getHttpVersion()).isEqualTo(HttpVersion.HTTP_1_1);
        assertThat(httpRequest.getHeader(RequestHeaders.CONNECTION)).isEqualTo(httpRequest.getConnection());
    }

    @Test
    void post_non_content_length() {
        // given
        String content = "userId=john park&password=1234";
        String httpRequestMessage = String.format("""
                    POST /user/create HTTP/1.1
                    Connection: keep-alive
                    Content-Type: %s
                    
                    %s
                    
                    """,  ResourceType.URL.getMimeType(), content);

        // when
        createHttpRequest(httpRequestMessage);

        // then
        assertThat(httpRequest.getRequestParameter("userId")).isNull();
        assertThat(httpRequest.getRequestParameter("password")).isNull();
    }

    @Test
    void post_negative_content_length() {
        // given
        String content = "userId=john park&password=1234";
        String httpRequestMessage = String.format("""
                    POST /user/create HTTP/1.1
                    Connection: keep-alive
                    Content-Type: %s
                    Content-Length: %d
                    
                    %s
                    
                    """, ResourceType.URL.getMimeType(), -1, content);

        // when
        createHttpRequest(httpRequestMessage);

        // then
        assertThat(httpRequest.getRequestParameter("userId")).isNull();
        assertThat(httpRequest.getRequestParameter("password")).isNull();
    }

    @Test
    void post_non_content_type() {
        // given
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
        assertThat(httpRequest.getRequestParameter("userId")).isNull();
        assertThat(httpRequest.getRequestParameter("password")).isNull();
    }

    @Test
    void post_requestLine_body_params_duplication() {
        // given
        String content = "userId=john&password=1234";
        int contentLength = content.length();

        String httpRequestMessage = String.format("""
                    POST /user/create?userId=jizz&name=jimmy HTTP/1.1
                    Connection: keep-alive
                    Content-Type: %s
                    Content-Length: %d
                    
                    %s
                    
                    """,  ResourceType.URL.getMimeType(), contentLength, content);

        // when
        createHttpRequest(httpRequestMessage);

        // then
        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(httpRequest.getPath()).isEqualTo("/user/create");
        assertThat(httpRequest.getRequestParameter("userId")).isEqualTo("john");
        assertThat(httpRequest.getRequestParameter("name")).isEqualTo("jimmy");
        assertThat(httpRequest.getRequestParameter("password")).isEqualTo("1234");
        assertThat(httpRequest.getHttpVersion()).isEqualTo(HttpVersion.HTTP_1_1);
        assertThat(httpRequest.getHeader(RequestHeaders.CONNECTION)).isEqualTo(httpRequest.getConnection());
    }

    @Test
    void post_getAllBody() throws IOException {
        // given
        String content = "hello world";
        int contentLength = content.length();

        String httpRequestMessage = String.format("""
                    POST /user/create HTTP/1.1
                    Connection: keep-alive
                    Content-Type: %s
                    Content-Length: %d
                    
                    %s
                    
                    """, ResourceType.TEXT.getMimeType(), contentLength, content);

        // when
        createHttpRequest(httpRequestMessage);

        // then
        assertThat(httpRequest.getAllBody()).isEqualTo(content);
    }

    @Test
    void post_getAllBodyAsBytes() throws IOException {
        // given
        String content = "hello world";
        int contentLength = content.length();

        String httpRequestMessage = String.format("""
                    POST /user/create HTTP/1.1
                    Connection: keep-alive
                    Content-Type: %s
                    Content-Length: %d
                    
                    %s
                    
                    """, ResourceType.TEXT.getMimeType(), contentLength, content);

        // when
        createHttpRequest(httpRequestMessage);

        // then
        assertThat(httpRequest.getAllBody()).isEqualTo(content);
    }
}