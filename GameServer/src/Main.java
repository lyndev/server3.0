
/**
 * @date 2014/3/21
 * @author ChenLong
 */
import game.server.GameServer;

/**
 * <b>GameServer启动main方法</b>
 *
 * @author ChenLong
 */
public class Main
{
    public static void main(String[] args)
    {
        GameServer.getInstance().start(args);
    }
}
