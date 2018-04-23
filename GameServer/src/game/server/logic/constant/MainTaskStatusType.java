/**
 * @date 2014/5/5
 * @author ChenLong
 */
package game.server.logic.constant;

/**
 * <b>任务状态</b>
 *
 * @author ChenLong
 */
public enum MainTaskStatusType
{   
    UNLOCK(0),    //还未解锁
    IN_PROCESS(1), // 进行中
    COMPLETE_NOT_GET_AWARD(2), // 完成但未领取奖励
    COMPLETE_HAS_GET_AWARD(3);     // 完成并且已领取奖励

    private final int value;

    MainTaskStatusType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static MainTaskStatusType getStateFromValue(int value)
    {
        for (MainTaskStatusType taskState : MainTaskStatusType.values())
        {
            if (taskState.getValue() == value)
                return taskState;
        }
        throw new IllegalArgumentException("unknow taskType value = " + value);
    }
}
