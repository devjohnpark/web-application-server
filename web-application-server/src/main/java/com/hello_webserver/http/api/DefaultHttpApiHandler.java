package com.hello_webserver.http.api;

import com.hello_webserver.http.request.HttpMethod;
import com.hello_webserver.http.request.HttpRequest;
import com.hello_webserver.http.response.HttpResponse;
import com.hello_webserver.http.response.HttpStatus;
import com.hello_webserver.webresources.Resource;
import com.hello_webserver.webresources.ResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultHttpApiHandler extends AbstractHttpApiHandler {
    private static final Logger log = LoggerFactory.getLogger(DefaultHttpApiHandler.class);
    private final ResourceProvider resourceProvider;

    public DefaultHttpApiHandler(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
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
        Resource resource = resourceProvider.getResource(request.getPath());
        if (resource.isEmpty()) {
            response.sendError(HttpStatus.NOT_FOUND);
        } else {
            response.setContentType(resource.getContentType())
                    .setBody(resource.getData())
                    .send();
        }
    }
}
