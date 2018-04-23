package game.server.logic.keys.bean;

import game.server.logic.item.bean.Item;
import java.util.List;

/**
 *
 * <b>激活码/兑换码的使用结果.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class KeyUseResult
{
    private String key; // 激活码/兑换码的唯一标识

    private String batchCode; // 激活码/兑换码发行的批次编号，两个相同编号的表示属于同一种码

    private long endTime; // 激活码/兑换码的有效时间

    private long useTime; // 激活码/兑换码的使用时间

    private List<Item> props; // 激活码/兑换码绑定的道具信息

    private String playerId; // 使用者的玩家角色ID

    private int status; // 使用状态
    
    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getBatchCode()
    {
        return batchCode;
    }

    public void setBatchCode(String batchCode)
    {
        this.batchCode = batchCode;
    }

    public long getEndTime()
    {
        return endTime;
    }

    public void setEndTime(long endTime)
    {
        this.endTime = endTime;
    }

    public long getUseTime()
    {
        return useTime;
    }

    public void setUseTime(long useTime)
    {
        this.useTime = useTime;
    }

    public List<Item> getProps()
    {
        return props;
    }

    public void setProps(List<Item> props)
    {
        this.props = props;
    }

    public String getPlayerId()
    {
        return playerId;
    }

    public void setPlayerId(String playerId)
    {
        this.playerId = playerId;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

}
