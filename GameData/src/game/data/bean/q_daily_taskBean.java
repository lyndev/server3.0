/**
 * Auto generated, do not edit it
 *
 * q_daily_task
 */
package game.data.bean;

public class q_daily_taskBean
{
    private int q_id; // 任务ID
    private int q_name; // 任务名称
    private String q_str; // 任务描述：策划使用
    private int q_describe; // 任务描述
    private int q_type; // 日常任务类型（参照Sheet2中的定义）
    private int q_unlock_level; // 解锁等级
    private int q_cond_num; // 任务需完成功能数量
    private int q_exp; // 完成获得召唤师经验
    private String q_resource; // 完成获得资源（类型_数量;类型_数量）1金币，2钻石，3体力，4技能点，5星魂，6荣誉值，7远征币，8灵魂碎片
    private String q_item; // 完成获得道具(ID_数量;ID_数量)
    private String q_icon; // 图标
    private int q_goto; // 是否有前往按钮（0，没有、1，有）

    /**
     * get 任务ID
     * @return
     */
    public int getQ_id()
    {
        return q_id;
    }

    /**
     * set 任务ID
     */
    public void setQ_id(int q_id)
    {
        this.q_id = q_id;
    }

    /**
     * get 任务名称
     * @return
     */
    public int getQ_name()
    {
        return q_name;
    }

    /**
     * set 任务名称
     */
    public void setQ_name(int q_name)
    {
        this.q_name = q_name;
    }

    /**
     * get 任务描述：策划使用
     * @return
     */
    public String getQ_str()
    {
        return q_str;
    }

    /**
     * set 任务描述：策划使用
     */
    public void setQ_str(String q_str)
    {
        this.q_str = q_str;
    }

    /**
     * get 任务描述
     * @return
     */
    public int getQ_describe()
    {
        return q_describe;
    }

    /**
     * set 任务描述
     */
    public void setQ_describe(int q_describe)
    {
        this.q_describe = q_describe;
    }

    /**
     * get 日常任务类型（参照Sheet2中的定义）
     * @return
     */
    public int getQ_type()
    {
        return q_type;
    }

    /**
     * set 日常任务类型（参照Sheet2中的定义）
     */
    public void setQ_type(int q_type)
    {
        this.q_type = q_type;
    }

    /**
     * get 解锁等级
     * @return
     */
    public int getQ_unlock_level()
    {
        return q_unlock_level;
    }

    /**
     * set 解锁等级
     */
    public void setQ_unlock_level(int q_unlock_level)
    {
        this.q_unlock_level = q_unlock_level;
    }

    /**
     * get 任务需完成功能数量
     * @return
     */
    public int getQ_cond_num()
    {
        return q_cond_num;
    }

    /**
     * set 任务需完成功能数量
     */
    public void setQ_cond_num(int q_cond_num)
    {
        this.q_cond_num = q_cond_num;
    }

    /**
     * get 完成获得召唤师经验
     * @return
     */
    public int getQ_exp()
    {
        return q_exp;
    }

    /**
     * set 完成获得召唤师经验
     */
    public void setQ_exp(int q_exp)
    {
        this.q_exp = q_exp;
    }

    /**
     * get 完成获得资源（类型_数量;类型_数量）1金币，2钻石，3体力，4技能点，5星魂，6荣誉值，7远征币，8灵魂碎片
     * @return
     */
    public String getQ_resource()
    {
        return q_resource;
    }

    /**
     * set 完成获得资源（类型_数量;类型_数量）1金币，2钻石，3体力，4技能点，5星魂，6荣誉值，7远征币，8灵魂碎片
     */
    public void setQ_resource(String q_resource)
    {
        this.q_resource = q_resource;
    }

    /**
     * get 完成获得道具(ID_数量;ID_数量)
     * @return
     */
    public String getQ_item()
    {
        return q_item;
    }

    /**
     * set 完成获得道具(ID_数量;ID_数量)
     */
    public void setQ_item(String q_item)
    {
        this.q_item = q_item;
    }

    /**
     * get 图标
     * @return
     */
    public String getQ_icon()
    {
        return q_icon;
    }

    /**
     * set 图标
     */
    public void setQ_icon(String q_icon)
    {
        this.q_icon = q_icon;
    }

    /**
     * get 是否有前往按钮（0，没有、1，有）
     * @return
     */
    public int getQ_goto()
    {
        return q_goto;
    }

    /**
     * set 是否有前往按钮（0，没有、1，有）
     */
    public void setQ_goto(int q_goto)
    {
        this.q_goto = q_goto;
    }
}
