/**
 * @date 2014/9/24
 * @author ChenLong
 */
package game.server.logic.recharge;

/**
 * 获取回来的充值结果里item_code字段下的goodsType枚举值
 *
 * @author ChenLong
 */
public enum GoodsType
{
    MonthCard(1, "月卡"),
    QuarterCard(2, "季卡"),
    YearCard(3, "年卡"),
    Gold(4, "元宝");

    private final int value;
    private final String desc;

    GoodsType(int value, String desc)
    {
        this.value = value;
        this.desc = desc;
    }

    public int getValue()
    {
        return value;
    }

    public String getDesc()
    {
        return desc;
    }

    public static GoodsType getType(int value)
    {
        GoodsType ret = null;
        for (GoodsType type : GoodsType.values())
        {
            if (type.getValue() == value)
            {
                ret = type;
                break;
            }
        }
        return ret;
    }
}
