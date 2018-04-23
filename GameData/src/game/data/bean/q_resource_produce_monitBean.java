/**
 * Auto generated, do not edit it
 *
 * q_resource_produce_monit
 */
package game.data.bean;

public class q_resource_produce_monitBean
{
    private int q_res_type; // 资源类型
    private int limit; // 正常产出上限
    private String description; // 描述（可空）

    /**
     * get 资源类型
     * @return
     */
    public int getQ_res_type()
    {
        return q_res_type;
    }

    /**
     * set 资源类型
     */
    public void setQ_res_type(int q_res_type)
    {
        this.q_res_type = q_res_type;
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
