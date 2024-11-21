package org.doci.webserver;

import org.doci.http.api.AbstractHttpApiHandler;
import org.doci.webresource.WebResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

// Connector가 WebServiceConfig 인스턴스로 HttpApiMapper를 생성해서 RequestHandler에게 반환
public class WebServiceConfig {
    private static final String WEB_SERVICE_BASE = "/webservice.xml";
    private static final Logger log = LoggerFactory.getLogger(WebServiceConfig.class);
    private final RequestMapper requestMapper;

    public WebServiceConfig(String resourceBase, ServerConfig serverConfig) throws Exception {
        this.requestMapper = new RequestMapper(loadWebServices(serverConfig.getWebBase(), resourceBase + WEB_SERVICE_BASE));
    }

    private Map<String, AbstractHttpApiHandler> loadWebServices(String webResourcePath, String webServicePath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // XML 파일 파싱 설정
        File webServiceFile = new File(webServicePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(webServiceFile);

        // XML에서 'service' 엘리먼트 가져오기
        NodeList serviceNodes = document.getElementsByTagName("service");
        if (serviceNodes.getLength() == 0) {
            throw new IllegalArgumentException("No handler elements found in XML.");
        }

        Map<String, AbstractHttpApiHandler> handlerMap = new HashMap<>();
        WebResourceProvider webResourceProvider = WebResourceProvider.getInstance(webResourcePath);

        // 각 service 태그에서 class와 path 속성 값을 가져오기
        for (int i = 0; i < serviceNodes.getLength(); i++) {
            Element handlerElement = (Element) serviceNodes.item(i);
            String className = handlerElement.getAttribute("class");
            String path = handlerElement.getAttribute("path");

            if (className.isEmpty() || path.isEmpty()) {
                throw new IllegalArgumentException("service element must have 'class' and 'path' attributes.");
            }

            // 리플렉션을 사용하여 클래스 동적 로딩
            Class<?> handlerClass = Class.forName(className);

            // 생성자에 의존성을 전달하여 인스턴스 생성
            AbstractHttpApiHandler handlerInstance = (AbstractHttpApiHandler)
                    handlerClass.getDeclaredConstructor(webResourceProvider.getClass())
                            .newInstance(webResourceProvider);

            handlerMap.put(path, handlerInstance);
        }

        return handlerMap;
    }

    public RequestMapper getRequestMapper() {
        return requestMapper;
    }
}


