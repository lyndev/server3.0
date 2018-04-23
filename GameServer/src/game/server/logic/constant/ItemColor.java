/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.server.logic.constant;

/**
 * 
 * @date   2014-7-28
 * @author pengmian
 */
public class ItemColor 
{
     // 公告物品名字颜色
    private static final String itemNameColor[] = new String[]
    {
        "{ffffff}", // 白色0
        "{99cc00}", // 绿色1
        "{49def8}", // 蓝色2
        "{fb87ea}", // 紫色3
        "{cf9f38}"  // 橙色4
    };

    public static String getItemNameColor(int idx)
    {
        if (idx >= itemNameColor.length || idx < 0)
        {
            idx = 1;
        }
        return itemNameColor[idx];
    }
}
