package org.doci.webresources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WebResourceProviderTest {
    private WebResourceProvider webResourceProvider;

    @BeforeEach
    void setUp() {
        webResourceProvider = WebResourceProvider.getInstance("webapp");
    }

    @Test
    void getResource_root_path() {
        Resource resource = webResourceProvider.getResource("/");
        assertThat(resource.isEmpty()).isEqualTo(false);
    }

    @Test
    void getResource_index_path() {
        Resource resource = webResourceProvider.getResource("/index.html");
        assertThat(resource.isEmpty()).isEqualTo(false);
    }

    @Test
    void getResource_non_exist_path() {
        Resource resource = webResourceProvider.getResource("/index");
        assertThat(resource.isEmpty()).isEqualTo(true);
    }
}