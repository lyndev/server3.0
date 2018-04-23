package game.server.logic.rebate.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.core.util.ArrayUtils;
//import game.data.bean.q_rebateRewardBean;
import game.message.RebateMessage;
import game.server.logic.item.bean.Item;
import game.server.logic.support.BeanFactory;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.IJsonConverter;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * 一次活动中的一档奖励
 *
 * @author ZouZhaopeng
 */
public class RebateRewardBean implements IJsonConverter
{
    private static final Logger LOG = Logger.getLogger(RebateRewardBean.class);

    private int id;
    //是否已领取
    private boolean alreadyGet;

    public boolean isAlreadyGet()
    {
        return alreadyGet;
    }

    public void setAlreadyGet(boolean alreadyGet)
    {
        this.alreadyGet = alreadyGet;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }
    
//    public q_rebateRewardBean getBean()
//    {
//        q_rebateRewardBean bean = BeanTemplet.getRebateRewardBean(id);
//        if (bean == null)
//        {
//            LOG.error("返利活动奖励配置缺失, 奖励id = " + id);
////            throw new NullPointerException("返利活动奖励配置缺失, 奖励id = " + id);
//        }
//        return bean;
//    }

    /**
     * 该档奖励配置可领取奖励的条件(充值/消费的金额)
     * 
     * @return 
     */
//    public int getAmount()
//    {
//        if (getBean() == null)
//        {
//            return 0;
//        }
//        return getBean().getQ_target();
//    }
//
//    public int getMoney()
//    {
//        if (getBean() == null)
//        {
//            return 0;
//        }
//        return getBean().getQ_awardmoney();
//    }
//
//    public int getBullion()
//    {
//        if (getBean() == null)
//        {
//            return 0;
//        }
//        return getBean().getQ_awardgold();
//    }
//
//    public List<Item> getItems()
//    {
//        if (getBean() == null)
//        {
//            return parseItem(null);
//        }
//        return parseItem(getBean().getQ_items());
//    }

    public List<Item> parseItem(String config)
    {
        List<Item> list = new ArrayList<>();
        if (config == null)
        {
            LOG.error("返利活动奖励配置中物品配置为空, 奖励id = " + id);
            return list;
        }

        String[] splited = config.split(";");
        if (splited == null)
        {
            LOG.error("返利活动奖励配置中物品配置为空, 奖励id = " + id);
            return list;
        }

        for (String each : splited)
        {
            int[] id_num = ArrayUtils.parseInt(each.split("_"));
            if (id_num != null && id_num.length >= 2)
            {
                Item item = BeanFactory.createItem(id_num[0], id_num[1]);
                if (item == null)
                {
                    LOG.error("创建物品失败, 物品id: " + id_num[0]);
                }
                else
                {
                    list.add(item);
                }
            }
            else
            {
                LOG.error("返利活动奖励配置中, 物品配置有误: " + each);
            }
        }
        return new ArrayList<>();
    }
    
    
    public RebateMessage.RebateReward.Builder getMessageBuilder()
    {
        RebateMessage.RebateReward.Builder builder = RebateMessage.RebateReward.newBuilder();
        builder.setId(id);
        builder.setAlreadyGet(alreadyGet);
        return builder;
    }

    @Override
    public JSON toJson()
    {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("alreadyGet", alreadyGet);
        return obj;
    }

    @Override
    public void fromJson(JSON json)
    {
        if (json == null)
        {
            throw new NullPointerException("RebateRewardBean.fromJson: 参数json == null");
        }
        if (!(json instanceof JSONObject))
        {
            throw new IllegalArgumentException("RebateRewardBean.fromJson invoke with unexpected parameter type: " + json.getClass().getSimpleName());
        }
        JSONObject obj = (JSONObject)json;
        id = obj.getIntValue("id");
        alreadyGet = obj.getBooleanValue("alreadyGet");
    }
    
}
