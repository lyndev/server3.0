/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.server.logic.constant;

/**
 *  提示类型
 * 
 * @date   2014-7-9
 * @author pengmian
 */
public enum NoticeLevel 
{

    ERROR(0),       // 错误
    SUCCESS(1),     // 成功
    NORMAL(2);      // 普通提示
    
    private final int val;
        
    private NoticeLevel(int val)
    {
        this.val = val;
    }
    
    public int value()
    {
        return val;
    }
}
