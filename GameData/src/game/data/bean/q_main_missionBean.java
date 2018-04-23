/**
 * Auto generated, do not edit it
 *
 * q_main_mission
 */
package game.data.bean;

public class q_main_missionBean
{
    private int q_id; // 关卡ID(章节(3)_关卡节点(2))
    private int q_next_id; // 下一关
    private int q_unlock_id; // 解锁关卡
    private int q_name; // 关卡名字
    private int q_mapId; // 地图Id
    private String q_name_point; // 据点图片名字
    private int q_difficulty; // 难度（1-普通；2-精英）
    private int q_daily_count; // 每日通关次数（-1无限制；）
    private int q_type; // 大小关(1，小关；2，大关)
    private int q_effective; // 战斗力数值
    private int q_maxNode; // 最大节点数（1-3）
    private int q_force_main_card; // 主角是否强制上场
    private int q_limit_jion_num; // 英雄上阵数量可
    private int q_count_down_1; // 场景1倒计时（秒）
    private int q_count_down_2; // 场景2倒计时（秒）
    private int q_count_down_3; // 场景3倒计时（秒）
    private int q_chapter_id; // 对应章节ID
    private int q_power; // 体力消耗
    private int q_mop_goodsid; // 扫荡消耗物品ID
    private int q_mop_goods_num; // 扫荡道具个数
    private int q_gold; // 关卡金币掉落
    private int q_bullion; // 通关钻石掉落
    private int q_leader_exp; // 主角获得经验
    private int q_exp; // 关卡总经验
    private String q_raid_exp; // 扫荡获得的经验药水
    private String q_monsterId; // 怪物ID
    private String q_drop_items; // 关卡掉落（只用于显示）：ID_ID_ID
    private int q_monsterPosId; // 怪物配置ID
    private int q_firstDropId; // 首次通关掉落ID
    private int q_first_bullion; // 首次通关掉落钻石数量
    private int q_normalDropIds; // 普通怪物掉落包ID
    private String q_bossIds; // BOSSID
    private int q_bossDropItems; // BOSS掉落包
    private int q_node_effect; // 震屏碎屏ID
    private String q_music; // 背景音乐
    private String q_mission_talk_id; // 关卡对话ID（关联关卡对话表）
    private String q_over_trigger; // 结束条件（ID_ID）

    /**
     * get 关卡ID(章节(3)_关卡节点(2))
     * @return
     */
    public int getQ_id()
    {
        return q_id;
    }

    /**
     * set 关卡ID(章节(3)_关卡节点(2))
     */
    public void setQ_id(int q_id)
    {
        this.q_id = q_id;
    }

    /**
     * get 下一关
     * @return
     */
    public int getQ_next_id()
    {
        return q_next_id;
    }

    /**
     * set 下一关
     */
    public void setQ_next_id(int q_next_id)
    {
        this.q_next_id = q_next_id;
    }

    /**
     * get 解锁关卡
     * @return
     */
    public int getQ_unlock_id()
    {
        return q_unlock_id;
    }

    /**
     * set 解锁关卡
     */
    public void setQ_unlock_id(int q_unlock_id)
    {
        this.q_unlock_id = q_unlock_id;
    }

    /**
     * get 关卡名字
     * @return
     */
    public int getQ_name()
    {
        return q_name;
    }

    /**
     * set 关卡名字
     */
    public void setQ_name(int q_name)
    {
        this.q_name = q_name;
    }

    /**
     * get 地图Id
     * @return
     */
    public int getQ_mapId()
    {
        return q_mapId;
    }

    /**
     * set 地图Id
     */
    public void setQ_mapId(int q_mapId)
    {
        this.q_mapId = q_mapId;
    }

    /**
     * get 据点图片名字
     * @return
     */
    public String getQ_name_point()
    {
        return q_name_point;
    }

    /**
     * set 据点图片名字
     */
    public void setQ_name_point(String q_name_point)
    {
        this.q_name_point = q_name_point;
    }

    /**
     * get 难度（1-普通；2-精英）
     * @return
     */
    public int getQ_difficulty()
    {
        return q_difficulty;
    }

    /**
     * set 难度（1-普通；2-精英）
     */
    public void setQ_difficulty(int q_difficulty)
    {
        this.q_difficulty = q_difficulty;
    }

    /**
     * get 每日通关次数（-1无限制；）
     * @return
     */
    public int getQ_daily_count()
    {
        return q_daily_count;
    }

    /**
     * set 每日通关次数（-1无限制；）
     */
    public void setQ_daily_count(int q_daily_count)
    {
        this.q_daily_count = q_daily_count;
    }

    /**
     * get 大小关(1，小关；2，大关)
     * @return
     */
    public int getQ_type()
    {
        return q_type;
    }

    /**
     * set 大小关(1，小关；2，大关)
     */
    public void setQ_type(int q_type)
    {
        this.q_type = q_type;
    }

    /**
     * get 战斗力数值
     * @return
     */
    public int getQ_effective()
    {
        return q_effective;
    }

    /**
     * set 战斗力数值
     */
    public void setQ_effective(int q_effective)
    {
        this.q_effective = q_effective;
    }

    /**
     * get 最大节点数（1-3）
     * @return
     */
    public int getQ_maxNode()
    {
        return q_maxNode;
    }

    /**
     * set 最大节点数（1-3）
     */
    public void setQ_maxNode(int q_maxNode)
    {
        this.q_maxNode = q_maxNode;
    }

    /**
     * get 主角是否强制上场
     * @return
     */
    public int getQ_force_main_card()
    {
        return q_force_main_card;
    }

    /**
     * set 主角是否强制上场
     */
    public void setQ_force_main_card(int q_force_main_card)
    {
        this.q_force_main_card = q_force_main_card;
    }

    /**
     * get 英雄上阵数量可
     * @return
     */
    public int getQ_limit_jion_num()
    {
        return q_limit_jion_num;
    }

    /**
     * set 英雄上阵数量可
     */
    public void setQ_limit_jion_num(int q_limit_jion_num)
    {
        this.q_limit_jion_num = q_limit_jion_num;
    }

    /**
     * get 场景1倒计时（秒）
     * @return
     */
    public int getQ_count_down_1()
    {
        return q_count_down_1;
    }

    /**
     * set 场景1倒计时（秒）
     */
    public void setQ_count_down_1(int q_count_down_1)
    {
        this.q_count_down_1 = q_count_down_1;
    }

    /**
     * get 场景2倒计时（秒）
     * @return
     */
    public int getQ_count_down_2()
    {
        return q_count_down_2;
    }

    /**
     * set 场景2倒计时（秒）
     */
    public void setQ_count_down_2(int q_count_down_2)
    {
        this.q_count_down_2 = q_count_down_2;
    }

    /**
     * get 场景3倒计时（秒）
     * @return
     */
    public int getQ_count_down_3()
    {
        return q_count_down_3;
    }

    /**
     * set 场景3倒计时（秒）
     */
    public void setQ_count_down_3(int q_count_down_3)
    {
        this.q_count_down_3 = q_count_down_3;
    }

    /**
     * get 对应章节ID
     * @return
     */
    public int getQ_chapter_id()
    {
        return q_chapter_id;
    }

    /**
     * set 对应章节ID
     */
    public void setQ_chapter_id(int q_chapter_id)
    {
        this.q_chapter_id = q_chapter_id;
    }

    /**
     * get 体力消耗
     * @return
     */
    public int getQ_power()
    {
        return q_power;
    }

    /**
     * set 体力消耗
     */
    public void setQ_power(int q_power)
    {
        this.q_power = q_power;
    }

    /**
     * get 扫荡消耗物品ID
     * @return
     */
    public int getQ_mop_goodsid()
    {
        return q_mop_goodsid;
    }

    /**
     * set 扫荡消耗物品ID
     */
    public void setQ_mop_goodsid(int q_mop_goodsid)
    {
        this.q_mop_goodsid = q_mop_goodsid;
    }

    /**
     * get 扫荡道具个数
     * @return
     */
    public int getQ_mop_goods_num()
    {
        return q_mop_goods_num;
    }

    /**
     * set 扫荡道具个数
     */
    public void setQ_mop_goods_num(int q_mop_goods_num)
    {
        this.q_mop_goods_num = q_mop_goods_num;
    }

    /**
     * get 关卡金币掉落
     * @return
     */
    public int getQ_gold()
    {
        return q_gold;
    }

    /**
     * set 关卡金币掉落
     */
    public void setQ_gold(int q_gold)
    {
        this.q_gold = q_gold;
    }

    /**
     * get 通关钻石掉落
     * @return
     */
    public int getQ_bullion()
    {
        return q_bullion;
    }

    /**
     * set 通关钻石掉落
     */
    public void setQ_bullion(int q_bullion)
    {
        this.q_bullion = q_bullion;
    }

    /**
     * get 主角获得经验
     * @return
     */
    public int getQ_leader_exp()
    {
        return q_leader_exp;
    }

    /**
     * set 主角获得经验
     */
    public void setQ_leader_exp(int q_leader_exp)
    {
        this.q_leader_exp = q_leader_exp;
    }

    /**
     * get 关卡总经验
     * @return
     */
    public int getQ_exp()
    {
        return q_exp;
    }

    /**
     * set 关卡总经验
     */
    public void setQ_exp(int q_exp)
    {
        this.q_exp = q_exp;
    }

    /**
     * get 扫荡获得的经验药水
     * @return
     */
    public String getQ_raid_exp()
    {
        return q_raid_exp;
    }

    /**
     * set 扫荡获得的经验药水
     */
    public void setQ_raid_exp(String q_raid_exp)
    {
        this.q_raid_exp = q_raid_exp;
    }

    /**
     * get 怪物ID
     * @return
     */
    public String getQ_monsterId()
    {
        return q_monsterId;
    }

    /**
     * set 怪物ID
     */
    public void setQ_monsterId(String q_monsterId)
    {
        this.q_monsterId = q_monsterId;
    }

    /**
     * get 关卡掉落（只用于显示）：ID_ID_ID
     * @return
     */
    public String getQ_drop_items()
    {
        return q_drop_items;
    }

    /**
     * set 关卡掉落（只用于显示）：ID_ID_ID
     */
    public void setQ_drop_items(String q_drop_items)
    {
        this.q_drop_items = q_drop_items;
    }

    /**
     * get 怪物配置ID
     * @return
     */
    public int getQ_monsterPosId()
    {
        return q_monsterPosId;
    }

    /**
     * set 怪物配置ID
     */
    public void setQ_monsterPosId(int q_monsterPosId)
    {
        this.q_monsterPosId = q_monsterPosId;
    }

    /**
     * get 首次通关掉落ID
     * @return
     */
    public int getQ_firstDropId()
    {
        return q_firstDropId;
    }

    /**
     * set 首次通关掉落ID
     */
    public void setQ_firstDropId(int q_firstDropId)
    {
        this.q_firstDropId = q_firstDropId;
    }

    /**
     * get 首次通关掉落钻石数量
     * @return
     */
    public int getQ_first_bullion()
    {
        return q_first_bullion;
    }

    /**
     * set 首次通关掉落钻石数量
     */
    public void setQ_first_bullion(int q_first_bullion)
    {
        this.q_first_bullion = q_first_bullion;
    }

    /**
     * get 普通怪物掉落包ID
     * @return
     */
    public int getQ_normalDropIds()
    {
        return q_normalDropIds;
    }

    /**
     * set 普通怪物掉落包ID
     */
    public void setQ_normalDropIds(int q_normalDropIds)
    {
        this.q_normalDropIds = q_normalDropIds;
    }

    /**
     * get BOSSID
     * @return
     */
    public String getQ_bossIds()
    {
        return q_bossIds;
    }

    /**
     * set BOSSID
     */
    public void setQ_bossIds(String q_bossIds)
    {
        this.q_bossIds = q_bossIds;
    }

    /**
     * get BOSS掉落包
     * @return
     */
    public int getQ_bossDropItems()
    {
        return q_bossDropItems;
    }

    /**
     * set BOSS掉落包
     */
    public void setQ_bossDropItems(int q_bossDropItems)
    {
        this.q_bossDropItems = q_bossDropItems;
    }

    /**
     * get 震屏碎屏ID
     * @return
     */
    public int getQ_node_effect()
    {
        return q_node_effect;
    }

    /**
     * set 震屏碎屏ID
     */
    public void setQ_node_effect(int q_node_effect)
    {
        this.q_node_effect = q_node_effect;
    }

    /**
     * get 背景音乐
     * @return
     */
    public String getQ_music()
    {
        return q_music;
    }

    /**
     * set 背景音乐
     */
    public void setQ_music(String q_music)
    {
        this.q_music = q_music;
    }

    /**
     * get 关卡对话ID（关联关卡对话表）
     * @return
     */
    public String getQ_mission_talk_id()
    {
        return q_mission_talk_id;
    }

    /**
     * set 关卡对话ID（关联关卡对话表）
     */
    public void setQ_mission_talk_id(String q_mission_talk_id)
    {
        this.q_mission_talk_id = q_mission_talk_id;
    }

    /**
     * get 结束条件（ID_ID）
     * @return
     */
    public String getQ_over_trigger()
    {
        return q_over_trigger;
    }

    /**
     * set 结束条件（ID_ID）
     */
    public void setQ_over_trigger(String q_over_trigger)
    {
        this.q_over_trigger = q_over_trigger;
    }
}
