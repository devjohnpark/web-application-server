package com.hello_webserver.webserver;

import com.hello_webserver.http.HttpApiMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connector {
    private static final Logger log = LoggerFactory.getLogger(Connector.class);
    private final ServerSocket listenSocket;

    public Connector(ServerSocket listenSocket) {
        this.listenSocket = listenSocket;
    }

    public void connect() throws IOException {
        Socket estabalishedSocket;
        // accept(): 클라이언트와 연결 요청을 할때까지 block 되고 연결 요청 수락시 새로운 소켓을 생성, 따라서 acceptedSocket은 null 값이 될수 없음
        while ((estabalishedSocket = listenSocket.accept()) != null) {
            estabalishedSocket.setKeepAlive(true);
            RequestHandler requestHandler = new RequestHandler(estabalishedSocket, new HttpApiMapper(WebServer.ROOT_PATH));
            requestHandler.start();
        }
    }
}
