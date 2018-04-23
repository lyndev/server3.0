package game.server.logic.mail.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import game.core.command.ICommand;
import game.core.timer.DaysTimer;
import game.core.timer.SimpleTimerProcessor;
import game.core.timer.TimerEvent;
import game.core.util.ArrayUtils;
import game.server.db.game.bean.GlobalBean;
import game.server.db.game.dao.GlobalDao;
import game.server.logic.constant.GlobalTableKey;
import game.server.logic.item.bean.Item;
import game.server.logic.mail.bean.Mail;
import game.server.logic.mail.bean.ScheduleMail;
import game.server.logic.player.PlayerManager;
import game.server.logic.struct.Player;
import game.server.logic.struct.Resource;
import game.server.logic.support.BeanFactory;
import game.server.logic.support.DBView;
import game.server.logic.support.RoleView;
import game.server.thread.dboperator.GameDBOperator;
import game.server.thread.dboperator.handler.DBOperatorHandler;
import game.server.thread.dboperator.handler.ReqInsertMailHandler;
import game.server.util.MiscUtils;
import game.server.util.UniqueId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * 用于处理不在线玩家
 *
 * @author ZouZhaopeng
 */
public class MailService
{
    private final static Logger logger = Logger.getLogger(MailService.class);
    private final Map<UUID, ScheduleMail> scheduleMails = new ConcurrentHashMap<>();
    private volatile boolean scheduleMailsModify = false;

    private MailService()
    {
    }

    public static MailService getInstance()
    {
        return Singleton.INSTANCE.getMailServer();
    }

    /**
     * 启动时加载定时邮件
     */
    public synchronized void load()
    {
        GlobalBean bean = GlobalDao.select(GlobalTableKey.SCHEDULE_MAIL.getKey());
        if (bean != null)
            initScheduleMail(bean);
        addScheduleMailSaveTimer();
    }

    private void initScheduleMail(GlobalBean bean)
    {
        JSONArray jsonArray = JSON.parseArray(bean.getValue());
        for (int i = 0; i < jsonArray.size(); ++i)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject != null)
            {
                ScheduleMail scheduleMail = new ScheduleMail();
                scheduleMail.fromJson(jsonObject);
                Calendar cal = Calendar.getInstance();
                cal.set(scheduleMail.getYear(), scheduleMail.getMonth(), scheduleMail.getDate(), scheduleMail.getHour(), scheduleMail.getMinute());

                while (cal.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
                {
                    cal.add(Calendar.DATE, 1);
                }
                scheduleMails.put(scheduleMail.getId(), scheduleMail);

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int date = cal.get(Calendar.DATE);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                addScheduledSendTimer(year, month, date, hour, minute, scheduleMail.getRemainDays(), scheduleMail.getMail());
            }
        }
    }

    /**
     * 注：此方法最好在SimpleTimerProcessor停止后执行
     */
    public synchronized void save()
    {
        if (scheduleMailsModify)
        {
            JSONArray jsonObj = new JSONArray();
            for (ScheduleMail scheduleMail : scheduleMails.values())
            {
                jsonObj.add(scheduleMail.toJson());
            }
            GlobalBean bean = new GlobalBean();
            bean.setId(GlobalTableKey.SCHEDULE_MAIL.getKey());
            bean.setValue(jsonObj.toJSONString());
            if (GlobalDao.update(bean) <= 0)
                GlobalDao.insert(bean);
            scheduleMailsModify = false;
        }
    }

    /**
     * 群发邮件, 使用该接口提供的邮件, 接收者Id和接收者Name可以不填
     *
     * @param roleViews 需要发送的玩家视图列表
     * @param other
     */
    public synchronized void sendAll(List<RoleView> roleViews, Mail other)
    {
        if (roleViews != null)
        {
            for (RoleView roleView : roleViews)
            {
                if (roleView.getIsRobot() != 0)
                    continue;
                Mail mail = new Mail();
                mail.initFrom(other);
                mail.setReceiverId(UniqueId.toBase10(roleView.getRoleId()));
                mail.setReceiverName(roleView.getRoleName());

                Player player = PlayerManager.getPlayerByUserId(roleView.getUserId());
                sendPlayerMail(player, mail);
            }
        }
    }

    /**
     * 向指定userId的玩家发送指定邮件, 如果在线就直接发送邮件, 不在线就把邮件添加到数据库<b/>
     * 该接口发送的就是传递过来的Mail, 而不是复制一份再发送, 并且不会设置接收者Id和Name, 需要设置好再调用该接口
     * 
     * @param player 在线玩家, null表示玩家不在线
     * @param mail
     */
    private synchronized void sendPlayerMail(Player player, Mail mail)
    {
        //在发送之前才设置发送时间, 之前设置的不准确
        mail.setSendTime((int) (System.currentTimeMillis() / 1000));
        if (player != null)
        {
            if (!player.isRobot())
                player.getMailManager().addMailToPlayer(mail);
        }
        else
            GameDBOperator.getInstance().submitRequest(new ReqInsertMailHandler(mail.toMailBean()));
    }

    /**
     * 按指定方式发送邮件
     *
     * @param sendType 发送方式
     * @param parameter 对应发送方式的参数
     * @param mail 要发送的邮件模板
     */
    public synchronized void sendByType(int sendType, String parameter, Mail mail)
    {
        Validate.notNull(mail);
        
        if (parameter == null)
        {
            parameter = new String();
        }

        int[] min_max;
        switch (sendType)
        {
            case 1: //所有玩家
            {
                sendAll(DBView.getInstance().getRoles(), mail);
            }
            break;

            case 2: //指定玩家userId发放, 玩家ID(userId,userID,userID...)
            {
                String[] userIds = parameter.split(",");
                if (userIds != null)
                {
                    List<RoleView> dbViews = new ArrayList<>(userIds.length);
                    for (String userId : userIds)
                    {
                        dbViews.add(DBView.getInstance().getSingleRoleByUserId(userId));
                    }
                    sendAll(dbViews, mail);
                }
            }
            break;

            case 3: //按主角等级发放
            {
                min_max = ArrayUtils.parseInt(parameter.split("_"));
                if (min_max != null && min_max.length == 2)
                    sendAll(DBView.getInstance().getRolesByRoleLevel(min_max[0], min_max[1]), mail);
            }
            break;

            case 4: //按玩家VIP等级发放
            {
                min_max = ArrayUtils.parseInt(parameter.split("_"));
                if (min_max != null && min_max.length == 2)
                    sendAll(DBView.getInstance().getRolesByVipLevel(min_max[0], min_max[1]), mail);
            }
            break;

            case 5:
            {

            }
            break;

            case 6: //当日指定时间HH:MM发放
            {
                int[] hh_mm = ArrayUtils.parseInt(parameter.split(":"));
                if (hh_mm == null || hh_mm.length != 2)
                    break;

                scheduleMail(-1, -1, -1, hh_mm[0], hh_mm[1], 1, mail);
            }
            break;

            case 7: //指定日期的指定时间发放
            {
                String[] date_time_days = parameter.split(";");
                if (date_time_days == null || date_time_days.length != 3)
                    break;

                int[] yy_mm_dd = ArrayUtils.parseInt(date_time_days[0].split("-"));
                if (yy_mm_dd == null || yy_mm_dd.length != 3)
                    break;

                int[] hh_mm = ArrayUtils.parseInt(date_time_days[1].split(":"));
                if (hh_mm == null || hh_mm.length != 2)
                    break;

                int days = Integer.valueOf(date_time_days[2]);

                scheduleMail(yy_mm_dd[0], yy_mm_dd[1] - 1, yy_mm_dd[2], hh_mm[0], hh_mm[1], days, mail);
            }
            break;

            case 8: //按仙府等级发放
            {
                min_max = ArrayUtils.parseInt(parameter.split("_"));
                if (min_max != null && min_max.length == 2)
                    sendAll(DBView.getInstance().getRolesByXFLevel(min_max[0], min_max[1]), mail);
            }
            break;

            case 9: //按账号创建时间发放
            {
                min_max = ArrayUtils.parseInt(parameter.split("_"));
                if (min_max != null && min_max.length == 2)
                    sendAll(DBView.getInstance().getRolesByCreateTime(min_max[0], min_max[1]), mail);
            }
            break;

            case 10: //按竞技排名发放
            {
                min_max = ArrayUtils.parseInt(parameter.split("_"));
                if (min_max != null && min_max.length == 2)
                    sendAll(DBView.getInstance().getRolesByPVPRank(min_max[0], min_max[1]), mail);
            }
            break;

            case 11: //按服务器发放
            {
                sendAll(DBView.getInstance().getRolesByServer(Integer.valueOf(parameter)), mail);
            }
            break;

            case 12: //按平台发放
            {
                sendAll(DBView.getInstance().getRolesByPlatform(Integer.valueOf(parameter)), mail);
            }
            break;

            case 13: //指定玩家roleName发放, 玩家roleName(roleName,roleName,roleName...)
            {
                String[] roleNames = parameter.split(",");
                if (roleNames != null)
                {
                    List<RoleView> dbViews = new ArrayList<>(roleNames.length);
                    for (String roleName : roleNames)
                    {
                        dbViews.add(DBView.getInstance().getSingleRoleByRoleName(roleName));
                    }
                    sendAll(dbViews, mail);
                }
            }
            break;

            case 14: //指定玩家roleId发放
            {
                String[] roleIds = parameter.split(",");
                if (roleIds != null)
                {
                    List<RoleView> dbViews = new ArrayList<>(roleIds.length);
                    for (String roleId : roleIds)
                    {
                        dbViews.add(DBView.getInstance().getSingleRoleByRoleId(roleId));
                    }
                    sendAll(dbViews, mail);
                }
            }
            break;
                
            default:
                break;
        }
    }

    public static List<Resource> parseResources(String str)
    {
        if (str == null)
            return new ArrayList<>(0);

        String[] resStrings = str.split(";");
        List<Resource> retList = new ArrayList<>(resStrings.length);

        for (String s : resStrings)
        {
            int[] id_num = ArrayUtils.parseInt(s.split("_"));
            if (id_num != null && id_num.length == 2)
            {
                retList.add(new Resource(id_num[0], id_num[1]));
            }
        }
        return retList;
    }

    public static List<Item> parseItems(String str)
    {
        if (str == null)
            return new ArrayList<>(0);

        String[] resStrings = str.split(";");
        List<Item> retList = new ArrayList<>(resStrings.length);

        for (String s : resStrings)
        {
            int[] id_num = ArrayUtils.parseInt(s.split("_"));
            if (id_num != null && id_num.length == 2)
            {
                retList.add(BeanFactory.createItem(id_num[0], id_num[1]));
            }
        }
        return retList;
    }

    private void scheduleMail(int year, int month, int date, int hour, int minute, int days, final Mail mail)
    {
        ScheduleMail scheduleMail = new ScheduleMail(year, month, date, hour, minute, days, mail);
        addScheduleMail(scheduleMail);

        addScheduledSendTimer(scheduleMail.getYear(), scheduleMail.getMonth(), scheduleMail.getDate(),
                scheduleMail.getHour(), scheduleMail.getMinute(),
                scheduleMail.getDays(), scheduleMail.getMail());
    }

    /**
     * 从某年某月某日某时某分开始持续days天每天在同一时间点发送邮件mail
     *
     * @param year 某年
     * @param month 某月
     * @param date 某日
     * @param hour 某时
     * @param minute 某分
     * @param days 持续days天
     * @param mail 邮件内容
     */
    private void addScheduledSendTimer(int year, int month, int date, int hour, int minute, int days, final Mail mail)
    {
        Calendar cal = Calendar.getInstance();
        if (year > 0)
            cal.set(Calendar.YEAR, year);
        if (month >= 0)
            cal.set(Calendar.MONTH, month);
        if (date >= 0)
            cal.set(Calendar.DATE, date);
        if (hour >= 0)
            cal.set(Calendar.HOUR_OF_DAY, hour);
        if (minute >= 0)
            cal.set(Calendar.MINUTE, minute);

        long delay = cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();

        if (delay > 0)
        {
            SimpleTimerProcessor.getInstance().addEvent(new DaysTimer(delay, days, new ICommand()
            {
                @Override
                public void action()
                {
                    decScheduleMail(mail.getId());
                    sendAll(DBView.getInstance().getRoles(), mail);
                }
            }));
        }
        else
        {
            removeScheduleMail(mail.getId());
            logger.error("scheduleSend mail parameter error, nowTime: [" + Calendar.getInstance().getTime().toString() + "]"
                    + ",year = " + year + ", month = " + month + ", date = " + date
                    + ", hour = " + hour + ", minute = " + minute
                    + ", days = " + days
                    + ", mail: [" + mail.toString() + "]");
        }
    }

    private class ScheduleMailSaveTimerEvent extends TimerEvent
    {

        public ScheduleMailSaveTimerEvent(long initialDelay, long delay, boolean loopFixed)
        {
            super(initialDelay, delay, loopFixed);
        }

        @Override
        public void run()
        {
            GameDBOperator.getInstance().submitRequest(new DBOperatorHandler(0)
            {
                @Override
                public void action()
                {
                    MailService.this.save();
                }
            });
        }
    }

    private void addScheduleMailSaveTimer()
    {
        long delay = 30; // （单位：秒）回存间隔
        if (MiscUtils.isIDEEnvironment())
            delay = 1;
        SimpleTimerProcessor.getInstance().addEvent(new ScheduleMailSaveTimerEvent(delay * 1000, delay * 1000, true));
    }

    private void addScheduleMail(ScheduleMail scheduleMail)
    {
        scheduleMails.put(scheduleMail.getId(), scheduleMail);
        scheduleMailsModify = true;
    }

    private void removeScheduleMail(UUID id)
    {
        scheduleMails.remove(id);
        scheduleMailsModify = true;
    }

    private void decScheduleMail(UUID id)
    {
        ScheduleMail scheduleMail = scheduleMails.get(id);
        if (scheduleMail != null)
        {
            scheduleMailsModify = true;
            if (scheduleMail.decRemainDays() <= 0)
                scheduleMails.remove(id);
        }
    }

    private enum Singleton
    {
        INSTANCE;

        Singleton()
        {
            server = new MailService();
        }

        private final MailService server;

        MailService getMailServer()
        {
            return server;
        }
    }
}
