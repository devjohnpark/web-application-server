package org.doci.http.api;

import org.doci.http.request.HttpRequest;
import org.doci.http.response.HttpResponse;

public interface HttpApiHandler {
    void service(HttpRequest request, HttpResponse response);
}
