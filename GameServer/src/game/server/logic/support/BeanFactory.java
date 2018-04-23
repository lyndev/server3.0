package game.server.logic.support;

import com.alibaba.fastjson.JSONObject;
import game.data.bean.q_daily_taskBean;
import game.data.bean.q_itemBean;
import game.data.bean.q_main_missionBean;
import game.data.bean.q_mission_chapterBean;
import game.data.bean.q_taskBean;
import game.server.logic.constant.DailyTaskType;
import game.server.logic.constant.ItemType;
import game.server.logic.constant.MissionStar;
import game.server.logic.constant.MissionState;
import game.server.logic.item.bean.Item;
import game.server.logic.task.daily.bean.DailyTask;

import org.apache.log4j.Logger;

/**
 *
 * <b>实体Bean工厂类.</b>
 * <p>
 * 负责创建各种实体Bean的实例对象.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class BeanFactory
{

    private static final Logger LOG = Logger.getLogger(BeanFactory.class);

    /**
     * 创建指定物品类的实例.
     *
     * @param id 待创建的物品ID
     * @return 指定物品类的一个实例
     */
    public static Item createItem(int id)
    {
        return createItem(id, 0);
    }

    /**
     * 根据JSON创建物品实例.
     *
     * @param json JSON对象
     * @return 指定物品类的一个实例
     */
    public static Item createItem(JSONObject json)
    {
        Item item = createItem(json.getIntValue("itemId"), json.getIntValue("num"));
//        if (item instanceof Equipment)
//        {
//            Equipment equip = (Equipment) item;
//            if (json.containsKey("qualityExp"))
//            {
//                equip.setQualityExp(json.getIntValue("qualityExp"));
//            }
//            if (json.containsKey("equipLevel"))
//            {
//                equip.setEquipLevel(json.getIntValue("equipLevel"));
//            }
//        }

        return item;
    }

    /**
     * 创建指定物品类的实例.
     *
     * @param id 待创建的物品ID
     * @param amount 待创建的物品数量，默认为0，>0才设置
     * @return 指定物品类的一个实例
     */
    public static Item createItem(int id, int amount)
    {
        q_itemBean bean = BeanTemplet.getItemBean(id);
        if (bean == null)
        {
            //LOG.error("Not found ItemBean! itemId = " + id);
            return null;
        }

        for (ItemType type : ItemType.values())
        {
            if (type.compare(bean.getQ_type()))
            {
                Item item;
                try
                {
                    item = type.getBeanClass().newInstance();
                }
                catch (InstantiationException | IllegalAccessException ex)
                {
                    LOG.error("Item instantiation failed!", ex);
                    return null;
                }

                item.setId(id);
                if (amount > 0)
                {
                    item.setNum(amount);
                }

                return item;
            }
        }

        LOG.error("Not found item type!  itemId = " + id + ", itemType = " + bean.getQ_type());
        return null;
    }
    public static DailyTask createDailyTask(JSONObject json)
    {
        return createDailyTask(json.getIntValue(DailyTask.taskIdKey));
    }

    /**
     * 创建指定日常任务的实例.
     *
     * @param taskId
     * @return 指定日常任务的一个实例
     */
    public static DailyTask createDailyTask(int taskId)
    {
        DailyTask task = null;
        q_daily_taskBean bean = BeanTemplet.getDailyTaskBean(taskId);
        if (bean != null)
        {
            int typeValue = bean.getQ_type();
            DailyTaskType dailyTaskType = DailyTaskType.getDailyTaskType(typeValue);
            try
            {
                if (dailyTaskType != null)
                {
                    task = dailyTaskType.getClazz().newInstance();
                    task.setTaskId(taskId);
                }
                else
                {
                    LOG.error("cannot get DailyTaskType, typeValue = " + typeValue);
                }
            }
            catch (InstantiationException | IllegalAccessException ex)
            {
                LOG.error("DailyTask instantiation failed !", ex);
            }
        }
        else
        {
            LOG.error("Not found DailyTaskBean! taskId = " + taskId);
        }
        return task;
    }
}
