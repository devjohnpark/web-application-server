package org.doci.http.api;

import org.doci.webresources.WebResourceProvider;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class HttpApiMapperTest {

    @Test
    void set_get_HttpApiHandler() {
        HttpApiMapper.registerHttpApiHandler("/user/create", new LoginHttpApiHandler());
        HttpApiHandler httpApiHandler = HttpApiMapper.getHttpApiHandler("/user/create");
        assertEquals(LoginHttpApiHandler.class, httpApiHandler.getClass());
    }

    @Test
    void set_get_HttpApiHandler_default() {
        HttpApiMapper.registerHttpApiHandler(null, new DefaultHttpApiHandler(WebResourceProvider.getInstance("webapp")));
        HttpApiHandler httpApiHandler = HttpApiMapper.getHttpApiHandler(null);
        assertEquals(DefaultHttpApiHandler.class, httpApiHandler.getClass());
    }

    @Test
    void set_HttpApiHandler_null() {
        assertThrows(IllegalArgumentException.class, () -> HttpApiMapper.registerHttpApiHandler("/user", null));
    }
}