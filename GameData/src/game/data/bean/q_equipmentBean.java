/**
 * Auto generated, do not edit it
 *
 * q_equipment
 */
package game.data.bean;

public class q_equipmentBean
{
    private int q_id; // 装备ID
    private int q_name; // 名称
    private String q_name1; // 名称（策划用）
    private int q_tiny_icon; // ICON
    private int q_describe; // 装备描述
    private String q_describe1; // 装备描述(策划用）
    private int q_default; // 品质(白绿蓝紫橙，12345)
    private int q_goods_star; // 物品星级
    private int q_max; // 物品叠加数量上限
    private int q_level; // 使用等级需求
    private int q_cardlevel; // 使用卡牌等级需求
    private int q_sell_confirm; // 卖出时是否弹出确认框(0不弹出，1弹出)
    private int q_sell_price; // 卖出单价（金币）
    private int q_log; // 是否记录产出与操作日志(0不记录；1记录)
    private int q_show; // 是否在背包显示（0你显示；1显示）
    private String q_get_path; // 获得途径信息（填写关卡id，多个关卡id之间用‘_’隔开用于前端显示及操作，填0或不填表示没有该信息）[-1，可从神将招募中获得；-2，可从荣誉商店兑换获得；-3，可从番外战场获得；-4，首充礼包赠送；-5，可从神秘商店获得；-6，可从精英关卡获得；-7，所有主线关卡均有掉落；-8，可用3颗初品魂石合成获得/n初品魂石在所有主线关卡均有掉落；-9，无掉落途径；-10，无尽之塔掉落
    private String q_compound_list; // 合成此装备需要的其他装备或者碎片的Id和数量:id_数量；id_数量
    private int q_consumeGold; // 合成装备消耗金币数量
    private int q_str; // 力量:37
    private int q_agi; // 敏捷:38
    private int q_mid; // 智力:39
    private int q_hp; // 生命值：2
    private int q_pAtk; // 物理攻击：23
    private int q_mAtk; // 魔法攻击:30
    private int q_pDef; // 物理防御:4
    private int q_mDef; // 魔法防御：3
    private int q_pCrit; // 物理暴击值:24
    private int q_mCrit; // 魔法暴击:31
    private int q_pEvade; // 闪避值：9
    private int q_pDefPierce; // 护甲穿透:29
    private int q_mDefPierce; // 魔抗穿透:34
    private int q_hp_revert; // 生命值回复:42
    private int q_energy_revert; // 能量回复:43
    private int q_Drains; // 吸血值:17
    private int q_critDmg; // 暴击伤害:19
    private int q_dmgDerate; // 所有伤害减免值：5
    private int q_type; // 类型

    /**
     * get 装备ID
     * @return
     */
    public int getQ_id()
    {
        return q_id;
    }

    /**
     * set 装备ID
     */
    public void setQ_id(int q_id)
    {
        this.q_id = q_id;
    }

    /**
     * get 名称
     * @return
     */
    public int getQ_name()
    {
        return q_name;
    }

    /**
     * set 名称
     */
    public void setQ_name(int q_name)
    {
        this.q_name = q_name;
    }

    /**
     * get 名称（策划用）
     * @return
     */
    public String getQ_name1()
    {
        return q_name1;
    }

    /**
     * set 名称（策划用）
     */
    public void setQ_name1(String q_name1)
    {
        this.q_name1 = q_name1;
    }

    /**
     * get ICON
     * @return
     */
    public int getQ_tiny_icon()
    {
        return q_tiny_icon;
    }

    /**
     * set ICON
     */
    public void setQ_tiny_icon(int q_tiny_icon)
    {
        this.q_tiny_icon = q_tiny_icon;
    }

    /**
     * get 装备描述
     * @return
     */
    public int getQ_describe()
    {
        return q_describe;
    }

    /**
     * set 装备描述
     */
    public void setQ_describe(int q_describe)
    {
        this.q_describe = q_describe;
    }

    /**
     * get 装备描述(策划用）
     * @return
     */
    public String getQ_describe1()
    {
        return q_describe1;
    }

    /**
     * set 装备描述(策划用）
     */
    public void setQ_describe1(String q_describe1)
    {
        this.q_describe1 = q_describe1;
    }

    /**
     * get 品质(白绿蓝紫橙，12345)
     * @return
     */
    public int getQ_default()
    {
        return q_default;
    }

    /**
     * set 品质(白绿蓝紫橙，12345)
     */
    public void setQ_default(int q_default)
    {
        this.q_default = q_default;
    }

    /**
     * get 物品星级
     * @return
     */
    public int getQ_goods_star()
    {
        return q_goods_star;
    }

    /**
     * set 物品星级
     */
    public void setQ_goods_star(int q_goods_star)
    {
        this.q_goods_star = q_goods_star;
    }

    /**
     * get 物品叠加数量上限
     * @return
     */
    public int getQ_max()
    {
        return q_max;
    }

    /**
     * set 物品叠加数量上限
     */
    public void setQ_max(int q_max)
    {
        this.q_max = q_max;
    }

    /**
     * get 使用等级需求
     * @return
     */
    public int getQ_level()
    {
        return q_level;
    }

    /**
     * set 使用等级需求
     */
    public void setQ_level(int q_level)
    {
        this.q_level = q_level;
    }

    /**
     * get 使用卡牌等级需求
     * @return
     */
    public int getQ_cardlevel()
    {
        return q_cardlevel;
    }

    /**
     * set 使用卡牌等级需求
     */
    public void setQ_cardlevel(int q_cardlevel)
    {
        this.q_cardlevel = q_cardlevel;
    }

    /**
     * get 卖出时是否弹出确认框(0不弹出，1弹出)
     * @return
     */
    public int getQ_sell_confirm()
    {
        return q_sell_confirm;
    }

    /**
     * set 卖出时是否弹出确认框(0不弹出，1弹出)
     */
    public void setQ_sell_confirm(int q_sell_confirm)
    {
        this.q_sell_confirm = q_sell_confirm;
    }

    /**
     * get 卖出单价（金币）
     * @return
     */
    public int getQ_sell_price()
    {
        return q_sell_price;
    }

    /**
     * set 卖出单价（金币）
     */
    public void setQ_sell_price(int q_sell_price)
    {
        this.q_sell_price = q_sell_price;
    }

    /**
     * get 是否记录产出与操作日志(0不记录；1记录)
     * @return
     */
    public int getQ_log()
    {
        return q_log;
    }

    /**
     * set 是否记录产出与操作日志(0不记录；1记录)
     */
    public void setQ_log(int q_log)
    {
        this.q_log = q_log;
    }

    /**
     * get 是否在背包显示（0你显示；1显示）
     * @return
     */
    public int getQ_show()
    {
        return q_show;
    }

    /**
     * set 是否在背包显示（0你显示；1显示）
     */
    public void setQ_show(int q_show)
    {
        this.q_show = q_show;
    }

    /**
     * get 获得途径信息（填写关卡id，多个关卡id之间用‘_’隔开用于前端显示及操作，填0或不填表示没有该信息）[-1，可从神将招募中获得；-2，可从荣誉商店兑换获得；-3，可从番外战场获得；-4，首充礼包赠送；-5，可从神秘商店获得；-6，可从精英关卡获得；-7，所有主线关卡均有掉落；-8，可用3颗初品魂石合成获得/n初品魂石在所有主线关卡均有掉落；-9，无掉落途径；-10，无尽之塔掉落
     * @return
     */
    public String getQ_get_path()
    {
        return q_get_path;
    }

    /**
     * set 获得途径信息（填写关卡id，多个关卡id之间用‘_’隔开用于前端显示及操作，填0或不填表示没有该信息）[-1，可从神将招募中获得；-2，可从荣誉商店兑换获得；-3，可从番外战场获得；-4，首充礼包赠送；-5，可从神秘商店获得；-6，可从精英关卡获得；-7，所有主线关卡均有掉落；-8，可用3颗初品魂石合成获得/n初品魂石在所有主线关卡均有掉落；-9，无掉落途径；-10，无尽之塔掉落
     */
    public void setQ_get_path(String q_get_path)
    {
        this.q_get_path = q_get_path;
    }

    /**
     * get 合成此装备需要的其他装备或者碎片的Id和数量:id_数量；id_数量
     * @return
     */
    public String getQ_compound_list()
    {
        return q_compound_list;
    }

    /**
     * set 合成此装备需要的其他装备或者碎片的Id和数量:id_数量；id_数量
     */
    public void setQ_compound_list(String q_compound_list)
    {
        this.q_compound_list = q_compound_list;
    }

    /**
     * get 合成装备消耗金币数量
     * @return
     */
    public int getQ_consumeGold()
    {
        return q_consumeGold;
    }

    /**
     * set 合成装备消耗金币数量
     */
    public void setQ_consumeGold(int q_consumeGold)
    {
        this.q_consumeGold = q_consumeGold;
    }

    /**
     * get 力量:37
     * @return
     */
    public int getQ_str()
    {
        return q_str;
    }

    /**
     * set 力量:37
     */
    public void setQ_str(int q_str)
    {
        this.q_str = q_str;
    }

    /**
     * get 敏捷:38
     * @return
     */
    public int getQ_agi()
    {
        return q_agi;
    }

    /**
     * set 敏捷:38
     */
    public void setQ_agi(int q_agi)
    {
        this.q_agi = q_agi;
    }

    /**
     * get 智力:39
     * @return
     */
    public int getQ_mid()
    {
        return q_mid;
    }

    /**
     * set 智力:39
     */
    public void setQ_mid(int q_mid)
    {
        this.q_mid = q_mid;
    }

    /**
     * get 生命值：2
     * @return
     */
    public int getQ_hp()
    {
        return q_hp;
    }

    /**
     * set 生命值：2
     */
    public void setQ_hp(int q_hp)
    {
        this.q_hp = q_hp;
    }

    /**
     * get 物理攻击：23
     * @return
     */
    public int getQ_pAtk()
    {
        return q_pAtk;
    }

    /**
     * set 物理攻击：23
     */
    public void setQ_pAtk(int q_pAtk)
    {
        this.q_pAtk = q_pAtk;
    }

    /**
     * get 魔法攻击:30
     * @return
     */
    public int getQ_mAtk()
    {
        return q_mAtk;
    }

    /**
     * set 魔法攻击:30
     */
    public void setQ_mAtk(int q_mAtk)
    {
        this.q_mAtk = q_mAtk;
    }

    /**
     * get 物理防御:4
     * @return
     */
    public int getQ_pDef()
    {
        return q_pDef;
    }

    /**
     * set 物理防御:4
     */
    public void setQ_pDef(int q_pDef)
    {
        this.q_pDef = q_pDef;
    }

    /**
     * get 魔法防御：3
     * @return
     */
    public int getQ_mDef()
    {
        return q_mDef;
    }

    /**
     * set 魔法防御：3
     */
    public void setQ_mDef(int q_mDef)
    {
        this.q_mDef = q_mDef;
    }

    /**
     * get 物理暴击值:24
     * @return
     */
    public int getQ_pCrit()
    {
        return q_pCrit;
    }

    /**
     * set 物理暴击值:24
     */
    public void setQ_pCrit(int q_pCrit)
    {
        this.q_pCrit = q_pCrit;
    }

    /**
     * get 魔法暴击:31
     * @return
     */
    public int getQ_mCrit()
    {
        return q_mCrit;
    }

    /**
     * set 魔法暴击:31
     */
    public void setQ_mCrit(int q_mCrit)
    {
        this.q_mCrit = q_mCrit;
    }

    /**
     * get 闪避值：9
     * @return
     */
    public int getQ_pEvade()
    {
        return q_pEvade;
    }

    /**
     * set 闪避值：9
     */
    public void setQ_pEvade(int q_pEvade)
    {
        this.q_pEvade = q_pEvade;
    }

    /**
     * get 护甲穿透:29
     * @return
     */
    public int getQ_pDefPierce()
    {
        return q_pDefPierce;
    }

    /**
     * set 护甲穿透:29
     */
    public void setQ_pDefPierce(int q_pDefPierce)
    {
        this.q_pDefPierce = q_pDefPierce;
    }

    /**
     * get 魔抗穿透:34
     * @return
     */
    public int getQ_mDefPierce()
    {
        return q_mDefPierce;
    }

    /**
     * set 魔抗穿透:34
     */
    public void setQ_mDefPierce(int q_mDefPierce)
    {
        this.q_mDefPierce = q_mDefPierce;
    }

    /**
     * get 生命值回复:42
     * @return
     */
    public int getQ_hp_revert()
    {
        return q_hp_revert;
    }

    /**
     * set 生命值回复:42
     */
    public void setQ_hp_revert(int q_hp_revert)
    {
        this.q_hp_revert = q_hp_revert;
    }

    /**
     * get 能量回复:43
     * @return
     */
    public int getQ_energy_revert()
    {
        return q_energy_revert;
    }

    /**
     * set 能量回复:43
     */
    public void setQ_energy_revert(int q_energy_revert)
    {
        this.q_energy_revert = q_energy_revert;
    }

    /**
     * get 吸血值:17
     * @return
     */
    public int getQ_Drains()
    {
        return q_Drains;
    }

    /**
     * set 吸血值:17
     */
    public void setQ_Drains(int q_Drains)
    {
        this.q_Drains = q_Drains;
    }

    /**
     * get 暴击伤害:19
     * @return
     */
    public int getQ_critDmg()
    {
        return q_critDmg;
    }

    /**
     * set 暴击伤害:19
     */
    public void setQ_critDmg(int q_critDmg)
    {
        this.q_critDmg = q_critDmg;
    }

    /**
     * get 所有伤害减免值：5
     * @return
     */
    public int getQ_dmgDerate()
    {
        return q_dmgDerate;
    }

    /**
     * set 所有伤害减免值：5
     */
    public void setQ_dmgDerate(int q_dmgDerate)
    {
        this.q_dmgDerate = q_dmgDerate;
    }

    /**
     * get 类型
     * @return
     */
    public int getQ_type()
    {
        return q_type;
    }

    /**
     * set 类型
     */
    public void setQ_type(int q_type)
    {
        this.q_type = q_type;
    }
}
