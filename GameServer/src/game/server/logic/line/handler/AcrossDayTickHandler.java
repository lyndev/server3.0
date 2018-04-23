/**
 * @date 2014/5/29
 * @author ChenLong
 */
package game.server.logic.line.handler;

import game.core.command.Handler;
import game.server.logic.line.GameLine;
import org.apache.commons.lang.Validate;

/**
 * 跨天
 *
 * @author ChenLong
 */
public class AcrossDayTickHandler extends Handler
{
    private final GameLine gameLine;

    public AcrossDayTickHandler(GameLine gameLine)
    {
        Validate.notNull(gameLine);
        this.gameLine = gameLine;
    }

    @Override
    public void action()
    {
        gameLine.acrossDayTick();
    }
}
