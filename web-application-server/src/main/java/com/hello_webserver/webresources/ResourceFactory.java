package com.hello_webserver.webresources;

public class ResourceFactory {

    public final Resource getResource(String resourcePath, ResourceHandler resourceHandler) {
        ResourceType resourceType = ResourceType.fromFilePath(resourcePath);
        byte[] content = resourceHandler.readResource(resourcePath);
        if (isReadContent(content) && isSupportedResourceType(resourceType)) {
            return new Resource(content, resourceType.getContentType());
        }
        return new Resource();
    }

    private final boolean isSupportedResourceType(ResourceType resourceType) {
        return resourceType != null;
    }

    private final boolean isReadContent(byte[] content) {
        return content != null;
    }
}
