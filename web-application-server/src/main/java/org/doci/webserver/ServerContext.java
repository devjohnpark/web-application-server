package org.doci.webserver;

import org.doci.http.api.DefaultHttpApiHandler;
import org.doci.http.api.HttpApiMapper;
import org.doci.webresources.WebResourceProvider;

public class ServerContext {

    public ServerContext(ServerConfig serverConfig) {
        addHttpApiHandlers(getWebResourceProvider(serverConfig));
    }

    private WebResourceProvider getWebResourceProvider(ServerConfig serverConfig) {
        return WebResourceProvider.getInstance(serverConfig.getWebBase());
    }

    private void addHttpApiHandlers(WebResourceProvider webResourceProvider) {
        HttpApiMapper.registerHttpApiHandler(null, new DefaultHttpApiHandler(webResourceProvider));
    }
}
