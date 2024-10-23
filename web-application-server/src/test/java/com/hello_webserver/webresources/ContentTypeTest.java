package com.hello_webserver.webresources;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContentTypeTest {

    @Test
    void fromFilePath_support_file_format() {
        String resourcePath = "/index.html";
        assertThat(ContentType.fromFilePath(resourcePath)).isEqualTo(ContentType.HTML);
    }

    @Test
    void fromFilePath_unsupport_file_format() {
        String resourcePath = "/index.ht";
        assertThat(ContentType.fromFilePath(resourcePath)).isNull();
    }
}