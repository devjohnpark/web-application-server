package com.hello_webserver.webresources;

import com.hello_webserver.webserver.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WebResourceHandler {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final String webAppDir;

    public WebResourceHandler(String webAppDir) {
        this.webAppDir = webAppDir;
    }

    public Resource getResource(String path) {
        String resourcePath = webAppDir + setIfRootPath(path);
        ResourceType resourceType = ResourceType.fromFilePath(resourcePath);
        byte[] content = readResource(resourcePath);
        if (isReadContent(content) && isSupportedResourceType(resourceType)) {
            return new Resource(content, resourceType.getContentType());
        }
        return new Resource();
    }

    private boolean isSupportedResourceType(ResourceType resourceType) {
        return resourceType != null;
    }

    private boolean isReadContent(byte[] content) {
        return content != null;
    }

    private byte[] readResource(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            return null;
        }
    }

    private String setIfRootPath(String filePath) {
        if (filePath.equals("/")) {
            filePath = "/index.html";
        }
        return filePath;
    }
}
