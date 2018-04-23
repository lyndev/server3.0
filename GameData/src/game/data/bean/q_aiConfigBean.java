/**
 * Auto generated, do not edit it
 *
 * q_aiConfig
 */
package game.data.bean;

public class q_aiConfigBean
{
    private int q_aiID; // AIID
    private String q_aiName; // AI模式名称
    private String q_targetDead; // 当前攻击目标死亡选怪
    private String q_notMove; // 不能移动时的AI选择
    private String q_default; // 默认AI规则

    /**
     * get AIID
     * @return
     */
    public int getQ_aiID()
    {
        return q_aiID;
    }

    /**
     * set AIID
     */
    public void setQ_aiID(int q_aiID)
    {
        this.q_aiID = q_aiID;
    }

    /**
     * get AI模式名称
     * @return
     */
    public String getQ_aiName()
    {
        return q_aiName;
    }

    /**
     * set AI模式名称
     */
    public void setQ_aiName(String q_aiName)
    {
        this.q_aiName = q_aiName;
    }

    /**
     * get 当前攻击目标死亡选怪
     * @return
     */
    public String getQ_targetDead()
    {
        return q_targetDead;
    }

    /**
     * set 当前攻击目标死亡选怪
     */
    public void setQ_targetDead(String q_targetDead)
    {
        this.q_targetDead = q_targetDead;
    }

    /**
     * get 不能移动时的AI选择
     * @return
     */
    public String getQ_notMove()
    {
        return q_notMove;
    }

    /**
     * set 不能移动时的AI选择
     */
    public void setQ_notMove(String q_notMove)
    {
        this.q_notMove = q_notMove;
    }

    /**
     * get 默认AI规则
     * @return
     */
    public String getQ_default()
    {
        return q_default;
    }

    /**
     * set 默认AI规则
     */
    public void setQ_default(String q_default)
    {
        this.q_default = q_default;
    }
}
