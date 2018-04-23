/**
 * @date 2014/7/17
 * @author ChenLong
 */
package chenlong.stopserver.config;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * ServerConfig配置类
 */
public class ServerConfig {

    private final static Logger logger = Logger.getLogger(ServerConfig.class);

    private int gamePort = 0; // 监听端口
    private int httpPort = 0; // http监听端口
    private int gmPort = 0; // GM指令监听端口

    private boolean loaded = false;
    private final static ServerConfig instance;

    static {
        instance = new ServerConfig();
    }

    private ServerConfig() {
    }

    public static ServerConfig getInstance() {
        return instance;
    }

    public void load(String filePath) throws ParserConfigurationException, SAXException, IOException {
        if (loaded) {
            logger.warn("has loaded ServerConfig, [" + filePath + "]");
            return;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File xmlFile = new File(filePath);
        Document doc = builder.parse(xmlFile);
        Element configure = doc.getDocumentElement();
        NodeList children = configure.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child instanceof Element) {
                String name = child.getNodeName();
                switch (name) {
                    case "game-port":
                        gamePort = getIntValueConf(child);
                        break;
                    case "http-port":
                        httpPort = getIntValueConf(child);
                        break;
                    case "gm-port":
                        gmPort = getIntValueConf(child);
                        break;
                    default:
                        break;
                }
            }
        }
        loaded = true;
    }

    private int getIntValueConf(Node node) {
        String intValue = null;
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            if (attName.equals("value")) {
                intValue = attValue;
                break;
            }
        }
        return Integer.parseInt(intValue);
    }

    public int getGamePort() {
        checkLoaded();
        return gamePort;
    }

    public int getHttpPort() {
        checkLoaded();
        return httpPort;
    }

    public int getGmPort() {
        checkLoaded();
        return gmPort;
    }

    private void checkLoaded() {
        if (!loaded) {
            throw new RuntimeException();
        }
    }
}
