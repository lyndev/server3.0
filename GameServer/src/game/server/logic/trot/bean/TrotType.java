/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.server.logic.trot.bean;

import game.server.logic.trot.filter.TrotFilter;
import game.server.logic.trot.filter.TrotFilterByBuilding;
import game.server.logic.trot.filter.TrotFilterByCreatTime;
import game.server.logic.trot.filter.TrotFilterByLevel;
import game.server.logic.trot.filter.TrotFilterByOnlineTime;
import game.server.logic.trot.filter.TrotFilterByRange;
import game.server.logic.trot.filter.TrotFilterByVip;
import game.server.logic.trot.filter.TrotFilterToAll;
import game.server.logic.trot.filter.TrotFilterToPlatform;
import game.server.logic.trot.filter.TrotFilterToPlayer;

/**
 * @date   2014-7-15
 * @author pengmian
 */
public enum TrotType 
{
    TROT_TO_ALL(1, TrotFilterToAll.class),           // 所有玩家
    TROT_TO_PLAYER(2, TrotFilterToPlayer.class),        // 针对玩家发送
    TROT_BY_LEVEL(3, TrotFilterByLevel.class),         // 按照玩家等级发送
    TROT_BY_VIP(4, TrotFilterByVip.class),           // 按照玩家VIP等级发送
    TROT_BY_BUILDING(5, TrotFilterByBuilding.class),      // 按照仙府等级发送
    TROT_BY_CREATETIME(6, TrotFilterByCreatTime.class),    // 按照账号创建时间发送
    TROT_BY_ONLINETIME(7, TrotFilterByOnlineTime.class),    // 按照在线时间发送
    TROT_BY_RANGE(8, TrotFilterByRange.class),         // 按照竞技排名发送
    TROT_TO_PLATFORM(9, TrotFilterToPlatform.class);      // 按照平台发送
    
    public static TrotType getTypeByValue(int val)
    {
        for (TrotType type : TrotType.values())
        {
            if (type.value == val)
            {
                return type;
            }
        }
        throw new IllegalStateException("未知的类型值: " + val);
    }
    
    private final int value;
    private final Class<? extends TrotFilter> clazz;
    
    private TrotType(int value, Class<? extends TrotFilter> clazz)
    {
        this.value = value;
        this.clazz = clazz;
    }

    public TrotFilter getFilter() throws InstantiationException, IllegalAccessException
    {
        return this.clazz.newInstance();
    }
    
    public int value()
    {
        return this.value;
    }
}
