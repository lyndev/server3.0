/**
 * @date 2014/5/19
 * @author ChenLong
 */

package game.server.thread.dboperator.handler;

import game.core.command.ICommand;

/**
 *
 * @author ChenLong
 */
public abstract class DBOperatorHandler implements ICommand
{
    private final int lineId; // 从哪个线来的请求，结果返回到哪个线
    
    public DBOperatorHandler(int lineId)
    {
        this.lineId = lineId;
    }

    public int getLineId()
    {
        return lineId;
    }
}
