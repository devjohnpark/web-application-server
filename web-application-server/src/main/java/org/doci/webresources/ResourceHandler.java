package org.doci.webresources;

public interface ResourceHandler {
    byte[] readResource(String filePath);
    byte[] writeResource(String filePath);
}
