/**
 * Auto generated, do not edit it
 *
 * q_gem_Lottery
 */
package game.data.bean;

public class q_gem_LotteryBean
{
    private int q_id; // ID
    private String q_desc; // 注释（策划使用）
    private int q_rewardId; // 抽奖奖励Id：itemId或cardId
    private int q_reward_num; // 奖励数量
    private int q_reward_type; // 1卡牌，2普通物品
    private int q_reward_level; // 十连抽[9+1]:1普通奖励级别，2高级奖励级别
    private int q_rate; // 物品的概率

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
     * get 抽奖奖励Id：itemId或cardId
     * @return
     */
    public int getQ_rewardId()
    {
        return q_rewardId;
    }

    /**
     * set 抽奖奖励Id：itemId或cardId
     */
    public void setQ_rewardId(int q_rewardId)
    {
        this.q_rewardId = q_rewardId;
    }

    /**
     * get 奖励数量
     * @return
     */
    public int getQ_reward_num()
    {
        return q_reward_num;
    }

    /**
     * set 奖励数量
     */
    public void setQ_reward_num(int q_reward_num)
    {
        this.q_reward_num = q_reward_num;
    }

    /**
     * get 1卡牌，2普通物品
     * @return
     */
    public int getQ_reward_type()
    {
        return q_reward_type;
    }

    /**
     * set 1卡牌，2普通物品
     */
    public void setQ_reward_type(int q_reward_type)
    {
        this.q_reward_type = q_reward_type;
    }

    /**
     * get 十连抽[9+1]:1普通奖励级别，2高级奖励级别
     * @return
     */
    public int getQ_reward_level()
    {
        return q_reward_level;
    }

    /**
     * set 十连抽[9+1]:1普通奖励级别，2高级奖励级别
     */
    public void setQ_reward_level(int q_reward_level)
    {
        this.q_reward_level = q_reward_level;
    }

    /**
     * get 物品的概率
     * @return
     */
    public int getQ_rate()
    {
        return q_rate;
    }

    /**
     * set 物品的概率
     */
    public void setQ_rate(int q_rate)
    {
        this.q_rate = q_rate;
    }
}
