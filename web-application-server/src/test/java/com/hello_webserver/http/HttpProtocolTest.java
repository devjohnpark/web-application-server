package com.hello_webserver.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class HttpProtocolTest {

    @Test
    void protocol() {
        HttpProtocol httpProtocol = HttpProtocol.fromString("HTTP/1.1");
        assertThat(httpProtocol.getVersion()).isEqualTo("HTTP/1.1");
    }

    @Test
    void protocol_exception() {
        assertThrows(IllegalArgumentException.class, () -> HttpProtocol.fromString("HTTP/1.2"));
    }

}