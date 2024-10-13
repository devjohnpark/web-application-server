package com.hello_webserver.webresources;

import com.hello_webserver.webserver.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileResourceReader implements ResourceReader {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    // 각 리소스 포맷마다 FileResourceReader 필요
    public Resource readResource(String filePath) {
        if (filePath.endsWith(".html")) {
            return new Resource(readFile(filePath), ".html");
        } else if (filePath.endsWith(".js")) {
            return new Resource(readFile(filePath), ".js");
        } else if (filePath.endsWith(".css")) {
            return new Resource(readFile(filePath), ".css");
        } else if (filePath.endsWith(".png")) {
            return new Resource(readFile(filePath), ".png");
        } else if (filePath.endsWith(".jpeg")) {
            return new Resource(readFile(filePath), ".jpg");
        } else if (filePath.endsWith(".jpg")) {
            return new Resource(readFile(filePath), ".jpg");
        }
        return null;
    }

    private byte[] readFile(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        return null;
    }
}
