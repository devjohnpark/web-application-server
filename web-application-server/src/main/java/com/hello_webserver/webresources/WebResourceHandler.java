package com.hello_webserver.webresources;

import java.nio.file.Files;
import java.nio.file.Path;

public class WebResourceHandler {
    private WebResourceReader reader;
    private final String webAppDir;

    public WebResourceHandler(String webAppDir) {
        this.webAppDir = webAppDir;
    }

    public Resource getResource(String path) {
        String resourcePath = webAppDir + setIfRootPath(path);
        if (isExistReader(resourcePath)) {
            return reader.readResource(resourcePath);
        }
        return new Resource();
    }

    private boolean isExistReader(String resourcePath) {
        if (isFile(resourcePath)) {
            reader = new FileResourceReader();
        }
        return reader != null;
    }

    private boolean isFile(String filePath) {
        return Files.exists(Path.of(filePath));
    }

    private String setIfRootPath(String filePath) {
        if (filePath.equals("/")) {
            filePath = "/index.html";
        }
        return filePath;
    }
}
