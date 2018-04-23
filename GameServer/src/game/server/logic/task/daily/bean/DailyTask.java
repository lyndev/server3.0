/**
 * @date 2014/6/16
 * @author ChenLong
 */
package game.server.logic.task.daily.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.message.TaskMessage;
import game.server.logic.constant.DailyTaskStatus;
import game.server.logic.item.bean.Item;
import game.server.logic.struct.Resource;
import game.server.logic.support.BeanFactory;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.IJsonConverter;
import game.server.util.MiscUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.time.DateUtils;

/**
 * 日常任务基类
 *
 * @author ChenLong
 */
public class DailyTask implements IJsonConverter
{
    public final static String taskIdKey = "taskId";

    private int taskId = 0;
    private int progress = 0; // 进度
    private DailyTaskStatus status = DailyTaskStatus.Unlock;
    private long lastModifyTime = System.currentTimeMillis(); // 最后一次改变时间

    public DailyTask()
    {
    }

    @Override
    public JSON toJson()
    {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put(taskIdKey, taskId);
        jsonObj.put("progress", progress);
        jsonObj.put("status", status.getValue());
        jsonObj.put("lastModifyTime", lastModifyTime);

        return jsonObj;
    }

    @Override
    public void fromJson(JSON json)
    {
        if (!(json instanceof JSONObject))
            throw new IllegalArgumentException("unknow json type: " + json.getClass().toString());

        JSONObject jsonObj = (JSONObject) json;

        taskId = jsonObj.getIntValue(taskIdKey);
        progress = jsonObj.getIntValue("progress");
        int statusValue = jsonObj.getIntValue("status");
        status = DailyTaskStatus.getDailyTaskStatus(statusValue);
        lastModifyTime = jsonObj.getLongValue("lastModifyTime");
    }

    /**
     * 改变进度, 并且判断是否已达到完成条件
     *
     * @param value 进度改变值
     * @return 是否完成, 已完成任务始终返回false
     */
    public boolean modifyProgress(int value)
    {
        boolean result = false;
        if (status == DailyTaskStatus.InProcess)
        {
            int newProgress = progress + value;
            if (newProgress >= this.getDailyTaskCondNum())
            {
                result = true;
                progress = this.getDailyTaskCondNum();
            }
            else
            {
                progress = newProgress;
            }
            lastModifyTime = System.currentTimeMillis();
        }
        return result;
    }

    public boolean modifyProgress()
    {
        return modifyProgress(1);
    }

    public boolean checkAcrossDay(long now)
    {
        boolean isSameDay = true;
        if (!DateUtils.isSameDay(new Date(now), new Date(lastModifyTime)))
        {
            progress = 0;
            status = DailyTaskStatus.InProcess;
            lastModifyTime = now;
            isSameDay = false;
        }
        return isSameDay;
    }

    public boolean checkAcross5(long now)
    {
        boolean isSameDay = true;
        if (MiscUtils.across5(lastModifyTime, now))
        {
            progress = 0;
            status = DailyTaskStatus.InProcess;
            lastModifyTime = now;
            isSameDay = false;
        }
        return isSameDay;
    }

    public final int getTaskId()
    {
        return taskId;
    }

    public final void setTaskId(int taskId)
    {
        this.taskId = taskId;
    }

    public final int getProgress()
    {
        return progress;
    }

    public DailyTaskStatus getStatus()
    {
        return status;
    }

    public final void setProgress(int progress)
    {
        this.progress = progress;
    }

    public final void setStatus(DailyTaskStatus status)
    {
        this.status = status;
    }

    public final long getLastModifyTime()
    {
        return lastModifyTime;
    }

    public final void setLastModifyTime(long lastModifyTime)
    {
        this.lastModifyTime = lastModifyTime;
    }

    /**
     * 获取日常任务类型
     *
     * @return
     */
    public final int getDailyTaskType()
    {
        return BeanTemplet.getDailyTaskBean(taskId).getQ_type();
    }

    /**
     * 获取任务完成条件：数量
     *
     * @return
     */
    public final int getDailyTaskCondNum()
    {
        return BeanTemplet.getDailyTaskBean(taskId).getQ_cond_num();
    }

    /**
     * 获取任务名
     *
     * @return
     */
    public final String getTaskName()
    {
        return BeanTemplet.getDailyTaskBean(taskId).getQ_str();
    }

    public final int getUnlockLevel()
    {
        return BeanTemplet.getDailyTaskBean(taskId).getQ_unlock_level();
    }

    /**
     * 获取完成任务能获得的召唤师经验配置
     *
     * @return
     */
    public final int getRewardExp()
    {
        return BeanTemplet.getDailyTaskBean(taskId).getQ_exp();
    }

    public List<Resource> getRewardResource()
    {
        String strValue = BeanTemplet.getDailyTaskBean(taskId).getQ_resource();
        if (strValue.isEmpty() || strValue.equals(""))
        {
            return null;
        }
        String[] arrRes = strValue.split(";");
        if (arrRes == null || arrRes.length == 0)
        {
            return null;
        }
        List<Resource> resources = new ArrayList<>();
        for (String strRes : arrRes)
        {
            String[] resData = strRes.split("_");
            Resource resource = new Resource(Integer.valueOf(resData[0]), Integer.valueOf(resData[1]));
            resources.add(resource);
        }
        if (resources.isEmpty())
        {
            return null;
        }
        return resources;
    }

    public List<Item> getRewardItem()
    {
        String strValue = BeanTemplet.getDailyTaskBean(taskId).getQ_item();
        if (strValue.isEmpty() || strValue.equals(""))
        {
            return null;
        }
        String[] arrItem = strValue.split(";");
        if (arrItem == null || arrItem.length == 0)
        {
            return null;
        }
        List<Item> items = new ArrayList<>();
        for (String strItem : arrItem)
        {
            String[] itemData = strItem.split("_");
            Item item = BeanFactory.createItem(Integer.valueOf(itemData[0]), Integer.valueOf(itemData[1]));
            items.add(item);
        }
        if (items.isEmpty())
        {
            return null;
        }
        return items;
    }

    public TaskMessage.DailyTask.Builder getMessageBuider()
    {
        TaskMessage.DailyTask.Builder buider = TaskMessage.DailyTask.newBuilder();
        buider.setTaskId(taskId);
        buider.setTaskState(status.getValue());
        buider.setProgress(progress);
        return buider;
    }

}
