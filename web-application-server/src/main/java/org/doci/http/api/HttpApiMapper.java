package org.doci.http.api;

import org.doci.webresources.ResourceProvider;
import org.doci.webresources.WebResourceHandler;
import org.doci.webserver.WebServer;

import java.util.HashMap;
import java.util.Map;

public class HttpApiMapper {
    private static Map<String, HttpApiHandler> httpApiHandlers = new HashMap<String, HttpApiHandler>();

    private final String webAppDir = WebServer.rootPath;

    static {
        httpApiHandlers.put("/user/create", new LoginHttpApihandler());
    }

    public HttpApiHandler getHttpApiHandler(String path) {
        // url에 맵핑된 http method 요청 조회하고 없으면 DefaultHttpApiHandler 반환
        HttpApiHandler httpApiHandler = httpApiHandlers.get(path);
        if (httpApiHandler == null) {
            httpApiHandler = new DefaultHttpApiHandler(new ResourceProvider(webAppDir, new WebResourceHandler()));
        }
        return httpApiHandler;
    }
}
