package com.hello_webserver.webresources;

import com.hello_webserver.webserver.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileResourceReader implements WebResourceReader {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private enum FileType {
        HTML(".html"),
        JS(".js"),
        CSS(".css"),
        PNG(".png"),
        JPEG(".jpeg");

        private final String extension;

        FileType(String extension) {
            this.extension = extension;
        }

        public String getExtension() {
            return extension;
        }

        public static FileType fromFilePath(String filePath) {
            for (FileType fileType : values()) {
                if (filePath.endsWith(fileType.getExtension())) {
                    return fileType;
                }
            }
            return null;
        }
    }

    public Resource readResource(String filePath) {
        FileType fileType = FileType.fromFilePath(filePath);
        if (fileType != null) {
            return new Resource(readFile(filePath), fileType.getExtension());
        }
        return null;
    }

    private byte[] readFile(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        return null;
    }
}
