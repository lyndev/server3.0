/**
 * @date 2014/7/17
 * @author ChenLong
 */
package game.server.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
public class ServerConfig
{
    private final static Logger logger = Logger.getLogger(ServerConfig.class);
    private int gameUDPPort = 0; // udp端口
    private int gamePort = 0; // 监听端口
    private int httpPort = 0; // http监听端口
    private int gmPort = 0; // GM指令监听端口
    private int maxConnect = 0; // 最大连接数
    private int heartTime = 0; // 心跳时间（单位：秒，大于0生效）
    private int platformId = 0; // 平台ID
    private int serverId = 0; // 服务器ID
    private int robotCount; // 启动时生成机器人数量
    private long openTime = 0; // 开服时间
    private String loginKey = new String();
    private DataBaseConfig gameDBConfig = null; // game数据库配置
    private DataBaseConfig gameDataDBConfig = null; // game_data数据库配置
    private FightServerConfig fightServerConfig = null; // fight server配置
    private MongoDBConfig mongodbConfig = null; // game-logger mongodb相关配置
    private int gameServerType = 1; // 服务器的类型，默认是cocos2d 服务器
    private Map<String, Integer> gameServerTypes = null;

    private boolean loaded = false;
    private final static ServerConfig instance;

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
                    case "game-server-type":
                        gameServerType = getIntValueConf(child);
                        break;
                    //case "game-server-types":
                        //gameServerTypes = getGameServerTypesConf(child);
                        //break;
                    case "game-udp-port":
                        gameUDPPort = getIntValueConf(child);
                        break;
                    case "game-port":
                        gamePort = getIntValueConf(child);
                        break;
                    case "http-port":
                        httpPort = getIntValueConf(child);
                        break;
                    case "gm-port":
                        gmPort = getIntValueConf(child);
                        break;
                    case "platform-id":
                        platformId = getIntValueConf(child);
                        break;
                    case "server-id":
                        serverId = getIntValueConf(child);
                        break;
                    case "max-connect":
                        maxConnect = getIntValueConf(child);
                        break;
                    case "heart-time":
                        heartTime = getIntValueConf(child);
                        break;
                    case "db-game":
                        gameDBConfig = getDBConf(child);
                        break;
                    case "db-game-data":
                        gameDataDBConfig = getDBConf(child);
                        break;
                    case "fight-server":
                        fightServerConfig = getFightServerConf(child);
                        break;
                    case "robot-count":
                        robotCount = getIntValueConf(child);
                        break;
                    case "game-logger":
                        mongodbConfig = getMongoDBConf(child);
                        break;
                    case "open-time":
                        openTime = getLongValueConf(child);
                        break;
                    case "login-key":
                        loginKey = getStringValueConf(child);
                        break;
                    default:
                        logger.error("unknow node [" + name + "]");
                        break;
                }
            }
        }
        loaded = true;
    }

    private long getLongValueConf(Node node)
    {
        String longVal = null;
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j)
        {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            if (attName.equals("value"))
            {
                longVal = attValue;
                break;
            }
        }
        return Long.parseLong(longVal);
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

    private String getStringValueConf(Node node)
    {
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j)
        {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            if (attName.equals("value"))
            {
                return attValue;
            }
        }
        return null;
    }

    private Map<String, Integer> getGameServerTypesConf(Node node)
    {
        String url = null, username = null, password = null;
        NamedNodeMap attributes = node.getAttributes();
        Map<String, Integer> _tempMap = new  HashMap<>();
        for (int j = 0; j < attributes.getLength(); ++j)
        {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            _tempMap.put(attName, Integer.parseInt(attValue));
        }
        return _tempMap;
    }

    private DataBaseConfig getDBConf(Node node)
    {
        String url = null, username = null, password = null;
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j)
        {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName)
            {
                case "url":
                    url = attValue;
                    break;
                case "username":
                    username = attValue;
                    break;
                case "password":
                    password = attValue;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
        if (url == null || url.isEmpty() || username == null || username.isEmpty() || password == null || password.isEmpty())
            throw new ServerConfigException();
        return new DataBaseConfig(url, username, password);
    }

    private FightServerConfig getFightServerConf(Node node)
    {
        String ip = null, port = null;
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j)
        {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName)
            {
                case "ip":
                    ip = attValue;
                    break;
                case "port":
                    port = attValue;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
        if (ip == null || ip.isEmpty() || port == null || port.isEmpty())
            throw new ServerConfigException();
        return new FightServerConfig(ip, Integer.parseInt(port));
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

    public int getServerType()
    {
        checkLoaded();
        return this.gameServerType;
    }
            
    public int getGamePort()
    {
        checkLoaded();
        return gamePort;
    }

    public int getGameUDPPort()
    {
        checkLoaded();
        return gameUDPPort;
    }

    public int getHttpPort()
    {
        checkLoaded();
        return httpPort;
    }

    public int getGmPort()
    {
        checkLoaded();
        return gmPort;
    }

    public int getMaxConnect()
    {
        checkLoaded();
        return maxConnect;
    }

    public int getHeartTime()
    {
        checkLoaded();
        return heartTime;
    }

    public int getPlatformId()
    {
        checkLoaded();
        return platformId;
    }

    public int getServerId()
    {
        checkLoaded();
        return serverId;
    }

    public int getRobotCount()
    {
        checkLoaded();
        return robotCount;
    }

    public DataBaseConfig getGameDBConfig()
    {
        checkLoaded();
        return gameDBConfig;
    }

    public DataBaseConfig getGameDataDBConfig()
    {
        checkLoaded();
        return gameDataDBConfig;
    }

    public FightServerConfig getFightServerConfig()
    {
        checkLoaded();
        return fightServerConfig;
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

    public long getOpenTime()
    {
        return openTime;
    }

    public String getLoginKey()
    {
        return loginKey;
    }

}
