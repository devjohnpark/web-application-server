package com.hello_webserver.http;

public interface HttpApiHandler {
    void service(HttpRequest request, HttpResponse response);
}
