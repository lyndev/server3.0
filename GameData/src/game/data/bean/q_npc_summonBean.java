/**
 * Auto generated, do not edit it
 *
 * q_npc_summon
 */
package game.data.bean;

public class q_npc_summonBean
{
    private int q_id; // ID
    private String q_name; // 名称（策划用）
    private int q_hero_type; // 类别
    private int q_count_filter; // 是否算做怪物计数
    private int q_aiId; // AI行为
    private int q_ai; // AI检测范围
    private int q_tag; // 标记
    private int q_ai_frequency; // Ai频率（秒）
    private int q_energy; // 死亡时攻击者可获能量值
    private int q_hp_tf_energy; // 掉血转换能量比率
    private int q_hard_odds; // 硬直几率
    private int q_atk_type; // 普攻方式
    private String q_skill_param; // 技能ID_等级
    private int q_hero_Lv; // 召唤物初始等级
    private int q_live_time; // 召唤物持续时间(mS)
    private int q_cnt_max; // 召唤物共存上限
    private int q_str_growup; // 力量成长值
    private int q_agi_growup; // 智力成长值
    private int q_mid_growup; // 敏捷成长值
    private int q_str; // 初始力量
    private int q_agi; // 初始敏捷
    private int q_mid; // 初始智力
    private int q_hp_factor; // 生命系数
    private int q_phy_atk_factor; // 物攻系数
    private int q_magic_atk_factor; // 魔攻系数
    private int q_phy_def_factor; // 护甲系数
    private int q_magic_def_factor; // 魔抗系数
    private int q_hit_factor; // 命中系数
    private int q_phy_crit_factor; // 物理暴击系数
    private int q_magic_crit_factor; // 魔法暴击系数
    private int q_phy_evade_factor; // 物理闪避系数
    private int q_magic_evade_factor; // 魔法闪避系数
    private int q_crit_dmg_factor; // 暴击伤害系数
    private int q_phy_Pierce_factor; // 护甲穿透系数
    private int q_magic_Pierce_factor; // 魔抗穿透系数
    private int q_drains_factor; // 吸血系数
    private int q_toughness_factor; // 韧性系数
    private int q_atk_speed; // 攻击速度(次/秒）
    private int q_move_speed; // 移动速度（像素/秒）
    private String q_show_effect; // 出场特效
    private int q_show_delay; // 出场特效事件延迟
    private String q_hero_res; // 召唤物角色资源
    private int q_action_time_id; // spine动作时间
    private int q_scale; // 角色缩放
    private String q_color; // 角色颜色
    private int q_first_activity; // 首选行为
    private int q_blood_type; // 血条类型

    /**
     * get ID
     * @return
     */
    public int getQ_id()
    {
        return q_id;
    }

    /**
     * set ID
     */
    public void setQ_id(int q_id)
    {
        this.q_id = q_id;
    }

    /**
     * get 名称（策划用）
     * @return
     */
    public String getQ_name()
    {
        return q_name;
    }

    /**
     * set 名称（策划用）
     */
    public void setQ_name(String q_name)
    {
        this.q_name = q_name;
    }

    /**
     * get 类别
     * @return
     */
    public int getQ_hero_type()
    {
        return q_hero_type;
    }

    /**
     * set 类别
     */
    public void setQ_hero_type(int q_hero_type)
    {
        this.q_hero_type = q_hero_type;
    }

    /**
     * get 是否算做怪物计数
     * @return
     */
    public int getQ_count_filter()
    {
        return q_count_filter;
    }

    /**
     * set 是否算做怪物计数
     */
    public void setQ_count_filter(int q_count_filter)
    {
        this.q_count_filter = q_count_filter;
    }

    /**
     * get AI行为
     * @return
     */
    public int getQ_aiId()
    {
        return q_aiId;
    }

    /**
     * set AI行为
     */
    public void setQ_aiId(int q_aiId)
    {
        this.q_aiId = q_aiId;
    }

    /**
     * get AI检测范围
     * @return
     */
    public int getQ_ai()
    {
        return q_ai;
    }

    /**
     * set AI检测范围
     */
    public void setQ_ai(int q_ai)
    {
        this.q_ai = q_ai;
    }

    /**
     * get 标记
     * @return
     */
    public int getQ_tag()
    {
        return q_tag;
    }

    /**
     * set 标记
     */
    public void setQ_tag(int q_tag)
    {
        this.q_tag = q_tag;
    }

    /**
     * get Ai频率（秒）
     * @return
     */
    public int getQ_ai_frequency()
    {
        return q_ai_frequency;
    }

    /**
     * set Ai频率（秒）
     */
    public void setQ_ai_frequency(int q_ai_frequency)
    {
        this.q_ai_frequency = q_ai_frequency;
    }

    /**
     * get 死亡时攻击者可获能量值
     * @return
     */
    public int getQ_energy()
    {
        return q_energy;
    }

    /**
     * set 死亡时攻击者可获能量值
     */
    public void setQ_energy(int q_energy)
    {
        this.q_energy = q_energy;
    }

    /**
     * get 掉血转换能量比率
     * @return
     */
    public int getQ_hp_tf_energy()
    {
        return q_hp_tf_energy;
    }

    /**
     * set 掉血转换能量比率
     */
    public void setQ_hp_tf_energy(int q_hp_tf_energy)
    {
        this.q_hp_tf_energy = q_hp_tf_energy;
    }

    /**
     * get 硬直几率
     * @return
     */
    public int getQ_hard_odds()
    {
        return q_hard_odds;
    }

    /**
     * set 硬直几率
     */
    public void setQ_hard_odds(int q_hard_odds)
    {
        this.q_hard_odds = q_hard_odds;
    }

    /**
     * get 普攻方式
     * @return
     */
    public int getQ_atk_type()
    {
        return q_atk_type;
    }

    /**
     * set 普攻方式
     */
    public void setQ_atk_type(int q_atk_type)
    {
        this.q_atk_type = q_atk_type;
    }

    /**
     * get 技能ID_等级
     * @return
     */
    public String getQ_skill_param()
    {
        return q_skill_param;
    }

    /**
     * set 技能ID_等级
     */
    public void setQ_skill_param(String q_skill_param)
    {
        this.q_skill_param = q_skill_param;
    }

    /**
     * get 召唤物初始等级
     * @return
     */
    public int getQ_hero_Lv()
    {
        return q_hero_Lv;
    }

    /**
     * set 召唤物初始等级
     */
    public void setQ_hero_Lv(int q_hero_Lv)
    {
        this.q_hero_Lv = q_hero_Lv;
    }

    /**
     * get 召唤物持续时间(mS)
     * @return
     */
    public int getQ_live_time()
    {
        return q_live_time;
    }

    /**
     * set 召唤物持续时间(mS)
     */
    public void setQ_live_time(int q_live_time)
    {
        this.q_live_time = q_live_time;
    }

    /**
     * get 召唤物共存上限
     * @return
     */
    public int getQ_cnt_max()
    {
        return q_cnt_max;
    }

    /**
     * set 召唤物共存上限
     */
    public void setQ_cnt_max(int q_cnt_max)
    {
        this.q_cnt_max = q_cnt_max;
    }

    /**
     * get 力量成长值
     * @return
     */
    public int getQ_str_growup()
    {
        return q_str_growup;
    }

    /**
     * set 力量成长值
     */
    public void setQ_str_growup(int q_str_growup)
    {
        this.q_str_growup = q_str_growup;
    }

    /**
     * get 智力成长值
     * @return
     */
    public int getQ_agi_growup()
    {
        return q_agi_growup;
    }

    /**
     * set 智力成长值
     */
    public void setQ_agi_growup(int q_agi_growup)
    {
        this.q_agi_growup = q_agi_growup;
    }

    /**
     * get 敏捷成长值
     * @return
     */
    public int getQ_mid_growup()
    {
        return q_mid_growup;
    }

    /**
     * set 敏捷成长值
     */
    public void setQ_mid_growup(int q_mid_growup)
    {
        this.q_mid_growup = q_mid_growup;
    }

    /**
     * get 初始力量
     * @return
     */
    public int getQ_str()
    {
        return q_str;
    }

    /**
     * set 初始力量
     */
    public void setQ_str(int q_str)
    {
        this.q_str = q_str;
    }

    /**
     * get 初始敏捷
     * @return
     */
    public int getQ_agi()
    {
        return q_agi;
    }

    /**
     * set 初始敏捷
     */
    public void setQ_agi(int q_agi)
    {
        this.q_agi = q_agi;
    }

    /**
     * get 初始智力
     * @return
     */
    public int getQ_mid()
    {
        return q_mid;
    }

    /**
     * set 初始智力
     */
    public void setQ_mid(int q_mid)
    {
        this.q_mid = q_mid;
    }

    /**
     * get 生命系数
     * @return
     */
    public int getQ_hp_factor()
    {
        return q_hp_factor;
    }

    /**
     * set 生命系数
     */
    public void setQ_hp_factor(int q_hp_factor)
    {
        this.q_hp_factor = q_hp_factor;
    }

    /**
     * get 物攻系数
     * @return
     */
    public int getQ_phy_atk_factor()
    {
        return q_phy_atk_factor;
    }

    /**
     * set 物攻系数
     */
    public void setQ_phy_atk_factor(int q_phy_atk_factor)
    {
        this.q_phy_atk_factor = q_phy_atk_factor;
    }

    /**
     * get 魔攻系数
     * @return
     */
    public int getQ_magic_atk_factor()
    {
        return q_magic_atk_factor;
    }

    /**
     * set 魔攻系数
     */
    public void setQ_magic_atk_factor(int q_magic_atk_factor)
    {
        this.q_magic_atk_factor = q_magic_atk_factor;
    }

    /**
     * get 护甲系数
     * @return
     */
    public int getQ_phy_def_factor()
    {
        return q_phy_def_factor;
    }

    /**
     * set 护甲系数
     */
    public void setQ_phy_def_factor(int q_phy_def_factor)
    {
        this.q_phy_def_factor = q_phy_def_factor;
    }

    /**
     * get 魔抗系数
     * @return
     */
    public int getQ_magic_def_factor()
    {
        return q_magic_def_factor;
    }

    /**
     * set 魔抗系数
     */
    public void setQ_magic_def_factor(int q_magic_def_factor)
    {
        this.q_magic_def_factor = q_magic_def_factor;
    }

    /**
     * get 命中系数
     * @return
     */
    public int getQ_hit_factor()
    {
        return q_hit_factor;
    }

    /**
     * set 命中系数
     */
    public void setQ_hit_factor(int q_hit_factor)
    {
        this.q_hit_factor = q_hit_factor;
    }

    /**
     * get 物理暴击系数
     * @return
     */
    public int getQ_phy_crit_factor()
    {
        return q_phy_crit_factor;
    }

    /**
     * set 物理暴击系数
     */
    public void setQ_phy_crit_factor(int q_phy_crit_factor)
    {
        this.q_phy_crit_factor = q_phy_crit_factor;
    }

    /**
     * get 魔法暴击系数
     * @return
     */
    public int getQ_magic_crit_factor()
    {
        return q_magic_crit_factor;
    }

    /**
     * set 魔法暴击系数
     */
    public void setQ_magic_crit_factor(int q_magic_crit_factor)
    {
        this.q_magic_crit_factor = q_magic_crit_factor;
    }

    /**
     * get 物理闪避系数
     * @return
     */
    public int getQ_phy_evade_factor()
    {
        return q_phy_evade_factor;
    }

    /**
     * set 物理闪避系数
     */
    public void setQ_phy_evade_factor(int q_phy_evade_factor)
    {
        this.q_phy_evade_factor = q_phy_evade_factor;
    }

    /**
     * get 魔法闪避系数
     * @return
     */
    public int getQ_magic_evade_factor()
    {
        return q_magic_evade_factor;
    }

    /**
     * set 魔法闪避系数
     */
    public void setQ_magic_evade_factor(int q_magic_evade_factor)
    {
        this.q_magic_evade_factor = q_magic_evade_factor;
    }

    /**
     * get 暴击伤害系数
     * @return
     */
    public int getQ_crit_dmg_factor()
    {
        return q_crit_dmg_factor;
    }

    /**
     * set 暴击伤害系数
     */
    public void setQ_crit_dmg_factor(int q_crit_dmg_factor)
    {
        this.q_crit_dmg_factor = q_crit_dmg_factor;
    }

    /**
     * get 护甲穿透系数
     * @return
     */
    public int getQ_phy_Pierce_factor()
    {
        return q_phy_Pierce_factor;
    }

    /**
     * set 护甲穿透系数
     */
    public void setQ_phy_Pierce_factor(int q_phy_Pierce_factor)
    {
        this.q_phy_Pierce_factor = q_phy_Pierce_factor;
    }

    /**
     * get 魔抗穿透系数
     * @return
     */
    public int getQ_magic_Pierce_factor()
    {
        return q_magic_Pierce_factor;
    }

    /**
     * set 魔抗穿透系数
     */
    public void setQ_magic_Pierce_factor(int q_magic_Pierce_factor)
    {
        this.q_magic_Pierce_factor = q_magic_Pierce_factor;
    }

    /**
     * get 吸血系数
     * @return
     */
    public int getQ_drains_factor()
    {
        return q_drains_factor;
    }

    /**
     * set 吸血系数
     */
    public void setQ_drains_factor(int q_drains_factor)
    {
        this.q_drains_factor = q_drains_factor;
    }

    /**
     * get 韧性系数
     * @return
     */
    public int getQ_toughness_factor()
    {
        return q_toughness_factor;
    }

    /**
     * set 韧性系数
     */
    public void setQ_toughness_factor(int q_toughness_factor)
    {
        this.q_toughness_factor = q_toughness_factor;
    }

    /**
     * get 攻击速度(次/秒）
     * @return
     */
    public int getQ_atk_speed()
    {
        return q_atk_speed;
    }

    /**
     * set 攻击速度(次/秒）
     */
    public void setQ_atk_speed(int q_atk_speed)
    {
        this.q_atk_speed = q_atk_speed;
    }

    /**
     * get 移动速度（像素/秒）
     * @return
     */
    public int getQ_move_speed()
    {
        return q_move_speed;
    }

    /**
     * set 移动速度（像素/秒）
     */
    public void setQ_move_speed(int q_move_speed)
    {
        this.q_move_speed = q_move_speed;
    }

    /**
     * get 出场特效
     * @return
     */
    public String getQ_show_effect()
    {
        return q_show_effect;
    }

    /**
     * set 出场特效
     */
    public void setQ_show_effect(String q_show_effect)
    {
        this.q_show_effect = q_show_effect;
    }

    /**
     * get 出场特效事件延迟
     * @return
     */
    public int getQ_show_delay()
    {
        return q_show_delay;
    }

    /**
     * set 出场特效事件延迟
     */
    public void setQ_show_delay(int q_show_delay)
    {
        this.q_show_delay = q_show_delay;
    }

    /**
     * get 召唤物角色资源
     * @return
     */
    public String getQ_hero_res()
    {
        return q_hero_res;
    }

    /**
     * set 召唤物角色资源
     */
    public void setQ_hero_res(String q_hero_res)
    {
        this.q_hero_res = q_hero_res;
    }

    /**
     * get spine动作时间
     * @return
     */
    public int getQ_action_time_id()
    {
        return q_action_time_id;
    }

    /**
     * set spine动作时间
     */
    public void setQ_action_time_id(int q_action_time_id)
    {
        this.q_action_time_id = q_action_time_id;
    }

    /**
     * get 角色缩放
     * @return
     */
    public int getQ_scale()
    {
        return q_scale;
    }

    /**
     * set 角色缩放
     */
    public void setQ_scale(int q_scale)
    {
        this.q_scale = q_scale;
    }

    /**
     * get 角色颜色
     * @return
     */
    public String getQ_color()
    {
        return q_color;
    }

    /**
     * set 角色颜色
     */
    public void setQ_color(String q_color)
    {
        this.q_color = q_color;
    }

    /**
     * get 首选行为
     * @return
     */
    public int getQ_first_activity()
    {
        return q_first_activity;
    }

    /**
     * set 首选行为
     */
    public void setQ_first_activity(int q_first_activity)
    {
        this.q_first_activity = q_first_activity;
    }

    /**
     * get 血条类型
     * @return
     */
    public int getQ_blood_type()
    {
        return q_blood_type;
    }

    /**
     * set 血条类型
     */
    public void setQ_blood_type(int q_blood_type)
    {
        this.q_blood_type = q_blood_type;
    }
}
