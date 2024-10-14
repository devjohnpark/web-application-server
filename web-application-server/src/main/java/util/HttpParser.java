package util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// 공통 Http
public class HttpParser {
    // Request Line: GET /user/create?userId=john&password=1234&name=john park HTTP/1.1
    // url: /user/create?userId=john&password=1234&name=john park
    // endpoint: /user/create
    // query string: userId=john&password=1234&name=john park
    // ?: resource와 query string 간의 분리
    // &: query parameter 간의 분리
    // =: key, value 간의 분리

    // 1. " " 기준으로 split해서 Request Line 저장
    // 2. Request Line에서 1번 인덱스 값 추출해서 url 저장
    // 3. url에서 ? 문자 탐색해서 resource와 query string으로 분리해서 Request Line에 저장
    // 4. query string에서 & 문자 기준으로 split 해서 query parameters로 분리
    // 5. query parameter에서 = 문자 기준으로 분리 후 key, value로 분리
    // 6. HttpRequest에서 query parameters의 key, value 쌍들을 가져와서 특정 로직 처리하여 응답을 준다.


    // url에서 ? 문자 탐색해서 resource와 query string으로 분리
    // 공통 아님 -> Request Line에 저장
//    private Map<String, String> parseUrl(String url) {
//        int idx = url.indexOf("?");
//        String path = url.substring(0, idx);
//        String queryString = url.substring(idx+1);
//    }

    // header의 값에 따라서 응답을 처리해야하므로 HttpRequest에 요청 메세지의 header를 key, value로 저장이 필요하다.)
    // 이후, HttpRequest에 header에 특정 값에 대해서 처리해서 HttpResponse를 사용해서 응답을 준다.


    public static Map<String, String> parseQueryString(String queryString, String separator) {
        return parseValues(queryString, "&");
    }

    public static Map<String, String> parseValues(String values, String separator) {
        Map<String, String> map = new HashMap<>();
        if (values == null || values.isEmpty()) {
            return map;
        }
        String[] pairs = values.split(separator);
        for (String pair : pairs) {
            Pair p = getPair(pair, "=");
            if (p != null) {
                map.put(p.key, p.value);
            }
        }
        return map;
    }

    private static Pair getPair(String keyValue, String separator) {
        if (keyValue == null || keyValue.isEmpty()) {
            return null;
        }
        String[] pair = keyValue.split(separator);
        if (pair.length != 2) {
            return null;
        }
        return new Pair(pair[0].trim(), pair[1].trim());
    }

    public static Pair parseHeader(String header) {
        return getPair(header, ": ");
    }

    public record Pair(String key, String value) {}

}
