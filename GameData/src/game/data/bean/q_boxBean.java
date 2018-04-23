/**
 * Auto generated, do not edit it
 *
 * q_box
 */
package game.data.bean;

public class q_boxBean
{
    private int q_id; // 宝箱Id（和物品表中开启宝箱配置对应）
    private String q_desc; // 描述（策划使用）
    private String q_weight; // 权重（权重包ID_权重值_道具种类）

    /**
     * get 宝箱Id（和物品表中开启宝箱配置对应）
     * @return
     */
    public int getQ_id()
    {
        return q_id;
    }

    /**
     * set 宝箱Id（和物品表中开启宝箱配置对应）
     */
    public void setQ_id(int q_id)
    {
        this.q_id = q_id;
    }

    /**
     * get 描述（策划使用）
     * @return
     */
    public String getQ_desc()
    {
        return q_desc;
    }

    /**
     * set 描述（策划使用）
     */
    public void setQ_desc(String q_desc)
    {
        this.q_desc = q_desc;
    }

    /**
     * get 权重（权重包ID_权重值_道具种类）
     * @return
     */
    public String getQ_weight()
    {
        return q_weight;
    }

    /**
     * set 权重（权重包ID_权重值_道具种类）
     */
    public void setQ_weight(String q_weight)
    {
        this.q_weight = q_weight;
    }
}
