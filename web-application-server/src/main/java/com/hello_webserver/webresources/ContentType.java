package com.hello_webserver.webresources;

public enum ContentType {
    HTML(".html", "text/html"),
    JS(".js", "application/javascript"),
    CSS(".css", "text/css"),
    PNG(".png", "image/png"),
    JPEG(".jpeg", "image/jpeg"),
    JSON(".json", "application/json"),
    URL(".url", "application/x-www-form-urlencoded");

    private final String extension;
    private final String contentType;

    ContentType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getExtension() {
        return extension;
    }

    public String getContentType() {
        return contentType;
    }

    public static ContentType fromFilePath(String filePath) {
        for (ContentType fileType : values()) {
            if (filePath.endsWith(fileType.getExtension())) {
                return fileType;
            }
        }
        return null;
    }
}