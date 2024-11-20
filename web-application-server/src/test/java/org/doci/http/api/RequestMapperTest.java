package org.doci.http.api;

import org.doci.webresources.WebResourceProvider;
import org.doci.webserver.RequestMapper;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class RequestMapperTest {

    @Test
    void set_get_HttpApiHandler() {
        RequestMapper.registerHttpApiHandler("/user/create", new LoginHttpApiHandler());
        HttpApiHandler httpApiHandler = RequestMapper.getHttpApiHandler("/user/create");
        assertEquals(LoginHttpApiHandler.class, httpApiHandler.getClass());
    }

    @Test
    void set_get_HttpApiHandler_default() {
        RequestMapper.registerHttpApiHandler("/", new DefaultHttpApiHandler(WebResourceProvider.getInstance("webapp")));
        HttpApiHandler httpApiHandler = RequestMapper.getHttpApiHandler("/");
        assertEquals(DefaultHttpApiHandler.class, httpApiHandler.getClass());
    }

    @Test
    void set_HttpApiHandler_null() {
        assertThrows(IllegalArgumentException.class, () -> RequestMapper.registerHttpApiHandler("/user", null));
    }
}