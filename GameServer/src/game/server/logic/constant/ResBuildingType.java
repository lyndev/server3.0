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
public enum ResBuildingType
{
    /**
     * 灵泉井
     */
    LQJ_BUILD(1),
    /**
     * 摇钱树
     */
    YQS_BUILD(2),
    /**
     * 玄晶矿
     */
    XJK_BUILD(3),
    /**
     * 仙府
     */
    XF_BUILD(4);
 

    private final int value;

    ResBuildingType(int value)
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

    public static ResBuildingType getType(int value)
    {
        for (ResBuildingType type : ResBuildingType.values())
        {
            if (type.value() == value)
                return type;
        }
        return null;
    }
}
