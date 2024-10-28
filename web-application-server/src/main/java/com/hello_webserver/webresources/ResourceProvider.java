package com.hello_webserver.webresources;

public class ResourceProvider {
    private final String rootPath;
    private final ResourceHandler resourceHandler;

    public ResourceProvider(String rootPath, ResourceHandler resourceHandler) {
        this.rootPath = rootPath;
        this.resourceHandler = resourceHandler;
    }

    public final Resource getResource(String path) {
        String resourcePath = rootPath + setIfRootPath(path);
        byte[] content = resourceHandler.readResource(resourcePath);
        ResourceType resourceType = ResourceType.fromFilePath(resourcePath);
        if (isReadContent(content) && isSupportedResourceType(resourceType)) {
            return new Resource(content, resourceType.getMimeType());
        }
        return new Resource();
    }

    private final boolean isSupportedResourceType(ResourceType contentType) {
        return contentType != null;
    }

    private final boolean isReadContent(byte[] content) {
        return content != null;
    }

    private String setIfRootPath(String filePath) {
        if (filePath.equals("/")) {
            filePath = "/index.html";
        }
        return filePath;
    }
}
