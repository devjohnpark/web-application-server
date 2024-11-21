package org.doci.webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebServiceConfigTest {
    private WebServiceConfig webServiceConfig;

    @BeforeEach
    void setUp() throws Exception {
        String testDir = "./src/main/resources";
        webServiceConfig = new WebServiceConfig(testDir, new ServerConfig(testDir));
    }

    @Test
    void getRequestMapper() {
        assertNotNull(webServiceConfig.getRequestMapper());
    }
}