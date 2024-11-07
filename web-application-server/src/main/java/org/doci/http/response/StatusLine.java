package org.doci.http.response;

import org.doci.http.request.HttpVersion;

public class StatusLine {
    private HttpVersion protocol;
    private HttpStatus status;

    public StatusLine(HttpVersion protocol, HttpStatus status) {
        this.protocol = protocol;
        this.status = status;
    }

    public void setStatus(HttpStatus status) {
        if (status == null) {
            return;
        }
        this.status = status;
    }

    public void setProtocol(HttpVersion protocol) {
        if (protocol == null) {
            return;
        }
        this.protocol = protocol;
    }

    public HttpVersion getProtocol() {
        return protocol;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("%s %d %s\r\n", protocol.getVersion(), status.getCode(), status.getMessage());
    }
}
