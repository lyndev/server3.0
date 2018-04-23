/**
 * @date 2014/7/24
 * @author ChenLong
 */
package script.task.daily;

import game.core.script.IScript;
import game.server.logic.struct.Player;
import game.server.logic.util.ScriptArgs;
import org.apache.log4j.Logger;

/**
 * 接收日常任务脚本
 *
 * @author ChenLong
 */
public class AcceptDailyTaskScript implements IScript
{
    private final Logger logger = Logger.getLogger(AcceptDailyTaskScript.class);

    @Override
    public int getId()
    {
        return 1017;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        ScriptArgs args = (ScriptArgs) arg;
        Player player = (Player) args.get(ScriptArgs.Key.ARG1);

        logger.info("call java script " + scriptId);

        if (player == null)
        {
            logger.error("player == null");
            return null;
        }

        return null;
    }

    private void addLogReceiveTask(Player player)
    {

    }
}
