package game.server.logic.robot;

import game.core.util.SimpleRandom;
import game.data.GameDataManager;
import game.data.bean.q_robot_configBean;
import game.server.config.ServerConfig;
import game.server.config.ServerConfigException;
import game.server.db.game.bean.RoleBean;
import game.server.db.game.bean.UserBean;
import game.server.db.game.dao.RoleDao;
import game.server.db.game.dao.UserDao;
import game.server.exception.ServerException;
import game.server.logic.struct.Player;
import game.server.logic.support.DBView;
import game.server.logic.support.RoleView;
import game.server.util.UniqueId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * <b>机器人生成器.</b>
 * <p>
 * 机器人的生成和清除只能在启服时进行，不支持热更新.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class NewRobotBuilder
{

    private final int platformId; // 平台ID

    private final int serverId; // 服务器ID

    private List<RobotConfig> robotConfigs; // 机器人配置列表

    private final SimpleRandom random = new SimpleRandom();

    static final Logger LOG = Logger.getLogger(NewRobotBuilder.class);

    public NewRobotBuilder()
    {
        // 读取平台ID和服务器ID
        try
        {
            platformId = ServerConfig.getInstance().getPlatformId();
            serverId = ServerConfig.getInstance().getServerId();
        }
        catch (ServerConfigException e)
        {
            throw new ServerException("ServerConfig not load!");
        }

        // 读取机器人的配置列表
        loadConfig();
    }

    /**
     * 生成机器人数据.
     * 
     * @return 成功生成的机器人数量
     */
    public int build()
    {
        LOG.info("开始生成机器人数据... （预计生成总数：" + robotConfigs.size() + "）");
        long nowTime = System.currentTimeMillis();
        // 生成所有机器人的基础数据
        Queue<RobotBaseInfo> infos = generateRoleBaseInfo(robotConfigs.size());
        // 开始生成机器人的详细信息
        int count = 0;
        begin:
        for (RobotConfig rc : robotConfigs)
        {
            RobotBaseInfo info = infos.poll();
            // 生成机器人账号
            UserBean user = createUser(info);
            // 生成机器人角色
            RoleBean role = createRole(user, rc, info);

            // 初始化玩家对象
            Player player = new Player();
            player.setCreateRoleTime(nowTime);
            player.loadInitialize(user, role, null);
            /*
            player.getCardManager().setLeaderId(null);
            // 生成机器人卡牌
            List<ArenaCard> arenaCards = new ArrayList<>(rc.getCards().size());
            for (CardConfig cc : rc.getCards())
            {
                // 将卡牌数据添加到角色信息
                Card card = player.getCardManager().activeCard(
                        cc.getTempletId(), cc.getQuality(), CardOperateType.ACTIVE_INIT);
                if (card == null)
                {
                    continue begin;
                }
                //设置机器人的主角ID
                if(card.getModelId() == rc.getLeaderId()){
                    player.getCardManager().setLeaderId(card.getId());
                }
                card.setStarLevel(cc.getStarLevel());
                card.setCardLevel(role.getRoleLevel());
                
                ArenaCard ac = new ArenaCard();
                ac.setId(card.getId());
                ac.setModelId(card.getModelId());
                ac.setLevel(card.getCardLevel());
                ac.setStarLevel(card.getStarLevel());
                ac.setQuality(card.getQualityLevel());
                ac.setCorrection(cc.getCorrection());
                ac.setPower(cc.getPower());
                arenaCards.add(ac);
            }
            
            // 生成竞技场数据
            player.getArenaManager().getArenaData().setRank(rc.getArenaRank());
            player.getArenaManager().getArenaData().setMaxRank(rc.getArenaRank());
            player.getArenaManager().getArenaData().setCardList(arenaCards);
            */
            player.setRobotLevel(rc.getLevel());
            role.setMiscData(player.toJson().toString());
            role = role.compress();

            // 将机器人各项数据写入数据库
            UserDao.insert(user);
            RoleDao.insert(role);

            RoleView roleView = new RoleView(
                    user.getUserId(),
                    user.getUserName(),
                    user.getSoleId(),
                    role.getRoleId(),
                    role.getRoleName(),
                    role.getRoleLevel(),
                    role.getVipLevel(),
                    role.getServerId(),
                    role.getPlatformId());
            DBView.getInstance().addRoleView(roleView);
            count++;
        }
        LOG.info("生成完毕. 实际生成总数：" + count);
        
        return count;
    }

    // .....................
    // private utils
    // .....................
    /**
     * 加载机器人配置列表.
     */
    private void loadConfig()
    {
        List<q_robot_configBean> list = GameDataManager.getInstance().q_robot_configContainer.getList();
        if (list != null && !list.isEmpty())
        {
            robotConfigs = new ArrayList<>(list.size());
            for (q_robot_configBean obj : list)
            {
                RobotConfig robot = new RobotConfig();
                // 读取机器人基础数据
                robot.setLevel(obj.getQ_act_level());
                robot.setArenaRank(obj.getQ_initial_ranking());

                // 读取机器人卡牌数据
                List<CardConfig> cardList = new ArrayList<>(5);
                if (obj.getQ_leader_id()!= null
                        && !obj.getQ_leader_id().isEmpty())
                {
                    try
                    {
                        CardConfig card = createCardConfig(
                                obj.getQ_leader_id(), obj.getQ_leader_quality(),
                                obj.getQ_leader_star(), obj.getQ_leader_correction());
                        cardList.add(card);
                        robot.setLeaderId(card.getTempletId());
                        String[] arrDemon = obj.getQ_leader_demon().split("_");
                        robot.setDemoSoul(random.next(Integer.valueOf(arrDemon[0]), Integer.valueOf(arrDemon[1])));
                    }
                    catch (Exception ex)
                    {
                        LOG.error("为机器人生成第1张卡牌失败！cause：" + ex.getMessage());
                        return;
                    }
                }

                if (obj.getQ_card_id_2() != null
                        && !obj.getQ_card_id_2().isEmpty())
                {
                    try
                    {
                        cardList.add(createCardConfig(
                                obj.getQ_card_id_2(), obj.getQ_card_quality_2(),
                                obj.getQ_card_star_2(), obj.getQ_card_correction_2()));
                    }
                    catch (Exception ex)
                    {
                        LOG.error("为机器人生成第2张卡牌失败！cause：" + ex.getMessage());
                        return;
                    }
                }

                if (obj.getQ_card_id_3() != null
                        && !obj.getQ_card_id_3().isEmpty())
                {
                    try
                    {
                        cardList.add(createCardConfig(
                                obj.getQ_card_id_3(), obj.getQ_card_quality_3(),
                                obj.getQ_card_star_3(), obj.getQ_card_correction_3()));
                    }
                    catch (Exception ex)
                    {
                        LOG.error("为机器人生成第3张卡牌失败！cause：" + ex.getMessage());
                        return;
                    }
                }

                if (obj.getQ_card_id_4() != null
                        && !obj.getQ_card_id_4().isEmpty())
                {
                    try
                    {
                        cardList.add(createCardConfig(
                                obj.getQ_card_id_4(), obj.getQ_card_quality_4(),
                                obj.getQ_card_star_4(), obj.getQ_card_correction_4()));
                    }
                    catch (Exception ex)
                    {
                        LOG.error("为机器人生成第4张卡牌失败！cause：" + ex.getMessage());
                        return;
                    }
                }

//                if (obj.getQ_card_id_5() != null
//                        && !obj.getQ_card_id_5().isEmpty())
//                {
//                    try
//                    {
//                        cardList.add(createCardConfig(
//                                obj.getQ_card_id_5(), obj.getQ_card_quality_5(),
//                                obj.getQ_card_star_5(), obj.getQ_card_correction_5()));
//                    }
//                    catch (Exception ex)
//                    {
//                        LOG.error("为机器人生成第5张卡牌失败！cause：" + ex.getMessage());
//                        return;
//                    }
//                }

                if (cardList.isEmpty())
                {
                    LOG.error("必须为每个机器人配置至少1张卡牌！q_initial_ranking = " + robot.getArenaRank());
                    return;
                }
                int power = obj.getQ_power() / cardList.size();
                for(CardConfig cardConfig : cardList){
                    cardConfig.setPower(power);
                }
                robot.setCards(cardList);
                robotConfigs.add(robot);
            }
        }

    }

    /**
     * 根据配置信息创建卡牌配置对象.
     *
     * @param cardId
     * @param quality
     * @param starLevel
     * @param correction
     * @return
     * @throws Exception 创建失败
     */
    private CardConfig createCardConfig(String cardId,
            String quality, String starLevel, String correction) throws Exception
    {
        CardConfig card = new CardConfig();

        String[] arr1 = cardId.split("_");
        if (arr1 == null || arr1.length == 0)
        {
            throw new Exception("没有定义卡牌ID");
        }
        card.setTempletId(Integer.parseInt(
                arr1[random.next(1, arr1.length) - 1]));

        String[] arr2 = quality.split("_");
        if (arr2 == null || arr2.length == 0)
        {
            throw new Exception("没有定义卡牌品质");
        }
        card.setQuality(random.next(Integer.valueOf(arr2[0]), Integer.valueOf(arr2[1])));

        String[] arr3 = starLevel.split("_");
        if (arr3 == null || arr3.length == 0)
        {
            throw new Exception("没有定义卡牌星级");
        }
        card.setStarLevel(random.next(Integer.valueOf(arr3[0]), Integer.valueOf(arr3[1])));
        card.setCorrection(Integer.parseInt(correction));

        return card;
    }

    private UserBean createUser(RobotBaseInfo info)
    {
        UserBean user = new UserBean();
        user.setUserId(UniqueId.toBase36(info.userId));
        user.setUserName(info.userName);
        return user;
    }

    private RoleBean createRole(UserBean user, RobotConfig config, RobotBaseInfo info)
    {
        RoleBean role = new RoleBean();
        role.setIsRobot(1);
        role.setRoleId(UniqueId.toBase36(info.roleId));
        role.setUserId(user.getUserId());
        role.setPlatformId(platformId);
        role.setServerId(serverId);
        role.setMiscData(StringUtils.EMPTY);
        role.setRoleHead(Integer.valueOf(config.getLeaderId()+""+config.getDemoSoul()));
        
        // 随机头像编号
//        int headTotal = GameDataManager.getInstance().q_act_headContainer.getList().size();
//        role.setRoleHead(
//                GameDataManager.getInstance().q_act_headContainer
//                .getList().get(random.next(1, headTotal) - 1).getQ_acthead_id());

        role.setRoleName(info.roleName);
        role.setRoleLevel(config.getLevel());

        return role.compress();
    }

    private Queue<RobotBaseInfo> generateRoleBaseInfo(int count)
    {
        Queue<RobotBaseInfo> infos = new LinkedList<>();
        HashSet<String> userNames = new HashSet<>();
        do
        {
            long userId = Long.parseLong(UniqueId.getUniqueIdBase36(serverId, platformId, UniqueId.FuncType.USER), 36);
            String userName = "$ROBOT_" + userId;
            if (DBView.getInstance().hasUserName(userName))
            {
                LOG.warn("Robot userName: [" + userName + "] has exist");
                continue;
            }
            long roleId = Long.parseLong(UniqueId.getUniqueIdBase36(serverId, platformId, UniqueId.FuncType.ROLE), 36);
            String roleName = StringUtils.EMPTY;
            do
            {
                roleName = RobotNames.getRandomName();
            } while (DBView.getInstance().hasRoleName(roleName) || userNames.contains(roleName));
            userNames.add(roleName);
            if (roleName.isEmpty())
                throw new IllegalStateException();

            RobotBaseInfo info = new RobotBaseInfo();
            info.userId = userId;
            info.userName = userName;
            info.roleId = roleId;
            info.roleName = roleName;
            infos.add(info);
        } while (infos.size() < count);

        return infos;
    }

    private class RobotBaseInfo
    {
        public long userId;
        public long roleId;
        public String userName;
        public String roleName;
    }

    /**
     * 机器人配置信息.
     */
    class RobotConfig
    {

        private int level; // 等级

        private int arenaRank; // 竞技场初始排名
        
        private int leaderId;  //领袖模板ID
        
        private int demoSoul; //恶魔之魂等级

        private List<CardConfig> cards;
        
        public int getLevel()
        {
            return level;
        }

        public void setLevel(int level)
        {
            this.level = level;
        }

        public int getArenaRank()
        {
            return arenaRank;
        }

        public void setArenaRank(int arenaRank)
        {
            this.arenaRank = arenaRank;
        }

        public List<CardConfig> getCards()
        {
            return cards;
        }

        public void setCards(List<CardConfig> cards)
        {
            this.cards = cards;
        }

        public int getLeaderId()
        {
            return leaderId;
        }

        public void setLeaderId(int leaderId)
        {
            this.leaderId = leaderId;
        }

        public int getDemoSoul()
        {
            return demoSoul;
        }

        public void setDemoSoul(int demoSoul)
        {
            this.demoSoul = demoSoul;
        }

    }

    /**
     * 卡牌配置信息.
     */
    class CardConfig
    {

        private int templetId; // 模板ID

        private int quality; // 卡牌品质

        private int starLevel; // 卡牌星级

        private int correction; // 修正系数
        
        private int power;  //战斗力
        
        public int getTempletId()
        {
            return templetId;
        }

        public void setTempletId(int templetId)
        {
            this.templetId = templetId;
        }

        public int getQuality()
        {
            return quality;
        }

        public void setQuality(int quality)
        {
            this.quality = quality;
        }

        public int getStarLevel()
        {
            return starLevel;
        }

        public void setStarLevel(int starLevel)
        {
            this.starLevel = starLevel;
        }

        public int getCorrection()
        {
            return correction;
        }

        public void setCorrection(int correction)
        {
            this.correction = correction;
        }

        public int getPower()
        {
            return power;
        }

        public void setPower(int power)
        {
            this.power = power;
        }
    }

}
