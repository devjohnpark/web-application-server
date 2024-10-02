package com.hello_webserver.webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DateUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

// RequsetHandler
// 사용자 요청을 처리하는 스레드로 응답을 처리한다.
// HTTP Message의 header에 응답 status 상태와 body에 Hello World!를 저장해서 응답한다.
// 이때, Java의 I/O 스트림을 사용해서 소켓을 통해 데이터 송수신한다.
// 클라이언트와 연결된 소켓을 닫기 (커널 영역에 할당된 I/O 자원을 해제)
public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String ROOT_PATH = "webapp";
    private final Socket connectedSocket;

    public RequestHandler(Socket connectedSocket) {
        this.connectedSocket = connectedSocket;
    }

    // 1. 클라이언트 요청 헤더에서 리소스 읽기  (리소스 null 이면 400)
    // 2. 헤더에서 리소스 경로 추출 (/와 /index.html은 index.html로 응답)
    // 3. 해당 경로의 리소스의 데이터 가져오기 (리소스 가져오기 실패시 404/리소그 가져오기 성공시 200)
    // 4. 응답 헤더 쓰기
    // 5. 응답 바디 쓰기
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

            String resourcePath = readResourcePath(reader); // 헤더에서 요청된 리소스 읽기
            HttpResponse httpResponse = findResource(resourcePath); // 요청된 리소스의 데이터를 읽기

            DataOutputStream dos = new DataOutputStream(out);
            responseHeader(dos, httpResponse, httpResponse.getBody().length);
            responseBody(dos, httpResponse.getBody());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String readResourcePath(BufferedReader br) {
        String resource = null;
        try {
            String line = br.readLine();
            String[] tokens = line.split(" ");
            if (tokens.length >= 2) { resource = tokens[1]; }
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        return resource;
    }

    private HttpResponse findResource(String resourcePath) {
        if (resourcePath == null) {
            return new HttpResponse(HttpStatus.BAD_REQUEST, "The server cannot or will not process the request.".getBytes());
        }

        byte[] content = getResource(resourcePath);
        if (content == null) {
            return new HttpResponse(HttpStatus.NOT_FOUND, "The requested resource could not be found.".getBytes());
        }

        return new HttpResponse(HttpStatus.OK, content);
    }

    private byte[] getResource(String filePath) {
        byte[] body = null;
        if (filePath.equals("/")) { filePath = "/index.html"; }
        try {
            body = Files.readAllBytes(Paths.get(ROOT_PATH + filePath));
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        return body;
    }

    private void responseHeader(DataOutputStream dos, HttpResponse httpResponse, int lengthBody) {
        try {
            // HTTP 메세지에서 문자열 줄끝을 구분하기 위해 '\r\n'을 사용
            dos.writeBytes(String.format("HTTP/1.1 %d %s\r\n", httpResponse.getStatus().getCode(), httpResponse.getStatus().getMessage()));
            dos.writeBytes(String.format("Date: %s\r\n", DateUtils.getCurrentDate()));
            dos.writeBytes("Content-Type: text/html; charset=utf-8\r\n");
            dos.writeBytes( "Content-Length: " + lengthBody + "\r\n");
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

