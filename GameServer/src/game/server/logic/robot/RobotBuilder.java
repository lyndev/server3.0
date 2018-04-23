//package game.server.logic.robot;
//
//import game.core.util.ArrayUtils;
//import game.core.util.SimpleRandom;
//import game.data.GameDataManager;
//import game.data.bean.q_card_combinationBean;
//import game.data.bean.q_robot_armyBean;
//import game.data.bean.q_robot_propertiesBean;
//import game.server.config.ServerConfig;
//import game.server.config.ServerConfigException;
//import game.server.db.DBFactory;
//import game.server.db.game.bean.RoleBean;
//import game.server.db.game.bean.UserBean;
//import game.server.db.game.dao.RoleDao;
//import game.server.db.game.dao.UserDao;
//import game.server.db.game.dao.XFBaseDao;
//import game.server.exception.ServerException;
//import game.server.logic.card.bean.CardOperateType;
//import game.server.logic.constant.ResBuildingType;
//import game.server.logic.struct.Player;
//import game.server.logic.support.BeanTemplet;
//import game.server.logic.support.DBView;
//import game.server.logic.support.RoleView;
//import game.server.util.UniqueId;
//import game.server.world.building.struct.ResBuilding;
//import game.server.world.building.struct.XFBase;
//import game.server.world.building.struct.XFCard;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Queue;
//import java.util.Set;
//import java.util.UUID;
//import org.apache.commons.lang.StringUtils;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.log4j.Logger;
//
///**
// *
// * <b>机器人生成器.</b>
// * <p>
// * 机器人的生成和清除只能在启服时进行，不支持热更新.
// * <p>
// * <b>Sample:</b>
// *
// * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
// * @version 1.0.0
// */
//public class RobotBuilder
//{
//
//    private final int platformId; // 平台ID
//
//    private final int serverId; // 服务器ID
//
//    private final List<Integer> cardsStarLevel; // 卡牌星级对应的等级上限，索引表示星级（下标+1），元素值表示该星级可达到的等级上限
//
//    private final Map<Integer, List<CardConfig>> cardConfigs; // 可生成卡牌的配置信息：key = 位置编号（1~5），value = 该位置可能出现的卡牌配置列表
//
//    private final SimpleRandom random;
//
//    static final Logger LOG = Logger.getLogger(RobotBuilder.class);
//
//    public RobotBuilder()
//    {
//        // 读取平台ID和服务器ID
//        try
//        {
//            platformId = ServerConfig.getInstance().getPlatformId();
//            serverId = ServerConfig.getInstance().getServerId();
//        }
//        catch (ServerConfigException e)
//        {
//            throw new ServerException("ServerConfig not load!");
//        }
//
//        // 读取卡牌星级对应的等级上限
//        String[] strArr = BeanTemplet.getGlobalBean(1083).getQ_string_value().split(";");
//        cardsStarLevel = new ArrayList<>(strArr.length);
//        for (String str : strArr)
//        {
//            cardsStarLevel.add(Integer.parseInt(str.split("_")[1]));
//        }
//        if (cardsStarLevel.size() < 10)
//        {
//            throw new ServerException(
//                    "卡牌星级对应的等级上限的配置有误！(配置不满足10个卡牌星级）请检查全局变量表.");
//        }
//
//        // 读取可生成卡牌的配置信息
//        cardConfigs = new HashMap<>(5);
//        for (int location = 1; location <= 5; location++)
//        {
//            cardConfigs.put(location, new ArrayList<CardConfig>());
//        }
//        List<q_robot_armyBean> list = GameDataManager.getInstance().q_robot_armyContainer.getList();
//        for (q_robot_armyBean obj : list)
//        {
//            if (obj.getQ_card_id_1() != 0
//                    && obj.getQ_card_prob_1() != 0)
//            {
//                cardConfigs.get(1).add(
//                        new CardConfig(obj.getQ_card_id_1(), obj.getQ_card_prob_1()));
//            }
//            if (obj.getQ_card_id_2() != 0
//                    && obj.getQ_card_prob_2() != 0)
//            {
//                cardConfigs.get(2).add(
//                        new CardConfig(obj.getQ_card_id_2(), obj.getQ_card_prob_2()));
//            }
//            if (obj.getQ_card_id_3() != 0
//                    && obj.getQ_card_prob_3() != 0)
//            {
//                cardConfigs.get(3).add(
//                        new CardConfig(obj.getQ_card_id_3(), obj.getQ_card_prob_3()));
//            }
//            if (obj.getQ_card_id_4() != 0
//                    && obj.getQ_card_prob_4() != 0)
//            {
//                cardConfigs.get(4).add(
//                        new CardConfig(obj.getQ_card_id_4(), obj.getQ_card_prob_4()));
//            }
//            if (obj.getQ_card_id_5() != 0
//                    && obj.getQ_card_prob_5() != 0)
//            {
//                cardConfigs.get(5).add(
//                        new CardConfig(obj.getQ_card_id_5(), obj.getQ_card_prob_5()));
//            }
//        }
//        for (Entry<Integer, List<CardConfig>> entry : cardConfigs.entrySet())
//        {
//            if (entry.getValue().isEmpty())
//            {
//                throw new ServerException(
//                        "没有读取到可生成卡牌的配置信息！卡牌位置：" + entry.getKey());
//            }
//        }
//
//        random = new SimpleRandom();
//    }
//
//    /**
//     * 生成一批机器人数据.
//     *
//     * @param amount 指定数量：按仙府战场星级来生成机器人数据，每个星级的战场生成指定数量的机器人
//     */
//    public void build(int amount)
//    {
//        int starAmount = 5;
//        int amountOrig = amount;
//        amount -= DBView.getInstance().getRobotCount();
//        if (amount <= 0)
//            return;
//
//        amount /= starAmount;
//        if (amount <= 0)
//            amount = starAmount;
//
//        LOG.info("开始生成机器人数据... （生成总数：" + amount * starAmount + "）");
//        long nowTime = System.currentTimeMillis();
//        for (int bfStar = 1; bfStar <= starAmount; ++bfStar)
//        {
//            Queue<RobotBaseInfo> infos = generateRoleBaseInfo(amount, bfStar);
//            for (int count = 1; count <= amount; ++count)
//            {
//                RobotBaseInfo info = infos.poll();
//                // 生成机器人账号
//                UserBean user = createUser(info);
//                // 生成机器人角色
//                RoleBean role = createRole(user, info);
//                // 生成机器人仙府
//                XFBase base = createXFBase(user, bfStar);
//                // 生成机器人卡牌
//                CardRule rule = createCardRule(bfStar);
//                List<RobotCard> cards = createCards(bfStar, rule);
//
//                Player player = new Player();
//                player.setCreateRoleTime(nowTime);
//                player.loadInitialize(user, role, null);
//
//                for (RobotCard card : cards)
//                {
//                    // 将卡牌数据添加到角色信息
//                    player.getCardManager().activeCard(
//                            card.getModel(), 0, CardOperateType.ACTIVE_INIT);
//
//                    // 将卡牌数据添加到仙府信息
//                    XFCard xfCard = new XFCard();
//                    xfCard.setId(card.getId());
//                    xfCard.setModelId(card.getModel());
//                    xfCard.setCardLevel(card.getLevel());
//                    xfCard.setStarLevel(card.getStarLevel());
//                    xfCard.setQualityLevel(0);
//                    xfCard.setSkillLevel(card.getAllSkillLevel());
//                    base.getDefendCards().add(xfCard);
//                }
//
//                // 将仙府数据添加到角色信息中
//                player.getBuildingManager().getInfo().setBfUnlock(true);
//                player.getBuildingManager().getInfo().setBfStarLevel(bfStar);
//                player.getBuildingManager().getInfo().setRankValue(1000 + rule.getRankValue());
//
//                role.setMiscData(player.toJson().toString());
//                role = role.compress();
//
//                // 将机器人各项数据写入数据库
//                UserDao.insert(user);
//                RoleDao.insert(role);
//                XFBaseDao.insert(base.toBean(), false);
//
//                RoleView roleView = new RoleView(
//                        user.getUserId(),
//                        user.getUserName(),
//                        role.getRoleId(),
//                        role.getRoleName(),
//                        role.getRoleLevel(),
//                        role.getVipLevel(),
//                        role.getServerId(),
//                        role.getPlatformId());
//                DBView.getInstance().addRoleView(roleView);
//            }
//        }
//        LOG.info("生成完毕.");
//    }
//
//    /**
//     * 清除所有机器人数据.
//     */
//    public static void clear()
//    {
//        LOG.info("开始清除所有机器人数据...");
//        LOG.info("删除机器人账号：" + deleteRobotUsers() + "个");
//        LOG.info("删除机器人角色：" + deleteRobotRoles() + "个");
//        LOG.info("删除机器人仙府：" + deleteRobotXFBases() + "个");
//        LOG.info("清除完毕.");
//    }
//
//    // .....................
//    // private utils
//    // .....................
//    private UserBean createUser(RobotBaseInfo info)
//    {
//        UserBean user = new UserBean();
//        user.setUserId(UniqueId.toBase36(info.userId));
//        user.setUserName(info.userName);
//        return user;
//    }
//
//    private class RobotBaseInfo
//    {
//        public long userId;
//        public long roleId;
//        public String userName;
//        public String roleName;
//    }
//
//    private Queue<RobotBaseInfo> generateRoleBaseInfo(int count, int star)
//    {
//        Queue<RobotBaseInfo> infos = new LinkedList<>();
//        HashSet<String> userNames = new HashSet<>();
//        do
//        {
//            long userId = Long.parseLong(UniqueId.getUniqueIdBase36(serverId, platformId, UniqueId.FuncType.USER), 36);
//            String userName = "ROBOT_X" + star + "_" + userId;
//            if (DBView.getInstance().hasUserName(userName))
//            {
//                LOG.warn("Robot userName: [" + userName + "] has exist");
//                continue;
//            }
//            long roleId = Long.parseLong(UniqueId.getUniqueIdBase36(serverId, platformId, UniqueId.FuncType.ROLE), 36);
//            String roleName = StringUtils.EMPTY;
//            do
//            {
//                roleName = RobotNames.getRandomName();
//            } while (DBView.getInstance().hasRoleName(roleName) || userNames.contains(roleName));
//            userNames.add(roleName);
//            if (roleName.isEmpty())
//                throw new IllegalStateException();
//
//            RobotBaseInfo info = new RobotBaseInfo();
//            info.userId = userId;
//            info.userName = userName;
//            info.roleId = roleId;
//            info.roleName = roleName;
//            infos.add(info);
//        } while (infos.size() < count);
//        return infos;
//    }
//
//    private RoleBean createRole(UserBean user, RobotBaseInfo info)
//    {
//        RoleBean role = new RoleBean();
//        role.setIsRobot(1);
//        role.setRoleId(UniqueId.toBase36(info.roleId));
//        role.setUserId(user.getUserId());
//        role.setPlatformId(platformId);
//        role.setServerId(serverId);
//        role.setMiscData(StringUtils.EMPTY);
//
//        // 随机头像编号
//        int headTotal = GameDataManager.getInstance().q_act_headContainer.getList().size();
//        role.setRoleHead(
//                GameDataManager.getInstance().q_act_headContainer
//                .getList().get(random.next(1, headTotal) - 1).getQ_acthead_id());
//
//        role.setRoleName(info.roleName);
//
//        return role.compress();
//    }
//
//    private XFBase createXFBase(UserBean user, int bfStar)
//    {
//        XFBase base = new XFBase(UniqueId.toBase10(user.getUserId()), true);
//        base.setBuildId(UUID.randomUUID());
//        base.setOrder(1);
//
//        // 设置仙府星级
//        if (bfStar == 1)
//            base.getXFBuild().setStarLevel(random.next(2, 3));
//        else if (bfStar == 2)
//            base.getXFBuild().setStarLevel(random.next(4, 5));
//        else if (bfStar == 3)
//            base.getXFBuild().setStarLevel(random.next(6, 7));
//        else if (bfStar == 4)
//            base.getXFBuild().setStarLevel(random.next(8, 9));
//        else if (bfStar == 5)
//            base.getXFBuild().setStarLevel(10);
//
//        // 设置建筑等级
//        for (Entry<ResBuildingType, ResBuilding> entry : base.getResBuildings().entrySet())
//        {
//            if (base.getXFBuild().getStarLevel() == 1)
//            {
//                entry.getValue().setLevel(random.next(1,
//                        GameDataManager.getInstance().q_xf_baseContainer.getMap().get(
//                                base.getXFBuild().getStarLevel()).getQ_max_level()));
//            }
//            else
//            {
//                entry.getValue().setLevel(random.next(
//                        GameDataManager.getInstance().q_xf_baseContainer.getMap().get(
//                                base.getXFBuild().getStarLevel() - 1).getQ_max_level(),
//                        GameDataManager.getInstance().q_xf_baseContainer.getMap().get(
//                                base.getXFBuild().getStarLevel()).getQ_max_level()));
//            }
//        }
//
//        return base;
//    }
//
//    private CardRule createCardRule(int bfStar)
//    {
//        q_robot_propertiesBean config = GameDataManager.getInstance().q_robot_propertiesContainer.getMap().get(bfStar);
//
//        CardRule rule = new CardRule();
//        rule.setTeam(random.probability(config.getQ_card_team()));
//        if (rule.isTeam())
//            rule.setRankValue(rule.getRankValue() + config.getQ_card_team_rank());
//
//        rule.setFullStar(random.probability(config.getQ_card_full_star()));
//        if (rule.isFullStar())
//            rule.setRankValue(rule.getRankValue() + config.getQ_card_full_star_rank());
//
//        rule.setFullLevel(random.probability(config.getQ_card_full_level()));
//        if (rule.isFullLevel())
//            rule.setRankValue(rule.getRankValue() + config.getQ_card_full_level_rank());
//
//        rule.setFullSkill(random.probability(config.getQ_card_full_skill()));
//        if (rule.isFullSkill())
//            rule.setRankValue(rule.getRankValue() + config.getQ_card_full_skill_rank());
//
//        if (random.probability(config.getQ_card_quality_1()))
//        {
//            rule.setQuality(1);
//            rule.setRankValue(rule.getRankValue() + config.getQ_card_quality_rank_1());
//        }
//        else if (random.probability(config.getQ_card_quality_2()))
//        {
//            rule.setQuality(2);
//            rule.setRankValue(rule.getRankValue() + config.getQ_card_quality_rank_2());
//        }
//        else if (random.probability(config.getQ_card_quality_3()))
//        {
//            rule.setQuality(3);
//            rule.setRankValue(rule.getRankValue() + config.getQ_card_quality_rank_3());
//        }
//
//        return rule;
//    }
//
//    private List<RobotCard> createCards(int bfStar, CardRule rule)
//    {
//        List<RobotCard> cardList = getCards(rule);
//
//        // 设置卡牌属性
//        for (RobotCard card : cardList)
//        {
//            // 设置卡牌星级
//            if (rule.isFullStar())
//            {
//                card.setStarLevel(bfStar * 2); // 战场星级有5级，卡牌星级有10级，刚好每2级对应1级战场
//            }
//            else
//            {
//                card.setStarLevel(random.next((bfStar - 1) * 2 + 1, bfStar * 2));
//            }
//
//            // 设置卡牌等级
//            if (rule.isFullLevel())
//            {
//                card.setLevel(cardsStarLevel.get(card.getStarLevel() - 1));
//            }
//            else
//            {
//                if (card.getStarLevel() == 1)
//                {
//                    card.setLevel(random.next(1,
//                            cardsStarLevel.get(card.getStarLevel() - 1)));
//                }
//                else
//                {
//                    card.setLevel(random.next(
//                            cardsStarLevel.get(card.getStarLevel() - 2),
//                            cardsStarLevel.get(card.getStarLevel() - 1)));
//                }
//            }
//
//            // 设置技能等级
//            if (rule.isFullSkill())
//            {
//                card.setAllSkillLevel(cardsStarLevel.get(card.getStarLevel() - 1));
//            }
//            else
//            {
//                if (card.getStarLevel() == 1)
//                {
//                    card.setAllSkillLevel(random.next(1,
//                            cardsStarLevel.get(card.getStarLevel() - 1)));
//                }
//                else
//                {
//                    card.setAllSkillLevel(random.next(
//                            cardsStarLevel.get(card.getStarLevel() - 2),
//                            cardsStarLevel.get(card.getStarLevel() - 1)));
//                }
//            }
//
//            // 设置卡牌品质
//            switch (rule.getQuality())
//            {
//                case 1:
//                    card.setQuality(random.next(4, 5)); // 紫色-橙色
//                    break;
//                case 2:
//                    card.setQuality(random.next(3, 4)); // 蓝色-紫色
//                    break;
//                case 3:
//                    card.setQuality(random.next(2, 3)); // 绿色-蓝色
//                    break;
//                default:
//                    card.setQuality(random.next(1, 4)); // 白色-紫色
//                    break;
//            }
//        }
//
//        return cardList;
//    }
//
//    private List<RobotCard> getCards(CardRule rule)
//    {
//        Set<Integer> cardIds = new HashSet<>(5);
//
//        // 先按规则随机获取卡牌ID
//        for (int location = 1; location <= 5; location++)
//        {
//            if (location == 1)
//            {
//                int fristCardId = getCardId(location);
//                cardIds.add(fristCardId);
//
//                // 如果首张卡牌随机到了组合卡牌，按组合生成剩下的卡牌
//                if (rule.isTeam())
//                {
////                    for (q_card_combinationBean bean : GameDataManager
////                            .getInstance().q_card_combinationContainer.getList())
////                    {
////                        if (bean.getQ_heroid_core() == fristCardId)
////                        {
////                            int[] teamOthers = ArrayUtils.parseInt(bean.getQ_heroid().split(","));
////                            if (teamOthers != null && teamOthers.length > 0)
////                            {
////                                for (int cardId : teamOthers)
////                                {
////                                    cardIds.add(cardId);
////                                    location++; // 更新位置计数器，如果不够凑足队伍，再随机获取其他卡牌
////                                }
////                            }
////                            break;
////                        }
////                    }
//                }
//            }
//            else
//            {
//                while (true)
//                {
//                    int cardId = getCardId(location);
//                    // 生成的卡牌不能和之前的重复
//                    if (!cardIds.contains(cardId))
//                    {
//                        cardIds.add(cardId);
//                        break;
//                    }
//                }
//            }
//        }
//
//        List<RobotCard> cardList = new ArrayList<>(cardIds.size());
//        for (Integer cardId : cardIds)
//        {
//            cardList.add(new RobotCard(cardId));
//        }
//
//        return cardList;
//    }
//
//    private int getCardId(int location)
//    {
//        for (CardConfig config : cardConfigs.get(location))
//        {
//            if (random.probability(config.getProbability()))
//            {
//                return config.getCardId();
//            }
//        }
//
//        return cardConfigs.get(location).get(
//                random.next(1, cardConfigs.get(location).size()) - 1).getCardId();
//    }
//
//    private static int deleteRobotUsers()
//    {
//        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
//        {
//            int rows = session.delete("user.deleteAllRobot");
//            session.commit();
//            return rows;
//        }
//        catch (Exception e)
//        {
//            DBFactory.GAME_DB.getLogger().error(e);
//        }
//        return 0;
//    }
//
//    private static int deleteRobotRoles()
//    {
//        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
//        {
//            int rows = session.delete("role.deleteAllRobot");
//            session.commit();
//            return rows;
//        }
//        catch (Exception e)
//        {
//            DBFactory.GAME_DB.getLogger().error(e);
//        }
//        return 0;
//    }
//
//    private static int deleteRobotXFBases()
//    {
//        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
//        {
//            int rows = session.delete("xfBase.deleteAllRobot");
//            session.commit();
//            return rows;
//        }
//        catch (Exception e)
//        {
//            DBFactory.GAME_DB.getLogger().error(e);
//        }
//        return 0;
//    }
//
//    /**
//     * 可生成卡牌的配置信息.
//     */
//    class CardConfig
//    {
//
//        private int cardId; // 卡牌模板ID
//
//        private int probability; // 出现几率
//
//        CardConfig(int cardId, int probability)
//        {
//            this.cardId = cardId;
//            this.probability = probability;
//        }
//
//        public int getCardId()
//        {
//            return cardId;
//        }
//
//        public void setCardId(int cardId)
//        {
//            this.cardId = cardId;
//        }
//
//        public int getProbability()
//        {
//            return probability;
//        }
//
//        public void setProbability(int probability)
//        {
//            this.probability = probability;
//        }
//
//    }
//
//    /**
//     * 卡牌生成的基本规则.
//     */
//    class CardRule
//    {
//
//        private boolean team; // 是否按卡牌组合生成一支队伍
//
//        private boolean fullStar; // 卡牌是否满星级
//
//        private boolean fullLevel; // 卡牌是否满等级
//
//        private boolean fullSkill; // 卡牌技能是否满等级
//
//        private int quality; // 卡牌品质：1 = 在紫色和橙色中随机；2 = 在蓝色和紫色中随机；3 = 在绿色和蓝色中随机；0 = 没有固定规则，从白色到紫色中随机
//
//        private int rankValue; // 增加的积分系数
//
//        public boolean isTeam()
//        {
//            return team;
//        }
//
//        public void setTeam(boolean team)
//        {
//            this.team = team;
//        }
//
//        public boolean isFullStar()
//        {
//            return fullStar;
//        }
//
//        public void setFullStar(boolean fullStar)
//        {
//            this.fullStar = fullStar;
//        }
//
//        public boolean isFullLevel()
//        {
//            return fullLevel;
//        }
//
//        public void setFullLevel(boolean fullLevel)
//        {
//            this.fullLevel = fullLevel;
//        }
//
//        public boolean isFullSkill()
//        {
//            return fullSkill;
//        }
//
//        public void setFullSkill(boolean fullSkill)
//        {
//            this.fullSkill = fullSkill;
//        }
//
//        public int getQuality()
//        {
//            return quality;
//        }
//
//        public void setQuality(int quality)
//        {
//            this.quality = quality;
//        }
//
//        public int getRankValue()
//        {
//            return rankValue;
//        }
//
//        public void setRankValue(int rankValue)
//        {
//            this.rankValue = rankValue;
//        }
//
//    }
//
//    /**
//     * 机器人卡牌属性.
//     */
//    class RobotCard
//    {
//
//        private UUID id; // 卡牌实例ID
//
//        private int model; // 卡牌模板ID
//
//        private int level; // 卡牌等级
//
//        private int quality; // 卡牌品质
//
//        private int starLevel; // 卡牌星级
//
//        private int allSkillLevel; // 卡牌所有技能等级
//
//        RobotCard(int model)
//        {
//            this.model = model;
//            this.id = UUID.randomUUID();
//        }
//
//        @Override
//        public String toString()
//        {
//            return model + "_" + level + "_" + quality + "_" + starLevel + "_" + allSkillLevel;
//        }
//
//        public UUID getId()
//        {
//            return id;
//        }
//
//        public int getModel()
//        {
//            return model;
//        }
//
//        public void setModel(int model)
//        {
//            this.model = model;
//        }
//
//        public int getLevel()
//        {
//            return level;
//        }
//
//        public void setLevel(int level)
//        {
//            this.level = level;
//        }
//
//        public int getQuality()
//        {
//            return quality;
//        }
//
//        public void setQuality(int quality)
//        {
//            this.quality = quality;
//        }
//
//        public int getStarLevel()
//        {
//            return starLevel;
//        }
//
//        public void setStarLevel(int starLevel)
//        {
//            this.starLevel = starLevel;
//        }
//
//        public int getAllSkillLevel()
//        {
//            return allSkillLevel;
//        }
//
//        public void setAllSkillLevel(int allSkillLevel)
//        {
//            this.allSkillLevel = allSkillLevel;
//        }
//
//    }
//
//}
