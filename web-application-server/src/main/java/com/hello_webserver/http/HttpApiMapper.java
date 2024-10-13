package com.hello_webserver.http;

import com.hello_webserver.webresources.ResourceHandler;

import java.util.HashMap;
import java.util.Map;

public class HttpApiMapper {
    private static Map<String, HttpApiHandler> httpApiHandlers = new HashMap<String, HttpApiHandler>();

    private final String webAppDir;

    public HttpApiMapper(String webAppDir) {
        this.webAppDir = webAppDir;
    }

    static {
//        httpApiHandlers.put("/user/create", new UserApiHandler());
    }

    public HttpApiHandler getHttpApiHandler(String url) {
        // url에 맵핑된 http method 요청 조회하고 없으면 DefaultHttpApiHandler 반환
        HttpApiHandler httpApiHandler = httpApiHandlers.get(url);
        if (httpApiHandler == null) {
            httpApiHandler = new DefaultHttpApiHandler(new ResourceHandler(webAppDir));
        }
        return httpApiHandler;
    }
}
