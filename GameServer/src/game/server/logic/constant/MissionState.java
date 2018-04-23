package game.server.logic.constant;

/**
 * 关卡/章节状态枚举
 *
 * @date 2014/5/10 17:37
 * @author ZouZhaopeng
 */
public enum MissionState
{
    /**
     * 关卡初始状态
     */
    MISSION_INIT(0),
    
    /**
     * 关卡未解锁
     */
    MISSION_LOCKED(1),
    
    /**
     * 关卡未通关
     */
    MISSION_NOTPASS(2),
    
    /**
     * 关卡已通关
     */
    MISSION_PASSED(3);

    private MissionState(int state)
    {
        this.state = state;
    }
    
    private final int state;
    
    /**
     * 返回枚举变量的int值
     * @return
     */
    public int value()
    {
        return state;
    }
    
    public boolean compare(int state)
    {
        return this.state == state;
    }
    
    public static MissionState getMissionState(int value)
    {
        for (MissionState ms : MissionState.values())
        {
            if (ms.compare(value))
                return ms;
        }
        throw new IllegalArgumentException("未知的关卡类型值: " + value);
    }
}
