package game.server.logic.constant;

/**
 * 关卡类型枚举
 *
 * @date 2014/5/10 11:07
 * @author ZouZhaopeng
 */
public enum MissionType
{
    /**
     * 普通关卡
     */
    ORDINARY(1),
    /**
     * 精英关卡
     */
    ELITE(2),
    /**
     * 英雄关卡
     */
    HERO(3);

    private MissionType(int value)
    {
        this.value = value;
    }
    private final int value;
    
    /**
     * 返回类型的枚举值
     * @return 
     */
    public int value()
    {
        return value;
    }
    
    public static MissionType getMissionType(int value)
    {
        for (MissionType mt : MissionType.values())
        {
            if (mt.value == value)
                return mt;
        }
        throw new IllegalArgumentException("未知的关卡类型值: " + value);
    }
    
    public boolean compare(int value)
    {
        return this.value == value;
    }
    
}
