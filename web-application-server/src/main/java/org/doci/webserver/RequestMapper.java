package org.doci.webserver;

import org.doci.http.api.AbstractHttpApiHandler;
import org.doci.http.api.HttpApiHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class RequestMapper {
    private static final Logger log = LoggerFactory.getLogger(RequestMapper.class);
    private final Map<String, AbstractHttpApiHandler> handlerMappings; // new HashMap<String, HttpApiHandler>()

    public RequestMapper(Map<String, AbstractHttpApiHandler> handlerMappings) {
         this.handlerMappings = handlerMappings;
    }

    public HttpApiHandler getHttpApiHandler(String path) {
        HttpApiHandler httpApiHandler = handlerMappings.get(path);
        if (httpApiHandler == null) {
            return handlerMappings.get("/");
        }
        return httpApiHandler;
    }
}
