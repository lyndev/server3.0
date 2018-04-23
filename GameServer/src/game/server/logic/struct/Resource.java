package game.server.logic.struct;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.core.util.DateUtils;
import game.message.BackpackMessage;
import game.server.logic.constant.ResourceType;
import game.server.logic.support.IJsonConverter;
import org.apache.log4j.Logger;

/**
 *
 * <b>资源类.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public final class Resource implements IJsonConverter
{

    private int type; // 资源类型

    private int amount; // 资源数量

    private int maxAmount; // 可拥有的资源上限

    private Producer producer; // 资源生产者

    private final static Logger LOG = Logger.getLogger(Resource.class);

    public Resource()
    {
    }

    public Resource(int type, int amount)
    {
        this.type = type;
        this.amount = amount;
        maxAmount = ResourceType.getType(type).getMaxAmount();
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
        maxAmount = ResourceType.getType(type).getMaxAmount();
    }

    public int getMaxAmount()
    {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount)
    {
        this.maxAmount = maxAmount;
    }
    
    public Producer getProducer()
    {
        return producer;
    }

    public void setProducer(Producer producer)
    {
        this.producer = producer;
    }

    public int getAmount()
    {
        if (producer != null)
            producer.make();

        return amount;
    }

    /**
     * 设置资源数量（一般是初始化时调用，添加/扣除资源应该调用addAmount/subAmount）.
     *
     * @param amount 资源数量
     */
    public void setAmount(int amount)
    {
        this.amount = amount;
        if (this.amount > maxAmount)
            this.amount = maxAmount;
    }

    /**
     * 添加资源数量.
     *
     * @param amount 要添加的数量
     * @return 计算后的当前数量
     */
    public int addAmount(int amount)
    {
        this.amount = getAmount() + Math.abs(amount);
        if (this.amount > maxAmount)
            this.amount = maxAmount;

        // 添加资源后，如果资源可以自增长，同时正处于增长状态，且当前数量达到增长上限，则暂停产出
        if (producer != null
                && producer.getLastTime() != 0
                && this.amount >= producer.getLimit())
        {
            producer.setLastTime(0);
        }

        return this.amount;
    }

    /**
     * 扣除资源数量.
     *
     * @param amount 要扣除的数量
     * @return 计算后的当前数量
     */
    public int subAmount(int amount)
    {
        this.amount = getAmount() - Math.abs(amount);
        if (this.amount < 0)
            this.amount = 0;

        // 扣除资源后，如果资源可以自增长，但已停止产出，且当前数量小于增长上限，则重新启动自增长
        if (producer != null
                && producer.getLastTime() == 0
                && this.amount < producer.getLimit())
        {
            producer.setLastTime(System.currentTimeMillis()); // 以当前时间作为下次计算资源产出的起始点
        }

        return this.amount;
    }

    /**
     * 获取对应的BackpackMessage.Resource消息的Builder
     *
     * @return
     */
    public BackpackMessage.Resource.Builder getMessageBuilder()
    {
        BackpackMessage.Resource.Builder builder = BackpackMessage.Resource.newBuilder();
        builder.setType(type);
        builder.setNum(getAmount()); //这里不能用amount
        if (producer != null)
        {
            builder.setProducer(producer.getMessageBuilder());
        }
        return builder;
    }

    @Override
    public JSON toJson()
    {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("type", type);
        jsonObj.put("num", getAmount());

        if (producer != null && producer.getLastTime() != -1)
        {
            jsonObj.put("time", producer.getLastTime());
        }

        return jsonObj;
    }

    @Override
    public void fromJson(JSON json)
    {
        JSONObject jsonObj = (JSONObject) json;
        type = jsonObj.getIntValue("type");
        amount = jsonObj.getIntValue("num");

        if (producer != null)
        {
            throw new UnsupportedOperationException(
                    "Please use game.server.logic.backpack.BackpackManager.fromJson(JSON json)");
        }
    }

    /**
     * 资源生产者（指随时间自动产出，不包括其他途径获得）.
     */
    public class Producer
    {

        private int addValue; // 每次生产的产出值

        private int interval; // 每次生产的间隔时间（单位：秒）

        private int limit; // 自动产出的上限值

        private long lastTime; // 最近一次产出时间，0表示上次计算时已达产出上限，重置产出时间，-1表示未初始化

        public int getAddValue()
        {
            return addValue;
        }

        public void setAddValue(int addValue)
        {
            this.addValue = addValue;
        }

        public int getInterval()
        {
            return interval;
        }

        public void setInterval(int interval)
        {
            this.interval = interval;
        }

        public int getLimit()
        {
            return limit;
        }

        public void setLimit(int limit)
        {
            this.limit = limit;
        }

        public long getLastTime()
        {
            return lastTime;
        }

        public void setLastTime(long lastTime)
        {
            this.lastTime = lastTime;
        }

        /**
         * 计算自产出的资源.
         */
        public void make()
        {
            if (lastTime > 0)
            {
                // 上次计算时，资源未达产出上限，本次重新判断是否低于上限，如果是，则计算是否有产出
                if (amount < limit)
                {
                    LOG.debug("资源（" + type + "）未达上限（current=" + amount + "），计算产出...");
                    int output; // 实际产出值
                    long nowTime = System.currentTimeMillis();
                    long diffTime = DateUtils.getDiffSeconds(lastTime, nowTime);

                    if (diffTime >= interval)
                    {
                        int num = (int) (diffTime / interval);
                        output = addValue * num;
                        int total = output + amount;
                        LOG.debug("共计算" + num + "轮增长，预计产出" + total + "个资源");

                        if (total >= limit)
                        {
                            LOG.debug("资源产出已达上限！limit = " + limit);
                            if (total > limit)
                            {
                                output = limit - amount;
                            }
                            // 资源产出已达上限，重置lastTime
                            lastTime = 0;
                        }
                        else
                        {
                            // 资源还能继续产出，更新lastTime
                            lastTime = nowTime - ((diffTime % interval) * 1000);
                        }
                        LOG.debug("实际产出" + output + "个资源");
                        // 更新资源的当前数量
                        amount = amount + output;
                    }

                    LOG.debug("计算产出后的当前数量：" + amount);
                }
                else
                {
                    lastTime = 0; // 资源产出已达上限，重置lastTime（保险判断）
                }
            }
        }

        /**
         * 获取对应的BackpackMessage.ResourceProducer消息的Builder
         *
         * @return
         */
        public BackpackMessage.ResourceProducer.Builder getMessageBuilder()
        {
            BackpackMessage.ResourceProducer.Builder builder = BackpackMessage.ResourceProducer.newBuilder();
            builder.setInterval(interval);
            builder.setAddValue(addValue);
            builder.setLimit(limit);
            builder.setLastTime(String.valueOf(lastTime));

            return builder;
        }

    }

}
