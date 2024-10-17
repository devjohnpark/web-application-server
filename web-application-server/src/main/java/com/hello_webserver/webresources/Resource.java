package com.hello_webserver.webresources;

public class Resource {
    private byte[] data;
    private String format;

    public Resource() {}

    public Resource(byte[] data, String format) {
        this.data = data;
        this.format = format;
    }

    public byte[] getData() {
        return data;
    }

    public String getFormat() {
        return format;
    }

    public boolean isEmpty() {
        return data == null && getFormat() == null;
    }
}