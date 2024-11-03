package org.doci.http.util;

import org.junit.jupiter.api.Test;

import java.util.Map;

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
        HttpParser.Pair actual = HttpParser.parseUrl(url);

        // then
        HttpParser.Pair expected = new HttpParser.Pair("/user/create", "key=value");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseUrl_separator_duplication() {
        // given
        String url = "key??value";

        // when
        HttpParser.Pair actual = HttpParser.parseUrl(url);

        // then
        HttpParser.Pair expected = new HttpParser.Pair("key??value", "");;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseUrl_including_space() {
        // given
        String url = "/user/crea te?ke y=val ue";

        // when
        HttpParser.Pair actual = HttpParser.parseUrl(url);

        // then
        HttpParser.Pair expected = new HttpParser.Pair("/user/crea te", "ke y=val ue");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseUrl_non_querystring() {
        // given
        String url = "/";

        // when
        HttpParser.Pair actual = HttpParser.parseUrl(url);

        // then
        HttpParser.Pair expected = new HttpParser.Pair("/", "");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseHeader_correct_input() {
        String header = "Host: www.example.com";

        HttpParser.Pair actual = HttpParser.parseHeader(header);

        HttpParser.Pair expected = new HttpParser.Pair("Host", "www.example.com");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseHeader_non_value() {
        String header = "Host";

        HttpParser.Pair actual = HttpParser.parseHeader(header);

        HttpParser.Pair expected = new HttpParser.Pair("Host", "");

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
    void parseHeaderValues() {
        String headerValues = "text/html; charset=utf-8";
        String[] tokens = HttpParser.parseHeaderValues(headerValues);
        assertThat(tokens[0]).isEqualTo("text/html");
        assertThat(tokens[1]).isEqualTo("charset=utf-8");
    }

    @Test
    void parseRequestLine_incorrect_input() {
        String requestLine = "GET / ";

        assertThrows(IllegalStateException.class, () -> HttpParser.parseRequestLine(requestLine));
    }
}