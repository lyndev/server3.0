package game.core.handler;

import game.core.command.Handler;
import game.core.message.Player;

/**
 * 测试Handler.
 *
 * @author wangJingWei
 */
public class PlayerHandler extends Handler
{

    @Override
    public void action()
    {
        try
        {
            Player.Person person = (Player.Person) getMessage().getData();
            System.out.println("处理消息:\n" + person);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
