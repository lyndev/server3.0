package game.server.logic.constant;

/**
 * 关卡/章节星级评价枚举
 *
 * @date 2014/5/12 9:51
 * @author ZouZhaopeng
 */
public enum MissionStar
{
    /**
     * 零星评价
     */
    ZERO_STAR(0),
    /**
     * 一星评价
     */
    ONE_STAR(1),
    /**
     * 二星评价
     */
    TWO_STAR(2),
    /**
     * 三星评价
     */
    THREE_STAR(3);

    private MissionStar(int star)
    {
        this.star = star;
    }
    private final int star;
    
    public int value()
    {
        return star;
    }
    
    public static MissionStar getMissionStar(int star)
    {
        for (MissionStar ms : MissionStar.values())
        {
            if (ms.compare(star))
                return ms;
        }
        throw new IllegalArgumentException("未知的关卡星级值: " + star);
    }
    
    public boolean compare(int star)
    {
        return this.star == star;
    }
}
