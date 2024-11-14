package org.doci.http.api;

import org.doci.webresources.ResourceProvider;
import org.doci.webresources.WebResourceHandler;
import org.doci.webserver.WebServer;

import java.util.HashMap;
import java.util.Map;

public class HttpApiMapper {
    private static final Map<String, HttpApiHandler> httpApiHandlers = new HashMap<String, HttpApiHandler>();

    static {
        httpApiHandlers.put("/", new DefaultHttpApiHandler(new ResourceProvider(WebServer.rootPath, new WebResourceHandler())));
        httpApiHandlers.put("/user/create", new LoginHttpApiHandler());
    }

    public static HttpApiHandler getHttpApiHandler(String path) {
        HttpApiHandler httpApiHandler = httpApiHandlers.get(path);
        if (httpApiHandler == null) {
            httpApiHandler = httpApiHandlers.get("/");
        }
        return httpApiHandler;
    }
}
