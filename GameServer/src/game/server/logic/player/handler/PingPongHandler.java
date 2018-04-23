/**
 * @date 2014/7/14
 * @author ChenLong
 */
package game.server.logic.player.handler;

import game.core.command.Handler;
import game.message.LoginMessage;
import game.server.logic.struct.Player;

/**
 * 乒乓测试, 即客户端发一个消息, 服务端收到后再返回给客户端, 客户端收到后再发给服务端; 用于模拟大量消息请求操作, 测试网络/逻辑层处理能力
 *
 * @author ChenLong
 */
public class PingPongHandler extends Handler
{
    @Override
    public void action()
    {
        Player player = (Player) getAttribute("player");
        player.resPingPongIncrement((LoginMessage.ResPingPong) getMessage().getData());
    }
}
