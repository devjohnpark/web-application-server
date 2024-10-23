//package com.hello_webserver.http;
//
//import com.hello_webserver.webserver.RequestHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//// HTTP 요청에 따른 API Handler를 할당받아 처리
//public class HttpProcessor {
//    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
//    private final HttpApiMapper httpApiMapper;
//
//    public HttpProcessor(HttpApiMapper httpApiMapper) {
//        this.httpApiMapper = httpApiMapper;
//    }
//
//    public void process(HttpRequest request, HttpResponse response) {
//        HttpApiHandler httpApiHandler = httpApiMapper.getHttpApiHandler(request.getPath());
//        httpApiHandler.service(request, response);
//    }
//
//     들어온 값이 유효하지 않아서 연산 결과값이 도출이안될경우에 null로 표현하게된다.
//     null을 반환할경우에 호출자에게 위험이 전파된다. 따라서 is 메서드로 boolean으로 제어한다. (꼭 null을 반환이 불가피한 경우 Nullable 주석을 작성한다.)
//
//     Ex) parse header
//     boolean parseHeader() 메서드 호출 -> 구현부에서 HeaderParseStatus parseHeader() 호출해서 로직 실행 후 상태 결과값 저장 및 반환
//     -> parseHeader() 구현부애서 HeaderParseStatus에 따라 true/false 반환
//     true면 getHeader()를 호출해서 header 가져오기
//
//     특정 메서드를 호출하기 전에, 그 메서드를 호출하기 유효한지 boolean 메서드를 호출한다. 그리고 결과값이 true이면 특정 메서드를 호출하도롥한다.
//     null 처리를 exception으로 처리할려면 안된다. Exception은 예기치 못한 값이 발생할때만 사용해야한다.
//     유효한 값/무효한 값이 들어왔을때 처리해서 그 상태를 알려주면된다. 무효한 값이 들어올수있는 가정이 있으면 exception이 아니다.
//     exception은 boundary 내에서만 처리하도록한다. 안그러면 상위 메서드로 전파되기 때문에 처리하기 어려워진다.
//
//    // HttpRequestFilter
//    // filter 기능: 불명확한 요청 -> 에러응답/명확한 요청 -> api handle
//    // filter할 상태 값
//    // 각 객체 내에 상태값 저장
//    // SocketState -> Open, Closed
//    // RequestLineState -> HTTP Method, URL, HTTP Version
//    // HeaderParseStatus -> 분리 ParameterParseStatus (Query Parameter, Header Parameter)
//
//    /*
//    결정 사항: Request Line, Request header 파싱하면서 값을 검사할 것인가? 아니면 파싱 후에 검사할것 인가?
//    -> 성능 효율적: Request header 파싱하면서 값을 검사
//
//     */
//
//    /*
//    HttpHeaderState
//    1. HttpParser로 request 헤더 파싱 후 이상 발생시 예외 반환
//    2. HeaderParseStatus 상태 저장
//    3. HttpRequestFilter로 상태에 따라 에러 지정 및 바디에 에러 메세지 지정
//    4. HttpResponse로 응답
//     */
//
//}