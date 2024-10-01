package com.hello_webserver.webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

// RequsetHandler
// 사용자 요청을 처리하는 스레드로 응답을 처리한다.
// HTTP Message의 header에 응답 status 상태와 body에 Hello World!를 저장해서 응답한다.
// 이때, Java의 I/O 스트림을 사용해서 소켓을 통해 데이터 송수신한다.
// 클라이언트와 연결된 소켓을 닫기 (커널 영역에 할당된 I/O 자원을 해제)
public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connectedSocket;

    public RequestHandler(Socket connectedSocket) {
        this.connectedSocket = connectedSocket;
    }

    @Override
    public void run() {

        log.debug("New Client Connected IP: {}, Port: {}", connectedSocket.getInetAddress(), connectedSocket.getPort());
        try (
                // Socket(TCP) Buffer에 저장된 데이터를 읽기 위한 InputStream을 제공, 이 스트림을 통해 클라이언트로 부터 받은 데이터 받아올수 있다.
                InputStream in = connectedSocket.getInputStream();
                // Socket(TCP) Buffer에 저장된 데이터를 쓰기 위한 OutputStream을 제공, 이 스트림을 통해 클라이언트에게 데이터를 보낼수 있다.
                OutputStream out = connectedSocket.getOutputStream();
        ) {
            String filePath = readHeader(in);
            byte[] body = getResource(filePath);

            // 데이터를 읽고 쓰는데 바이트 단위가 아닌 기본형 또는 참조형으로 읽고 쓸수 있도록 DataInputStream과 DataOutputStream 사용
            DataOutputStream dos = new DataOutputStream(out);

            response200Header(dos, body.length);
            responseBody(dos, in, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String readHeader(InputStream in) {
        String filePath = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            String[] tokens = line.split(" ");
            for (String token : tokens) {
                if (token.equals("/index.html")) {
                    filePath = "/index.html";
                    log.debug("Resource was requested file path: {}", filePath);
                }
            }

            while (line != null && !line.isEmpty()) {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return filePath;
    }

    private byte[] getResource(String filePath) {
        byte[] body = new byte[0];
        if (filePath == null || filePath.isEmpty()) {
            return body;
        }
        String rootPath = "webapp";
        try {
            body = Files.readAllBytes(Paths.get(rootPath + filePath));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return body;
    }


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            // HTTP 메세지에서 문자열 줄끝을 구분하기 위해 '\r\n'을 사용
            dos.writeBytes("HTTP/1.1 200 OK\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes( "Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n"); // HTTP header 마지막줄에 body을 구분하기 위해 반드시 필요
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, InputStream in, byte[] body) {
        try {
            dos.write(body, 0, body.length);
//            dos.flush(); // OS의 네트워크 스택인 TCP(socket) 버퍼에 즉시 전달 보장 (flush)
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

