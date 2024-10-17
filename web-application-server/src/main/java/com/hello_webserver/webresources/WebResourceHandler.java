package com.hello_webserver.webresources;

import java.nio.file.Files;
import java.nio.file.Path;

public class WebResourceHandler {
    private WebResourceReader reader;
    private final String webAppDir;

    public WebResourceHandler(String webAppDir) {
        this.webAppDir = webAppDir;
    }

    // Nullable
    public Resource getResource(String path) {
        String resourcePath = webAppDir + path;
        if (isExistResourceReader(resourcePath)) {
            return reader.readResource(resourcePath);
        }
        return null;
    }

    private boolean isExistResourceReader(String resourcePath) {
        if (isFile(resourcePath)) {
            reader = new FileResourceReader();
        }
        return reader != null;
    }

    private boolean isFile(String filePath) {
        String resourcePath = webAppDir + filePath;
        return Files.exists(Path.of(resourcePath));
    }

    private String setIfRootPath(String filePath) {
        if (filePath.equals("/")) {
            filePath = "/index.html";
        }
        return filePath;
    }
}
