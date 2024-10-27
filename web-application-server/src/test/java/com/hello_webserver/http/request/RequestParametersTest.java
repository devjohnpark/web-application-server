package com.hello_webserver.http.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
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
    void addBodyRequestParameters() {
        String queryString = "name=john park&age=20";
        parameters.addRequestLineParameters(queryString);
        assertThat(parameters.getParameter("name")).isEqualTo("john park");
        assertThat(parameters.getParameter("age")).isEqualTo("20");
    }

}