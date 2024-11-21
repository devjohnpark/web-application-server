package org.doci.webserver;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;

public class ServerConfig {
    private static final String SERVER_CONFIG_BASE = "/server.xml";

    private int port = 8080;
    private String webBase = "webapp";

    public ServerConfig(String filePath) throws Exception {
        loadServerConfig(filePath);
    }

    private void loadServerConfig(String filePath) throws ParserConfigurationException, SAXException, IOException {
        File configFile = new File(filePath + SERVER_CONFIG_BASE);

        // XML 파일을 파싱하기 위한 DocumentBuilder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(configFile);

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
