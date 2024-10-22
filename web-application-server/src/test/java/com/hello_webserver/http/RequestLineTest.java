package com.hello_webserver.http;

import com.hello_webserver.webresources.Resource;
import com.hello_webserver.webresources.ResourceProvider;
import com.hello_webserver.webresources.WebResourceHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RequestLineTest {
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
    void getResource_incorrect_path() {
        Resource resource = resourceProvider.getResource("/index");
        assertThat(resource.isEmpty()).isEqualTo(true);
    }
}