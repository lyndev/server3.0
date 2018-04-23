/**
 * Auto generated, do not edit it
 *
 * q_arena_ranking_reward
 */
package game.data.bean;

public class q_arena_ranking_rewardBean
{
    private int q_rank; // 排名(最后一个数配置是以后无限大)
    private int q_reward_gold; // 奖励（金币）
    private int q_reward_gem; // 奖励（钻石）
    private int q_reward_honor; // 奖励（荣誉值）
    private String q_reward_item; // 奖励（道具）：Id_数量;Id_数量

    /**
     * get 排名(最后一个数配置是以后无限大)
     * @return
     */
    public int getQ_rank()
    {
        return q_rank;
    }

    /**
     * set 排名(最后一个数配置是以后无限大)
     */
    public void setQ_rank(int q_rank)
    {
        this.q_rank = q_rank;
    }

    /**
     * get 奖励（金币）
     * @return
     */
    public int getQ_reward_gold()
    {
        return q_reward_gold;
    }

    /**
     * set 奖励（金币）
     */
    public void setQ_reward_gold(int q_reward_gold)
    {
        this.q_reward_gold = q_reward_gold;
    }

    /**
     * get 奖励（钻石）
     * @return
     */
    public int getQ_reward_gem()
    {
        return q_reward_gem;
    }

    /**
     * set 奖励（钻石）
     */
    public void setQ_reward_gem(int q_reward_gem)
    {
        this.q_reward_gem = q_reward_gem;
    }

    /**
     * get 奖励（荣誉值）
     * @return
     */
    public int getQ_reward_honor()
    {
        return q_reward_honor;
    }

    /**
     * set 奖励（荣誉值）
     */
    public void setQ_reward_honor(int q_reward_honor)
    {
        this.q_reward_honor = q_reward_honor;
    }

    /**
     * get 奖励（道具）：Id_数量;Id_数量
     * @return
     */
    public String getQ_reward_item()
    {
        return q_reward_item;
    }

    /**
     * set 奖励（道具）：Id_数量;Id_数量
     */
    public void setQ_reward_item(String q_reward_item)
    {
        this.q_reward_item = q_reward_item;
    }
}
