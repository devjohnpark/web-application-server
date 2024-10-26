package com.hello_webserver.http;

import com.hello_webserver.http.request.HttpMethod;
import com.hello_webserver.http.request.HttpReqHeaders;
import com.hello_webserver.http.request.HttpRequest;
import com.hello_webserver.http.request.HttpVersion;
import com.hello_webserver.webresources.ResourceType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {
    private HttpRequest request;

    // 문자열 바이트 배열로 변환 -> 입력 보조 스트림의 내부 버퍼에 저장 -> 입력 스트림으로 버퍼에서 읽어오기
    private void createHttpRequest(String requestString) {
        InputStream in = new ByteArrayInputStream(requestString.getBytes(StandardCharsets.UTF_8));
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
        assertThat(request.getRequestLineParamValue("name")).isNull();
        assertThat(request.getRequestLineParamValue("")).isNull();
        assertThat(request.getHttpVersion()).isEqualTo(HttpVersion.HTTP_1_1);
        assertThat(request.getHeader(HttpReqHeaders.CONNECTION)).isEqualTo(request.getConnection());
    }

    @Test
    void request_get_url_components_full() {
        // given
        String httpRequestMessage = """
                    GET /user/create?name=john&age=20 HTTP/1.1
                    Connection: keep-alive

                    """;

        // when
        createHttpRequest(httpRequestMessage);

        // then
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(request.getPath()).isEqualTo("/user/create");
        assertThat(request.getQueryString()).isEqualTo("name=john&age=20");
        assertThat(request.getRequestLineParamValue("name")).isEqualTo("john");
        assertThat(request.getRequestLineParamValue("age")).isEqualTo("20");
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
        assertThat(request.getBodyParamValue("userId")).isEqualTo("john park");
        assertThat(request.getBodyParamValue("password")).isEqualTo("1234");
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
        assertThat(request.getBodyParamValue("userId")).isNull();
        assertThat(request.getBodyParamValue("password")).isNull();
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
        assertThat(request.getBodyParamValue("userId")).isNull();
        assertThat(request.getBodyParamValue("password")).isNull();
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
        assertThat(request.getBodyParamValue("userId")).isNull();
        assertThat(request.getBodyParamValue("password")).isNull();
    }


}