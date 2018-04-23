package game.server.logic.constant;

/**
 * 成就状态枚举
 *
 * @date 2014/6/9 21:53
 * @author ZouZhaopeng
 */
public enum AchievementState
{
    NOT_OPEN(3), //还未开启
    IN_PROCESS(2), // 进行中
    COMPLETE_NOT_GET_AWARD(1), // 完成但未领取奖励
    COMPLETE_HAS_GET_AWARD(4);     // 完成并且已领取奖励
    
    private final int value;
    private AchievementState(int value)
    {
        this.value = value;
    }

    public int value()
    {
        return this.value;
    }

    public static AchievementState getStateFromValue(int value)
    {
        for (AchievementState as : AchievementState.values())
        {
            if (as.compare(value))
                return as;
        }
        throw new IllegalArgumentException("unknow AchievementState value = " + value);
    }
    
    public boolean compare(int value)
    {
        return this.value == value;
    }

}
