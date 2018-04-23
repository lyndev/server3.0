/**
 * @date 2014/4/25
 * @author ChenLong
 */
package game.server.logic.line.handler;

import game.core.command.Handler;
import game.server.logic.line.GameLine;
import org.apache.commons.lang.Validate;

/**
 *
 * @author ChenLong
 */
public class GameLineTickHandler extends Handler
{
    private final GameLine gameLine;

    public GameLineTickHandler(GameLine gameLine)
    {
        Validate.notNull(gameLine);
        this.gameLine = gameLine;
    }

    @Override
    public void action()
    {
        gameLine.tick();
    }
}
