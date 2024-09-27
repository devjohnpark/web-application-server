package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        if (args != null && args.length > 0) { port = Integer.parseInt(args[0]); }

        try (ServerSocket listeningSocket = new ServerSocket()) {
            log.info("Web Application Server started {} port.", port);

            listeningSocket.bind(new InetSocketAddress(port));
            Socket acceptedSocket;
            // accept(): 클라이언트와 연결 요청을 할때까지 block 되고 연결 요청 수락시 새로운 소켓을 생성, 따라서 acceptedSocket은 null 값이 될수 없음
            while ((acceptedSocket = listeningSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(acceptedSocket);
                requestHandler.start();
            }
        }
    }
}

//2024-09-27T15:44:21.850+09:00  INFO 71005 --- [hello-spring] [           main] c.h.hello_spring.HelloSpringApplication  : Starting HelloSpringApplication using Java 21.0.3 with PID 71005 (/Users/junseopark/Documents/dev/backend/spring/spring_study/hello-spring/build/classes/java/main started by junseopark in /Users/junseopark/Documents/dev/backend/spring/spring_study/hello-spring)
//2024-09-27T15:44:21.863+09:00  INFO 71005 --- [hello-spring] [           main] c.h.hello_spring.HelloSpringApplication  : No active profile set, falling back to 1 default profile: "default"
//2024-09-27T15:44:22.681+09:00  INFO 71005 --- [hello-spring] [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
//2024-09-27T15:44:22.740+09:00  INFO 71005 --- [hello-spring] [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 53 ms. Found 1 JPA repository interface.
//2024-09-27T15:44:23.494+09:00  INFO 71005 --- [hello-spring] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
//2024-09-27T15:44:23.505+09:00  INFO 71005 --- [hello-spring] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
//2024-09-27T15:44:23.505+09:00  INFO 71005 --- [hello-spring] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.25]
//2024-09-27T15:44:23.550+09:00  INFO 71005 --- [hello-spring] [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
//2024-09-27T15:44:23.551+09:00  INFO 71005 --- [hello-spring] [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1630 ms
//2024-09-27T15:44:23.689+09:00  INFO 71005 --- [hello-spring] [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
//2024-09-27T15:44:23.728+09:00  INFO 71005 --- [hello-spring] [           main] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 6.5.2.Final
//2024-09-27T15:44:23.762+09:00  INFO 71005 --- [hello-spring] [           main] o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
//2024-09-27T15:44:23.966+09:00  INFO 71005 --- [hello-spring] [           main] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
//2024-09-27T15:44:23.983+09:00  INFO 71005 --- [hello-spring] [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
//2024-09-27T15:44:26.322+09:00  WARN 71005 --- [hello-spring] [           main] o.h.e.j.e.i.JdbcEnvironmentInitiator     : HHH000342: Could not obtain connection to query metadata