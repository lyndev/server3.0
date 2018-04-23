/**
 * Auto generated, do not edit it
 *
 * q_item
 */
package game.data.bean;

public class q_itemBean
{
    private int q_id; // 物品ID
    private int q_name; // 物品名称
    private String q_name_plan; // 物品名称（策划用）
    private int q_tiny_icon; // 物品图标编号
    private String q_tiny_effect; // 物品光效编号
    private int q_type; // 物品類型：1礼包 (消耗)，2宝箱 (消耗)，3钥匙 (消耗)，4经验药水(消耗)，5英雄魂石(魂石)，6装备(装备)，7道具，8卷轴（卷轴）,9:（宝石），10：装备碎片,11,卷轴碎片
    private int q_goods_star; // 物品星级
    private int q_default; // 物品品质
    private int q_level; // 使用等级需求
    private int q_cardlevel; // 使用卡牌等级需求
    private String q_goods; // 使用需求物品（ID1_数量；ID2_数量）
    private int q_can_synthetic; // 是否可合成
    private int q_can_use; // 是否可用
    private int q_role_object; // 作用对象 0无对象 1玩家 2卡牌
    private String q_add_resources; // 使用后可以获得资源 效果类型[1-金币，2-钻石，3-体力，4-技能点，5-星魂，6-荣誉值，7-远征币，8-灵魂碎片]_数值;类型_数量
    private String q_add_items; // 使用后可以获得物品/装备，表达式：物品/装备ID + _+ 数量，多个以分号间隔。如：10001_10;10002_10
    private int q_ui; // 使用打开的对应界面ID
    private int q_max; // 物品叠加数量上限
    private int q_add_exp; // 使用增加卡牌经验
    private int q_sell_price; // 卖出单价（金币）
    private int q_sell_confirm; // 卖出时是否弹出确认框(0不弹出，1弹出)
    private String q_get_path; // 获得途径信息（填写关卡id，多个关卡id之间用‘_’隔开用于前端显示及操作，填0或不填表示没有该信息）[-1，可从神将招募中获得；-2，可从荣誉商店兑换获得；-3，可从番外战场获得；-4，首充礼包赠送；-5，可从神秘商店获得；-6，可从精英关卡获得；-7，所有主线关卡均有掉落；-8，可用3颗初品魂石合成获得/n初品魂石在所有主线关卡均有掉落；-9，无掉落途径；-10，无尽之塔掉落
    private String q_compound_list; // 合成此装备需要的其他装备或者碎片的Id和数量:id_数量；id_数量
    private int q_consumeGold; // 合成装备消耗金币数量
    private int q_describe; // 物品描述，用于前端tip显示(本處需要支援html標記)
    private int q_script; // 使用对应的宝箱ID
    private int q_daily_num; // 单日物品使用上限
    private String q_usetime_limit; // 使用期限年，月，日，时，分;年，月，日，时，分，
    private int q_log; // 是否记录产出与操作日志(0不记录；1记录)
    private int q_equipment_position; // 部位（此列仅用于装备表，物品表不填）
    private int q_show; // 是否在背包显示（0不显示；1显示）

    /**
     * get 物品ID
     * @return
     */
    public int getQ_id()
    {
        return q_id;
    }

    /**
     * set 物品ID
     */
    public void setQ_id(int q_id)
    {
        this.q_id = q_id;
    }

    /**
     * get 物品名称
     * @return
     */
    public int getQ_name()
    {
        return q_name;
    }

    /**
     * set 物品名称
     */
    public void setQ_name(int q_name)
    {
        this.q_name = q_name;
    }

    /**
     * get 物品名称（策划用）
     * @return
     */
    public String getQ_name_plan()
    {
        return q_name_plan;
    }

    /**
     * set 物品名称（策划用）
     */
    public void setQ_name_plan(String q_name_plan)
    {
        this.q_name_plan = q_name_plan;
    }

    /**
     * get 物品图标编号
     * @return
     */
    public int getQ_tiny_icon()
    {
        return q_tiny_icon;
    }

    /**
     * set 物品图标编号
     */
    public void setQ_tiny_icon(int q_tiny_icon)
    {
        this.q_tiny_icon = q_tiny_icon;
    }

    /**
     * get 物品光效编号
     * @return
     */
    public String getQ_tiny_effect()
    {
        return q_tiny_effect;
    }

    /**
     * set 物品光效编号
     */
    public void setQ_tiny_effect(String q_tiny_effect)
    {
        this.q_tiny_effect = q_tiny_effect;
    }

    /**
     * get 物品類型：1礼包 (消耗)，2宝箱 (消耗)，3钥匙 (消耗)，4经验药水(消耗)，5英雄魂石(魂石)，6装备(装备)，7道具，8卷轴（卷轴）,9:（宝石），10：装备碎片,11,卷轴碎片
     * @return
     */
    public int getQ_type()
    {
        return q_type;
    }

    /**
     * set 物品類型：1礼包 (消耗)，2宝箱 (消耗)，3钥匙 (消耗)，4经验药水(消耗)，5英雄魂石(魂石)，6装备(装备)，7道具，8卷轴（卷轴）,9:（宝石），10：装备碎片,11,卷轴碎片
     */
    public void setQ_type(int q_type)
    {
        this.q_type = q_type;
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
     * get 物品品质
     * @return
     */
    public int getQ_default()
    {
        return q_default;
    }

    /**
     * set 物品品质
     */
    public void setQ_default(int q_default)
    {
        this.q_default = q_default;
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
     * get 使用需求物品（ID1_数量；ID2_数量）
     * @return
     */
    public String getQ_goods()
    {
        return q_goods;
    }

    /**
     * set 使用需求物品（ID1_数量；ID2_数量）
     */
    public void setQ_goods(String q_goods)
    {
        this.q_goods = q_goods;
    }

    /**
     * get 是否可合成
     * @return
     */
    public int getQ_can_synthetic()
    {
        return q_can_synthetic;
    }

    /**
     * set 是否可合成
     */
    public void setQ_can_synthetic(int q_can_synthetic)
    {
        this.q_can_synthetic = q_can_synthetic;
    }

    /**
     * get 是否可用
     * @return
     */
    public int getQ_can_use()
    {
        return q_can_use;
    }

    /**
     * set 是否可用
     */
    public void setQ_can_use(int q_can_use)
    {
        this.q_can_use = q_can_use;
    }

    /**
     * get 作用对象 0无对象 1玩家 2卡牌
     * @return
     */
    public int getQ_role_object()
    {
        return q_role_object;
    }

    /**
     * set 作用对象 0无对象 1玩家 2卡牌
     */
    public void setQ_role_object(int q_role_object)
    {
        this.q_role_object = q_role_object;
    }

    /**
     * get 使用后可以获得资源 效果类型[1-金币，2-钻石，3-体力，4-技能点，5-星魂，6-荣誉值，7-远征币，8-灵魂碎片]_数值;类型_数量
     * @return
     */
    public String getQ_add_resources()
    {
        return q_add_resources;
    }

    /**
     * set 使用后可以获得资源 效果类型[1-金币，2-钻石，3-体力，4-技能点，5-星魂，6-荣誉值，7-远征币，8-灵魂碎片]_数值;类型_数量
     */
    public void setQ_add_resources(String q_add_resources)
    {
        this.q_add_resources = q_add_resources;
    }

    /**
     * get 使用后可以获得物品/装备，表达式：物品/装备ID + _+ 数量，多个以分号间隔。如：10001_10;10002_10
     * @return
     */
    public String getQ_add_items()
    {
        return q_add_items;
    }

    /**
     * set 使用后可以获得物品/装备，表达式：物品/装备ID + _+ 数量，多个以分号间隔。如：10001_10;10002_10
     */
    public void setQ_add_items(String q_add_items)
    {
        this.q_add_items = q_add_items;
    }

    /**
     * get 使用打开的对应界面ID
     * @return
     */
    public int getQ_ui()
    {
        return q_ui;
    }

    /**
     * set 使用打开的对应界面ID
     */
    public void setQ_ui(int q_ui)
    {
        this.q_ui = q_ui;
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
     * get 使用增加卡牌经验
     * @return
     */
    public int getQ_add_exp()
    {
        return q_add_exp;
    }

    /**
     * set 使用增加卡牌经验
     */
    public void setQ_add_exp(int q_add_exp)
    {
        this.q_add_exp = q_add_exp;
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
     * get 物品描述，用于前端tip显示(本處需要支援html標記)
     * @return
     */
    public int getQ_describe()
    {
        return q_describe;
    }

    /**
     * set 物品描述，用于前端tip显示(本處需要支援html標記)
     */
    public void setQ_describe(int q_describe)
    {
        this.q_describe = q_describe;
    }

    /**
     * get 使用对应的宝箱ID
     * @return
     */
    public int getQ_script()
    {
        return q_script;
    }

    /**
     * set 使用对应的宝箱ID
     */
    public void setQ_script(int q_script)
    {
        this.q_script = q_script;
    }

    /**
     * get 单日物品使用上限
     * @return
     */
    public int getQ_daily_num()
    {
        return q_daily_num;
    }

    /**
     * set 单日物品使用上限
     */
    public void setQ_daily_num(int q_daily_num)
    {
        this.q_daily_num = q_daily_num;
    }

    /**
     * get 使用期限年，月，日，时，分;年，月，日，时，分，
     * @return
     */
    public String getQ_usetime_limit()
    {
        return q_usetime_limit;
    }

    /**
     * set 使用期限年，月，日，时，分;年，月，日，时，分，
     */
    public void setQ_usetime_limit(String q_usetime_limit)
    {
        this.q_usetime_limit = q_usetime_limit;
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
     * get 部位（此列仅用于装备表，物品表不填）
     * @return
     */
    public int getQ_equipment_position()
    {
        return q_equipment_position;
    }

    /**
     * set 部位（此列仅用于装备表，物品表不填）
     */
    public void setQ_equipment_position(int q_equipment_position)
    {
        this.q_equipment_position = q_equipment_position;
    }

    /**
     * get 是否在背包显示（0不显示；1显示）
     * @return
     */
    public int getQ_show()
    {
        return q_show;
    }

    /**
     * set 是否在背包显示（0不显示；1显示）
     */
    public void setQ_show(int q_show)
    {
        this.q_show = q_show;
    }
}
