/**
 * @date 2014/7/29
 * @author ChenLong
 */
package game.fixdata;

import game.core.script.ScriptManager;
import game.data.GameDataManager;
import game.data.bean.q_scriptBean;
import game.server.config.ServerConfig;
import game.server.db.DBFactory;
import game.server.logic.util.ScriptArgs;
import game.server.util.MiscUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.script.ScriptException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.xml.sax.SAXException;

/**
 * 数据修复工具入口, 运行方式 java -cp GameServer.jar game.fixdata.Main
 *
 * @author ChenLong
 */
public class Main
{
    private final static Logger logger = Logger.getLogger(Main.class);

    /**
     * 加载log4j配置信息.
     */
    private static void loadLog4JConfig()
    {
        StringBuilder pathBuilder = new StringBuilder(System.getProperty("user.dir") + File.separator + "config" + File.separator);
        if (MiscUtils.isIDEEnvironment())
            pathBuilder.append("log4j_devel.xml");
        else
            pathBuilder.append("log4j_server.xml");
        DOMConfigurator.configureAndWatch(pathBuilder.toString());
    }

    /**
     * 加载ServerConfig配置信息
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private static void loadServerConfig() throws ParserConfigurationException, SAXException, IOException
    {
        logger.info("load server-config.xml");
        String filePath = SystemUtils.USER_DIR + File.separator + "config" + File.separator + "server-config.xml";
        ServerConfig.getInstance().load(filePath);
    }

    /**
     * 加载GameData
     */
    private static void loadGameData()
    {
        logger.info("load kapai_game_data");
        GameDataManager.getInstance().setSqlSessionFactory(DBFactory.GAME_DATA_DB.getSessionFactory()).loadAll(); // 加载配置数据
    }

    /**
     * 初始化脚本系统
     */
    private static void initScriptManager()
    {
        logger.info("initialize ScriptManager");
        Map<Integer, String> scriptNames = new HashMap<>();
        for (Map.Entry entry : GameDataManager.getInstance().q_scriptContainer.getMap().entrySet())
        {
            int scriptId = (Integer) entry.getKey();
            String scriptName = ((q_scriptBean) entry.getValue()).getQ_script_name();

            scriptNames.put(scriptId, scriptName);
        }

        try
        {
            ScriptManager.getInstance().initialize("scripts/java", "./scriptBin", scriptNames, "scripts/js");
        }
        catch (FileNotFoundException | ScriptException ex)
        {
            logger.error("FileNotFoundException | ScriptException", ex);
        }
    }

    /**
     * 调用FixDataScript
     *
     * @param args
     */
    private static void callFixDataScript(String[] args)
    {
        logger.info("call FixDataScript");
        ScriptArgs params = new ScriptArgs();
        params.put(ScriptArgs.Key.ARG1, args);

        ScriptManager.getInstance().call(9000, params);
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
    {
        System.out.println("game.fixdata.Main tools");
        loadLog4JConfig();
        loadServerConfig();
        loadGameData();
        initScriptManager();
        callFixDataScript(args);
    }
}
