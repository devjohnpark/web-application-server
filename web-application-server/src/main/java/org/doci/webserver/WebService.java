package org.doci.webserver;

import org.doci.http.api.AbstractHttpApiHandler;
import org.doci.http.api.DefaultHttpApiHandler;
import org.doci.webresource.WebResourceProvider;

import java.util.HashMap;
import java.util.Map;

public class WebService {
    private Map<String, AbstractHttpApiHandler> services = new HashMap<>();
    private String webResourcePath = "webapp";

    public void addService(String path, AbstractHttpApiHandler service) {
        services.put(path, service);
    }

    public void setServicesService(Map<String, AbstractHttpApiHandler> services) {
        this.services = services;
    }

    public void setWebResourcePath(String webResourcePath) {
        this.webResourcePath = webResourcePath;
    }

    public Map<String, AbstractHttpApiHandler> getServices() {
        WebResourceProvider webResourceProvider = new WebResourceProvider(webResourcePath);
        if (webResourcePath.equals("webapp")) {
            services.put("/", new DefaultHttpApiHandler());
        }
        for (AbstractHttpApiHandler service : services.values()) {
            service.init(webResourceProvider);
        }
        return services;
    }
}
