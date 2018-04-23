/**
 * Auto generated, do not edit it
 *
 * q_expedition_node_group
 */
package game.data.bean;

public class q_expedition_node_groupBean
{
    private int q_player_level; // 玩家等级
    private String q_id_and_rate; // 关卡Id和选中率
    private int q_complete_reward; // 打完15个节点后的掉落包ID

    /**
     * get 玩家等级
     * @return
     */
    public int getQ_player_level()
    {
        return q_player_level;
    }

    /**
     * set 玩家等级
     */
    public void setQ_player_level(int q_player_level)
    {
        this.q_player_level = q_player_level;
    }

    /**
     * get 关卡Id和选中率
     * @return
     */
    public String getQ_id_and_rate()
    {
        return q_id_and_rate;
    }

    /**
     * set 关卡Id和选中率
     */
    public void setQ_id_and_rate(String q_id_and_rate)
    {
        this.q_id_and_rate = q_id_and_rate;
    }

    /**
     * get 打完15个节点后的掉落包ID
     * @return
     */
    public int getQ_complete_reward()
    {
        return q_complete_reward;
    }

    /**
     * set 打完15个节点后的掉落包ID
     */
    public void setQ_complete_reward(int q_complete_reward)
    {
        this.q_complete_reward = q_complete_reward;
    }
}
