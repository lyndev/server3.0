package game.server.thread.dboperator.handler;

import game.server.db.game.bean.AudioDataBean;
import game.server.logic.struct.Player;
import game.server.world.GameWorld;

/**
 *
 * @author ZouZhaopeng
 */
public class ResSelectAudioDataHandler extends DBOperatorHandler
{
    private final Player player;
    private final AudioDataBean bean;
    public ResSelectAudioDataHandler(Player player, AudioDataBean bean)
    {
        super(0);
        this.player = player;
        this.bean = bean;
    }

    @Override
    public void action()
    {
        GameWorld.getInstance().getChatManager().afterFetchFromDB(player, bean);
    }
    
}
