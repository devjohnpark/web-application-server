package com.hello_webserver.http;

import com.hello_webserver.webresources.Resource;
import com.hello_webserver.webresources.WebResourceHandler;
import com.hello_webserver.webserver.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultHttpApiHandler extends AbstractHttpApiHandler {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final WebResourceHandler resourceHandler;

    public DefaultHttpApiHandler(WebResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        if (HttpMethod.GET.equals(request.getMethod())) {
            doGet(request, response);
        } else {
            super.service(request, response);
        }
    }

    public void doGet(HttpRequest request, HttpResponse response) {
//        response.sendResource(resourceHandler.getResource(request.getPath()));
        Resource resource = resourceHandler.getResource(request.getPath());
        if (resource == null) {
            response.sendError(HttpStatus.NOT_FOUND);
        } else {
            response.sendResource(resource);
        }
    }
}
