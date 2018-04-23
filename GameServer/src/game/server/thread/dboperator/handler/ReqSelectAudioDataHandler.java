package game.server.thread.dboperator.handler;

import game.server.db.game.bean.AudioDataBean;
import game.server.db.game.dao.AudioDataDao;
import game.server.logic.struct.Player;
import game.server.world.GameWorld;
import java.util.UUID;

/**
 *
 * @author ZouZhaopeng
 */
public class ReqSelectAudioDataHandler extends DBOperatorHandler
{
    private final Player player;
    private final UUID uuid;

    public ReqSelectAudioDataHandler(Player player, UUID uuid)
    {
        super(0);
        this.player = player;
        this.uuid = uuid;
    }
    

    @Override
    public void action()
    {
        AudioDataBean bean = AudioDataDao.select(uuid.toString());
        GameWorld.getInstance().addCommand(new ResSelectAudioDataHandler(player, bean));
    }
    
}
