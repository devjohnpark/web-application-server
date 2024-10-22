package com.hello_webserver.webresources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class ResourceProviderTest {
    private ResourceProvider resourceProvider;

    @BeforeEach
    void setUp() {
        resourceProvider = new ResourceProvider("webapp", new WebResourceHandler());
    }

    @Test
    void getResource_root_path() {
        Resource resource = resourceProvider.getResource("/");
        assertThat(resource.isEmpty()).isEqualTo(false);
    }

    @Test
    void getResource_index_path() {
        Resource resource = resourceProvider.getResource("/index.html");
        assertThat(resource.isEmpty()).isEqualTo(false);
    }

    @Test
    void getResource_non_exist_path() {
        Resource resource = resourceProvider.getResource("/index");
        assertThat(resource.isEmpty()).isEqualTo(true);
    }
}