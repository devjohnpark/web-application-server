package org.doci.webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;

// WebServer
// main 메서드 인자값으로 포트 번호를 받을 수 있도록 구현
// HTTP는 TCP/IP 위에서 동작하기 때문에, 연결 요청 대기용 소켓을 생성하고 Port 번호와 IP를 바인딩한다. 그래야 어떤 디바이스와 프로세스로 패킷이 송수신 될지 결정된다.
// 소켓을 listen 상태로 변경해서 클라이언트의 요청을 대기한다.
// 클라이언트가 서버로 들어온 요청을 수락하고, 실제 클라이언트와 연결할 소켓을 생성한다.
// 클라이언트 연결 대기용 소켓 닫기 (커널 영역에 할당된 I/O 자원을 해제)
public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final ServerConfig serverConfig = new ServerConfig();
    private static final ServerContext serverContext = new ServerContext();

    public static void main(String[] args) throws Exception {
        serverInit();
        int port = serverConfig.getPort();
        log.debug("Web Application Server started {} Port.", port);
        Connector connector = new Connector(new ServerSocket(port));
        connector.connect();
    }

    private static void serverInit() throws Exception {
        serverConfig.init("./src/main/resources/server.xml");
        serverContext.init(serverConfig);
    }
}
