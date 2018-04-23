/**
 * @date 2014/4/14 14:20
 * @author ChenLong
 */
package game.server.logic.struct;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import game.core.message.SMessage;
import game.core.script.ScriptManager;
import game.data.bean.q_globalBean;
import game.message.LoginMessage;
import game.message.PlayerMessage;
import game.server.db.game.bean.MailBean;
import game.server.db.game.bean.RankBean;
import game.server.db.game.bean.RoleBean;
import game.server.db.game.bean.UserBean;
import game.server.db.game.bean.UserRoleBean;
import game.server.logic.backpack.BackpackManager;
import game.server.logic.constant.JsonKey;
import game.server.logic.constant.Reasons;
import game.server.logic.constant.ResourceType;
import game.server.logic.exception.LogicException;
import game.server.logic.feature.FeatureManager;
import game.server.logic.loginGift.LoginGiftManager;
import game.server.logic.mail.MailManager;
import game.server.logic.operActivity.OperActivityManager;
import game.server.logic.rebate.RebateManager;
import game.server.logic.recharge.RechargeManager;
import game.server.logic.signIn.SignInManager;
import game.server.logic.store.StoreManager;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.DBView;
import game.server.logic.support.IJsonConverter;
import game.server.logic.task.daily.DailyTaskManager;
import game.server.logic.util.ScriptArgs;
import game.server.logic.vip.VipManager;
import game.server.util.MessageUtils;
import game.server.util.MiscUtils;
import game.server.util.UniqueId;
import game.server.world.GameWorld;
import game.server.world.wplayer.handler.LWPlayerUpdateHandler;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 * 玩家对象
 *
 * @author ChenLong
 */
public class Player implements IJsonConverter
{
    private static final Logger LOG = Logger.getLogger(Player.class);

    private transient Integer lineId; // 所在线ID
    private final transient PlayerState playerState = new PlayerState();
    private long userId;
    private long roleId;
    private String userName;
    private String roleName;
    private int roleLevel;
    private int roleExp;
    private int vipLevel;
    private int sex;
    private int roleHead; // 头像
    private String headURL; // 头像url
    private boolean isRobot;
    private String fgi = StringUtils.EMPTY; // 渠道ID
    private String fedId = StringUtils.EMPTY;//
    private int platformId;
    private int serverId;
    private transient Token token;
    private transient IoSession session;
    private transient IoSession udpSession;
    private String soleId = "";
    private long lastConnectTime; // 上次连接时间
    private long createRoleTime; // 角色创建时间
    private long lastWorldChatTime;//最近一次向世界频道发消息的时间
    private volatile int gmLevel; // gm等级
    private long lastResetBuyNum;// 重置购买体力次数时刻
    private int energyBuyNum;// 当日体力购买次数
    private long lastSkillBuyTime;// 重置购买体力次数时刻
    private int skillBuyNum;//当日购买技能点次数
    private long monthCardExpiryDate = 0; // 月卡有效期(毫秒时间戳)
    private transient boolean allLoading; // 是否载入所有数据, 如果没有载入所有数据, 则不允许回存！
    private int friendNum = 0; // 好友数量
    private List<Integer> QuestionFinishedList = new ArrayList<>(); // 问卷调查记录
    private int denyChatTimestamp = 0; //禁聊结束时间, 在此之前禁止聊天

    private String clientVer = StringUtils.EMPTY;
    private String device = StringUtils.EMPTY;
    private String platform_uid = StringUtils.EMPTY;
    private String client = StringUtils.EMPTY;
    private int curRoomNum = 0;
    private int curRoomType = 0;
    private int curGameType = 0;
    private transient long lastSaveTime = System.currentTimeMillis(); // 在线时上次回存时间戳
    private transient String lastSaveDataDigest = StringUtils.EMPTY; // 在线是上次回存数据的MD5

    private transient final List<String> searchBaseList = new ArrayList<>(5); // 缓存上次搜索出的仙府基地列表（仙府ID）
    private final Set<Integer> scriptFlag = new HashSet<>();

    private final BackpackManager backpackManager = new BackpackManager(this);
    private final FeatureManager featureManager = new FeatureManager(this);
    private final OperActivityManager operActivityMgr = new OperActivityManager(this);
    private final DailyTaskManager dailyTaskManager = new DailyTaskManager(this);
    private final MailManager mailManager = new MailManager(this);
    private final StoreManager storeManager = new StoreManager(this);
    private final SignInManager signInManager = new SignInManager(this);
    private final RechargeManager rechargeManager = new RechargeManager(this);
    private final VipManager vipManager = new VipManager(this);
    private final RebateManager rebateManager = new RebateManager(this);
    private final LoginGiftManager loginGiftManager = new LoginGiftManager(this);

    /**
     * 初始化玩家数据.
     *
     * @param userRole
     */
    public void loadInitialize(UserRoleBean userRole)
    {
        initialize(userRole);

        if (!userRole.getMiscData().isEmpty())
        {
            if (!isRobot)
                this.fromJson(JSON.parseObject(userRole.getMiscData()));
            else
                this.fromJsonForRobot(JSON.parseObject(userRole.getMiscData()));
        }
        else
        {
            allLoading = true;
        }
    }

    /**
     * 初始化玩家数据.
     *
     * @param userBean
     * @param roleBean
     * @param mailList
     */
    public void loadInitialize(UserBean userBean, RoleBean roleBean, List<MailBean> mailList)
    {
        roleBean.uncompress();
        userId = UniqueId.toBase10(userBean.getUserId());
        roleId = UniqueId.toBase10(roleBean.getRoleId());
        userName = userBean.getUserName();
        roleName = roleBean.getRoleName();
        roleLevel = roleBean.getRoleLevel();
        vipLevel = roleBean.getVipLevel();
        fgi = roleBean.getFgi();
        fedId = roleBean.getFedId();
        platformId = roleBean.getPlatformId();
        serverId = roleBean.getServerId();
        roleHead = roleBean.getRoleHead();
        isRobot = roleBean.getIsRobot() == 1;
        headURL = roleBean.getHeadURL();
        roleExp = roleBean.getRoleExp();
        sex = roleBean.geSex();
        soleId = roleBean.getSoleId();
        //getMailManager().loginLoadMail(mailList); //加载邮件

        if (!roleBean.getMiscData().isEmpty())
            this.fromJson(JSON.parseObject(roleBean.getMiscData()));
        else
            allLoading = true;
    }

    private void initialize(UserRoleBean userRole)
    {
        userRole.uncompress();
        userId = UniqueId.toBase10(userRole.getUserId());
        roleId = UniqueId.toBase10(userRole.getRoleId());
        userName = userRole.getUserName();
        roleName = userRole.getRoleName();
        roleLevel = userRole.getRoleLevel();
        vipLevel = userRole.getVipLevel();
        fgi = userRole.getFgi();
        platformId = userRole.getPlatformId();
        serverId = userRole.getServerId();
        roleHead = userRole.getRoleHead();
        isRobot = userRole.getIsRobot() == 1;
    }

    /**
     * 初始化玩家详细数据（某些地方加载的时候用）
     *
     * @param miscData
     */
    public void loadInitializeMiscData(String miscData)
    {
        if (miscData != null && !miscData.isEmpty())
        {
            try
            {
                miscData = MiscUtils.uncompressText(miscData);
                this.fromJson(JSON.parseObject(miscData));
            }
            catch (UnsupportedEncodingException ex)
            {
                LOG.error("UnsupportedEncodingException", ex);
            }
        }
    }

    /**
     * 创建角色时初始化玩家 比如: 初始任务, 创建角色送的卡牌/道具等
     *
     * @param userBean
     * @param roleBean
     * @param mainCardId
     */
    public void createInitialize(UserBean userBean, RoleBean roleBean)
    {
        roleBean.setMiscData(StringUtils.EMPTY);
        this.roleLevel = 1;
        this.setLastResetBuyNum(System.currentTimeMillis());
        this.setLastSkillBuyTime(System.currentTimeMillis());
        setRoleHead(roleBean.getRoleHead());
        loadInitialize(userBean, roleBean, new ArrayList<MailBean>(0));
        callSetRoleNameScript();

        // 记录角色创建时间
        createRoleTime = System.currentTimeMillis();

        //初始化背包中资源
        backpackManager.createInitialize();

        // 初始日常任务
        dailyTaskManager.createInitialize();
        allLoading = true;
    }

    public void reqPingPongIncrement()
    {
        LoginMessage.ResPingPong.Builder reqMsg = LoginMessage.ResPingPong.newBuilder();
        reqMsg.setValue(1);
        MessageUtils.send(session, new SMessage(LoginMessage.ResPingPong.MsgID.eMsgID_VALUE, reqMsg.build().toByteArray()));
    }

    public void resPingPongIncrement(LoginMessage.ResPingPong resMsg)
    {
        LoginMessage.ResPingPong.Builder reqMsg = LoginMessage.ResPingPong.newBuilder();
        reqMsg.setValue(resMsg.getValue() + 1);
        MessageUtils.send(session, new SMessage(LoginMessage.ResPingPong.MsgID.eMsgID_VALUE, reqMsg.build().toByteArray()));
    }

    public void reqTimestamp()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        int timeStamp = (int) (cal.getTimeInMillis() / 1000L);
        PlayerMessage.ResTimestamp.Builder resMsg = PlayerMessage.ResTimestamp.newBuilder();
        resMsg.setTime(Integer.toString(timeStamp));
        MessageUtils.send(session, new SMessage(PlayerMessage.ResTimestamp.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
    }

    /**
     * 客户端加载完成
     */
    public void clientInitializeOver()
    {
        try
        {
            featureManager.clientInitializeOver();
        }
        catch (Exception ex)
        {
            LOG.error("Exception", ex);
        }

        try
        {
            backpackManager.clientInitializeOver();
        }
        catch (Exception ex)
        {
            LOG.error("Exception", ex);
        }

        try
        {
            //检测是够重置体力购买次数或者技能点购买次数
            checkBuyEnergyAndSkillNum();
        }
        catch (Exception ex)
        {
            LOG.error("Exception", ex);
        }
        try
        {
            storeManager.clientInitializeOver();
        }
        catch (Exception ex)
        {
            LOG.error("Exception", ex);
        }

        try
        {
            mailManager.clientInitializeOver();
        }
        catch (Exception ex)
        {
            LOG.error("Exception", ex);
        }

        try
        {
            dailyTaskManager.clientInitializeOver();
        }
        catch (Exception ex)
        {
            LOG.error("Exception", ex);
        }
        try
        {
            vipManager.clientInitializeOver();
        }
        catch (Exception ex)
        {
            LOG.error("Exception", ex);
        }
        try
        {
            signInManager.clientInitializeOver();
        }
        catch (Exception ex)
        {
            LOG.error("Exception", ex);
        }
        try
        {
            operActivityMgr.clientInitializeOver();
        }
        catch (Exception ex)
        {
            LOG.error("Exception", ex);
        }

        try
        {
            rechargeManager.clientInitializeOver();
        }
        catch (Exception ex)
        {
            LOG.error("Exception", ex);
        }
        //每日任务触发:登陆(客户端初始化完成后表示登陆完成)
        dailyTaskManager.dailyLoginTrigger();
        
        // 如果在房间则重进入房间，（暂时这样处理后面加入到玩家房间逻辑里面）
        if(this.curGameType != 0 && this.curRoomNum !=0 && this.curRoomType != 0 ){
            LOG.info("玩家已将在房间，重新进入房间。");
            GameWorld.getInstance().getRoomManager().playerReenterRoom(this, this.curGameType, this.curRoomType, this.curRoomNum);
        }
    }

    /**
     * 发送玩家的系统重置消息.
     */
    public void sendSystemReset()
    {
        dailyTaskManager.clientInitializeOver();
        signInManager.clientInitializeOver();
        SendPlayerInfoMsg();
    }

    public void sessionClose()
    {
        setSession(null);
        setDetachState(); // 置为 分离 状态
    }

    public UserBean toUserBean()
    {
        UserBean userBean = new UserBean();

        userBean.setUserName(userName);
        userBean.setUserId(UniqueId.toBase36(userId));

        return userBean;
    }

    public RoleBean toRoleBean()
    {
        RoleBean roleBean = new RoleBean();
        roleBean.setUserId(UniqueId.toBase36(userId));
        roleBean.setRoleId(UniqueId.toBase36(roleId));
        roleBean.setRoleName(roleName);
        roleBean.setRoleLevel(roleLevel);
        roleBean.setPlatformId(platformId);
        roleBean.setServerId(serverId);
        roleBean.setMiscData(toJson().toString());

        return roleBean;
    }

    public RankBean toRankBean()
    {
        RankBean rankBean = new RankBean();

        rankBean.setUserId(UniqueId.toBase36(userId));
        rankBean.setRoleId(UniqueId.toBase36(roleId));
        rankBean.setRoleName(roleName);
        rankBean.setRoleHead(roleHead);
        rankBean.setRoleLevel(roleLevel);
        return rankBean;
    }

    /**
     * 购买月卡
     */
    public void buyMonthCard()
    {
        boolean isNewMonthCard = false; // 新月卡还是续期?
        long nowTime = System.currentTimeMillis();
        if (monthCardExpiryDate < nowTime)
            isNewMonthCard = true;

        long startTime = Math.max(nowTime, monthCardExpiryDate); // 计算累加月卡有效期起始时间点
        LOG.info("buyMonthCard, nowTime = " + nowTime + ", monthCardExpiryDate = " + monthCardExpiryDate + ", startTime = " + startTime);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startTime);
        cal.add(Calendar.DAY_OF_MONTH, 30); // 增加月卡有效期30天
        this.setMonthCardExpiryDate(cal.getTimeInMillis());
        //月卡到期时间更新
        this.SendAttributeChangeMsg(6, (int) (monthCardExpiryDate / 1000L));
        LOG.info("buyMonthCard, new monthCardExpiryDate = " + this.monthCardExpiryDate);

        if (isNewMonthCard) // 2014.10.29 王老板/梁晨要求新月卡购买时就发一次奖励
        {
            DBView.getInstance().issueMonthCardAward(roleId);
            LOG.info("buyMonthCard, isNewMonthCard issueMonthCardAward");
        }
    }

    // 检查是否重置购买体力次数
    private void checkBuyEnergyAndSkillNum()
    {
        // 跨天（5点）
        if (MiscUtils.across5(this.lastResetBuyNum, System.currentTimeMillis()))
        {
            resetBuyEnergy();
        }
        if (MiscUtils.across5(this.lastSkillBuyTime, System.currentTimeMillis()))
        {
            resetBuySkill();
        }
    }

    /**
     * 执行GM命令
     *
     * @param param
     */
    public void doGmCommand(String param)
    {
        ScriptArgs args = new ScriptArgs();
        args.put(ScriptArgs.Key.PLAYER, this);
        args.put(ScriptArgs.Key.STRING_VALUE, param);

        ScriptManager.getInstance().call(1002, args);
    }

    /**
     *
     * @return
     */
    public PlayerMessage.PlayerInfo.Builder getPlayerInfo()
    {
        PlayerMessage.PlayerInfo.Builder builder = PlayerMessage.PlayerInfo.newBuilder();
        //builder.setUserId(UUIDUtils.toCompactString(userId));
        //builder.setRoleId(UUIDUtils.toCompactString(roleId));
        builder.setUserId(UniqueId.toBase36(userId));
        builder.setRoleId(UniqueId.toBase36(roleId));
        builder.setRoleLevel(getRoleLevel());
        builder.setRoleName(roleName);
        builder.setExp(getRoleExp());
        //builder.setVipLevel(vipManager.getVipLevel());
        builder.setEnergyBuyNum(energyBuyNum);
        builder.setMonthCardExpiryDate((int) (monthCardExpiryDate / 1000L));
        //builder.setQuestNum(getQuestionNum());

        return builder;
    }

    /**
     * 发送ResPlayerInfo消息
     */
    public void SendPlayerInfoMsg()
    {
       
    }

    /**
     *
     * @param type 属性类型 (1:当前等级 2:当前经验 3:vip等级 4:当日体力免费领取次数 5:当日体力购买次数 6:月卡到期时间
     * 7:技能点购买次数)
     * @param value 属性值
     */
    public void SendAttributeChangeMsg(int type, int value)
    {

    }

    /**
     * 设置Player为正常游戏状态
     */
    public void setNormalState()
    {
        playerState.setState(PlayerState.State.NORMAL);
    }

    /**
     * 设置Player为分离状态
     */
    public void setDetachState()
    {
        playerState.setState(PlayerState.State.DETACH);
    }

    public PlayerState getPlayerState()
    {
        return playerState;
    }

    /**
     * 调用角色等级改变脚本
     */
    private void callSetRoleLevelScript()
    {
        ScriptArgs args = new ScriptArgs();
        args.put(ScriptArgs.Key.ARG1, this);
        args.put(ScriptArgs.Key.ARG2, getSession());

        ScriptManager.getInstance().call(1013, args);
    }

    /**
     * 调用角色名改变脚本
     */
    private void callSetRoleNameScript()
    {
        ScriptArgs args = new ScriptArgs();
        args.put(ScriptArgs.Key.ARG1, this);
        args.put(ScriptArgs.Key.ARG2, getSession());

        ScriptManager.getInstance().call(1014, args);
    }

    private void resetBuyEnergy()
    {
        // 购买次数刷新
        this.setEnergyBuyNum(0);
        this.setLastResetBuyNum(System.currentTimeMillis());
        this.SendAttributeChangeMsg(5, this.getEnergyBuyNum());
    }

    /**
     * 重置购买技能点次数和时间
     */
    private void resetBuySkill()
    {
        // 购买次数刷新
        this.setSkillBuyNum(0);
        this.setLastSkillBuyTime(System.currentTimeMillis());
        this.SendAttributeChangeMsg(7, this.getSkillBuyNum());
    }

    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public Integer getLineId()
    {
        return lineId;
    }

    public void setLineId(Integer lineId)
    {
        this.lineId = lineId;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(long roleId)
    {
        this.roleId = roleId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setSoleId(String soleId)
    {
        this.soleId = soleId;
    }

    public String getSoleId()
    {
        return soleId;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
        callSetRoleNameScript();
    }

    public int getRoleHead()
    {
        return roleHead;
    }

    public void setRoleHead(int roleHead)
    {
        this.roleHead = roleHead;
    }

    public String getHeadURL()
    {
        return headURL;
    }

    public void setHeadURL(String headURL)
    {
        this.headURL = headURL;
    }
    
    public int getRoleLevel()
    {
        return this.roleLevel;
    }

    public int getRoleExp()
    {
        return 0;
    }

    /**
     * 获取当前开放的等级上限
     *
     * @return
     */
    public static int getRoleLevelLimit()
    {
        final int defaultLevel = 60;
        if (BeanTemplet.getGlobalBean(1046) == null)
        {
            return defaultLevel;
        }
        return BeanTemplet.getGlobalBean(1046).getQ_int_value();
    }

    public void setRoleLevel(int roleLevel)
    {
//        final int MAX_LEVEL = getRoleLevelLimit();
//        if (roleLevel > MAX_LEVEL)
//        {
//            roleLevel = MAX_LEVEL;
//        }
        if (this.roleLevel >= roleLevel)
        {
            return;
        }
        int oldRoleLevel = this.roleLevel; // 之前的等级
        this.roleLevel = roleLevel;
        DBView.getInstance().setRoleLevel(roleId, oldRoleLevel, roleLevel);
        callSetRoleLevelScript();

        //体力增加，体力上限也增加
        double nFlag = backpackManager.getResource(ResourceType.ENERGY).getProducer().getLimit() / 2;
        int addEnergy = (int) Math.ceil(nFlag);
        for (int i = 1; i < (this.roleLevel - oldRoleLevel); i++)
        {
            addEnergy += (addEnergy + 1);
        }
        //这里不用通知客户端，flushResourceList会通知客户端
        backpackManager.addResource(ResourceType.ENERGY, addEnergy, false, Reasons.PLAYER_LEVEL_UP, Calendar.getInstance().getTime());
        backpackManager.flushResourceList(true);

        featureManager.trigger(null);
        //触发是否有等级解锁的任务
        dailyTaskManager.checkUnlockTask();

        // 更新World数据
        if (roleLevel > 1)
        {
            GameWorld.getInstance().addCommand(new LWPlayerUpdateHandler(this));
        }
    }

    public void setRobotLevel(int robotLevel)
    {
        if (isRobot)
        {
            roleLevel = robotLevel;
        }
        else
        {
            LOG.error("只有机器人才能调用此接口。");
        }
    }

    public boolean isRobot()
    {
        return isRobot;
    }

    public String getFgi()
    {
        return fgi;
    }

    public void setFgi(String fgi)
    {
        this.fgi = fgi;
    }

    public String getFedId()
    {
        return fedId;
    }

    public int getPlatformId()
    {
        return platformId;
    }

    public void setPlatformId(int platform)
    {
        this.platformId = platform;
    }

    public int getServerId()
    {
        return serverId;
    }

    public void setServerId(int server)
    {
        this.serverId = server;
    }

    public Token getToken()
    {
        return token;
    }

    public void setToken(Token token)
    {
        this.token = token;
    }

    public IoSession getSession()
    {
        return session;
    }

    public void setSession(IoSession session)
    {
        this.session = session;
    }

    public IoSession getUDPSession()
    {
        return udpSession;
    }

    public void setUDPSession(IoSession session)
    {
        this.udpSession = session;
    }
    
    public long getLastConnectTime()
    {
        return lastConnectTime;
    }

    public void setLastConnectTime(long lastConnectTime)
    {
        this.lastConnectTime = lastConnectTime;
    }

//    public int getRoleExp()
//    {
//        return roleExp;
//    }
//
//    public void setRoleExp(int roleExp)
//    {
//        this.roleExp = roleExp;
//    }
    public int getGmLevel()
    {
        return gmLevel;
    }

    public void setGmLevel(int gmLevel)
    {
        if (gmLevel < 0 || gmLevel > 2)
        {
            return;
        }
        this.gmLevel = gmLevel;
    }

    public long getLastSaveTime()
    {
        return lastSaveTime;
    }

    public void setLastSaveTime(long lastSaveTime)
    {
        this.lastSaveTime = lastSaveTime;
    }

    public String getLastSaveDataDigest()
    {
        return lastSaveDataDigest;
    }

    public void setLastSaveDataDigest(String digest)
    {
        this.lastSaveDataDigest = digest;
    }

    public Set<Integer> getScriptFlag()
    {
        return scriptFlag;
    }

    public long getCreateRoleTime()
    {
        return createRoleTime;
    }

    public void setCreateRoleTime(long createRoleTime)
    {
        this.createRoleTime = createRoleTime;
    }

    public BackpackManager getBackpackManager()
    {
        return backpackManager;
    }

    public DailyTaskManager getDailyTaskManager()
    {
        return dailyTaskManager;
    }

    public MailManager getMailManager()
    {
        return mailManager;
    }

    public StoreManager getStoreManager()
    {
        return storeManager;
    }

    public FeatureManager getFeatureManager()
    {
        return featureManager;
    }

    public OperActivityManager getOperActivityManager()
    {
        return operActivityMgr;
    }

    public SignInManager getSignInManager()
    {
        return signInManager;
    }
/*
    public KeysManager getKeysManager()
    {
        return keysManager;
    }
*/
    public LoginGiftManager getLoginGiftManager()
    {
        return loginGiftManager;
    }
/*
    public GuideManager getGuideManager()
    {
        return guideManager;
    }*/

    public RechargeManager getRechargeManager()
    {
        return rechargeManager;
    }

    public VipManager getVipManager()
    {
        return vipManager;
    }

    public long getLastResetBuyNum()
    {
        return lastResetBuyNum;
    }

    public void setLastResetBuyNum(long lastResetBuyNum)
    {
        this.lastResetBuyNum = lastResetBuyNum;
    }

    public int getEnergyBuyNum()
    {
        return energyBuyNum;
    }

    public void setEnergyBuyNum(int energyBuyNum)
    {
        this.energyBuyNum = energyBuyNum;
    }

    public long getLastSkillBuyTime()
    {
        return lastSkillBuyTime;
    }

    public void setLastSkillBuyTime(long lastSkillBuyTime)
    {
        this.lastSkillBuyTime = lastSkillBuyTime;
    }

    public int getSkillBuyNum()
    {
        return skillBuyNum;
    }

    public void setSkillBuyNum(int skillBuyNum)
    {
        this.skillBuyNum = skillBuyNum;
    }

    public long getMonthCardExpiryDate()
    {
        return monthCardExpiryDate;
    }

    public void setMonthCardExpiryDate(long monthCardExpiryDate)
    {
        this.monthCardExpiryDate = monthCardExpiryDate;
        DBView.getInstance().setMonthCardExpiryDate(roleId, this.monthCardExpiryDate);
    }

    public int getFriendNum()
    {
        return friendNum;
    }

    public void modifyFriendNum(int friendNum)
    {
    }

    /**
     * 获取好友数量对体力上限的加成; 好友数量>10, 体力上限+20; 小于10, 每少一个-2
     *
     * @return
     */
    public int getFriendNumEnergyAddition()
    {
        int result = 0;
        int friendNumEnergyAddition = 10; //BeanTemplet.getFriendNumEnergyAddition();
        if (friendNum > friendNumEnergyAddition)
            result = 2 * friendNumEnergyAddition;
        else if (friendNum >= 0 && friendNum <= friendNumEnergyAddition)
            result = 2 * friendNum;
        else
            LOG.error("getFriendNumEnergyAddition friendNum = " + friendNum);
        return result;
    }

    public RebateManager getRebateManager()
    {
        return rebateManager;
    }

    public List<Integer> getQuestionFinishedList()
    {
        return QuestionFinishedList;
    }

    public void setQuestionFinishedList(List<Integer> QuestionFinishedList)
    {
        this.QuestionFinishedList = QuestionFinishedList;
    }

    // 获得当前问卷序列
    public int getQuestionNum()
    {
        q_globalBean globalBean = BeanTemplet.getGlobalBean(1183);
        if (globalBean == null)
        {
            LOG.error("获得当前问卷序列时，全局配置错误：1183");
            return 0;
        }
        // 是否已经答过问卷
        if (false == QuestionFinishedList.contains(globalBean.getQ_int_value()))
        {
            return globalBean.getQ_int_value();
        }

        return 0;
    }

    /**
     * 获取玩家当前指定资源的数量.
     * <p>
     * ps：修改请调用BackpackManager的addResource或subResource.
     *
     * @param type 指定资源
     * @return
     */
    public int getResource(ResourceType type)
    {
        Resource res = this.backpackManager.getResource(type);
        return res != null ? res.getAmount() : 0;
    }

    /**
     * 获取玩家当前体力.
     * <p>
     * ps：修改请调用BackpackManager的addResource或subResource.
     *
     * @return
     */
    public int getEnergy()
    {
        Resource res = this.backpackManager.getResource(ResourceType.ENERGY);
        return res != null ? res.getAmount() : 0;
    }

    /**
     * 获取玩家当前金币.
     * <p>
     * ps：修改请调用BackpackManager的addResource或subResource.
     *
     * @return
     */
    public int getGold()
    {
        Resource res = this.backpackManager.getResource(ResourceType.GOLD);
        return res != null ? res.getAmount() : 0;
    }

    /**
     * 获取玩家当前元宝.
     * <p>
     * ps：修改请调用BackpackManager的addResource或subResource.
     *
     * @return
     */
    public int getGoldBullion()
    {
        Resource res = this.backpackManager.getResource(ResourceType.GOLD_BULLION);
        return res != null ? res.getAmount() : 0;
    }

    /**
     * 获取玩家当前木材.
     * <p>
     * ps：修改请调用BackpackManager的addResource或subResource.
     *
     * @return
     */
    public int getWood()
    {
        Resource res = this.backpackManager.getResource(ResourceType.WOOD);
        return res != null ? res.getAmount() : 0;
    }

    /**
     * 获取玩家当前星魂.
     * <p>
     * ps：修改请调用BackpackManager的addResource或subResource.
     *
     * @return
     */
    public int getStarSoul()
    {
        Resource res = this.backpackManager.getResource(ResourceType.STAR_SOUL);
        return res != null ? res.getAmount() : 0;
    }

    /**
     * 获取玩家当前成就点.
     * <p>
     * ps：修改请调用BackpackManager的addResource或subResource.
     *
     * @return
     */
    public int getAchievement()
    {
        Resource res = this.backpackManager.getResource(ResourceType.ACHIEVEMENT);
        return res != null ? res.getAmount() : 0;
    }

    /**
     * 获取玩家当前星钻数量.
     * <p>
     * ps：修改请调用BackpackManager的addResource或subResource.
     *
     * @return
     */
    public int getStarDiamond()
    {
        Resource res = this.backpackManager.getResource(ResourceType.STAR_DIAMOND);
        return res != null ? res.getAmount() : 0;
    }

    /**
     * 获取玩家当前精力.
     * <p>
     * ps：修改请调用BackpackManager的addResource或subResource.
     *
     * @return
     */
    public int getMana()
    {
        Resource res = this.backpackManager.getResource(ResourceType.MANA);
        return res != null ? res.getAmount() : 0;
    }

    /**
     * 获取玩家当前荣誉值.
     * <p>
     * ps：修改请调用BackpackManager的addResource或subResource.
     *
     * @return
     */
    public int getHonor()
    {
        Resource res = this.backpackManager.getResource(ResourceType.HONOR);
        return res != null ? res.getAmount() : 0;
    }

    /**
     * 获取技能点
     *
     * @return
     */
    public int getSkillPoint()
    {
        Resource res = this.backpackManager.getResource(ResourceType.SKILL_POINT);
        return res != null ? res.getAmount() : 0;
    }

    /**
     * 获取上次搜索出的仙府基地列表（仙府ID）.
     *
     * @return 列表不会为空，没有记录时size为0
     */
    public List<String> getSearchBaseList()
    {
        return searchBaseList;
    }

    @Override
    public JSON toJson()
    {
        if (!allLoading)
        {
            throw new LogicException("没有载入全部数据(fromJson), 不能转出JSON!");
        }

        JSONObject playerJsonObj = new JSONObject();
        { // 基本信息
            playerJsonObj.put(JsonKey.ROLE_HEAD.getKey(), roleHead);
            playerJsonObj.put(JsonKey.LAST_CONNECT_TIME.getKey(), lastConnectTime);
            playerJsonObj.put(JsonKey.CREATE_ROLE_TIME.getKey(), createRoleTime);
            playerJsonObj.put(JsonKey.ENERGY_BUY_NUM.getKey(), this.energyBuyNum);
            playerJsonObj.put(JsonKey.MONTH_CARD_EXPIRY_DATE.getKey(), this.monthCardExpiryDate);
            playerJsonObj.put(JsonKey.LAST_RESET_BUY_NUM.getKey(), this.lastResetBuyNum);
            playerJsonObj.put(JsonKey.SKILLPOINT_BY_NUM.getKey(), this.skillBuyNum);
            playerJsonObj.put(JsonKey.LAST_RESET_BUY_SKILL.getKey(), this.lastSkillBuyTime);
            playerJsonObj.put(JsonKey.LAST_WORLD_CHAT_TIME.getKey(), this.lastWorldChatTime);
            playerJsonObj.put(JsonKey.ROOM_GAME_TYPE.getKey(), this.curGameType);
            playerJsonObj.put(JsonKey.ROOM_TYPE.getKey(), this.curRoomType);
            playerJsonObj.put(JsonKey.ROOM_NUMBER.getKey(), this.curRoomNum);
            
            playerJsonObj.put(JsonKey.FRIEND_NUM.getKey(), this.friendNum);
            { // scriptFlag
                JSONArray scriptFlagArray = new JSONArray();
                scriptFlagArray.addAll(scriptFlag);
                playerJsonObj.put(JsonKey.SCRIPT_FLAG.getKey(), scriptFlagArray);
            }
            {// 问卷调查
                JSONArray questionArray = new JSONArray();
                questionArray.addAll(QuestionFinishedList);
                playerJsonObj.put(JsonKey.QUESTION_LIST.getKey(), questionArray);
            }
        }
        playerJsonObj.put(JsonKey.DENY_CHAT_JSON_KEY.getKey(), this.denyChatTimestamp);//禁聊结束时间, 在此之前禁止聊天
        playerJsonObj.put(JsonKey.BACKPACK_JSON_KEY.getKey(), backpackManager.toJson()); // 背包数据
        playerJsonObj.put(JsonKey.DAILY_TASK_JSON_KEY.getKey(), dailyTaskManager.toJson()); // 日常任务数据
        playerJsonObj.put(JsonKey.STORE_JSON_KEY.getKey(), storeManager.toJson()); // 商店数据
        playerJsonObj.put(JsonKey.SIGNIN_JSON_KEY.getKey(), signInManager.toJson()); // 签到数据
        //playerJsonObj.put(JsonKey.GUIDE.getKey(), guideManager.toJson()); // 新手引导数据
        playerJsonObj.put(JsonKey.RECHARGE.getKey(), rechargeManager.toJson()); // 充值相关数据
        playerJsonObj.put(JsonKey.VIP_MGR.getKey(), vipManager.toJson()); // 充值相关数据
        playerJsonObj.put(JsonKey.REBATE_JSON_KEY.getKey(), rebateManager.toJson()); // 返利数据
        //playerJsonObj.put(JsonKey.KEYS_JSON_KEY.getKey(), keysManager.toJson()); // 激活码/兑换码数据
        playerJsonObj.put(JsonKey.LOGINGIFT_JSON_KEY.getKey(), loginGiftManager.toJson()); //登陆有奖活动数据
        playerJsonObj.put(JsonKey.OPER_ACTIVITY.getKey(), operActivityMgr.toJson());//运营活动

        // 其他系统 toJson ...
        return playerJsonObj;
    }

    @Override
    public void fromJson(JSON json)
    {
        JSONObject playerJsonObj = (JSONObject) json;

        {   //基本信息
            setRoleHead(playerJsonObj.getIntValue(JsonKey.ROLE_HEAD.getKey()));
            setLastConnectTime(playerJsonObj.getLong(JsonKey.LAST_CONNECT_TIME.getKey()));
            createRoleTime = playerJsonObj.getLong(JsonKey.CREATE_ROLE_TIME.getKey());
            this.setEnergyBuyNum(playerJsonObj.getIntValue(JsonKey.ENERGY_BUY_NUM.getKey()));
            if (playerJsonObj.containsKey(JsonKey.LAST_RESET_BUY_NUM.getKey()))
                this.setLastResetBuyNum(playerJsonObj.getLong(JsonKey.LAST_RESET_BUY_NUM.getKey()));
            this.setSkillBuyNum(playerJsonObj.getIntValue(JsonKey.SKILLPOINT_BY_NUM.getKey()));
            if (playerJsonObj.containsKey(JsonKey.LAST_RESET_BUY_SKILL.getKey()))
                this.setLastSkillBuyTime(playerJsonObj.getLong(JsonKey.LAST_RESET_BUY_SKILL.getKey()));
            if (playerJsonObj.containsKey(JsonKey.MONTH_CARD_EXPIRY_DATE.getKey()))
                this.setMonthCardExpiryDate(playerJsonObj.getLongValue(JsonKey.MONTH_CARD_EXPIRY_DATE.getKey()));
            if (playerJsonObj.containsKey(JsonKey.FRIEND_NUM.getKey()))
                this.friendNum = playerJsonObj.getIntValue(JsonKey.FRIEND_NUM.getKey());
            if (playerJsonObj.containsKey(JsonKey.LAST_WORLD_CHAT_TIME.getKey()))
                this.lastWorldChatTime = playerJsonObj.getIntValue(JsonKey.LAST_WORLD_CHAT_TIME.getKey());
            this.denyChatTimestamp = playerJsonObj.getIntValue(JsonKey.DENY_CHAT_JSON_KEY.getKey());

            this.curGameType = playerJsonObj.getIntValue(JsonKey.ROOM_GAME_TYPE.getKey());
            this.curRoomType = playerJsonObj.getIntValue(JsonKey.ROOM_TYPE.getKey());
            this.curRoomNum  =playerJsonObj.getIntValue(JsonKey.ROOM_NUMBER.getKey());
            
            if (roleLevel == 0)
                roleLevel = 1;
            { // scriptFlag
                if (playerJsonObj.containsKey(JsonKey.SCRIPT_FLAG.getKey()))
                {
                    JSONArray scriptFlagArray = playerJsonObj.getJSONArray(JsonKey.SCRIPT_FLAG.getKey());
                    for (int i = 0; i < scriptFlagArray.size(); ++i)
                    {
                        scriptFlag.add(scriptFlagArray.getInteger(i));
                    }
                }
            }
            {// 问卷调查
                if (playerJsonObj.containsKey(JsonKey.QUESTION_LIST.getKey()))
                {
                    JSONArray questionArray = playerJsonObj.getJSONArray(JsonKey.QUESTION_LIST.getKey());
                    for (int i = 0; i < questionArray.size(); ++i)
                    {
                        QuestionFinishedList.add(questionArray.getInteger(i));
                    }
                }
            }
        }

        { // backpackManager fromJson
            JSONObject jsonObj = playerJsonObj.getJSONObject(JsonKey.BACKPACK_JSON_KEY.getKey());
            backpackManager.fromJson(jsonObj);
        }

        { // dailyTaskManager fromJson
            JSONObject jsonObj = playerJsonObj.getJSONObject(JsonKey.DAILY_TASK_JSON_KEY.getKey());
            dailyTaskManager.fromJson(jsonObj);
        }

        { // buildingManager fromJson
            JSONObject jsonObj = playerJsonObj.getJSONObject(JsonKey.STORE_JSON_KEY.getKey());
            storeManager.fromJson(jsonObj);
        }

        { // signInManager fromJson
            JSONObject jsonObj = playerJsonObj.getJSONObject(JsonKey.SIGNIN_JSON_KEY.getKey());
            signInManager.fromJson(jsonObj);
        }

        { // 新手引导
            JSONObject jsonObj = playerJsonObj.getJSONObject(JsonKey.GUIDE.getKey());
           // guideManager.fromJson(jsonObj);
        }
        { // 充值相关
            JSONObject jsonObj = playerJsonObj.getJSONObject(JsonKey.RECHARGE.getKey());
            rechargeManager.fromJson(jsonObj);
        }
        { // VIP相关
            JSONObject jsonObj = playerJsonObj.getJSONObject(JsonKey.VIP_MGR.getKey());
            vipManager.fromJson(jsonObj);
        }

        {
            JSONObject jsonObj = playerJsonObj.getJSONObject(JsonKey.REBATE_JSON_KEY.getKey());
            rebateManager.fromJson(jsonObj);
        }
        {
            JSONObject jsonObj = playerJsonObj.getJSONObject(JsonKey.KEYS_JSON_KEY.getKey());
            //keysManager.fromJson(jsonObj);
        }
        {
            JSONObject jsonObj = playerJsonObj.getJSONObject(JsonKey.LOGINGIFT_JSON_KEY.getKey());
            loginGiftManager.fromJson(jsonObj);
        }

        {
            JSONObject jsonObj = playerJsonObj.getJSONObject(JsonKey.OPER_ACTIVITY.getKey());
            operActivityMgr.fromJson(jsonObj);
        }
        // 其他系统 fromJson ...

        allLoading = true;
    }

    private void fromJsonForRobot(JSON json)
    {
        JSONObject playerJsonObj = (JSONObject) json;

        {
            setRoleHead(playerJsonObj.getIntValue(JsonKey.ROLE_HEAD.getKey()));
            setLastConnectTime(playerJsonObj.getLong(JsonKey.LAST_CONNECT_TIME.getKey()));
            createRoleTime = playerJsonObj.getLong(JsonKey.CREATE_ROLE_TIME.getKey());
        }
        allLoading = true; // 其他数据对机器人无用
    }

    private void fromJsonForCards(JSON json)
    {
        JSONObject playerJsonObj = (JSONObject) json;

        {
            setRoleHead(playerJsonObj.getIntValue(JsonKey.ROLE_HEAD.getKey()));
            setLastConnectTime(playerJsonObj.getLong(JsonKey.LAST_CONNECT_TIME.getKey()));
            createRoleTime = playerJsonObj.getLong(JsonKey.CREATE_ROLE_TIME.getKey());
        }
        allLoading = false;
    }

    /**
     * 定时器tick
     */
    public void tick()
    {
        //LOG.info("in tick, roleId: [" + String.valueOf(this.roleId) + "], roleName: [" + roleName + "]");
    }

    /**
     * 跨天tick
     */
    public void acrossDay()
    {
        LOG.info("in acrossDay, roleId: [" + roleId + "], roleName: [" + roleName + "]");
        featureManager.trigger(null);  // 功能开放 （开服时间检测）
    }

    /**
     * 凌晨5点定时器
     */
    public void tick5()
    {
        getDailyTaskManager().tick5(); // 日常任务重置
        resetBuyEnergy();//重置购买体力次数
        resetBuySkill(); //重置购买技能点次数
        signInManager.resetSignInNum(); // 签到重置
        getSignInManager().tick5();//签到

    }

    public String getClientVer()
    {
        return clientVer;
    }

    public void setClientVer(String clientVer)
    {
        this.clientVer = clientVer;
    }

    public String getDevice()
    {
        return device;
    }

    public void setDevice(String device)
    {
        this.device = device;
    }

    public String getPlatform_uid()
    {
        return platform_uid;
    }

    public void setPlatform_uid(String platform_uid)
    {
        this.platform_uid = platform_uid;
    }

    public String getClient()
    {
        return client;
    }

    public void setClient(String client)
    {
        this.client = client;
    }

    public long getLastWorldChatTime()
    {
        return lastWorldChatTime;
    }

    public void setLastWorldChatTime(long lastWorldChatTime)
    {
        this.lastWorldChatTime = lastWorldChatTime;
    }

    public int getDenyChatTimestamp()
    {
        return denyChatTimestamp;
    }

    public void setDenyChatTimestamp(int denyChatTimestamp)
    {
        this.denyChatTimestamp = denyChatTimestamp;
        GameWorld.getInstance().refreshDenyChatTimes(userId, this.denyChatTimestamp);
    }

    public int getCurRoomNum()
    {
        return this.curRoomNum;
    }

    public void setCurRoomNum(int roomNum)
    {
        this.curRoomNum = roomNum;
    }

    public int getCurRoomType()
    {
        return this.curRoomType;
    }

    public void setCurRoomType(int roomType)
    {
        this.curRoomType = roomType;
    }
    
    public int getCurGameType()
    {
        return this.curGameType;
    }

    public void setCurGameType(int gameType)
    {
        this.curGameType = gameType;
    }
    
}