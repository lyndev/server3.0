/**
 * Auto generated, do not edit it
 *
 * q_item_produce_monit
 */
package game.data.bean;

public class q_item_produce_monitBean
{
    private int q_item_id; // 物品/装备ID
    private int limit; // 正常产出上限
    private String description; // 描述（可空）

    /**
     * get 物品/装备ID
     * @return
     */
    public int getQ_item_id()
    {
        return q_item_id;
    }

    /**
     * set 物品/装备ID
     */
    public void setQ_item_id(int q_item_id)
    {
        this.q_item_id = q_item_id;
    }

    /**
     * get 正常产出上限
     * @return
     */
    public int getLimit()
    {
        return limit;
    }

    /**
     * set 正常产出上限
     */
    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    /**
     * get 描述（可空）
     * @return
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * set 描述（可空）
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
}
