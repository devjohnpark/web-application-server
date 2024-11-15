package org.doci.http.api;

import org.doci.webresources.WebResourceHandler;
import org.doci.webresources.WebResourceProvider;
import org.doci.webserver.ServerConfig;
import org.doci.webserver.WebServer;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class HttpApiMapperTest {

    @Test
    void getHttpApiHandler() {
        HttpApiHandler httpApiHandler = HttpApiMapper.getHttpApiHandler("/user");
        assertEquals(DefaultHttpApiHandler.class, httpApiHandler.getClass());
    }

    @Test
    void registerHttpApiHandler_default() {
        HttpApiMapper.registerHttpApiHandler(null, new DefaultHttpApiHandler(WebResourceProvider.getInstance("webapp")));
        HttpApiHandler httpApiHandler = HttpApiMapper.getHttpApiHandler(null);
        assertEquals(DefaultHttpApiHandler.class, httpApiHandler.getClass());
    }

    @Test
    void registerHttpApiHandler() {
        HttpApiMapper.registerHttpApiHandler("/user/create", new LoginHttpApiHandler());
        HttpApiHandler httpApiHandler = HttpApiMapper.getHttpApiHandler("/user/create");
        assertEquals(LoginHttpApiHandler.class, httpApiHandler.getClass());
    }
}