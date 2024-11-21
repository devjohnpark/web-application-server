package org.doci.webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerConfigTest {
    private ServerConfig config;

    @BeforeEach
    void setUp() throws Exception {
        config =  new ServerConfig("./src/main/resources");
    }

    @Test
    void getPort() {
        assertEquals(8080, config.getPort());
    }

    @Test
    void getWebBase() {
        assertEquals("webapp", config.getWebBase());
    }
}