/**
 * @date 2014/7/24
 * @author ChenLong
 */
package script.task.main;

import game.core.script.IScript;
import game.server.logic.struct.Player;
import game.server.logic.util.ScriptArgs;
import org.apache.log4j.Logger;

/**
 * 接收主线任务脚本
 *
 * @author ChenLong
 */
public class AcceptMainTaskScript implements IScript
{
    private final Logger logger = Logger.getLogger(AcceptMainTaskScript.class);

    @Override
    public int getId()
    {
        return 1016;
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

        addLogReceiveTask(player);

        return null;
    }

    private void addLogReceiveTask(Player player)
    {

    }
}
