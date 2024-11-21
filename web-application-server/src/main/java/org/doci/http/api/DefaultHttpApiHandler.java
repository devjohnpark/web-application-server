package org.doci.http.api;

import org.doci.http.request.HttpMethod;
import org.doci.http.request.HttpRequest;
import org.doci.http.response.HttpResponse;
import org.doci.http.response.HttpStatus;
import org.doci.webresource.Resource;
import org.doci.webresource.WebResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultHttpApiHandler extends AbstractHttpApiHandler {
    private static final Logger log = LoggerFactory.getLogger(DefaultHttpApiHandler.class);

    @Override
    public void handleApi(HttpRequest request, HttpResponse response) {
        if (HttpMethod.GET.equals(request.getMethod())) {
            doGet(request, response);
        } else {
            super.handleApi(request, response);
        }
    }

    public void doGet(HttpRequest request, HttpResponse response) {
        Resource resource = webResourceProvider.getResource(request.getPath());
        if (resource.isEmpty()) {
            response.sendError(HttpStatus.NOT_FOUND);
        } else {
            response.send(HttpStatus.OK, resource.getData(), resource.getContentType());
        }
    }
}
