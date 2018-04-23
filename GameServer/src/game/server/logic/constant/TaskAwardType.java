/**
 * @date 2014/5/24
 * @author ChenLong
 */
package game.server.logic.constant;

/**
 * 任务奖励类型
 *
 * @author ChenLong
 */
public enum TaskAwardType
{
    ITEM(1), // 1：物品
    HERO(2), // 2：英雄
    RESOURCE(3); // 3：资源

    private final int value;

    TaskAwardType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static TaskAwardType getTaskAwardType(int value)
    {
        for (TaskAwardType taskAwardType : TaskAwardType.values())
        {
            if (taskAwardType.getValue() == value)
                return taskAwardType;
        }
        return null;
    }
}
