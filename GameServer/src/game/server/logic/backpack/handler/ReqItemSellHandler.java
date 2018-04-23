package game.server.logic.backpack.handler;

import game.core.command.Handler;
import game.message.BackpackMessage;
import game.server.logic.struct.Player;

public class ReqItemSellHandler extends Handler
{

    @Override
    public void action()
    {
        ((Player) getAttribute("player")).getBackpackManager()
                .onItemSell((BackpackMessage.ReqItemSell) getMessage().getData());
    }

}
