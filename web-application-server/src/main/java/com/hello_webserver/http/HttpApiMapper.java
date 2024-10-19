package com.hello_webserver.http;

import com.hello_webserver.webresources.WebResourceHandler;
import com.hello_webserver.webresources.WebResourceManager;

import java.util.HashMap;
import java.util.Map;

public class HttpApiMapper {
    private static Map<String, HttpApiHandler> httpApiHandlers = new HashMap<String, HttpApiHandler>();

    private final String webAppDir;

    public HttpApiMapper(String webAppDir) {
        this.webAppDir = webAppDir;
    }

    static {
        httpApiHandlers.put("/user/create", new LoginHttpApihandler());
    }

    public HttpApiHandler getHttpApiHandler(String path) {
        // url에 맵핑된 http method 요청 조회하고 없으면 DefaultHttpApiHandler 반환
        HttpApiHandler httpApiHandler = httpApiHandlers.get(path);
        if (httpApiHandler == null) {
            WebResourceHandler webResourceHandler = new WebResourceHandler();
            WebResourceManager webResourceManager = new WebResourceManager(webAppDir, webResourceHandler);
            httpApiHandler = new DefaultHttpApiHandler(webResourceManager);
        }
        return httpApiHandler;
    }
}
