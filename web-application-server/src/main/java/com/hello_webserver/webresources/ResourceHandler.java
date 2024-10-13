package com.hello_webserver.webresources;

public class ResourceHandler {
    private final ResourceReader reader = new FileResourceReader();
    private final String webAppDir;

    public ResourceHandler(String webAppDir) {
        this.webAppDir = webAppDir;
    }

    public Resource getResource(String resourcePath) {
        String path = webAppDir + setIfRootPath(resourcePath);
        return reader.readResource(path);
    }

    private String setIfRootPath(String filePath) {
        if (filePath.equals("/")) {
            filePath = "/index.html";
        }
        return filePath;
    }
}
