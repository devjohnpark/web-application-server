package com.hello_webserver.http;

public class AbstractHttpApiHandler implements HttpApiHandler {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpMethod method = request.getMethod();
        if (HttpMethod.GET.equals(method)) {
            doGet(request, response);
        } else if (HttpMethod.POST.equals(method)) {
            doPost(request, response);
        } else if (HttpMethod.PUT.equals(method)) {
            doPut(request, response);
        }  else if (HttpMethod.PATCH.equals(method)) {
            doPatch(request, response);
        } else if (HttpMethod.DELETE.equals(method)) {
            doDelete(request, response);
        } else {
            response.sendError(HttpStatus.NOT_IMPLEMENTED);
        }
    }

    protected void doGet(HttpRequest request, HttpResponse response) {
        sendDefaultError(request, response);
    }

    protected void doPost(HttpRequest request, HttpResponse response) {
        sendDefaultError(request, response);
    }

    protected void doPut(HttpRequest request, HttpResponse response) {
        sendDefaultError(request, response);
    }

    protected void doPatch(HttpRequest request, HttpResponse response) {
        sendDefaultError(request, response);
    }

    protected void doDelete(HttpRequest request, HttpResponse response) {
        sendDefaultError(request, response);
    }

    private void sendDefaultError(HttpRequest request, HttpResponse response) {
        HttpProtocol protocol = request.getProtocol();
        if (!protocol.equals(HttpProtocol.HTTP_1_1)) {
            response.sendError(HttpStatus.METHOD_NOT_ALLOWED);
        } else {
            response.sendError(HttpStatus.BAD_REQUEST);
        }
    }
}
