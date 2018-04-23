package game.server.logic.rebate.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import game.core.util.ArrayUtils;
//import game.data.bean.q_rebateBean;
import game.message.RebateMessage;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.IJsonConverter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * 一次回馈活动
 *
 * @author ZouZhaopeng
 */
public class RebateBean implements IJsonConverter
{
    private static final Logger LOG = Logger.getLogger(RebateBean.class);

    private int id;
    //当前已充值
    private int amount;

    private final Map<Integer, RebateRewardBean> map;

    public RebateBean()
    {
        amount = 0;
        map = new HashMap<>();
    }

    public void putRebateReward(RebateRewardBean bean)
    {
        map.put(bean.getId(), bean);
    }

    public RebateRewardBean getRebateReward(int id)
    {
        return map.get(id);
    }

    /**
     * 用配置初始化一个返利活动
     *
     * @param bean
     */
//    public void init(q_rebateBean bean)
//    {
//        if (bean == null)
//        {
//            LOG.error("返利活动配置为空");
//            return;
//        }
//
//        this.id = bean.getQ_ID();
//
//        //该活动有几个档的可领奖励
//        int[] list = parseReward(bean.getQ_awardID());
//        if (list == null)
//        {
//            LOG.error("活动奖励列表为空");
//            return;
//        }
//
//        for (int index : list)
//        {
//            RebateRewardBean rewardBean = new RebateRewardBean();
//            rewardBean.setId(index);
//            rewardBean.setAlreadyGet(false);
//            putRebateReward(rewardBean);
//        }
//    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * 本次活动已累计充值/消费的金额
     *
     * @return
     */
    public int getAmount()
    {
        return amount;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    /**
     * 指定日期是否开放
     *
     * @param date
     * @return
     */
    public boolean isOpening(long date)
    {
//        return date >= getStartTime() && date <= getEndTime();
        return false;
    }
    
    /**
     * 是否过期
     * 
     * @return 
     */
    public boolean isOverdue()
    {
//        return System.currentTimeMillis() > getEndTime();
        return false;
    }
    
//    public q_rebateBean getBean()
//    {
//        q_rebateBean bean = BeanTemplet.getRebateBean(id);
//        if (bean == null)
//        {
//            LOG.error("返利活动配置缺失, 活动id = " + id);
////            throw new NullPointerException("返利活动配置缺失, 活动id = " + id);
//        }
//        return bean;
//    }

    private long parseTime(String config)
    {
        if (config == null)
        {
            LOG.error("回馈活动配置有误, 时间为空, 活动id = " + id);
        }

        int[] time = ArrayUtils.parseInt(config.split("_"));
        if (time == null || time.length != 5)
        {
            LOG.error("回馈活动配置有误, 时间为空或者长度不对, 错误的配置为: " + config);
        }

        if (time[0] < 0
                || time[1] < 1 || time[1] > 12
                || time[2] < 1 || time[2] > 31
                || time[3] < 0 || time[3] > 23
                || time[4] < 0 || time[4] > 59)
        {
            LOG.error("回馈活动配置有误, 时间不合法, 错误的配置为: " + config);
        }

        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, time[0]);
        date.set(Calendar.MONTH, time[1] - 1); //注意, 日历中的月份是0-11
        date.set(Calendar.DAY_OF_MONTH, time[2]);
        date.set(Calendar.HOUR_OF_DAY, time[3]);
        date.set(Calendar.MINUTE, time[4]);

        return date.getTimeInMillis();
    }

    private int[] parseReward(String config)
    {
        int[] list = null;
        do
        {
            if (config == null)
                break;
            list = ArrayUtils.parseInt(config.split(";"));
        } while (false);
        return list;
    }

//    public long getStartTime()
//    {
//        if (getBean() == null)
//        {
//            return 0L;
//        }
//        return parseTime(getBean().getQ_startTime());
//    }
//
//    public long getEndTime()
//    {
//        if (getBean() == null)
//        {
//            return 0L;
//        }
//        return parseTime(getBean().getQ_endTime());
//    }
//
//    public int getType()
//    {
//        if (getBean() == null)
//        {
//            return -1;
//        }
//        return getBean().getQ_Type();
//    }
//    
//    public int getLevel()
//    {
//        if (getBean() == null)
//        {
//            return 0;
//        }
//        return getBean().getQ_level();
//    }
//    
//    public int getVip()
//    {
//        if (getBean() == null)
//        {
//            return 0;
//        }
//        return getBean().getQ_vip();
//    }

    public RebateMessage.Rebate.Builder getMessageBuilder()
    {
//        RebateMessage.Rebate.Builder builder = RebateMessage.Rebate.newBuilder();
//        builder.setId(id);
//        builder.setAmount(amount);
//        builder.setType(getType());
//        builder.setStartTime(Long.toString(getStartTime()));
//        builder.setEndTime(Long.toString(getEndTime()));
//        if (map != null)
//        {
//            for (RebateRewardBean reward : map.values())
//            {
//                builder.addRewards(reward.getMessageBuilder());
//            }
//        }
//        return builder;
        return null;
    }

    @Override
    public JSON toJson()
    {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("amount", amount);
        if (map != null)
        {
            JSONArray arr = new JSONArray();
            for (RebateRewardBean reward : map.values())
            {
                arr.add(reward.toJson());
            }
            obj.put("map", arr);
        }
        return obj;
    }

    @Override
    public void fromJson(JSON json)
    {
        if (json == null)
        {
            throw new NullPointerException("RebateBean.fromJson: 参数json == null");
        }
        if (!(json instanceof JSONObject))
        {
            throw new IllegalArgumentException("RebateBean.fromJson invoke with unexpected parameter type: " + json.getClass().getSimpleName());
        }
        
        JSONObject obj = (JSONObject)json;
        id = obj.getIntValue("id");
        amount = obj.getIntValue("amount");
        JSONArray arr = obj.getJSONArray("map");
        if (arr != null)
        {
            for (Object each : arr)
            {
                RebateRewardBean reward = new RebateRewardBean();
                reward.fromJson((JSON)each);
                putRebateReward(reward);
            }
        }
    }

}
