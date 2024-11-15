package org.doci.webresources;

import org.doci.webserver.ServerConfig;

public class WebResourceProvider {
    private static WebResourceProvider instance;

    private final String rootPath;
    private final ResourceHandler resourceHandler;

    private WebResourceProvider(String rootPath, ResourceHandler resourceHandler) {
        this.rootPath = rootPath;
        this.resourceHandler = resourceHandler;
    }

    public static synchronized WebResourceProvider getInstance(String webBase) {
        if (instance == null) {
            instance = new WebResourceProvider(webBase, new WebResourceHandler());
        }
        return instance;
    }

    public Resource getResource(String path) {
        String resourcePath = rootPath + setIfRootPath(path);
        byte[] content = resourceHandler.readResource(resourcePath);
        ResourceType resourceType = ResourceType.fromFilePath(resourcePath);
        if (isReadContent(content) && isSupportedResourceType(resourceType)) {
            return new Resource(content, resourceType.getMimeType());
        }
        return new Resource();
    }

    private boolean isSupportedResourceType(ResourceType contentType) {
        return contentType != null;
    }

    private boolean isReadContent(byte[] content) {
        return content != null;
    }

    private String setIfRootPath(String filePath) {
        if (filePath.equals("/")) {
            filePath = "/index.html";
        }
        return filePath;
    }
}
