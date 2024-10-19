package com.hello_webserver.webresources;

import com.hello_webserver.webserver.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WebResourceManager {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final ResourceFactory resourceFactory = new ResourceFactory();
    private final String webAppDir;
    private final ResourceHandler resourceHandler;

    public WebResourceManager(String webAppDir, ResourceHandler resourceHandler) {
        this.webAppDir = webAppDir;
        this.resourceHandler = resourceHandler;
    }

    public Resource getResource(String path) {
       return resourceFactory.getResource(webAppDir + setIfRootPath(path), resourceHandler);
    }

    private String setIfRootPath(String filePath) {
        if (filePath.equals("/")) {
            filePath = "/index.html";
        }
        return filePath;
    }
}
