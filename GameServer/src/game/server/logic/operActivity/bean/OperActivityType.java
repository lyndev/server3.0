/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.logic.operActivity.bean;

/**
 * 活动类型
 * @author Administrator
 */
public enum OperActivityType
{
    //掉落翻倍
    DOUBLE_DROP(1),
    //额外掉落
    EXTRA_DROP(2),
    //累计充值
    RECHARGE_CNT(3),
    //累计登录
    LOGIN_CNT(4),
    ;
    private OperActivityType(int code)
    {
        this.code = code;
    }

    private final int code;
    public static OperActivityType getActivityType(int type)
    {
        for (OperActivityType nodeType : OperActivityType.values())
        {
            if (nodeType.code == type)
                return nodeType;
        }
        throw new IllegalArgumentException("未知的活动类型: " + type);
    }
    public int getCode()
    {
        return this.code;
    }
}
