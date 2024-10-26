package com.hello_webserver.webresources;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContentTypeTest {

    @Test
    void fromFilePath_support_file_format() {
        String resourcePath = "/index.html";
        assertThat(ResourceType.fromFilePath(resourcePath)).isEqualTo(ResourceType.HTML);
    }

    @Test
    void fromFilePath_unsupport_file_format() {
        String resourcePath = "/index.ht";
        assertThat(ResourceType.fromFilePath(resourcePath)).isNull();
    }
}