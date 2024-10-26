package com.hello_webserver.http.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.hello_webserver.http.util.HttpParser;
import com.hello_webserver.http.util.HttpParser.Pair;
import com.hello_webserver.webresources.ResourceType;

public class RequestParameters {
    private Map<String, String> parameters = new HashMap<String, String>();

    private void addParameters(String queryString) {
        if (queryString == null || queryString.isEmpty()) { return; }
        parameters.putAll(HttpParser.parseQueryString(queryString));
    }

    public void addRequestLineParameters(String queryString) {
        addParameters(queryString);
    }

    public void addBodyParameters(String queryString, String contentType) {
        if (ResourceType.URL.getContentType().equals(contentType)) {
            addParameters(queryString);
        }
    }

    public String getParameter(String key) { return parameters.get(key); }
}
