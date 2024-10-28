package com.hello_webserver.http.request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.hello_webserver.http.util.HttpParser;
import com.hello_webserver.webresources.ResourceType;

public class RequestParameters {
    private Map<String, String> parameters = new HashMap<String, String>();

    private void addParameters(String queryString) {
        if (queryString == null || queryString.isEmpty()) { return; }
        parameters.putAll(HttpParser.parseQueryString(queryString));
    }

    public void addRequestLineParameters(String queryString) {
        addParameters(URLDecoder.decode(queryString, StandardCharsets.UTF_8));
    }

    public void addBodyParameters(String queryString) {
        addParameters(queryString);
    }

    public String getParameter(String key) { return parameters.get(key); }
}
