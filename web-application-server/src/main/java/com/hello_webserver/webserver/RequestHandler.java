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
    private final static String rootPath = "webapp";
    private final Socket connectedSocket;
    private int status = 200;

    public RequestHandler(Socket connectedSocket) {
        this.connectedSocket = connectedSocket;
    }

    // 1. 클라이언트 요청 헤더에서 리소스 읽기 (헤더 값 null일 경우, 400)
    // 2. 헤더에서 리소스 경로 추출 (/와 /index.html은 index.html로 응답)
    // 3. 해당 경로의 파일의 데이터 가져오기 (실패시 404/성공시 200)
    // 4. 응답 헤더 생성
    // 5. 응답 바디 생성 
    @Override
    public void run() {
        log.debug("New Client Connected IP: {}, Port: {}", connectedSocket.getInetAddress(), connectedSocket.getPort());
        try (
                // Socket(TCP) Buffer에 저장된 데이터를 읽기 위한 InputStream을 제공, 이 스트림을 통해 클라이언트로 부터 받은 데이터 받아올수 있다.
                InputStream in = connectedSocket.getInputStream();
                // Socket(TCP) Buffer에 저장된 데이터를 쓰기 위한 OutputStream을 제공, 이 스트림을 통해 클라이언트에게 데이터를 보낼수 있다.
                OutputStream out = connectedSocket.getOutputStream();
        ) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            byte[] body = receiveRequest(reader);

            // 데이터를 읽고 쓰는데 바이트 단위가 아닌 참조형 또는 기본형으로 읽고 쓸수 있도록 DataInputStream과 DataOutputStream 사용
            DataOutputStream dos = new DataOutputStream(out);
            sendResponse(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendResponse(DataOutputStream dos, byte[] body) {
        switch (status) {
            case 200:
                response200Header(dos, body.length);
                break;
            case 400:
                response400Header(dos, body.length);
                body = "The server cannot or will not process the request.".getBytes();
                break;
            case 404:
                response404Header(dos, body.length);
                body = "The requested url was not found on this server.".getBytes();
                break;
        }
        responseBody(dos, body);
    }

    private byte[] receiveRequest(BufferedReader reader) {
        String resource = readResourceFromHeader(reader);
        byte[] body = new byte[0];
        if (resource != null) {
            String path = findPath(resource);
            body = getResource(path);
        }
        return body;
    }

    private String readResourceFromHeader(BufferedReader br) {
        String resource = null;
        try {
            String line = br.readLine();
            String[] tokens = line.split(" ");
            if (tokens.length >= 2) { resource = tokens[1]; }
        } catch (IOException e) {
            log.error(e.getMessage());
            status = 400;
        }
        return resource;
    }

    private String findPath(String path) {
        if (path.equals("/") || path.equals("/index.html")) {
            path = "/index.html";
            log.debug("Resource was requested file path: {}", path);
        }
        return path;
    }

    private byte[] getResource(String filePath) {
        byte[] body = new byte[0];
        try {
            body = Files.readAllBytes(Paths.get(rootPath + filePath));
        } catch (IOException e) {
            status = 404;
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

    private void response404Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            // HTTP 메세지에서 문자열 줄끝을 구분하기 위해 '\r\n'을 사용
            dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("\r\n"); // HTTP header 마지막줄에 body을 구분하기 위해 반드시 필요
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response400Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            // HTTP 메세지에서 문자열 줄끝을 구분하기 위해 '\r\n'을 사용
            dos.writeBytes("HTTP/1.1 400 Bad Request\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("\r\n"); // HTTP header 마지막줄에 body을 구분하기 위해 반드시 필요
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush(); // OS의 네트워크 스택인 TCP(socket) 버퍼에 즉시 전달 보장 (flush)
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

