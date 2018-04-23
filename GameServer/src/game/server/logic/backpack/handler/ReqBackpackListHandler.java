package game.server.logic.backpack.handler;

import game.core.command.Handler;
import game.server.logic.struct.Player;

public class ReqBackpackListHandler extends Handler
{

    @Override
    public void action()
    {
        ((Player) getAttribute("player")).getBackpackManager().sendBackpackList();
    }

}
