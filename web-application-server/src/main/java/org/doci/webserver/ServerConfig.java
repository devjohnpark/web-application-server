package org.doci.webserver;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;

public class ServerConfig {
    private int port = 8080;
    private String webBase = "webapp";

    // XML 파일을 읽어와서 port와 webBase를 설정
    public void init(String filePath) throws Exception {
        File configFile = new File(filePath);

        // XML 파일을 파싱하기 위한 DocumentBuilder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(configFile);

        // XML에서 port와 webBase 값을 추출
        NodeList portNodes = document.getElementsByTagName("port");
        port = Integer.parseInt(portNodes.item(0).getTextContent());

        NodeList webBaseNodes = document.getElementsByTagName("webBase");
        webBase = webBaseNodes.item(0).getTextContent();
    }

    public int getPort() {
        return port;
    }

    public String getWebBase() {
        return webBase;
    }
}
