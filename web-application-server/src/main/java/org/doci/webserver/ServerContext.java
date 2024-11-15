package org.doci.webserver;

import org.doci.http.api.DefaultHttpApiHandler;
import org.doci.http.api.HttpApiMapper;
import org.doci.webresources.WebResourceProvider;

public class ServerContext {
    private WebResourceProvider webResourceProvider;

    public void init(ServerConfig serverConfig) {
        addHttpApiHandler(getWebResourceProvider(serverConfig));
    }

    private WebResourceProvider getWebResourceProvider(ServerConfig serverConfig) {
        return WebResourceProvider.getInstance(serverConfig.getWebBase());
    }

    private void addHttpApiHandler(WebResourceProvider webResourceProvider) {
        HttpApiMapper.registerHttpApiHandler(null, new DefaultHttpApiHandler(webResourceProvider));
    }
}
