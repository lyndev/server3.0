/**
 * Auto generated, do not edit it
 *
 * q_gem
 */
package game.data.bean;

public class q_gemBean
{
    private int q_id; // 宝石ID(300000_310000) ID规则参见附表
    private int q_name; // 名称
    private int q_tiny_icon; // ICON
    private int q_gem_color; // 宝石颜色（1红，2黄，3蓝，4绿）
    private int q_gem_attrType; // 附加属性类型（参考宝石颜色属性对应表）
    private int q_gem_level; // 宝石等级
    private int q_describe; // 宝石描述（语言表Id）
    private String q_describe1; // 宝石描述(策划用）
    private int q_default; // 品质(白绿蓝紫橙，12345)
    private int q_fragment; // 是否是碎片(0 =不是，1 是)
    private int q_max; // 物品叠加数量上限
    private int q_sell_confirm; // 卖出时是否弹出确认框(0不弹出，1弹出)
    private int q_sell_price; // 卖出单价（金币）
    private int q_log; // 是否记录产出与操作日志(0不记录；1记录)
    private int q_show; // 是否在背包显示（0不显示；1显示）
    private String q_get_path; // 获得途径信息（填写关卡id，多个关卡id之间用‘，’隔开用于前端显示及操作，填0或不填表示没有该信息）[-1，可从神将招募中获得；-2，可从荣誉商店兑换获得；-3，可从番外战场获得；-4，首充礼包赠送；-5，可从神秘商店获得；-6，可从精英关卡获得；-7，所有主线关卡均有掉落；-8，可用3颗初品魂石合成获得/n初品魂石在所有主线关卡均有掉落；-9，无掉落途径；-10，无尽之塔掉落
    private int q_consumeGold; // 合成宝石消耗金币数量
    private int q_addAttr; // 附加的属性值
    private int q_type; // 类型

    /**
     * get 宝石ID(300000_310000) ID规则参见附表
     * @return
     */
    public int getQ_id()
    {
        return q_id;
    }

    /**
     * set 宝石ID(300000_310000) ID规则参见附表
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
     * get 宝石颜色（1红，2黄，3蓝，4绿）
     * @return
     */
    public int getQ_gem_color()
    {
        return q_gem_color;
    }

    /**
     * set 宝石颜色（1红，2黄，3蓝，4绿）
     */
    public void setQ_gem_color(int q_gem_color)
    {
        this.q_gem_color = q_gem_color;
    }

    /**
     * get 附加属性类型（参考宝石颜色属性对应表）
     * @return
     */
    public int getQ_gem_attrType()
    {
        return q_gem_attrType;
    }

    /**
     * set 附加属性类型（参考宝石颜色属性对应表）
     */
    public void setQ_gem_attrType(int q_gem_attrType)
    {
        this.q_gem_attrType = q_gem_attrType;
    }

    /**
     * get 宝石等级
     * @return
     */
    public int getQ_gem_level()
    {
        return q_gem_level;
    }

    /**
     * set 宝石等级
     */
    public void setQ_gem_level(int q_gem_level)
    {
        this.q_gem_level = q_gem_level;
    }

    /**
     * get 宝石描述（语言表Id）
     * @return
     */
    public int getQ_describe()
    {
        return q_describe;
    }

    /**
     * set 宝石描述（语言表Id）
     */
    public void setQ_describe(int q_describe)
    {
        this.q_describe = q_describe;
    }

    /**
     * get 宝石描述(策划用）
     * @return
     */
    public String getQ_describe1()
    {
        return q_describe1;
    }

    /**
     * set 宝石描述(策划用）
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
     * get 是否是碎片(0 =不是，1 是)
     * @return
     */
    public int getQ_fragment()
    {
        return q_fragment;
    }

    /**
     * set 是否是碎片(0 =不是，1 是)
     */
    public void setQ_fragment(int q_fragment)
    {
        this.q_fragment = q_fragment;
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

    /**
     * get 获得途径信息（填写关卡id，多个关卡id之间用‘，’隔开用于前端显示及操作，填0或不填表示没有该信息）[-1，可从神将招募中获得；-2，可从荣誉商店兑换获得；-3，可从番外战场获得；-4，首充礼包赠送；-5，可从神秘商店获得；-6，可从精英关卡获得；-7，所有主线关卡均有掉落；-8，可用3颗初品魂石合成获得/n初品魂石在所有主线关卡均有掉落；-9，无掉落途径；-10，无尽之塔掉落
     * @return
     */
    public String getQ_get_path()
    {
        return q_get_path;
    }

    /**
     * set 获得途径信息（填写关卡id，多个关卡id之间用‘，’隔开用于前端显示及操作，填0或不填表示没有该信息）[-1，可从神将招募中获得；-2，可从荣誉商店兑换获得；-3，可从番外战场获得；-4，首充礼包赠送；-5，可从神秘商店获得；-6，可从精英关卡获得；-7，所有主线关卡均有掉落；-8，可用3颗初品魂石合成获得/n初品魂石在所有主线关卡均有掉落；-9，无掉落途径；-10，无尽之塔掉落
     */
    public void setQ_get_path(String q_get_path)
    {
        this.q_get_path = q_get_path;
    }

    /**
     * get 合成宝石消耗金币数量
     * @return
     */
    public int getQ_consumeGold()
    {
        return q_consumeGold;
    }

    /**
     * set 合成宝石消耗金币数量
     */
    public void setQ_consumeGold(int q_consumeGold)
    {
        this.q_consumeGold = q_consumeGold;
    }

    /**
     * get 附加的属性值
     * @return
     */
    public int getQ_addAttr()
    {
        return q_addAttr;
    }

    /**
     * set 附加的属性值
     */
    public void setQ_addAttr(int q_addAttr)
    {
        this.q_addAttr = q_addAttr;
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
