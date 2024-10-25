package com.hello_webserver.webresources;

public enum ResourceType {
    HTML(".html", "text/html"),
    JS(".js", "application/javascript"),
    CSS(".css", "text/css"),
    PNG(".png", "image/png"),
    JPEG(".jpeg", "image/jpeg"),
    JSON(".json", "application/json"),
    URL(".url", "application/x-www-form-urlencoded");

    private final String extension;
    private final String contentType;

    ResourceType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getExtension() {
        return extension;
    }

    public String getContentType() {
        return contentType;
    }

    public static ResourceType fromFilePath(String filePath) {
        for (ResourceType fileType : values()) {
            if (filePath.endsWith(fileType.getExtension())) {
                return fileType;
            }
        }
        return null;
    }
}