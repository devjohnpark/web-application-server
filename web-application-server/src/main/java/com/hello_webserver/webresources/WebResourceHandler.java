package com.hello_webserver.webresources;

import com.hello_webserver.webserver.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WebResourceHandler implements ResourceHandler {
    private static final Logger log = LoggerFactory.getLogger(WebResourceHandler.class);

    public byte[] readResource(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public byte[] writeResource(String filePath) {
        return new byte[0];
    }
}
