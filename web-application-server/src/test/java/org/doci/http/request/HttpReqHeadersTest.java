package org.doci.http.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HttpReqHeadersTest {

    @Test
    void headers_add_get() {
        HttpReqHeaders headers = new HttpReqHeaders();
        String keepAlive = "keep-alive";
        String cookie = "login=true";
        String contentType = "text/html; charset=utf-8";
        int contentLength = 10;

        headers.addHeader(String.format("Connection: %s", keepAlive));
        headers.addHeader(String.format("Cookie: %s", cookie));
        headers.addHeader(String.format("Content-Type: %s", contentType));
        headers.addHeader(String.format("Content-Length: %d", contentLength));
        assertThat(headers.getConnection()).isEqualTo(keepAlive);
        assertThat(headers.getContentType()).isEqualTo(contentType);
        assertThat(headers.getContentLength()).isEqualTo(String.valueOf(contentLength));
    }
}