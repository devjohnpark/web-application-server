package webserver;

public class WebServer {
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        // WebServer
        // main 메서드 인자값으로 포트 번호를 받을 수 있도록 구현
        // HTTP는 TCP/IP 위에서 동작하기 때문에, 연결 요청 대기용 소켓을 생성하고 Port 번호와 IP를 바인딩한다. 그래야 어떤 디바이스와 프로세스로 패킷이 송수신 될지 결정된다.
        // 소켓을 listen 상태로 변경해서 클라이언트의 요청을 대기한다.
        // 클라이언트가 서버로 들어온 요청을 수락하고, 실제 클라이언트와 연결할 소켓을 생성한다.


        // RequsetHandler
        // 사용자 요청을 처리하는 스레드로 응답을 처리한다.
        // Java의 I/O 스트림을 사용해서 데이터 송수신한다.
        // 소켓을 닫고 커널영역에 할당된 I/O 자원을 해제한다.
    }
}
