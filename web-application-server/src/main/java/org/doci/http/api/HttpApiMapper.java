package org.doci.http.api;

import org.doci.webserver.ServerConfig;
import org.doci.webserver.WebServer;

import java.util.HashMap;
import java.util.Map;

public class HttpApiMapper {
    private final static Map<String, HttpApiHandler> httpApiHandlers = new HashMap<String, HttpApiHandler>();

    public static void registerHttpApiHandler(String path, HttpApiHandler handler) {
        httpApiHandlers.put(path, handler);
    }

    public static HttpApiHandler getHttpApiHandler(String path) {
        HttpApiHandler httpApiHandler = httpApiHandlers.get(path);
        if (httpApiHandler == null) {
            return httpApiHandlers.get(null);
        }
        return httpApiHandler;
    }
}
