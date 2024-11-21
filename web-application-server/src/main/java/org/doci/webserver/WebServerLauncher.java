package org.doci.webserver;

import org.doci.http.api.LoginHttpApiHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WebServerLauncher {
    public static void main(String[] args) throws IOException {
        WebServer server = new WebServer(8080, "localhost");
        server.getWebService().addService("/user/create", new LoginHttpApiHandler());
        server.start();
    }
}
