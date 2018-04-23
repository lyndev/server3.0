/**
 * @date 2014/7/17
 * @author ChenLong
 */
package servermonitor.config;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.StringUtils;
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
public class ServerConfig
{
    private final static Logger logger = Logger.getLogger(ServerConfig.class);
    private final static ServerConfig instance;
    private boolean loaded = false;

    private List<Integer> serverIds = new LinkedList<>(); // 服务器ID
    private MongoDBConfig mongodbConfig = null; // game-logger mongodb相关配置

    static
    {
        instance = new ServerConfig();
    }

    private ServerConfig()
    {
    }

    public static ServerConfig getInstance()
    {
        return instance;
    }

    public void load(String filePath) throws ParserConfigurationException, SAXException, IOException
    {
        if (loaded)
        {
            logger.warn("has loaded ServerConfig, [" + filePath + "]");
            return;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File xmlFile = new File(filePath);
        Document doc = builder.parse(xmlFile);
        Element configure = doc.getDocumentElement();
        NodeList children = configure.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i)
        {
            Node child = children.item(i);
            if (child instanceof Element)
            {
                String name = child.getNodeName();
                switch (name)
                {
                    case "game-port":
                    case "http-port":
                    case "platform-id":
                    case "max-connect":
                    case "heart-time":
                    case "db-game":
                    case "db-game-data":
                    case "fight-server":
                    case "robot-builder":
                        break;
                    case "server-id":
                        serverIds = getServerIdConf(child);
                        break;
                    case "game-logger":
                        mongodbConfig = getMongoDBConf(child);
                        break;
                    default:
                        logger.error("unknow node [" + name + "]");
                        break;
                }
            }
        }
        loaded = true;
    }

    private List<Integer> getServerIdConf(Node node)
    {
        List<Integer> ids = new LinkedList<>();
        String idsValue = StringUtils.EMPTY;
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j)
        {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            if (attName.equals("value"))
            {
                idsValue = attValue;
                break;
            }
        }
        String[] idsToken = idsValue.split(",");
        try
        {
            for (String idStr : idsToken)
            {
                int id = Integer.parseInt(idStr);
                ids.add(id);
            }
        }
        catch (NumberFormatException ex)
        {
            logger.error("server-config.xml server-id node error, cannot cast to integer");
            System.exit(1);
        }
        return ids;
    }

    private int getIntValueConf(Node node)
    {
        String intValue = null;
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j)
        {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            if (attName.equals("value"))
            {
                intValue = attValue;
                break;
            }
        }
        return Integer.parseInt(intValue);
    }

    private MongoDBConfig getMongoDBConf(Node node)
    {
        String gameId = null;
        String mongodbHost = null;
        int mongodbPort = 0;
        String mongodbName = null;
        String mongodbUser = null;
        String mongodbPasswd = null;

        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j)
        {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName)
            {
                case "game-id":
                    gameId = attValue;
                    break;
                case "mongodb-host":
                    mongodbHost = attValue;
                    break;
                case "mongodb-port":
                    mongodbPort = Integer.parseInt(attValue);
                    break;
                case "mongodb-name":
                    mongodbName = attValue;
                    break;
                case "mongodb-user":
                    mongodbUser = attValue;
                    break;
                case "mongodb-passwd":
                    mongodbPasswd = attValue;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
        if (gameId == null || gameId.isEmpty()
                || mongodbHost == null || mongodbHost.isEmpty()
                || mongodbPort == 0
                || mongodbName == null || mongodbName.isEmpty()
                || mongodbUser == null
                || mongodbPasswd == null)
            throw new ServerConfigException();
        return new MongoDBConfig(gameId, mongodbHost, mongodbPort, mongodbName, mongodbUser, mongodbPasswd);
    }

    public List<Integer> getServerIds()
    {
        checkLoaded();
        return serverIds;
    }

    public MongoDBConfig getMongoDBConfig()
    {
        checkLoaded();
        return mongodbConfig;
    }

    private void checkLoaded()
    {
        if (!loaded)
            throw new ServerConfigException();
    }
}
