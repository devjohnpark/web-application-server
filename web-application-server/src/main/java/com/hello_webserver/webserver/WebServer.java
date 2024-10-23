package com.hello_webserver.webserver;

import com.hello_webserver.http.HttpApiMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// WebServer
// main 메서드 인자값으로 포트 번호를 받을 수 있도록 구현
// HTTP는 TCP/IP 위에서 동작하기 때문에, 연결 요청 대기용 소켓을 생성하고 Port 번호와 IP를 바인딩한다. 그래야 어떤 디바이스와 프로세스로 패킷이 송수신 될지 결정된다.
// 소켓을 listen 상태로 변경해서 클라이언트의 요청을 대기한다.
// 클라이언트가 서버로 들어온 요청을 수락하고, 실제 클라이언트와 연결할 소켓을 생성한다.
// 클라이언트 연결 대기용 소켓 닫기 (커널 영역에 할당된 I/O 자원을 해제)
public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;
    public static final String ROOT_PATH = "webapp";

    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        String rootPath = ROOT_PATH;

        if (args != null && args.length > 0) { port = Integer.parseInt(args[0]); }
        if (args != null && args.length > 1) { rootPath = args[1]; }

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.debug("Web Application Server started {} port.", port);
            Socket estabalishedSocket;
            connect(listenSocket, rootPath);
        }
    }

    private static void connect(ServerSocket listenSocket, String rootPath) throws IOException {
        Socket estabalishedSocket;
        // accept(): 클라이언트와 연결 요청을 할때까지 block 되고 연결 요청 수락시 새로운 소켓을 생성, 따라서 acceptedSocket은 null 값이 될수 없음
        while ((estabalishedSocket = listenSocket.accept()) != null) {
            estabalishedSocket.setKeepAlive(true);
            RequestHandler requestHandler = new RequestHandler(estabalishedSocket, new HttpApiMapper(rootPath));
            requestHandler.start();
        }
    }
}
