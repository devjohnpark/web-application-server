package org.doci.webserver;

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
        Socket establishedSocket;
        // accept(): 클라이언트와 연결 요청을 할때까지 block 되고 연결 요청 수락시 새로운 소켓을 생성, 따라서 acceptedSocket은 null 값이 될수 없음
        while ((establishedSocket = listenSocket.accept()) != null) {
            RequestHandler requestHandler = new RequestHandler(establishedSocket);
            requestHandler.start();
        }
    }
}
