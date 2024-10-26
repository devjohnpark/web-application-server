package com.hello_webserver.util;

import org.junit.jupiter.api.Test;
import com.hello_webserver.http.util.HttpParser;

import java.util.Map;
import com.hello_webserver.http.util.HttpParser.Pair;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class HttpParserTest {
    void parseQueryString_correct_input() {
        String queryString = "userId=john&password=1234&name=john park";

        Map<String, String> expected = Map.of(
                "userId", "john",
                "password", "1234",
                "name", "john park"
        );

        Map<String, String> result = HttpParser.parseQueryString(queryString);

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

        Map<String, String> result = HttpParser.parseQueryString(queryString);

        assertEquals(expected, result);
    }

    @Test
    void parseQueryString_parameter_separator_duplication_sequence() {
        String queryString = "userId=john&password==1234&name=john park";

        Map<String, String> expected = Map.of(
                "userId", "john",
                "name", "john park"
        );

        Map<String, String> result = HttpParser.parseQueryString(queryString);

        assertEquals(expected, result);
    }

    @Test
    void parseQueryString_parameter_separator_duplication_disorder() {
        String queryString = "userId=john&pas=sword=1234&name=john park";

        Map<String, String> expected = Map.of(
                "userId", "john",
                "name", "john park"
        );

        Map<String, String> result = HttpParser.parseQueryString(queryString);

        assertEquals(expected, result);
    }

    @Test
    void parseQueryString_non_key() {
        String queryString = "userId=john&password=1234&=john park";

        Map<String, String> expected = Map.of(
                "userId", "john",
                "password", "1234"
        );

        Map<String, String> actual = HttpParser.parseQueryString(queryString);

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

        Map<String, String> actual = HttpParser.parseQueryString(queryString);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseUrl_correct_input() {
        // given
        String url = "/user/create?key=value";

        // when
        Pair actual = HttpParser.parseUrl(url);

        // then
        Pair expected = new Pair("/user/create", "key=value");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseUrl_separator_duplication() {
        // given
        String url = "key??value";

        // when
        Pair actual = HttpParser.parseUrl(url);

        // then
        Pair expected = new Pair("key??value", "");;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseUrl_including_space() {
        // given
        String url = "/user/crea te?ke y=val ue";

        // when
        Pair actual = HttpParser.parseUrl(url);

        // then
        Pair expected = new Pair("/user/crea te", "ke y=val ue");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseUrl_non_querystring() {
        // given
        String url = "/";

        // when
        Pair actual = HttpParser.parseUrl(url);

        // then
        Pair expected = new Pair("/", "");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseHeader_correct_input() {
        String header = "Host: www.example.com";

        Pair actual = HttpParser.parseHeader(header);

        Pair expected = new Pair("Host", "www.example.com");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseHeader_non_value() {
        String header = "Host";

        Pair actual = HttpParser.parseHeader(header);

        Pair expected = new Pair("Host", "");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseRequestLine_correct_input() {
        String requestLine = "GET / HTTP/1.1";

        String[] tokens = HttpParser.parseRequestLine(requestLine);

        String[] expected = new String[]{"GET", "/", "HTTP/1.1"};

        assertThat(tokens).isEqualTo(expected);
    }

    @Test
    void parseRequestLine_incorrect_input() {
        String requestLine = "GET / ";

        assertThrows(IllegalStateException.class, () -> HttpParser.parseRequestLine(requestLine));
    }
}