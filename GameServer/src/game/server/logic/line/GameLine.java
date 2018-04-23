/**
 * @date 2014/4/28
 * @author ChenLong
 */
package game.server.logic.line;

import game.core.command.CommandProcessor;
import game.core.command.ICommand;
import game.server.db.game.bean.RoleBean;
import game.server.db.game.bean.RoleUpdateBean;
import game.server.logic.player.PlayerManager;
import game.server.logic.struct.Player;
import game.server.logic.struct.PlayerState;
import game.server.logic.util.LoginTransactionLock;
import game.server.thread.dboperator.GameDBOperator;
import game.server.thread.dboperator.handler.DBOperatorHandler;
import game.server.thread.dboperator.handler.ReqUpdateRoleHandler;
import game.server.util.MessageUtils;
import game.server.util.MiscUtils;
import game.server.util.UniqueId;
import game.server.world.login.handler.LWFlushDetachPlayerHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

/**
 * 表示服务器为玩家分配的一个内部的一个逻辑执行线程, 不对某一应游戏功能
 *
 * @author ChenLong
 */
public class GameLine extends CommandProcessor
{
    private final static Logger logger = Logger.getLogger(GameLine.class);

    private final int lineId; // 线ID
    private final Map<Long, Player> userPlayers = new ConcurrentHashMap<>(); // userId -> Player

    /**
     * 登录、创建角色跨线程简单事务锁（loginLock的范围包含createRoleLock）
     */
    private final LoginTransactionLock loginLock = new LoginTransactionLock();
    private final LoginTransactionLock createRoleLock = new LoginTransactionLock();

    /**
     * flush tick相关状态
     */
    private volatile boolean flushDBCompleted = true; // 是否完成DB操作
    private volatile int flushDBMaxNum = 4; // 每次DB操作对多数量
    private long nextFlushTime = System.currentTimeMillis(); // 下次flush tick时间

    /**
     * 定时回存相关
     */
    private static final long DEFAULT_FLUSH_SAVE_PLAY_DELAY = 50; // （单位：秒）内存中玩家回存默认间隔
    private long flushSavePlayerInterval = DEFAULT_FLUSH_SAVE_PLAY_DELAY; // （单位：秒）内存中玩家回存间隔

    /**
     * 分离 状态玩家从内存中清除规则：
     * 1.该线玩家对象数达到flushDetachPlayerWaterLevel并且分离时间超过flushDetachPlayerWaterLevelDelay的玩家
     * 2.该线玩家对象数未达到flushDetachPlayerWaterLevel并且分离时间超过flushDetachPlayerDelay的玩家
     */
    // （单位：个）清除分离状态玩家水位（该线中玩家对象达到该值时将分离状态的玩家清除出内存）
    private long flushDetachPlayerWaterLevel = 1500 / Runtime.getRuntime().availableProcessors();

    // （单位：秒）达到清除分离状态玩家水位，并且分离时间超过此值时从内存中清除
    private long flushDetachPlayerWaterLevelDelay = 5 * 60; // 5分钟

    private long flushDetachPlayerDelay = 1 * 60 * 60; // （单位：秒） 1小时

    public GameLine(int lineId)
    {
        super(GameLine.class.getSimpleName() + "_" + lineId);
        this.lineId = lineId;
        if (MiscUtils.isIDEEnvironment())
            flushSavePlayerInterval = 1; // IDE环境下每秒回存
    }

    /**
     * 定时器tick
     */
    public void tick()
    {
        long currentTimeMillis = System.currentTimeMillis();

        loginLock.tick(currentTimeMillis); // 清除超时登录事务
        createRoleLock.tick(currentTimeMillis); // 清除超时创建事务
        flushPlayerTick(currentTimeMillis);
        playerTick(currentTimeMillis);
    }

    public void acrossDayTick()
    {
        for (Map.Entry<Long, Player> entry : userPlayers.entrySet())
        {
            Player player = entry.getValue();
            player.acrossDay();
        }
    }

    /**
     * 回存玩家数据tick，同时处理 清除分离状态玩家 和 在线玩家数据定时回存
     *
     * @param currentTimeMillis
     */
    private void flushPlayerTick(long currentTimeMillis)
    {
        if (!flushDBCompleted || currentTimeMillis < nextFlushTime)
            return;

        long flushDetachDelay = userPlayers.size() > flushDetachPlayerWaterLevel
                ? flushDetachPlayerWaterLevelDelay : flushDetachPlayerDelay; // 是否到清除“分离状态玩家”水位

        long detachInterval = 0; // 下次执行flushPlayerTick的时间间隔（毫秒）
        long saveInterval = 0; // 下次执行flushPlayerTick的时间间隔（毫秒）

        List<Player> flushDetachPlayers = new ArrayList<>();
        List<Player> flushSavePlayers = new ArrayList<>();

        for (Map.Entry<Long, Player> entry : userPlayers.entrySet())
        {
            Player player = entry.getValue();
            PlayerState state = player.getPlayerState();

            if (player.isRobot() || state.getState().equals(PlayerState.State.NULL_START)) // 跳过在登录过程中, 还未发送 初始化完成 消息的玩家
                continue;

            if (state.getState().equals(PlayerState.State.DETACH))
            {
                long nextFlushInterval = currentTimeMillis - state.getLastChangeStateTime() - flushDetachDelay * 1000;
                if (nextFlushInterval >= 0)
                {
                    if (flushDetachPlayers.size() + flushSavePlayers.size() < flushDBMaxNum)
                    {
                        flushDetachPlayers.add(player);
                    }
                    else if (detachInterval == 0 || detachInterval > nextFlushInterval) // 选出下次"最"需清除的Player的时间间隔
                    {
                        detachInterval = nextFlushInterval;
                    }
                }
            }
            if (state.getState().equals(PlayerState.State.NORMAL))
            {
                long nextFlushInterval = currentTimeMillis - player.getLastSaveTime() - flushSavePlayerInterval * 1000;
                if (nextFlushInterval >= 0)
                {
                    if (flushDetachPlayers.size() + flushSavePlayers.size() < flushDBMaxNum)
                    {
                        flushSavePlayers.add(player);
                    }
                    else if (saveInterval == 0 || saveInterval > nextFlushInterval) // 选出下次"最"需回存的Player的时间间隔
                    {
                        saveInterval = nextFlushInterval;
                    }
                }
            }
        }

        boolean hasDBOperate = false;

        for (Player player : flushDetachPlayers) // 清除分离玩家
        {
            try
            {
                logger.info("flush detach player, roleName: [" + player.getRoleName() + "], detachTime: "
                        + player.getPlayerState().getLastChangeStateTime());

                hasDBOperate = true;
                flushDetachPlayer(player);
            }
            catch (Throwable t)
            {
                logger.error("flush detach player exception, roleId: [" + UniqueId.toBase36(player.getRoleId()) + "], roleName: [" + player.getRoleName() + "]", t);
            }
        }
        // 在线玩家定时回存
        for (Player player : flushSavePlayers)
        {
            try
            {
                RoleBean roleBean = player.toRoleBean();
                String md5Digest = roleBean.getDigest();
                if (!player.getLastSaveDataDigest().equals(md5Digest))
                {
                    logger.info("save online player [" + player.getRoleName() + "]");
                    hasDBOperate = true;
                    player.setLastSaveDataDigest(md5Digest);
                    GameDBOperator.getInstance().submitRequest(new ReqUpdateRoleHandler(
                            getLineId(),
                            new RoleUpdateBean(
                                    player.toUserBean(),
                                    roleBean.compress(),
                                    player.toRankBean(),
                                    player.getMailManager().toMailBeanList(),
                                    false)));
                }
                player.setLastSaveTime(currentTimeMillis);
            }
            catch (Throwable t)
            {
                logger.error("save player exception, roleId: [" + UniqueId.toBase36(player.getRoleId()) + "], roleName: [" + player.getRoleName() + "]", t);
            }
        }

        // 设置下次flush时间
        long nextInterval = Math.min(detachInterval, saveInterval);
        if (nextInterval >= 0)
        {
            nextFlushTime = currentTimeMillis + nextInterval;
        }
        else
        {
            logger.error("flushPlayerTick, nextInterval exception, detachInterval = " + detachInterval + ", saveInterval = " + saveInterval);
            nextFlushTime = currentTimeMillis + flushSavePlayerInterval / 2; // 预防出错...
        }

        if (hasDBOperate)
        {
            flushDBCompleted = false;
            GameDBOperator.getInstance().submitRequest(new DBOperatorHandler(lineId)
            {
                @Override
                public void action()
                {
                    flushDBCompleted = true;
                }
            });
        }
    }

    public void saveAllPlayerCommand()
    {
        addCommand(new ICommand()
        {
            @Override
            public void action()
            {
                saveAllPlayer();
            }
        });
    }

    /**
     * 凌晨5点定时器
     */
    public void tick5Command()
    {
        addCommand(new ICommand()
        {
            @Override
            public void action()
            {
                tick5();
            }
        });
    }

    private void tick5()
    {
        logger.info("line " + lineId + " tick5");
        for (Map.Entry<Long, Player> entry : userPlayers.entrySet())
        {
            Player player = entry.getValue();
            player.tick5();
        }
    }

    /**
     * 停服时回存玩家数据
     */
    private void saveAllPlayer()
    {
        for (Map.Entry<Long, Player> entry : userPlayers.entrySet())
        {
            Player player = entry.getValue();
            try
            {
                if (player.getPlayerState().getState().equals(PlayerState.State.NULL_START)) // 跳过在登录过程中, 还未发送 初始化完成 消息的玩家
                    continue;
                GameDBOperator.getInstance().submitRequest(new ReqUpdateRoleHandler(
                        getLineId(),
                        new RoleUpdateBean(player.toUserBean(),
                                player.toRoleBean().compress(),
                                player.toRankBean(),
                                player.getMailManager().toMailBeanList(),
                                true)));
            }
            catch (Exception ex)
            {
                logger.error("save player exception, roleId: [" + UniqueId.toBase36(player.getRoleId()) + "], roleName: [" + player.getRoleName() + "]", ex);
            }
        }
    }

    public void flushAllDetachPlayerCommand()
    {
        addCommand(new ICommand()
        {
            @Override
            public void action()
            {
                flushAllDetachPlayer();
            }
        });
    }

    /**
     * 清除所有分离玩家
     */
    private void flushAllDetachPlayer()
    {
        logger.info("in line [" + lineId + "], flushAllDetachPlayer()");
        for (Map.Entry<Long, Player> entry : userPlayers.entrySet())
        {
            Player player = entry.getValue();
            PlayerState state = player.getPlayerState();
            if (!player.isRobot() && state.getState().equals(PlayerState.State.DETACH))
            {
                flushDetachPlayer(player);
            }
        }
    }

    private void flushDetachPlayer(Player player)
    {
        // 同步到GameWorld清理分离玩家
        LWFlushDetachPlayerHandler handler = new LWFlushDetachPlayerHandler();
        handler.setAttribute("userId", player.getUserId());
        MessageUtils.sendToGameWorld(handler);

        PlayerManager.removeDetachPlayer(player);
        removePlayer(player.getUserId());

        // 回存player数据
        GameDBOperator.getInstance().submitRequest(
                new ReqUpdateRoleHandler(
                        getLineId(),
                        new RoleUpdateBean(
                                player.toUserBean(),
                                player.toRoleBean().compress(), // 注: role数据需要压缩
                                player.toRankBean(),
                                player.getMailManager().toMailBeanList(),
                                false)));
    }

    private void playerTick(long currentTimeMillis)
    {
        for (Map.Entry<Long, Player> entry : userPlayers.entrySet())
        {
            Player player = entry.getValue();
            player.tick();
        }
    }

    /**
     * 匹配该线可夺宝的玩家
     *
     * @param nodeId
     * @param level 被匹配等级
     * @param itemId 匹配的道具id
     */
    public void grabTreasureMatchPlayer(UUID nodeId, int level, int itemId)
    {
        
    }

    private boolean grabTreasureMatchLevel(int lv1, int lv2)
    {
        return (MiscUtils.getLevelRank(lv1) == MiscUtils.getLevelRank(lv2));
    }

    private boolean grabTreasureMatchItem(Player player, int itemId)
    {
        if (player == null)
        {
            return false;
        }

        if (player.getBackpackManager().getAmount(itemId) < 1)
        {
            return false;
        }

        List<Integer> itemList = new ArrayList<>();
//        List<q_equipment_compoundBean> equipmentCompoundList = BeanTemplet.getEquipmentCompoundList();
//        for (int i = 0; i < equipmentCompoundList.size(); i++)
//        {
//            q_equipment_compoundBean bean = equipmentCompoundList.get(i);
//            if (bean != null)
//            {
//                itemList.add(bean.getQ_material_1());
//                itemList.add(bean.getQ_material_2());
//                itemList.add(bean.getQ_material_3());
//                itemList.add(bean.getQ_material_4());
//                itemList.add(bean.getQ_material_5());
//                itemList.add(bean.getQ_material_6());
//                itemList.add(bean.getQ_material_7());
//                if (bean.getQ_material_1() == itemId || bean.getQ_material_2() == itemId || bean.getQ_material_3() == itemId
//                        || bean.getQ_material_4() == itemId || bean.getQ_material_5() == itemId || bean.getQ_material_6() == itemId
//                        || bean.getQ_material_7() == itemId)
//                {
//                    int amount = 0;
//                    for (int j = 0; j < itemList.size(); j++)
//                    {
//                        amount = amount + player.getBackpackManager().getAmount(itemList.get(j));
//                    }
//                    if (amount >= 2)
//                    {
//                        return true;
//                    }
//                }
//                itemList.clear();
//            }
//        }
        return false;
    }

    public int getLineId()
    {
        return lineId;
    }

    public LoginTransactionLock getLoginLock()
    {
        return loginLock;
    }

    public LoginTransactionLock getCreateRoleLock()
    {
        return createRoleLock;
    }

    public Player addPlayer(Player player)
    {
        return userPlayers.put(player.getUserId(), player);
    }

    public Player removePlayer(long userId)
    {
        return userPlayers.remove(userId);
    }

    public Player getPlayer(long userId)
    {
        return userPlayers.get(userId);
    }

    public void setPlayerSaveDelayCommand(long playerSaveDelay)
    {
        addCommand(new ICommand()
        {
            private long playerSaveDelay;

            public ICommand set(long playerSaveDelay)
            {
                this.playerSaveDelay = playerSaveDelay;
                return this;
            }

            @Override
            public void action()
            {
                setPlayerSaveDelay(playerSaveDelay);
            }
        }.set(playerSaveDelay));
    }

    public void setFlushDetachPlayerWaterLevelCommand(long flushDetachPlayerWaterLevel)
    {
        addCommand(new ICommand()
        {
            private long flushDetachPlayerWaterLevel;

            public ICommand set(long flushDetachWaterLevel)
            {
                this.flushDetachPlayerWaterLevel = flushDetachWaterLevel;
                return this;
            }

            @Override
            public void action()
            {
                setFlushDetachPlayerWaterLevel(flushDetachPlayerWaterLevel);
            }
        }.set(flushDetachPlayerWaterLevel));
    }

    public void setFlushDetachPlayerWaterLevelDelayCommand(long flushDetachPlayerWaterLevelDelay)
    {
        addCommand(new ICommand()
        {
            private long flushDetachPlayerWaterLevelDelay;

            public ICommand set(long flushDetachPlayerWaterLevelDelay)
            {
                this.flushDetachPlayerWaterLevelDelay = flushDetachPlayerWaterLevelDelay;
                return this;
            }

            @Override
            public void action()
            {
                setFlushDetachPlayerWaterLevelDelay(flushDetachPlayerWaterLevelDelay);
            }
        }.set(flushDetachPlayerWaterLevelDelay));
    }

    public void setFlushDetachPlayerDelayCommand(long flushDetachPlayerDelay)
    {
        addCommand(new ICommand()
        {
            private long flushDetachPlayerDelay;

            public ICommand set(long flushDetachPlayerDelay)
            {
                this.flushDetachPlayerDelay = flushDetachPlayerDelay;
                return this;
            }

            @Override
            public void action()
            {
                setFlushDetachPlayerDelay(flushDetachPlayerDelay);
            }
        }.set(flushDetachPlayerDelay));
    }

    /**
     * 2014.11.7 16:00 王老板要求测试时能开启全服的月卡功能
     */
    public void buyAllPlayerMonthCardCommand()
    {
        addCommand(new ICommand()
        {
            @Override
            public void action()
            {
                for (Map.Entry<Long, Player> entry : userPlayers.entrySet())
                {
                    Player player = entry.getValue();
                    player.buyMonthCard();
                }
            }
        });
    }

    public long getPlayerSaveDelay()
    {
        return flushSavePlayerInterval;
    }

    private void setPlayerSaveDelay(long playerSaveDelay)
    {
        logger.info("set line [" + lineId + "] playerSaveDelay, original = " + this.flushSavePlayerInterval
                + ", new = " + playerSaveDelay);
        this.flushSavePlayerInterval = playerSaveDelay;
    }

    public long getFlushDetachPlayerWaterLevel()
    {
        return flushDetachPlayerWaterLevel;
    }

    private void setFlushDetachPlayerWaterLevel(long flushDetachPlayerWaterLevel)
    {
        logger.info("set line [" + lineId + "] flushDetachWaterLevel, original = " + this.flushDetachPlayerWaterLevel
                + ", new = " + flushDetachPlayerWaterLevel);
        this.flushDetachPlayerWaterLevel = flushDetachPlayerWaterLevel;
    }

    public long getFlushDetachPlayerWaterLevelDelay()
    {
        return flushDetachPlayerWaterLevelDelay;
    }

    private void setFlushDetachPlayerWaterLevelDelay(long flushDetachPlayerWaterLevelDelay)
    {
        logger.info("set line [" + lineId + "] flushDetachPlayerWaterLevelDelay, original = " + this.flushDetachPlayerWaterLevelDelay
                + ", new = " + flushDetachPlayerWaterLevelDelay);
        this.flushDetachPlayerWaterLevelDelay = flushDetachPlayerWaterLevelDelay;
    }

    public long getFlushDetachPlayerDelay()
    {
        return flushDetachPlayerDelay;
    }

    private void setFlushDetachPlayerDelay(long flushDetachPlayerDelay)
    {
        logger.info("set line [" + lineId + "] flushDetachPlayerDelay, original = " + this.flushDetachPlayerDelay + ", new = " + flushDetachPlayerDelay);
        this.flushDetachPlayerDelay = flushDetachPlayerDelay;
    }

    @Override
    public void writeError(String message)
    {
        logger.error(message);
    }

    @Override
    public void writeError(String message, Throwable t)
    {
        logger.error(message, t);
    }
}
