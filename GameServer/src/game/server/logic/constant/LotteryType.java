/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.server.logic.constant;

/**
 *
 * @author YangHanzhou
 */

public enum LotteryType
{
    
    /**
     * 金币抽奖
     */
    GOLD_ONCE_RECRUIT(1),
    /**
     * 钻石抽奖
     */
    GEM_ONCE_RECRUIT(2),
    /**
     * 钻石抽奖
     */
    GOLD_TEN_RECRUIT(3),
    /**
     * 钻石抽奖
     */
    GEM_TEN_RECRUIT(4);
    
    
//    /**
//     * 武将招募
//     */
//    WJ_RECRUIT(1),
//    /**
//     * 良将招募
//     */
//    LJ_RECRUIT(2),
//    /**
//     * 神将招募
//     */
//    SJ_RECRUIT(3),
//    /**
//     * 神将招募10次
//     */
//    SJ_RECRUIT10(4);
 

    private final int value;

    LotteryType(int value)
    {
        this.value = value;
    }

    public int value()
    {
        return value;
    }

    public boolean compare(int value)
    {
        return this.value == value;
    }

    public static LotteryType getType(int value)
    {
        for (LotteryType type : LotteryType.values())
        {
            if (type.value() == value)
                return type;
        }
        return null;
    }
}
