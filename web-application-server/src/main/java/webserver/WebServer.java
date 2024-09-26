package webserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

// WebServer
// main 메서드 인자값으로 포트 번호를 받을 수 있도록 구현
// HTTP는 TCP/IP 위에서 동작하기 때문에, 연결 요청 대기용 소켓을 생성하고 Port 번호와 IP를 바인딩한다. 그래야 어떤 디바이스와 프로세스로 패킷이 송수신 될지 결정된다.
// 소켓을 listen 상태로 변경해서 클라이언트의 요청을 대기한다.
// 클라이언트가 서버로 들어온 요청을 수락하고, 실제 클라이언트와 연결할 소켓을 생성한다.
// 클라이언트 연결 대기용 소켓 닫기 (커널 영역에 할당된 I/O 자원을 해제)
public class WebServer {
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        if (args != null && args.length > 0) { port = Integer.parseInt(args[0]); }

        try (ServerSocket listeningSocket = new ServerSocket()) {
            listeningSocket.bind(new InetSocketAddress(port));
            Socket acceptedSocket;
            // 클라이언트와 연결 요청을 할때까지 대기, 연결 요청 수락시 새로운 소켓을 생성
            while ((acceptedSocket = listeningSocket.accept()) != null) {
                System.out.println("Accepted");
                RequestHandler requestHandler = new RequestHandler(acceptedSocket);
                requestHandler.start();
            }
        }
    }
}

