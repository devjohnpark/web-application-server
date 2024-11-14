package org.doci.webserver;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;

public class ServerConfig {
    private static int port = 8080;
    private static String webBase = "webapp";

    // XML 파일을 읽어와서 port와 webBase를 설정
    public static void loadConfig(String filePath) throws Exception {
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

    public static int getPort() {
        return port;
    }

    public static String getWebBase() {
        return webBase;
    }
}
