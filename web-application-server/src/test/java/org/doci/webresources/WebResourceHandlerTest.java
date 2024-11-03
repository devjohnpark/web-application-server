package org.doci.webresources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WebResourceHandlerTest {
    private WebResourceHandler webResourceHandler;

    @BeforeEach
    void setUp() {
        webResourceHandler = new WebResourceHandler();
    }

    @Test
    void readFile_exist_path() {
        byte[] actual = webResourceHandler.readResource("webapp/index.html");
        assertThat(actual).isNotNull();
    }

    @Test
    void readFile_non_exist_path() {
        byte[] actual = webResourceHandler.readResource("webapp/index.h");
        assertThat(actual).isNull();
    }
}