/**
 * @date 2014/9/4
 * @author ChenLong
 */
package game.server.logic.notice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import game.core.timer.SimpleTimerProcessor;
import game.core.timer.TimerEvent;
import game.server.db.game.bean.GlobalBean;
import game.server.db.game.dao.GlobalDao;
import game.server.logic.constant.BackstageCmdResult;
import game.server.logic.constant.GlobalTableKey;
import game.server.logic.player.PlayerManager;
import game.server.util.MiscUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class NoticeManager
{
    private static final Logger logger = Logger.getLogger(NoticeManager.class);
    private static final NoticeManager instance;

    private final Map<Integer, NoticeTimerBean> noticeTimers = new HashMap<>();
    private String lastSaveJsonMD5 = StringUtils.EMPTY;

    static
    {
        instance = new NoticeManager();
    }

    private NoticeManager()
    {

    }

    public static NoticeManager getInstance()
    {
        return instance;
    }

    /**
     * GameServer启动时加载未播完的公告
     */
    public synchronized void load()
    {
        GlobalBean bean = GlobalDao.select(GlobalTableKey.NOTICE.getKey());
        if (bean != null)
        {
            JSONArray jsonArray = JSON.parseArray(bean.getValue());
            for (int i = 0; i < jsonArray.size(); ++i)
            {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                NoticeBean noticeBean = new NoticeBean();
                noticeBean.fromJson(jsonObj);
                loadInitNotice(noticeBean);
            }
        }
        addSaveTimer();
    }

    public synchronized void save()
    {
        save(getSaveJsonArray());
    }

    public synchronized BackstageCmdResult addNotice(NoticeBean noticeBean)
    {
        BackstageCmdResult result = BackstageCmdResult.NoticeParameterError;

        do
        {
            if (noticeBean.getType() != 0)
            {
                logger.error("只支持跑马灯公告, type != 0, type = " + noticeBean.getType());
                break;
            }
            if (noticeBean.getStartTime() <= 0)
            {
                logger.error("startTime <= 0, startTime = " + noticeBean.getStartTime());
                break;
            }
            if (noticeBean.getDuration() < 0)
            {
                logger.error("duration < 0, duration = " + noticeBean.getDuration());
                break;
            }
            int nowTime = (int) (System.currentTimeMillis() / 1000L);
            if (noticeBean.getStartTime() + noticeBean.getDuration() < nowTime)
            {
                logger.error("addNotice 已过期, nowTime = " + nowTime + ", noticeBean: [" + noticeBean.toString() + "]");
                break;
            }
            if (noticeBean.getInterval() <= 0)
            {
                logger.error("interval <= 0, interval = " + noticeBean.getInterval());
                break;
            }
            if (noticeBean.getContent().trim().isEmpty())
            {
                logger.error("content is empty, content: [" + noticeBean.getContent() + "]");
                break;
            }

            ScheduledFuture future = addNoticeTimer(noticeBean);
            if (future != null)
            {
                NoticeTimerBean timerBean = new NoticeTimerBean(noticeBean, future);
                noticeTimers.put(timerBean.getNoticeId(), timerBean);
                result = BackstageCmdResult.Success;
            }
        } while (false);

        return result;
    }

    /**
     * 添加一次性定时器
     *
     * @param noticeBean
     * @return
     */
    private synchronized ScheduledFuture addNoticeTimer(NoticeBean noticeBean)
    {
        int runTime = -1; // 公告执行时间点
        int nowTime = (int) (System.currentTimeMillis() / 1000L); // 当前时间点
        ScheduledFuture future = null;

        if (noticeBean.isOnceNotice() && !noticeBean.hasExecuted()) // 一次性公告, 并且没执行过，直接添加
        {
            runTime = noticeBean.getStartTime();
        }
        if (!noticeBean.isOnceNotice()) // 非一次性公告
        {
            // 第一次执行从NoticeBean.startTime开始, 第二次以后从LastNoticeTime开始
            int startTime = (noticeBean.getLastNoticeTime() == 0) ? noticeBean.getStartTime() : noticeBean.getLastNoticeTime();
            if (startTime < nowTime)
            {
                int n = (nowTime - startTime) / noticeBean.getInterval();
                startTime += (n * noticeBean.getInterval());
            }

            while (startTime <= nowTime)
            {
                startTime += noticeBean.getInterval();
            }

            int endTime = noticeBean.getStartTime() + noticeBean.getDuration(); // 公告结束时间点
            if (startTime < endTime) // 是否超过持续时间
                runTime = startTime;
        }

        int interval = runTime - nowTime; // 定时公告执行时间点距现在的间隔
        if (interval > 0)
        {
            future = SimpleTimerProcessor.getInstance().addEvent(new NoticeTimer(noticeBean, interval));
            logger.info("addNoticeTimer, nowTime = " + nowTime + ", runTime = " + runTime + ", interval = " + interval
                    + ", noticeBean: [" + noticeBean.toString() + "]");
        }
        else
        {
            logger.error("interval < 0, nowTime = " + nowTime + ", runTime = " + runTime + ", interval = " + interval
                    + ", noticeBean: [" + noticeBean.toString() + "]");
        }
        return future;
    }

    /**
     * Notice一次性定时器
     */
    private class NoticeTimer extends TimerEvent
    {
        private final NoticeBean noticeBean;

        public NoticeTimer(NoticeBean noticeBean, int interval)
        {
            super(interval * 1000);
            this.noticeBean = noticeBean;
        }

        @Override
        public void run()
        {
            broadcastNotice(noticeBean);
        }
    }

    private synchronized void broadcastNotice(NoticeBean noticeBean)
    {
        logger.info("broadcastNotice, noticeBean: [" + noticeBean.toString() + "]");
        PlayerManager.boardcastNotify(noticeBean.getContent());
        noticeBean.setLastNoticeTime((int) (System.currentTimeMillis() / 1000L));
        ScheduledFuture future = addNoticeTimer(noticeBean);
        if (future == null)
            removeNotice(noticeBean.getId());
    }

    public synchronized BackstageCmdResult removeNotice(int noticeId)
    {
        logger.info("remove notice, id = " + noticeId);
        BackstageCmdResult result = BackstageCmdResult.Success;
        NoticeTimerBean timerBean = noticeTimers.get(noticeId);
        if (timerBean != null)
        {
            timerBean.getTimerFuture().cancel(true);
            noticeTimers.remove(noticeId);
        }
        else
        {
            logger.error("cannot find noticeId = " + noticeId);
            result = BackstageCmdResult.CannotFindNotice;
        }
        return result;
    }

    /**
     * 新加定时公告 符合"间隔多少时间显示一次公告（间隔时间大于等于持续时间时判定为一次性公告）"的规则，加载老公告需要判断, 否则将多播一次
     *
     * @param noticeBean
     */
    private void loadInitNotice(NoticeBean noticeBean)
    {
        boolean hasNoticeTimes = noticeBean.getStartTime() + noticeBean.getDuration() > noticeBean.getLastNoticeTime() + noticeBean.getInterval();
        if (noticeBean.getLastNoticeTime() == 0 || hasNoticeTimes)
            addNotice(noticeBean);
    }

    private void addSaveTimer()
    {
        int saveIntervalSecond = 120;
        if (MiscUtils.isIDEEnvironment())
            saveIntervalSecond = 1;
        SimpleTimerProcessor.getInstance().addEvent(new NoticeSaveTimerEvent(saveIntervalSecond));
    }

    private class NoticeSaveTimerEvent extends TimerEvent
    {
        private final int saveIntervalSecond;

        public NoticeSaveTimerEvent(int saveIntervalSecond)
        {
            super(saveIntervalSecond * 1000, saveIntervalSecond * 1000, true); // 间隔 saveIntervalSecond 秒
            this.saveIntervalSecond = saveIntervalSecond;
        }

        @Override
        public void run()
        {
            checkAndSave();
        }
    }

    /**
     * 对比上次回存json串MD5值判断是否需要回存
     */
    private synchronized void checkAndSave()
    {
        JSONArray jsonArray = getSaveJsonArray();
        String jsonStr = jsonArray.toJSONString();
        String jsonMD5 = calcSaveJsonMD5(jsonStr);
        if (!lastSaveJsonMD5.equals(jsonMD5))
        {
            lastSaveJsonMD5 = jsonMD5;
            save(jsonStr);
        }
    }

    private synchronized void save(JSONArray jsonArray)
    {
        save(jsonArray.toJSONString());
    }

    private synchronized void save(String jsonArrayStr)
    {
        GlobalBean bean = new GlobalBean();
        bean.setId(GlobalTableKey.NOTICE.getKey());
        bean.setValue(jsonArrayStr);
        if (GlobalDao.update(bean) <= 0)
            GlobalDao.insert(bean);
    }

    private synchronized JSONArray getSaveJsonArray()
    {
        JSONArray jsonArray = new JSONArray();
        for (NoticeTimerBean noticeTimer : noticeTimers.values())
        {
            NoticeBean noticeBean = noticeTimer.getNoticeBean();
            jsonArray.add(noticeBean.toJson());
        }
        return jsonArray;
    }

    private synchronized String calcSaveJsonMD5(String jsonStr)
    {
        if (jsonStr.trim().isEmpty())
            throw new IllegalStateException("jsonStr isEmpty, jsonStr: [" + jsonStr + "]");
        String md5 = DigestUtils.md5Hex(jsonStr);
        return md5;
    }
}
