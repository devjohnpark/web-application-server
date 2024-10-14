package com.hello_webserver.util;

import org.junit.jupiter.api.Test;
import util.HttpParser;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpParserTest {
//    @Test
//    void parseQueryString_() {
//        String queryString = "userId=john&password=1234&name=john park";
//        Map<String, String> expected = Map.of(
//                "userId", "john",
//                "password", "1234",
//                "name", "john park"
//        );
//
//        Map<String, String> result = HttpParser.parseQueryString(queryString);
//
//        assertEquals(expected, result);
//    }
//
//    @Test
//    void parseQueryString() {
//        String queryString = "userId=john&password=1234&name=john park";
//        Map<String, String> expected = Map.of(
//                "userId", "john",
//                "password", "1234",
//                "name", "john park"
//        );
//
//        Map<String, String> result = HttpParser.parseQueryString(queryString);
//
//        assertEquals(result, expected);
//    }

    @Test
    void parseString() {
        String str = "  3 ";

        String[] actual = HttpParser.parseString(str, " ");

        for (int i = 0; i < actual.length; i++) {
            System.out.println(actual[i]);
        }
//        assertEquals(str, actual[0]);
    }

}