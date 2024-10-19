package com.hello_webserver.webresources;

public interface ResourceHandler {
    byte[] readResource(String filePath);
    void writeResource(String filePath);
}
