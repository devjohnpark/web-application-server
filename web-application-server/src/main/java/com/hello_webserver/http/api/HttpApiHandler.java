package com.hello_webserver.http.api;

import com.hello_webserver.http.request.HttpRequest;
import com.hello_webserver.http.response.HttpResponse;

public interface HttpApiHandler {
    void service(HttpRequest request, HttpResponse response);
}
