package com.hello_webserver.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class HttpProtocolTest {

    @Test
    void protocol() {
        HttpProtocol httpProtocol = HttpProtocol.fromString("HTTP/0.9");
        assertThat(httpProtocol.getVersion()).isEqualTo("HTTP/0.9");

        httpProtocol = HttpProtocol.fromString("HTTP/1.0");
        assertThat(httpProtocol.getVersion()).isEqualTo("HTTP/1.0");

        httpProtocol = HttpProtocol.fromString("HTTP/1.1");
        assertThat(httpProtocol.getVersion()).isEqualTo("HTTP/1.1");

        httpProtocol = HttpProtocol.fromString("HTTP/2.0");
        assertThat(httpProtocol.getVersion()).isEqualTo("HTTP/2.0");
    }

    @Test
    void protocol_exception() {
        assertThrows(IllegalArgumentException.class, () -> HttpProtocol.fromString("HTTP/1.2"));
    }

}