/**
 * Auto generated, do not edit it
 *
 * q_node_effect
 */
package game.data.bean;

public class q_node_effectBean
{
    private int q_id; // ID
    private String q_desc; // 注释（策划使用）
    private String q_shake_trigger; // 震屏幕触发条件
    private String q_crush_trigger; // 碎屏触发条件
    private int q_crush_node; // 碎屏的节点（1-3）
    private String q_crush_spine; // 碎屏的spine动画
    private int q_crushed_map; // 碎屏后的地图ID
    private int q_spine_delay_time; // Spine时长(毫秒)
    private int q_role_pause_delay_time; // 人物暂停时间（毫秒）

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
     * get 注释（策划使用）
     * @return
     */
    public String getQ_desc()
    {
        return q_desc;
    }

    /**
     * set 注释（策划使用）
     */
    public void setQ_desc(String q_desc)
    {
        this.q_desc = q_desc;
    }

    /**
     * get 震屏幕触发条件
     * @return
     */
    public String getQ_shake_trigger()
    {
        return q_shake_trigger;
    }

    /**
     * set 震屏幕触发条件
     */
    public void setQ_shake_trigger(String q_shake_trigger)
    {
        this.q_shake_trigger = q_shake_trigger;
    }

    /**
     * get 碎屏触发条件
     * @return
     */
    public String getQ_crush_trigger()
    {
        return q_crush_trigger;
    }

    /**
     * set 碎屏触发条件
     */
    public void setQ_crush_trigger(String q_crush_trigger)
    {
        this.q_crush_trigger = q_crush_trigger;
    }

    /**
     * get 碎屏的节点（1-3）
     * @return
     */
    public int getQ_crush_node()
    {
        return q_crush_node;
    }

    /**
     * set 碎屏的节点（1-3）
     */
    public void setQ_crush_node(int q_crush_node)
    {
        this.q_crush_node = q_crush_node;
    }

    /**
     * get 碎屏的spine动画
     * @return
     */
    public String getQ_crush_spine()
    {
        return q_crush_spine;
    }

    /**
     * set 碎屏的spine动画
     */
    public void setQ_crush_spine(String q_crush_spine)
    {
        this.q_crush_spine = q_crush_spine;
    }

    /**
     * get 碎屏后的地图ID
     * @return
     */
    public int getQ_crushed_map()
    {
        return q_crushed_map;
    }

    /**
     * set 碎屏后的地图ID
     */
    public void setQ_crushed_map(int q_crushed_map)
    {
        this.q_crushed_map = q_crushed_map;
    }

    /**
     * get Spine时长(毫秒)
     * @return
     */
    public int getQ_spine_delay_time()
    {
        return q_spine_delay_time;
    }

    /**
     * set Spine时长(毫秒)
     */
    public void setQ_spine_delay_time(int q_spine_delay_time)
    {
        this.q_spine_delay_time = q_spine_delay_time;
    }

    /**
     * get 人物暂停时间（毫秒）
     * @return
     */
    public int getQ_role_pause_delay_time()
    {
        return q_role_pause_delay_time;
    }

    /**
     * set 人物暂停时间（毫秒）
     */
    public void setQ_role_pause_delay_time(int q_role_pause_delay_time)
    {
        this.q_role_pause_delay_time = q_role_pause_delay_time;
    }
}
