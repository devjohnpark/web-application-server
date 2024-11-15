package org.doci.http.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HttpApiMapper {
    private static final Logger log = LoggerFactory.getLogger(HttpApiMapper.class);
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
