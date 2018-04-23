package script.mission;

import com.alibaba.fastjson.JSONObject;
import game.core.script.IScript;
import game.server.logic.util.ScriptArgs;
import game.server.thread.BackLogProcessor;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class MissionFailScript implements IScript
{
    private final Logger logger = Logger.getLogger(MissionFailScript.class);

    @Override
    public int getId()
    {
        return 1022;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        ScriptArgs args = (ScriptArgs) arg;
        JSONObject json = (JSONObject)args.get(ScriptArgs.Key.OBJECT_VALUE);

        logger.info("call java script " + scriptId);

        if (json == null)
        {
            logger.error("json == null");
            return null;
        }

        addMissionFail(json);

        return null;
    }

    /**
     * 记录日志
     *
     * @param json
     */
    private void addMissionFail(JSONObject json)
    {

    }
}
