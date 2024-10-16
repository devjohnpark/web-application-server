package com.hello_webserver.util;

import org.junit.jupiter.api.Test;
import util.HttpRequestParser;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class HttpParserTest {

    @Tes
    void parseQueryString_correct_input() {
        String queryString = "userId=john&password=1234&name=john park";

        Map<String, String> expected = Map.of(
                "userId", "john",
                "password", "1234",
                "name", "john park"
        );

        Map<String, String> result = HttpRequestParser.parseQueryString(queryString);

        assertEquals(expected, result);
    }

    @Test
    void parseQueryString_including_space() {
        String queryString = "userId =john & password =1234 &name=john park";

        Map<String, String> expected = Map.of(
                "userId", "john",
                "password", "1234",
                "name", "john park"
        );

        Map<String, String> result = HttpRequestParser.parseQueryString(queryString);

        assertEquals(expected, result);
    }

    @Test
    void parseQueryString_parameter_separator_duplication_sequence() {
        String queryString = "userId=john&password==1234&name=john park";

        Map<String, String> expected = Map.of(
                "userId", "john",
                "name", "john park"
        );

        Map<String, String> result = HttpRequestParser.parseQueryString(queryString);

        assertEquals(expected, result);
    }

    @Test
    void parseQueryString_parameter_separator_duplication_disorder() {
        String queryString = "userId=john&pas=sword=1234&name=john park";

        Map<String, String> expected = Map.of(
                "userId", "john",
                "name", "john park"
        );

        Map<String, String> result = HttpRequestParser.parseQueryString(queryString);

        assertEquals(expected, result);
    }

    @Test
    void parseQueryString_non_key() {
        String queryString = "userId=john&password=1234&=john park";

        Map<String, String> expected = Map.of(
                "userId", "john",
                "password", "1234"
        );

        Map<String, String> actual = HttpRequestParser.parseQueryString(queryString);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseQueryString_non_value() {
        String queryString = "userId=john&password=1234&name=";

        Map<String, String> expected = Map.of(
                "userId", "john",
                "password", "1234",
                "name", ""
        );

        Map<String, String> actual = HttpRequestParser.parseQueryString(queryString);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseUrl_correct_input() {
        // given
        String url = "/user/create?key=value";

        // when
        HttpRequestParser.Pair actual = HttpRequestParser.parseUrl(url);

        // then
        HttpRequestParser.Pair expected = new HttpRequestParser.Pair("/user/create", "key=value");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseUrl_separator_duplication() {
        // given
        String url = "key??value";

        // when
        HttpRequestParser.Pair actual = HttpRequestParser.parseUrl(url);

        // then
        HttpRequestParser.Pair expected = new HttpRequestParser.Pair("key??value", "");;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseUrl_including_space() {
        // given
        String url = "/user/crea te?ke y=val ue";

        // when
        HttpRequestParser.Pair actual = HttpRequestParser.parseUrl(url);

        // then
        HttpRequestParser.Pair expected = new HttpRequestParser.Pair("/user/crea te", "ke y=val ue");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseUrl_non_querystring() {
        // given
        String url = "/";

        // when
        HttpRequestParser.Pair actual = HttpRequestParser.parseUrl(url);

        // then
        HttpRequestParser.Pair expected = new HttpRequestParser.Pair("/", "");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseHeader_correct_input() {
        String header = "Host: www.example.com";

        HttpRequestParser.Pair actual = HttpRequestParser.parseHeader(header);

        HttpRequestParser.Pair expected = new HttpRequestParser.Pair("Host", "www.example.com");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseHeader_non_value() {
        String header = "Host";

        HttpRequestParser.Pair actual = HttpRequestParser.parseHeader(header);

        HttpRequestParser.Pair expected = new HttpRequestParser.Pair("Host", "");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseRequestLine_correct_input() {
        String requestLine = "GET / HTTP/1.1";

        String[] tokens = HttpRequestParser.parseRequestLine(requestLine);

        String[] expected = new String[]{"GET", "/", "HTTP/1.1"};

        assertThat(tokens).isEqualTo(expected);
    }

    @Test
    void parseRequestLine_incorrect_input() {
        String requestLine = "GET / ";

        assertThrows(IllegalStateException.class, () -> HttpRequestParser.parseRequestLine(requestLine));
    }
}