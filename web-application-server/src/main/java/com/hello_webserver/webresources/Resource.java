package com.hello_webserver.webresources;

public class Resource {
    private final byte[] data;
    private final String format;

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

}