package game.server.logic.constant;

/**
 * 购买金币错误码
 * 
 * @author Administrator
 */
public enum BuyGoldError
{
    /**
     * 次数不足
     */
    COUNT(1),
    /**
     * 元宝不足
     */
    BULLION(2),
    /**
     * 金币到达上限
     */
    GOLD_LIMIT(3),
    ;

    private BuyGoldError(int value)
    {
        this.value = value;
    }
    int value;
   
}
