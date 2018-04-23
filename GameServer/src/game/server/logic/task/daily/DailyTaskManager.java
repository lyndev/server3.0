/**
 * @date 2014/6/16
 * @author ChenLong
 */
package game.server.logic.task.daily;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import game.core.message.SMessage;
import game.data.bean.q_daily_taskBean;
import game.data.bean.q_vipBean;
import game.message.TaskMessage;
import game.server.logic.constant.DailyTaskStatus;
import game.server.logic.constant.DailyTaskType;
import game.server.logic.constant.ErrorCode;
import game.server.logic.constant.Reasons;
import game.server.logic.item.bean.Item;
import game.server.logic.struct.Player;
import game.server.logic.struct.Resource;
import game.server.logic.support.BeanFactory;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.IJsonConverter;
import game.server.logic.task.daily.bean.DailyTask;
import game.server.logic.util.LoggingMisc;
import game.server.util.MessageUtils;
import game.server.util.UniqueId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * 日常任务
 *
 * @author 徐能强
 */
public class DailyTaskManager implements IJsonConverter
{
    private final static Logger log = Logger.getLogger(DailyTaskManager.class);
    private final transient Player owner;
    private final Map<Integer, DailyTask> tasks = new HashMap<>();

    public DailyTaskManager(Player owner)
    {
        this.owner = owner;
    }

    @Override
    public JSON toJson()
    {
        JSONObject jsonObj = new JSONObject();

        { // 保存日常任务信息
            JSONArray dailyTaskArray = new JSONArray();
            for (Map.Entry<Integer, DailyTask> entry : tasks.entrySet())
            {
                DailyTask task = entry.getValue();
                dailyTaskArray.add(task.toJson());
            }
            jsonObj.put("tasks", dailyTaskArray);
        }

        return jsonObj;
    }

    @Override
    public void fromJson(JSON json)
    {
        if (!(json instanceof JSONObject))
            throw new IllegalArgumentException("unknow json type: " + json.getClass().toString());

        JSONObject jsonObj = (JSONObject) json;
      /*
        { // 读取日常任务信息
      
            JSONArray dailyTaskArray = jsonObj.getJSONArray("tasks");
            for (int i = 0; i < dailyTaskArray.size(); ++i)
            {
                JSONObject taskJsonObj = dailyTaskArray.getJSONObject(i);
                DailyTask task = BeanFactory.createDailyTask(taskJsonObj);
                if (task != null)
                {
                    task.fromJson(taskJsonObj);
                    tasks.put(task.getTaskId(), task);
                }
                else
                {
                    log.error("cannot create DailyTask");
                }
            }
        }

        fillAllDailyTask(); // 确保新增日常任务能够正常初始化
            */
    }

    /**
     * 创建角色初始化
     */
    public void createInitialize()
    {
        fillAllDailyTask();
    }

    /**
     * 客户端加载完成初始化
     */
    public void clientInitializeOver()
    {
        checkAcross5();
        sendResDailyTaskInfo();
    }

    /**
     * 角色升级：检测是否有新的任务解锁
     */
    public void checkUnlockTask()
    {
        int roleLevel = owner.getRoleLevel();
        List<DailyTask> unlockList = new ArrayList<>();
        for (Map.Entry<Integer, DailyTask> entry : tasks.entrySet())
        {
            DailyTask task = entry.getValue();
            if (task.getStatus() == DailyTaskStatus.Unlock && task.getUnlockLevel() <= roleLevel)
            {
                task.setStatus(DailyTaskStatus.InProcess);
                unlockList.add(task);
            }
        }
        if (unlockList.isEmpty())
        {
            return;
        }
        //发送更新消息
        sendResUpdateDailyTaskInfo(unlockList);
        //TODO: 这里不需要检测是否能完成
    }

    /**
     * 凌晨5点重置
     */
    public void tick5()
    {
        log.info("dailyTaskManager tick5, roleId = " + owner.getRoleId());

        for (Map.Entry<Integer, DailyTask> entry : tasks.entrySet())
        {
            DailyTask task = entry.getValue();
            acceptTaskLog(task);
        }
    }

    public void checkAcross5()
    {
        long now = System.currentTimeMillis();
        for (Map.Entry<Integer, DailyTask> entry : tasks.entrySet())
        {
            DailyTask task = entry.getValue();
            if (!task.checkAcross5(now))
                acceptTaskLog(task);
        }
    }

    /**
     * 领取奖励
     *
     * @param reqMsg
     */
    public void onGetOneDailyTaskAward(TaskMessage.ReqGetOneDailyTaskAward reqMsg)
    {
        Validate.notNull(reqMsg);

        int taskId = reqMsg.getTaskId();
        if (!owner.getFeatureManager().isOpen("C1403"))
        {
            log.error("主线任务功能未开放。 id = " + "C1403");
            sendGetOneDailyTaskAward(taskId, ErrorCode.FUNCTION_NOT_OPEN.getCode());
            return;
        }
        log.info("玩家[" + owner.getRoleName() + "],领取日常任务奖励, taskId = " + taskId);

        DailyTask task = tasks.get(taskId);
        if (task == null)
        {
            log.error("cannot find DailyTask taskId = " + taskId);
            sendGetOneDailyTaskAward(taskId, ErrorCode.CAN_NOT_GET_ONE_DAILY_TASK_AWARD.getCode());
            return;
        }

        if (task.getStatus() != DailyTaskStatus.Finished)
        {
            log.error("不能领取奖励, taskId = " + task.getTaskId()
                    + ", 任务状态为: " + task.getStatus().name() + "(" + task.getStatus().getValue() + ")");
            sendGetOneDailyTaskAward(taskId, ErrorCode.CAN_NOT_FIND_DAILY_TASK.getCode());
            return;
        }

        //设置为领取状态
        task.setStatus(DailyTaskStatus.GotAward);
        //领取奖励
        getAward(task);
        //发送结果消息（是否需要）
        sendGetOneDailyTaskAward(taskId, 0);
        //发送更新消息：不需要发送了
        //sendResUpdateDailyTaskInfo(task);
    }

    /**
     * 每天登陆触发（普通登陆、vip登陆）
     */
    public void dailyLoginTrigger()
    {
        commonTrigger(DailyTaskType.DAILY_LOGIN);
        if (owner.getVipManager().getVipLevel() >= 1)
        {
            commonTrigger(DailyTaskType.VIP_LOGIN);
        }
    }

    /**
     * 任意主线普通关卡完成触发
     */
    public void mainMissionFinishedTrigger()
    {
        commonTrigger(DailyTaskType.NORMAL_MISSION);
    }

    /**
     * 任意主线精英关卡完成触发
     */
    public void mainEliteMissionFinishedTrigger()
    {
        commonTrigger(DailyTaskType.ELITE_MISSION);
    }

    /**
     * 任意资源副本完成触发
     */
    public void resourceMissionFinishedTrigger()
    {
        commonTrigger(DailyTaskType.RESOURCE_MISSION);
    }

    /**
     * 任意回廊（远征）完成触发
     */
    public void expeditionMissionFinishedTrigger()
    {
        commonTrigger(DailyTaskType.EXPEDITION_MISSION);
    }

    /**
     * 升级技能触发
     */
    public void skillLevelUpTrigger()
    {
        commonTrigger(DailyTaskType.SKILL_UP);
    }

    /**
     * 竞技场触发
     */
    public void arenaTrigger()
    {
        commonTrigger(DailyTaskType.ARENA_COUNT);
    }

    /**
     * 竞技场挑战胜利触发
     */
    public void arenaWinTrigger()
    {
        commonTrigger(DailyTaskType.ARENA_WIN);
    }

    /**
     * 招财触发
     */
    public void makeMoneyWinTrigger()
    {
        commonTrigger(DailyTaskType.MAKE_MONEY);
    }

    private void commonTrigger(DailyTaskType taskType)
    {
        commonTrigger(taskType, 1);
    }

    /**
     * 通用触发方法
     *
     * @param taskType 日常任务类型
     * @param progressModify 进度改变值
     */
    private void commonTrigger(DailyTaskType taskType, int progressModify)
    {
        try
        {
            List<DailyTask> taskList = getDailyTask(taskType);
            List<DailyTask> modifyTask = new ArrayList<>();
            for (DailyTask task : taskList)
            {
                if (task.getStatus() == DailyTaskStatus.InProcess)
                {
                    if (task.modifyProgress(progressModify))
                    {
                        task.setStatus(DailyTaskStatus.Finished);
                        log.info("玩家[" + owner.getRoleName() + "],日常任务完成。taskId = " + task.getTaskId() + ", taskName = " + task.getTaskName());
                    }
                    modifyTask.add(task);
                    finishTaskLog(task);
                }
            }
            if (!modifyTask.isEmpty())
                //发送更新消息
                sendResUpdateDailyTaskInfo(modifyTask);
        }
        catch (Throwable t)
        {
            log.error("DailyTaskManager commonTrigger exception", t);
        }
    }

    private void sendResDailyTaskInfo()
    {
        TaskMessage.ResDailyTaskInfo.Builder resMsg = TaskMessage.ResDailyTaskInfo.newBuilder();
        for (Map.Entry<Integer, DailyTask> entrySet : tasks.entrySet())
        {
            DailyTask task = entrySet.getValue();
            if (task != null && task.getStatus() != DailyTaskStatus.Unlock)
            {
                TaskMessage.DailyTask.Builder resTask = task.getMessageBuider();
                resMsg.addTasks(resTask);
            }
        }
        MessageUtils.send(owner, new SMessage(TaskMessage.ResDailyTaskInfo.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
    }

    private void sendResUpdateDailyTaskInfo(DailyTask task)
    {
        List<DailyTask> taskList = new ArrayList<>(1);
        taskList.add(task);
        sendResUpdateDailyTaskInfo(taskList);
    }

    //发送日常任务更新的消息
    private void sendResUpdateDailyTaskInfo(List<DailyTask> taskList)
    {
        if (taskList.isEmpty() || taskList.size() == 0)
        {
            log.error("发送数据不能为空。");
            return;
        }
        TaskMessage.ResUpdateDailyTask.Builder resMsg = TaskMessage.ResUpdateDailyTask.newBuilder();
        for (DailyTask task : taskList)
        {
            if (task.getStatus() != DailyTaskStatus.Unlock)
            {
                TaskMessage.DailyTask.Builder resTask = task.getMessageBuider();
                resMsg.addTasks(resTask);
            }
        }
        MessageUtils.send(owner, new SMessage(TaskMessage.ResUpdateDailyTask.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
    }

    //领取奖励结果
    private void sendGetOneDailyTaskAward(int taskId, int result)
    {
        TaskMessage.ResGetOneDailyTaskAward.Builder resMsg = TaskMessage.ResGetOneDailyTaskAward.newBuilder();
        resMsg.setTaskId(taskId);
        resMsg.setResult(result);
        MessageUtils.send(owner, new SMessage(TaskMessage.ResGetOneDailyTaskAward.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
    }

    /**
     * 获取同一类任务
     *
     * @param taskType
     * @return
     */
    private List<DailyTask> getDailyTask(DailyTaskType taskType)
    {
        List<DailyTask> taskList = new ArrayList<>(5);
        for (Map.Entry<Integer, DailyTask> entry : tasks.entrySet())
        {
            DailyTask task = entry.getValue();
            if (task.getDailyTaskType() == taskType.getValue())
                taskList.add(task);
        }
        return taskList;
    }

    /**
     * 确保tasks内有所有日常任务
     */
    private void fillAllDailyTask()
    {
        /*
        Map<Integer, q_daily_taskBean> allTasks = BeanTemplet.getDailyTaskBeanMap();
        int roleLevel = owner.getRoleLevel();
        for (Map.Entry<Integer, q_daily_taskBean> entry : allTasks.entrySet())
        {
            int taskId = entry.getKey();
            if (!tasks.containsKey(taskId))
            {
                DailyTask dailyTask = BeanFactory.createDailyTask(taskId);
                if (dailyTask != null)
                {
                    //有等级限制
                    if (roleLevel <= entry.getValue().getQ_unlock_level())
                    {
                        dailyTask.setStatus(DailyTaskStatus.InProcess);
                    }
                    tasks.put(taskId, dailyTask);
                    acceptTaskLog(dailyTask);
                }
                else
                {
                    log.error("创建日常任务失败, taskId = " + taskId);
                }
            }
        }*/
    }

    private void acceptTaskLog(DailyTask task)
    {
        if (!owner.isRobot())
            LoggingMisc.getInstance().addLogFuncJoin(owner.getFgi(), Integer.toString(owner.getServerId()), UniqueId.toBase36(owner.getRoleId()), owner.getFedId(),
                    "任务", "日常任务", task.getTaskName(), Integer.toString(task.getTaskId()));
    }

    private void finishTaskLog(DailyTask task)
    {
        if (!owner.isRobot())
            LoggingMisc.getInstance().addLogFuncComplete(owner.getFgi(), Integer.toString(owner.getServerId()), UniqueId.toBase36(owner.getRoleId()), owner.getFedId(),
                    "任务", "日常任务", task.getTaskName(), Integer.toString(task.getTaskId()));
    }

    /**
     * 领取奖励
     *
     * @param dailyTask
     */
    private void getAward(DailyTask dailyTask)
    {

        List<Item> items = dailyTask.getRewardItem();
        if (dailyTask.getDailyTaskType() == DailyTaskType.VIP_LOGIN.getValue())
        {
            int vipLv = owner.getVipManager().getVipLevel();
            q_vipBean vipBean = BeanTemplet.getVipBean(vipLv);
        }
        if (items != null && !items.isEmpty())
        {
            owner.getBackpackManager().addItem(items, true, Reasons.TASK_AWARD);
        }

        List<Resource> resources = dailyTask.getRewardResource();
        if (resources != null && !resources.isEmpty())
        {
            for (Resource resource : resources)
            {
                owner.getBackpackManager().addResource(resource.getType(),
                        resource.getAmount(), true, Reasons.TASK_AWARD, new Date());
            }
        }
    }
}
