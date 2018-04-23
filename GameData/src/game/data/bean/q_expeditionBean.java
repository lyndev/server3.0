/**
 * Auto generated, do not edit it
 *
 * q_expedition
 */
package game.data.bean;

public class q_expeditionBean
{
    private int q_level_order_id; // 关卡顺序ID
    private int q_pvp_rate; // 选中PVP的概率
    private String q_ranking_offset; // 取排行榜时的名次偏移（左偏移量_右偏移量）
    private int q_difficulty_coefficient; // 难度系数（百分比）
    private String q_box_id; // 宝箱（就使用关卡掉落宝箱）ID_权重；权重概率换算和为100%
    private int q_rank_num; // 从背包里随机道具的次数
    private int q_coin; // 金币奖励数量
    private String q_soul_coin; // 灵魂碎片
    private int q_exped_coin; // 远征币

    /**
     * get 关卡顺序ID
     * @return
     */
    public int getQ_level_order_id()
    {
        return q_level_order_id;
    }

    /**
     * set 关卡顺序ID
     */
    public void setQ_level_order_id(int q_level_order_id)
    {
        this.q_level_order_id = q_level_order_id;
    }

    /**
     * get 选中PVP的概率
     * @return
     */
    public int getQ_pvp_rate()
    {
        return q_pvp_rate;
    }

    /**
     * set 选中PVP的概率
     */
    public void setQ_pvp_rate(int q_pvp_rate)
    {
        this.q_pvp_rate = q_pvp_rate;
    }

    /**
     * get 取排行榜时的名次偏移（左偏移量_右偏移量）
     * @return
     */
    public String getQ_ranking_offset()
    {
        return q_ranking_offset;
    }

    /**
     * set 取排行榜时的名次偏移（左偏移量_右偏移量）
     */
    public void setQ_ranking_offset(String q_ranking_offset)
    {
        this.q_ranking_offset = q_ranking_offset;
    }

    /**
     * get 难度系数（百分比）
     * @return
     */
    public int getQ_difficulty_coefficient()
    {
        return q_difficulty_coefficient;
    }

    /**
     * set 难度系数（百分比）
     */
    public void setQ_difficulty_coefficient(int q_difficulty_coefficient)
    {
        this.q_difficulty_coefficient = q_difficulty_coefficient;
    }

    /**
     * get 宝箱（就使用关卡掉落宝箱）ID_权重；权重概率换算和为100%
     * @return
     */
    public String getQ_box_id()
    {
        return q_box_id;
    }

    /**
     * set 宝箱（就使用关卡掉落宝箱）ID_权重；权重概率换算和为100%
     */
    public void setQ_box_id(String q_box_id)
    {
        this.q_box_id = q_box_id;
    }

    /**
     * get 从背包里随机道具的次数
     * @return
     */
    public int getQ_rank_num()
    {
        return q_rank_num;
    }

    /**
     * set 从背包里随机道具的次数
     */
    public void setQ_rank_num(int q_rank_num)
    {
        this.q_rank_num = q_rank_num;
    }

    /**
     * get 金币奖励数量
     * @return
     */
    public int getQ_coin()
    {
        return q_coin;
    }

    /**
     * set 金币奖励数量
     */
    public void setQ_coin(int q_coin)
    {
        this.q_coin = q_coin;
    }

    /**
     * get 灵魂碎片
     * @return
     */
    public String getQ_soul_coin()
    {
        return q_soul_coin;
    }

    /**
     * set 灵魂碎片
     */
    public void setQ_soul_coin(String q_soul_coin)
    {
        this.q_soul_coin = q_soul_coin;
    }

    /**
     * get 远征币
     * @return
     */
    public int getQ_exped_coin()
    {
        return q_exped_coin;
    }

    /**
     * set 远征币
     */
    public void setQ_exped_coin(int q_exped_coin)
    {
        this.q_exped_coin = q_exped_coin;
    }
}
