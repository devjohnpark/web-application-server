package org.doci.http.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class RequestParametersTest {

    private RequestParameters parameters;

    @BeforeEach
    void setUp() {
        parameters = new RequestParameters();
    }

    @Test
    void addRequestLineParameter() {
        String queryStringWithUrlEncoded = "name=john%20park&age=20";
        parameters.addRequestLineParameters(queryStringWithUrlEncoded);
        assertThat(parameters.getParameter("name")).isEqualTo("john park");
        assertThat(parameters.getParameter("age")).isEqualTo("20");
    }

    @Test
    void addRequestLineParameter_null() {
        parameters.addRequestLineParameters(null);
        assertThat(parameters.getParameter("name")).isNull();
        assertThat(parameters.getParameter("age")).isNull();
    }

    @Test
    void addRequestLineParameter_empty() {
        parameters.addRequestLineParameters("");
        assertThat(parameters.getParameter("")).isNull();
        assertThat(parameters.getParameter("")).isNull();
    }

    @Test
    void addBodyRequestParameters() {
        String queryString = "name=john%20park&age=20";
        parameters.addBodyParameters(queryString);
        assertThat(parameters.getParameter("name")).isEqualTo("john park");
        assertThat(parameters.getParameter("age")).isEqualTo("20");
    }

    @Test
    void addBodyRequestParameters_null() {
        parameters.addBodyParameters(null);
        assertThat(parameters.getParameter("name")).isNull();
        assertThat(parameters.getParameter("age")).isNull();
    }

    @Test
    void addBodyRequestParameters_empty() {
        parameters.addBodyParameters("");
        assertThat(parameters.getParameter("")).isNull();
        assertThat(parameters.getParameter("")).isNull();
    }
}