/**
 * @date 2014/6/26
 * @author ChenLong
 */
package game.server.logic.constant;

/**
 * 主线任务目标类型
 *
 * @author ChenLong
 */
public enum MainTaskTargetType
{
    
    NORMAL_MISSION(1),     // 普通关卡进度
    ELITE_MISSION(2),        // 精英关卡进度 
    LEADER_LEVEL_UP(3),  // 主角升级
    ACTIVATE_CARD(4),   // 激活卡牌（卡牌个数）
    CARD_STAR_UP(5),    // 卡牌升星
    CARD_QUALITY_UP(6); // 卡牌品质提升
    
//    FUNCTION(11), // 功能目标
//    CARD(12), // 卡牌目标
//    EQUIPMENT(13), // 装备目标
//    MISSION(4); // 关卡目标

    private final int value;

    MainTaskTargetType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static MainTaskTargetType getMainTaskTargetType(int value)
    {
        for (MainTaskTargetType mainTaskTargetType : MainTaskTargetType.values())
        {
            if (mainTaskTargetType.getValue() == value)
                return mainTaskTargetType;
        }
        return null;
    }
}
