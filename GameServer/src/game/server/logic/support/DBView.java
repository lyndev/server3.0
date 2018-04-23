package game.server.logic.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.core.script.ScriptManager;
import game.server.db.game.bean.UserRoleBean;
import game.server.db.game.dao.UserDao;
import game.server.logic.constant.JsonKey;
import game.server.logic.struct.Player;
import game.server.logic.util.ScriptArgs;
import game.server.util.MiscUtils;
import game.server.util.UniqueId;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author ZouZhaopeng
 */
public class DBView
{
    private final static Logger log = Logger.getLogger(DBView.class);

    private final Map<Long, RoleView> roles = new HashMap<>();
    private final Map<String, RoleView> userNames = new HashMap<>();
    private final Map<Long, RoleView> userIds = new HashMap<>();
    private final Map<String, RoleView> roleNames = new HashMap<>();
    private final Map<String, RoleView> soleIds = new HashMap<>();
    private final Map<Integer, Map<Long, RoleView>> roleLevels = new HashMap<>(); // roleLevel --> (roleId --> RoleView) 为好友推介功能添加

    public static DBView getInstance()
    {
        return Singleton.INSTANCE.getView();
    }

    /**
     * 深复制roles容器, 由于有可能有多线程调用该对象的方法, 故此处不能直接返回成员容器; 调用此方法有一定消耗, 不应被频繁调用
     *
     * @return
     */
    public synchronized List<RoleView> getRoles()
    {
        List<RoleView> retRoles = new ArrayList<>(roles.size());
        for (RoleView roleView : roles.values())
        {
            try
            {
                retRoles.add(roleView.clone());
            }
            catch (CloneNotSupportedException ex)
            {
                log.error("clone RoleView exception", ex);
            }
        }
        return retRoles;
    }

    /**
     * 根据RoleId获取单个玩家视图
     *
     * @param roleId
     * @return
     */
    public synchronized RoleView getSingleRoleByRoleId(String roleId)
    {
        RoleView roleView = null;
        try
        {
            RoleView tmp = roles.get(UniqueId.toBase10(roleId));
            if (tmp != null)
                roleView = tmp.clone();
        }
        catch (CloneNotSupportedException ex)
        {
            log.error("clone RoleView exception", ex);
        }

        return roleView;
    }

    /**
     * 根据RoleName获取单个玩家视图
     *
     * @param roleName
     * @return
     */
    public synchronized RoleView getSingleRoleByRoleName(String roleName)
    {
        RoleView roleView = null;
        try
        {
            RoleView tmp = roleNames.get(roleName);
            if (tmp != null)
                roleView = tmp.clone();
        }
        catch (CloneNotSupportedException ex)
        {
            log.error("clone RoleView exception", ex);
        }

        return roleView;
    }

    /**
     * 根据UserId获取单个玩家视图
     *
     * @param userId
     * @return
     */
    public synchronized RoleView getSingleRoleByUserId(String userId)
    {
        RoleView roleView = null;
        try
        {
            RoleView tmp = userIds.get(UniqueId.toBase10(userId));
            if (tmp != null)
                roleView = tmp.clone();
        }
        catch (CloneNotSupportedException ex)
        {
            log.error("clone RoleView exception", ex);
        }

        return roleView;
    }

    /**
     * 返回VIP等级在一定范围内的玩家视图 ps: 区间是一个闭区间 [min, max]
     *
     * @param min vip下限
     * @param max vip上限
     * @return
     */
    public synchronized List<RoleView> getRolesByVipLevel(int min, int max)
    {
        List<RoleView> retRoles = new ArrayList<>(roles.size());
        for (RoleView roleView : roles.values())
        {
            try
            {
                if (roleView.getVipLevel() >= min && roleView.getVipLevel() <= max)
                    retRoles.add(roleView.clone());
            }
            catch (CloneNotSupportedException ex)
            {
                log.error("clone RoleView exception", ex);
            }
        }
        return retRoles;
    }

    /**
     * 返回角色等级在一定范围内的玩家视图 ps: 区间是一个闭区间 [min, max]
     *
     * @param min 角色等级下限
     * @param max 角色等级上限
     * @return
     */
    public synchronized List<RoleView> getRolesByRoleLevel(int min, int max)
    {
        List<RoleView> retRoles = new ArrayList<>(roles.size());
        for (RoleView roleView : roles.values())
        {
            try
            {
                if (roleView.getRoleLevel() >= min && roleView.getRoleLevel() <= max)
                    retRoles.add(roleView.clone());
            }
            catch (CloneNotSupportedException ex)
            {
                log.error("clone RoleView exception", ex);
            }
        }
        return retRoles;
    }

    /**
     * 返回仙府等级在一定范围内的玩家视图 ps: 区间是一个闭区间 [min, max]
     *
     * @param min 仙府等级下限
     * @param max 仙府等级上限
     * @return
     */
    public synchronized List<RoleView> getRolesByXFLevel(int min, int max)
    {
        List<RoleView> retRoles = new ArrayList<>(roles.size());
        for (RoleView roleView : roles.values())
        {
            try
            {
                if (roleView.getXfLevel() >= min && roleView.getXfLevel() <= max)
                    retRoles.add(roleView.clone());
            }
            catch (CloneNotSupportedException ex)
            {
                log.error("clone RoleView exception", ex);
            }
        }
        return retRoles;
    }

    /**
     * 返回创建时间在一定范围内的玩家视图 ps: 区间是一个闭区间 [min, max]
     *
     * @param min 创建时间下限
     * @param max 创建时间上限
     * @return
     */
    public synchronized List<RoleView> getRolesByCreateTime(long min, long max)
    {
        List<RoleView> retRoles = new ArrayList<>(roles.size());
        for (RoleView roleView : roles.values())
        {
            try
            {
                if (roleView.getCreateTime() >= min && roleView.getCreateTime() <= max)
                    retRoles.add(roleView.clone());
            }
            catch (CloneNotSupportedException ex)
            {
                log.error("clone RoleView exception", ex);
            }
        }
        return retRoles;
    }

    /**
     * 返回竞技场排名在一定范围内的玩家视图 ps: 区间是一个闭区间 [min, max]
     *
     * @param min 竞技场排名下限
     * @param max 竞技场排名上限
     * @return
     */
    public synchronized List<RoleView> getRolesByPVPRank(long min, long max)
    {
        List<RoleView> retRoles = new ArrayList<>(roles.size());
        for (RoleView roleView : roles.values())
        {
            try
            {
                if (roleView.getPvpRank() >= min && roleView.getPvpRank() <= max)
                    retRoles.add(roleView.clone());
            }
            catch (CloneNotSupportedException ex)
            {
                log.error("clone RoleView exception", ex);
            }
        }
        return retRoles;
    }

    /**
     * 返回指定服务器的玩家视图
     *
     * @param id
     * @return
     */
    public synchronized List<RoleView> getRolesByServer(int id)
    {
        List<RoleView> retRoles = new ArrayList<>(roles.size());
        for (RoleView roleView : roles.values())
        {
            try
            {
                if (roleView.getServerId() == id)
                    retRoles.add(roleView.clone());
            }
            catch (CloneNotSupportedException ex)
            {
                log.error("clone RoleView exception", ex);
            }
        }
        return retRoles;
    }

    /**
     * 返回指定平台的玩家视图
     *
     * @param id
     * @return
     */
    public synchronized List<RoleView> getRolesByPlatform(int id)
    {
        List<RoleView> retRoles = new ArrayList<>(roles.size());
        for (RoleView roleView : roles.values())
        {
            try
            {
                if (roleView.getPlatformId() == id)
                    retRoles.add(roleView.clone());
            }
            catch (CloneNotSupportedException ex)
            {
                log.error("clone RoleView exception", ex);
            }
        }
        return retRoles;
    }

    /**
     * 返回指定UserId的玩家视图
     *
     * @param givenUserIds 玩家userId列表
     * @return
     */
    public synchronized List<RoleView> getRolesByUserID(List<Long> givenUserIds)
    {
        List<RoleView> retRoles = new ArrayList<>(roles.size());
        for (Long id : givenUserIds)
        {
            try
            {
                retRoles.add(userIds.get(id).clone());
            }
            catch (CloneNotSupportedException ex)
            {
                log.error("clone RoleView exception", ex);
            }
        }
        return retRoles;
    }

    public synchronized boolean hasUserName(String userName)
    {
        return userNames.containsKey(userName);
    }

    public synchronized boolean hasRoleName(String roleName)
    {
        return roleNames.containsKey(roleName);
    }
    
    public synchronized boolean hasSoleId(String soleId){
        return soleIds.containsKey(soleId);
    }
    
    public synchronized void addRoleView(RoleView roleView)
    {
        roles.put(UniqueId.toBase10(roleView.getRoleId()), roleView);
        roleNames.put(roleView.getRoleName(), roleView);
        userIds.put(UniqueId.toBase10(roleView.getUserId()), roleView);
        userNames.put(roleView.getUserName(), roleView);
        soleIds.put(roleView.getSoleId(), roleView);
        addRoleLevel(roleView.getRoleLevel(), roleView);
    }
    
    public synchronized void changeRoleViewRoleName( String beforeName, String newName)
    {
        RoleView view = roleNames.get(beforeName);
        view.setRoleName(newName);
        roleNames.remove(beforeName);
        roleNames.put(newName, view);     
    }

    /**
     * 加载所有RoleName
     */
    public synchronized void load()
    {
        log.info("load all user name");

        List<RoleView> roleViews = new LinkedList<>();
        List<UserRoleBean> dataBean = UserDao.selectAllPlayers();
        if (dataBean != null)
        {
            for (UserRoleBean roleBean : dataBean)
            {
                RoleView roleView = new RoleView();
                roleView.setUserId(roleBean.getUserId());
                roleView.setUserName(roleBean.getUserName());
                roleView.setSoleId(roleBean.getSoleId());
                roleView.setRoleId(roleBean.getRoleId());
                roleView.setRoleName(roleBean.getRoleName());
                
                roleView.setRoleLevel(roleBean.getRoleLevel());
                roleView.setVipLevel(roleBean.getVipLevel());

                roleView.setServerId(roleBean.getServerId());
                roleView.setPlatformId(roleBean.getPlatformId());
                roleView.setIsRobot(roleBean.getIsRobot());

                { // from MiscData
                    try
                    {
                        String miscData = MiscUtils.uncompressText(roleBean.getMiscData());
                        if (!miscData.trim().isEmpty())
                        {
                            JSONObject playerJsonObj = JSON.parseObject(miscData);

                            // 读取创建时间
                            roleView.setCreateTime(playerJsonObj.getLong(JsonKey.CREATE_ROLE_TIME.getKey()));

                            // 读取月卡有效期
                            if (playerJsonObj.containsKey(JsonKey.MONTH_CARD_EXPIRY_DATE.getKey()))
                                roleView.setMonthCardExpiryDate(playerJsonObj.getLongValue(JsonKey.MONTH_CARD_EXPIRY_DATE.getKey()));
                        }
                    }
                    catch (UnsupportedEncodingException ex)
                    {
                        log.error("UnsupportedEncodingException", ex);
                    }
                }

                roleViews.add(roleView);
            }
        }

        Map<Long, Map<Long, Long>> sequences = new HashMap<>(); // 启动时根据玩家数据重建sequences, 和UniqueId中的对比, 容错功能

        for (RoleView roleView : roleViews)
        {
            addRoleView(roleView);

            { // for userId sequence
                long userId = UniqueId.toBase10(roleView.getUserId());
                long serverId = UniqueId.parseServerId(userId);
                long sequence = UniqueId.parseSequence(userId);

                Map<Long, Long> serverSeq = sequences.get(serverId);
                if (serverSeq == null)
                {
                    serverSeq = new HashMap<>();
                    sequences.put(serverId, serverSeq);
                }

                if (!serverSeq.containsKey(UniqueId.FuncType.USER.value()))
                    serverSeq.put(UniqueId.FuncType.USER.value(), 1L);

                if (serverSeq.get(UniqueId.FuncType.USER.value()) < sequence)
                    serverSeq.put(UniqueId.FuncType.USER.value(), sequence);
            }
            { // for roleId sequence
                long roleId = UniqueId.toBase10(roleView.getRoleId());
                long serverId = UniqueId.parseServerId(roleId);
                long sequence = UniqueId.parseSequence(roleId);

                Map<Long, Long> serverSeq = sequences.get(serverId);
                if (serverSeq == null)
                {
                    serverSeq = new HashMap<>();
                    sequences.put(serverId, serverSeq);
                }

                if (!serverSeq.containsKey(UniqueId.FuncType.ROLE.value()))
                    serverSeq.put(UniqueId.FuncType.ROLE.value(), 1L);

                if (serverSeq.get(UniqueId.FuncType.ROLE.value()) < sequence)
                    serverSeq.put(UniqueId.FuncType.ROLE.value(), sequence);
            }
        }

        UniqueId.generatedFlushSequence(sequences);
    }

    /**
     * 刷新DBView中玩家VIP等级
     *
     * @param roleId
     * @param vipLevel
     */
    public synchronized void setVipLevel(long roleId, int vipLevel)
    {
        RoleView roleView = roles.get(roleId);
        if (roleView != null)
            roleView.setVipLevel(vipLevel);
    }

    /**
     * 刷新DBView中玩家PVP排名
     *
     * @param roleId
     * @param pvpRank
     */
    public synchronized void setPvpRank(long roleId, int pvpRank)
    {
        RoleView roleView = roles.get(roleId);
        if (roleView != null)
            roleView.setPvpRank(pvpRank);
    }

    /**
     * 刷新DBView中玩家仙府等级
     *
     * @param roleId
     * @param xfLevel
     */
    public synchronized void setXfLevel(long roleId, int xfLevel)
    {
        RoleView roleView = roles.get(roleId);
        if (roleView != null)
            roleView.setXfLevel(xfLevel);
    }

    /**
     * 刷新DBView中玩家等级
     *
     * @param roleId
     * @param oldRoleLevel 原等级
     * @param newRoleLevel 新等级
     */
    public synchronized void setRoleLevel(long roleId, int oldRoleLevel, int newRoleLevel)
    {
        // 更新RoleView内容
        try
        {
            RoleView roleView = roles.get(roleId);
            if (roleView != null)
                roleView.setRoleLevel(newRoleLevel);
        }
        catch (Throwable t)
        {
            log.error("Throwable", t);
        }

        // 更新roleLevels容器
        try
        {
            Map<Long, RoleView> roleViews = roleLevels.get(oldRoleLevel);
            if (roleViews != null)
            {
                RoleView roleView = roleViews.get(roleId);
                if (roleView != null)
                {
                    roleViews.remove(roleId);
                    addRoleLevel(newRoleLevel, roleView);
                }
                else
                {
                    // 创建新角色时调用的createInitialize会触发一下日志, 属正常情况
                    log.warn("DBView找不到玩家记录, roleId = " + roleId + ", oldRoleLevel = " + oldRoleLevel + ", newRoleLevel = " + newRoleLevel);
                }
            }
            else
            {
                log.error("DBView找不到等级记录, roleId = " + roleId + ", oldRoleLevel = " + oldRoleLevel + ", newRoleLevel = " + newRoleLevel);
            }
        }
        catch (Throwable t)
        {
            log.error("Throwable", t);
        }
    }

    /**
     * 刷新DBView中玩家月卡有效期
     *
     * @param roleId
     * @param monthCardExpiryDate
     */
    public synchronized void setMonthCardExpiryDate(long roleId, long monthCardExpiryDate)
    {
        RoleView roleView = roles.get(roleId);
        if (roleView != null)
            roleView.setMonthCardExpiryDate(monthCardExpiryDate);
    }

    /**
     * 获取所有机器人数量
     *
     * @return
     */
    public synchronized int getRobotCount()
    {
        int count = 0;
        for (RoleView roleView : roles.values())
        {
            if (roleView.getIsRobot() > 0)
                ++count;
        }
        return count;
    }

    /**
     * 领取月卡奖励
     */
    public synchronized void issueMonthCardAward()
    {
        long nowTime = System.currentTimeMillis();
        List<RoleView> awardPlayers = new LinkedList<>();
        for (RoleView roleView : roles.values())
        {
            if (roleView.getIsRobot() <= 0 && roleView.getMonthCardExpiryDate() >= nowTime)
                awardPlayers.add(roleView);
        }

        if (!awardPlayers.isEmpty())
        {
            // call script.player.IssueMonthCardAwardScript
            ScriptArgs args = new ScriptArgs();
            args.put(ScriptArgs.Key.ARG1, awardPlayers);
            ScriptManager.getInstance().call(1028, args);
        }
    }

    public synchronized void issueMonthCardAward(long roleId)
    {
        List<RoleView> awardPlayers = new LinkedList<>();
        RoleView roleView = roles.get(roleId);
        if (roleView != null)
            awardPlayers.add(roleView);
        if (!awardPlayers.isEmpty())
        {
            // call script.player.IssueMonthCardAwardScript
            ScriptArgs args = new ScriptArgs();
            args.put(ScriptArgs.Key.ARG1, awardPlayers);
            ScriptManager.getInstance().call(1028, args);
        }
        else
        {
            log.error("issueMonthCardAward(long roleId) awardPlayers.isEmpty");
        }
    }

    /**
     * 更新是否在线
     *
     * @param roleId
     * @param online
     */
    public synchronized void setOnline(long roleId, boolean online)
    {
        RoleView roleView = roles.get(roleId);
        if (roleView != null)
        {
            roleView.setOnline(online);
        }
        else
        {
            log.error("setOnline cannot find RoleView, roleId = " + roleId);
        }
    }

    /**
     * 搜索好友, 匹配roleId和roleName
     *
     * @param key
     * @return
     */
    public synchronized List<Long> searchFriend(String key)
    {
        List<Long> list = new LinkedList<>();
        for (RoleView roleView : roles.values())
        {
            if (roleView.getIsRobot() == 0 && (roleView.getRoleId().contains(key) || roleView.getRoleName().equals(key)))
                list.add(UniqueId.toBase10(roleView.getUserId()));
        }
        return list;
    }

    /**
     * 推介好友等级搜索的顺序, 按顺序搜索等级"相近"的
     *
     * @param level
     * @return
     */
    private List<Integer> recommandFriendSearchLevels(int level)
    {
        List<Integer> searchLevels = new LinkedList<>(); // 返回结果: 等级搜索的顺序
        int minLevel = 1;
        int maxLevel = Player.getRoleLevelLimit();

        searchLevels.add(level);
        for (int i = 1; i < maxLevel; ++i)
        {
            int v1 = level + i;
            int v2 = level - i;
            if (v1 >= minLevel && v1 <= maxLevel)
                searchLevels.add(v1);
            if (v2 >= minLevel && v2 <= maxLevel)
                searchLevels.add(v2);
        }
        return searchLevels;
    }

    /**
     * 推荐好友搜索
     *
     * @param level
     * @param friends 好友userId集合
     * @return 推介好友的userId集合
     */
    public synchronized List<Long> recommandFriend(int level, long selfUserId, boolean online, Set<Long> friends)
    {
        List<Long> list = new LinkedList<>();
        if (level > 0)
        {
            int num = 30; // 筛选多少个？
            List<Integer> searchLevels = recommandFriendSearchLevels(level);
            for (Integer searchLevel : searchLevels)
            {
                Map<Long, RoleView> roleLevel = roleLevels.get(searchLevel);
                if (roleLevel != null)
                {
                    for (RoleView roleView : roleLevel.values())
                    {
                        try
                        {
                            long userId = UniqueId.toBase10(roleView.getUserId());
                            if (userId != selfUserId && roleView.getIsRobot() == 0 && roleView.isOnline() == online && !friends.contains(userId))
                            {
                                if (list.size() < num)
                                    list.add(userId);
                                else
                                    break;
                            }
                        }
                        catch (Throwable t)
                        {
                            log.error("Throwable", t);
                        }
                    }
                }
                if (list.size() >= num)
                    break;
            }
        }
        else
        {
            log.error("recommandFriend level = " + level);
        }
        return list;
    }

    /**
     * 添加roleLevels容器内容
     *
     * @param roleLevel
     * @param roleView
     */
    private void addRoleLevel(int roleLevel, RoleView roleView)
    {
        if (roleLevel >= 0)
        {
            Map<Long, RoleView> roleViews = roleLevels.get(roleLevel);
            if (roleViews == null)
            {
                roleViews = new HashMap<>();
                roleLevels.put(roleLevel, roleViews);
            }
            roleViews.put(UniqueId.toBase10(roleView.getRoleId()), roleView);
        }
        else
        {
            log.error("addRoleLevel 等级错误 roleLeve = " + roleLevel);
        }
    }

    /**
     * 获取所有离线玩家roleId(注意, 该方法不会返回机器人玩家!)
     *
     * @return
     */
    public synchronized List<String> getAllOfflinePlayerRoleId()
    {
        List<String> roleIds = new LinkedList<>();
        for (RoleView roleView : roles.values())
        {
            if (roleView.getIsRobot() == 0 && !roleView.isOnline())
                roleIds.add(roleView.getRoleId());
        }
        return roleIds;
    }

    private enum Singleton
    {
        INSTANCE;

        Singleton()
        {
            dbView = new DBView();
        }

        DBView getView()
        {
            return dbView;
        }

        DBView dbView;
    }
}
