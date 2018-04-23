/**
 * @date 2014/9/4
 * @author ChenLong
 */
package game.server.logic.constant;

/**
 * 日常任务类型
 *
 * @author ChenLong
 */
public enum DailyTaskStatus
{
    Unlock(0), //未解锁
    InProcess(1), // 进行中
    Finished(2), // 完成但未领取奖励
    GotAward(3); // 完成并且已领取奖励

    private final int value;

    DailyTaskStatus(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static DailyTaskStatus getDailyTaskStatus(int value)
    {
        for (DailyTaskStatus type : DailyTaskStatus.values())
        {
            if (type.getValue() == value)
                return type;
        }
        return null;
    }
}
