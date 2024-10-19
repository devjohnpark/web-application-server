package com.hello_webserver.webresources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WebResourceHandler implements ResourceHandler {
    public byte[] readResource(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void writeResource(String filePath) {

    }
}
