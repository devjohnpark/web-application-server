package org.doci.webresources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WebResourceProvider implements ResourceProvider {
//    private static WebResourceProvider instance = null;
    private final String rootPath;

//    private WebResourceProvider(String rootPath) {
//        this.rootPath = rootPath;
//    }

    public WebResourceProvider(String webBase) {
        this.rootPath = webBase;
    }

//    public static synchronized WebResourceProvider getInstance(String webBase) {
//        if (instance == null) {
//            instance = new WebResourceProvider(webBase);
//        }
//        return instance;
//    }

//    public static WebResourceProvider getInstance(String webBase) {
////        if (instance == null) {
////            instance = new WebResourceProvider(webBase);
////        }
//        return instance;
//    }

    public final Resource getResource(String path) {
        String resourcePath = rootPath + setIfRootPath(path);
        byte[] content = readResource(resourcePath);
        ResourceType resourceType = ResourceType.fromFilePath(resourcePath);
        if (isReadContent(content) && isSupportedResourceType(resourceType)) {
            return new Resource(content, resourceType.getMimeType());
        }
        return new Resource();
    }

    private byte[] readResource(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            return null;
        }
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