/**
 * @date 2014/8/16
 * @author ChenLong
 */
package game.analyse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.server.config.ServerConfig;
import game.server.db.game.bean.RoleBean;
import game.server.db.game.dao.RoleDao;
import game.server.util.MiscUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.xml.sax.SAXException;

/**
 *
 * @author Administrator
 */
public class DataAnalyzer
{
    private Logger logger = Logger.getLogger(DataAnalyzer.class);

    public void loadConfig()
    {
        try
        {
            loadLog4JConfig(); // 加载log4j配置信息
            loadServerConfig(); // 加载ServerConfig配置
        }
        catch (Exception ex)
        {
            logger.error("load config failure", ex);
            System.exit(1);
        }
    }

    public void analyse()
    {
        roleLevelAnalyse1();
    }

    /**
     * 全部玩家等级分布
     */
    private void roleLevelAnalyse()
    {
        Map<Integer, Integer> roleLevelMap = new HashMap<>();
        List<RoleBean> beans = RoleDao.selectAll();
        for (RoleBean bean : beans)
        {
            if (bean.getIsRobot() != 1)
            {
                int roleLevel = bean.getRoleLevel();
                Integer rl = roleLevelMap.get(roleLevel);
                if (rl != null)
                {
                    rl += 1;
                }
                else
                {
                    rl = 1;
                }
                roleLevelMap.put(roleLevel, rl);
            }
        }

        int sum = 0;
        for (Map.Entry<Integer, Integer> entry : roleLevelMap.entrySet())
        {
            int roleLevel = entry.getKey();
            int num = entry.getValue();
            sum += num;
            System.out.println(roleLevel + "\t" + num);
        }
        System.out.println("总数: " + sum);
    }

    /**
     * 等级分布, 角色创建时间大于2014-8-16 0:0:0, 1408118400000
     */
    private void roleLevelAnalyse1()
    {
        Map<Integer, Integer> roleLevelMap = new HashMap<>();
        List<RoleBean> beans = RoleDao.selectAll();
        for (RoleBean bean : beans)
        {
            if (bean.getIsRobot() != 1)
            {
                JSONObject json = JSON.parseObject(bean.getMiscData());
                long createRoleTime = json.getLong("createRoleTime");
                if (createRoleTime >= 1408118400000L)
                {
                    int roleLevel = bean.getRoleLevel();
                    Integer rl = roleLevelMap.get(roleLevel);
                    if (rl != null)
                    {
                        rl += 1;
                    }
                    else
                    {
                        rl = 1;
                    }
                    roleLevelMap.put(roleLevel, rl);
                }
            }
        }

        int sum = 0;
        for (Map.Entry<Integer, Integer> entry : roleLevelMap.entrySet())
        {
            int roleLevel = entry.getKey();
            int num = entry.getValue();
            sum += num;
            System.out.println(roleLevel + "\t" + num);
        }
        System.out.println("总数: " + sum);
    }

    private void loadLog4JConfig()
    {
        StringBuilder pathBuilder = new StringBuilder(System.getProperty("user.dir") + File.separator + "config" + File.separator);
        if (MiscUtils.isIDEEnvironment())
            pathBuilder.append("log4j_devel.xml");
        else
            pathBuilder.append("log4j_server.xml");
        DOMConfigurator.configureAndWatch(pathBuilder.toString());
    }

    private void loadServerConfig() throws ParserConfigurationException, SAXException, IOException
    {
        String filePath = SystemUtils.USER_DIR + File.separator + "config" + File.separator + "server-config.xml";
        ServerConfig.getInstance().load(filePath);
    }
}
