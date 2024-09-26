package webserver;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

// RequsetHandler
// 사용자 요청을 처리하는 스레드로 응답을 처리한다.
// HTTP Message의 header에 응답 status 상태와 body에 Hello World!를 저장해서 응답한다.
// 이때, Java의 I/O 스트림을 사용해서 소켓을 통해 데이터 송수신한다.
// 클라이언트와 연결된 소켓을 닫기 (커널 영역에 할당된 I/O 자원을 해제)
public class RequestHandler extends Thread {
    private final Socket connectedSocket;

    public RequestHandler(Socket connectedSocket) {
        this.connectedSocket = connectedSocket;
    }

    @Override
    public void run() {
        try (
                // Socket(TCP) Buffer에 저장된 데이터를 읽기 위한 InputStream을 제공, 이 스트림을 통해 클라이언트로 부터 받은 데이터 받아올수 있다.
                InputStream in = connectedSocket.getInputStream();
                // Socket(TCP) Buffer에 저장된 데이터를 쓰기 위한 OutputStream을 제공, 이 스트림을 통해 클라이언트에게 데이터를 보낼수 있다.
                OutputStream out = connectedSocket.getOutputStream() //
        ) {
            // 데이터를 읽고 쓰는데 1 byte 단위가 아닌 8가지의 기본형(primitive type)으로 읽고 쓸수 있도록 DataInputStream과 DataOutputStream 사용
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = "Hello World!".getBytes();
            // header
            responseHeader(dos, body.length);
            // body
            responseBody(dos, body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // HTTP 메세지에서 줄끝을 구분하기 위해 '\r\n'을 사용
    private void responseHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes( "Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
