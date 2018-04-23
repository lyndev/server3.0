/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.logic.player.handler;

import game.core.command.Handler;
import game.server.logic.line.GameLine;
import game.server.logic.line.GameLineManager;
import game.server.logic.struct.Player;
import game.server.util.MessageUtils;
import game.server.world.chat.handler.LWChatInitializeOverHandler;
import org.apache.log4j.Logger;

/**
 * 客户端初始化完成消息处理
 *
 * @author Administrator
 */
public class ReqClientInitOverHandler extends Handler
{
    private final Logger log = Logger.getLogger(ReqClientInitOverHandler.class);

    @Override
    public void action()
    {
        Player player = (Player) getAttribute("player");
        if (player != null)
        {
            player.clientInitializeOver();
            GameLine gameLine = GameLineManager.getInstance().getLine(player.getLineId());
            if (gameLine != null)
            {
                gameLine.getCreateRoleLock().removeTransaction(player.getUserName());
                gameLine.getLoginLock().removeTransaction(player.getUserName());
            }
            
            // 请求聊天、好友初始化完成消息
            LWChatInitializeOverHandler handler = new LWChatInitializeOverHandler(player);
            MessageUtils.sendToGameWorld(handler);
        }
        else
        {
            log.error("player == null");
        }
    }
}
