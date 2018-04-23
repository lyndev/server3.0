/**
 * @date 2014/6/26
 * @author ChenLong
 */
package servermonitor;

import com.haowan.logger.service.ServicesFactory;
import game.core.timer.SimpleTimerProcessor;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.xml.sax.SAXException;
import servermonitor.config.MongoDBConfig;
import servermonitor.config.ServerConfig;
import servermonitor.handler.ServerCPUMonitorTick;
import servermonitor.handler.ServerMemMonitorTick;
import servermonitor.util.MiscUtils;

/**
 *
 * @author ChenLong
 */
public class ServerMonitor
{
    private final static Logger logger = Logger.getLogger(ServerMonitor.class);

    /**
     * 加载log4j配置信息.
     */
    private static void loadLog4JConfig()
    {
        String filePath = System.getProperty("user.dir") + File.separator + "config" + File.separator;
        if (MiscUtils.isIDEEnvironment())
            filePath += "log4j_devel.xml";
        else
            filePath += "log4j_server.xml";
        DOMConfigurator.configureAndWatch(filePath);
    }

    /**
     * 加载server-config.xml配置
     */
    private static void loadLogDBFactoryConfig() throws ParserConfigurationException, SAXException, IOException
    {
        String filePath = SystemUtils.USER_DIR + File.separator + "config" + File.separator + "server-config.xml";
        ServerConfig.getInstance().load(filePath);
    }

    private static void loadConfig()
    {
        try
        {
            loadLog4JConfig();
            loadLogDBFactoryConfig();
        }
        catch (ParserConfigurationException | SAXException | IOException ex)
        {
            logger.error("load config failure", ex);
            System.exit(1);
        }
    }

    /**
     * 初始化GameLogger
     */
    private static void initGameLogger()
    {
        MongoDBConfig mongoDBConfig = ServerConfig.getInstance().getMongoDBConfig();
        ServicesFactory.getSingleLogService().initGameLogger(
                mongoDBConfig.getGameId(),
                mongoDBConfig.getMongodbHost(),
                Integer.toString(mongoDBConfig.getMongodbPort()),
                mongoDBConfig.getMongodbName(),
                mongoDBConfig.getMongodbUser(),
                mongoDBConfig.getMongodbPasswd());
    }

    /**
     * 初始化服务器监控定时器
     */
    private static void initializeServerMonitorTick()
    {
        int second = 5 * 60; // 后台5分钟监控一次
        //int second = 1; // test
        SimpleTimerProcessor.getInstance().addEvent(new ServerMemMonitorTick(second));
        SimpleTimerProcessor.getInstance().addEvent(new ServerCPUMonitorTick(second));
    }

    public static void main(String[] args)
    {
        loadConfig();
        initGameLogger();
        initializeServerMonitorTick();
    }
}
