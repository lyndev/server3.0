/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.config;

/**
 *
 * @author Administrator
 */

/**
 * 成就类型枚举
 * <p>该成就完成条件的类型:等级, 次数, 排名, etc
 * 
 * @author ZouZhaopeng
 */
public enum ServerType
{
    /**
     * 1. cocos2d 服务器
     */
    COCOS2D_SERVER(1),
    /**
     * 2. 召唤师等级
     */
    UNITY3D_SERVER(2),
    /**
     * 3. 通过指定关卡
     */
    CREATOR_SERVER(3);

     
    private ServerType(int value)
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
    
    public static ServerType getTypeByValue(int value)
    {
        for (ServerType t : ServerType.values())
        {
            if (t.value == value)
                return t;
        }
        throw new IllegalStateException("未知的成就类型值: " + value);
    }
    
    private final int value;
}
