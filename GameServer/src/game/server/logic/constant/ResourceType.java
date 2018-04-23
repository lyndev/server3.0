package game.server.logic.constant;

/**
 *
 * <b>资源类型定义.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public enum ResourceType
{
    
    /**
     * 金币
     */
    GOLD("金币", 1, 0, 99999999),
    /**
     * 钻石
     */
    GOLD_BULLION("钻石", 2, 0, 99999999),
    /**
     * 体力
     */
    ENERGY("体力", 3, 80, 999),
    /**
     * 技能点
     */
    SKILL_POINT("技能点", 4, 10, 999),
    
    /**
     * 通关的星级总数
     */
    MISSION_STAR("通关的星级总数", 5, 0, 99999),
    
    /**
     * 荣誉值
     */
    HONOR("荣誉值", 6, 0, 99999999),
    
    /**
     * 远征币
     */
    EXPEDITION_COIN("远征币", 7, 0, 9999999),
    
    /**
     * 灵魂碎片
     */
    SOUL_FRAG("灵魂碎片", 8, 0, 9999999),
    
//============以下是万万的资源类型===================
    
    /**
     * 成就点
     */
    ACHIEVEMENT("成就点", 20, 0, 99999999),
    /**
     * 木材
     */
    WOOD("木材", 21, 0, 99999999),
    /**
     * 星魂
     */
    STAR_SOUL("星魂", 22, 0, 99999999),
    /**
     * 星钻
     */
    STAR_DIAMOND("星钻", 23, 0, 99999999),
    /**
     * 精力
     */
    MANA("精力", 24, 100, 9999),
    /**
     * 代币
     */
    TOKEN2("代币", 25, 0, 99999999),
    ;

    private final String name;

    private final int value;

    private final int limit; // 自动产出的基础上限值

    private final int maxAmount; // 玩家可拥有的资源上限

    ResourceType(String name, int value, int limit, int maxAmount)
    {
        this.name = name;
        this.value = value;
        this.limit = limit;
        this.maxAmount = maxAmount;
    }

    public int value()
    {
        return value;
    }

    public String getName()
    {
        return name;
    }

    /**
     * 获取自动产出的基础上限值.
     *
     * @return
     */
    public int getLimit()
    {
        return limit;
    }

    /**
     * 获取玩家可拥有的资源上限.
     *
     * @return
     */
    public int getMaxAmount()
    {
        return maxAmount;
    }

    public boolean compare(int value)
    {
        return this.value == value;
    }

    public static ResourceType getType(int value)
    {
        for (ResourceType type : ResourceType.values())
        {
            if (type.value() == value)
                return type;
        }
        return null;
    }

}
