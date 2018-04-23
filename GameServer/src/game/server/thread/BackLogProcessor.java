/**
 * @date 2014/7/23
 * @author ChenLong
 */
package game.server.thread;

import com.alibaba.fastjson.JSONObject;
import com.haowan.logger.service.ServicesFactory;
import game.core.command.CommandProcessor;
import game.core.command.ICommand;
import game.server.GameServer;
import game.server.logic.constant.GameServerState;
import java.util.Map;
import org.apache.log4j.Logger;
import org.json.JSONException;

/**
 * 后台日志线程
 *
 * @author ChenLong
 */
public class BackLogProcessor extends CommandProcessor
{
    private final static Logger logger = Logger.getLogger(BackLogProcessor.class);
    private final static BackLogProcessor instance;

    static
    {
        instance = new BackLogProcessor();
    }

    private BackLogProcessor()
    {
        super(BackLogProcessor.class.getSimpleName());
    }

    public static BackLogProcessor getInstance()
    {
        return instance;
    }

    /**
     * 记录后台日志
     *
     * @param logJson json参数串
     */
    public void addLog(String logJson)
    {
        addCommand(new ICommand()
        {
            private String logJson;

            public ICommand set(String jsonStr)
            {
                this.logJson = jsonStr;
                return this;
            }

            @Override
            public void action()
            {
                try
                {
                    ServicesFactory.getSingleLogService().addCommonLog1(logJson);
                }
                catch (JSONException ex)
                {
                    logger.error("JSONException", ex);
                }
            }
        }.set(logJson));
    }

    /**
     * 记录后台日志
     *
     * @param logMap 参数map
     */
    public void addLog(Map<String, String> logMap)
    {
        addCommand(new ICommand()
        {
            private Map<String, String> logMap;

            public ICommand set(Map<String, String> logMap)
            {
                this.logMap = logMap;
                return this;
            }

            @Override
            public void action()
            {
                try
                {
                    JSONObject jsonObj = new JSONObject();
                    for (Map.Entry<String, String> entry : logMap.entrySet())
                    {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        jsonObj.put(key, value);
                    }
                    ServicesFactory.getSingleLogService().addCommonLog1(jsonObj.toJSONString());
                }
                catch (JSONException ex)
                {
                    logger.error("JSONException", ex);
                }
            }
        }.set(logMap));
    }

    @Override
    public void addCommand(ICommand command)
    {
        if (GameServer.getInstance().getGameServerState().getValue() >= GameServerState.STARTED.getValue())
            super.addCommand(command);
    }

    @Override
    public void doCommand(ICommand command)
    {
        command.action();
    }

    @Override
    public void writeError(String message)
    {
        logger.error(message);
    }

    @Override
    public void writeError(String message, Throwable t)
    {
        logger.error(message, t);
    }
}
