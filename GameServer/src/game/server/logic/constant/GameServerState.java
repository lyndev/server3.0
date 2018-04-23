/**
 * @date 2014/8/11
 * @author ChenLong
 */
package game.server.logic.constant;

/**
 * GameServer状态
 *
 * @author ChenLong
 */
public enum GameServerState
{
    NULL(0),
    STARTING(1), // 启动中
    STARTED(2), // 启动完成
    SHUTDOWNING(3),
    SHUTDOWNED(4);

    private final int value;

    GameServerState(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
