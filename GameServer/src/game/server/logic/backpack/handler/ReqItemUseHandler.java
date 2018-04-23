package game.server.logic.backpack.handler;

import game.core.command.Handler;
import game.message.BackpackMessage;
import game.server.logic.struct.Player;

public class ReqItemUseHandler extends Handler
{

    @Override
    public void action()
    {
        ((Player) getAttribute("player")).getBackpackManager()
                .onItemUse((BackpackMessage.ReqItemUse) getMessage().getData());
    }

}
