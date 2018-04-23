/**
 * @date 2014/11/6
 * @author ChenLong
 */
package game.server.logic.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.server.db.game.bean.GlobalBean;
import game.server.db.game.dao.GlobalDao;
import game.server.logic.constant.GlobalTableKey;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 客户端版本
 *
 * @author ChenLong
 */
public class ClientVersion
{
    private static final Logger logger = Logger.getLogger(ClientVersion.class);
    private static final ClientVersion instance;
    private volatile String clientVer = StringUtils.EMPTY;

    static
    {
        instance = new ClientVersion();
    }

    public static ClientVersion getInstance()
    {
        return instance;
    }

    public synchronized void load()
    {
        GlobalBean bean = GlobalDao.select(GlobalTableKey.CLIENT_VERSION.getKey());
        try
        {
            if (bean != null && bean.getValue() != null && !bean.getValue().trim().isEmpty())
            {
                JSONObject jsonObj = new JSONObject(JSON.parseObject(bean.getValue().trim()));
                if (jsonObj.containsKey("clientVer"))
                {
                    String aClientVer = jsonObj.getString("clientVer");
                    clientVer = aClientVer;
                    logger.info("clientVer: [" + clientVer + "]");
                }
                else
                {
                    logger.error("!jsonObj.containsKey(\"clientVer\")");
                }
            }
        }
        catch (Exception ex)
        {
            logger.error("load exception", ex);
        }
    }

    public synchronized boolean matchingVersion(String aClientVer)
    {
        boolean result = true;
        if (!clientVer.isEmpty())
            result = clientVer.compareToIgnoreCase(aClientVer) <= 0;
        return result;
    }

    public String getClientVer()
    {
        return clientVer;
    }
}
