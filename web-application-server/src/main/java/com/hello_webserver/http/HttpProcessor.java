package com.hello_webserver.http;

import com.hello_webserver.webserver.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// HTTP 요청에 따른 API Handler를 배정받아 처리
public class HttpProcessor {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final HttpApiMapper httpApiMapper;

    public HttpProcessor(HttpApiMapper httpApiMapper) {
        this.httpApiMapper = httpApiMapper;
    }

    public void process(HttpRequest request, HttpResponse response) {
        HttpApiHandler httpApiHandler = httpApiMapper.getHttpApiHandler(request.getPath());
        httpApiHandler.service(request, response);
    }
}