package script.gm;

import game.core.command.ICommand;
import game.core.script.IScript;
import game.message.FriendMessage;
import game.message.RechargeMessage;
import game.server.db.game.dao.RankDao;
import game.server.logic.constant.ErrorCode;
import game.server.logic.constant.Reasons;
import game.server.logic.constant.ResourceType;
import game.server.logic.item.bean.Item;
import game.server.logic.mail.bean.Mail;
import game.server.logic.mail.service.MailService;
import game.server.logic.struct.Player;
import game.server.logic.struct.Resource;
import game.server.logic.support.BeanFactory;
import game.server.logic.support.DBView;
import game.server.logic.util.ScriptArgs;
import game.server.world.GameWorld;
import game.server.world.rank.RankServer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author YangHanzhou
 */
public class GmCommandScript implements IScript
{
    private final Logger log = Logger.getLogger(GmCommandScript.class);

    @Override
    public int getId()
    {
        return 1002;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        log.info("Call GM command.");
        // 解析参数
        ScriptArgs args = (ScriptArgs) arg;
        Player player = (Player) args.get(ScriptArgs.Key.PLAYER);
        String command = (String) args.get(ScriptArgs.Key.STRING_VALUE);
        if (command == null || command.isEmpty() || player == null)
        {
            log.error("GM arg error");
            return null;
        }
        String[] strs = command.split(" ");
        if (strs == null || strs.length < 1)
        {
            log.error("GM command error");
            return null;
        }

        if ("coin".equalsIgnoreCase(strs[0]) && strs.length == 2)   //加金币
        {
            int money = Integer.valueOf(strs[1]);
            player.getBackpackManager().addResource(
                    ResourceType.GOLD, money, true, Reasons.ITEM_GM, Calendar.getInstance().getTime());
        }
        else if ("gem".equalsIgnoreCase(strs[0]) && strs.length == 2) //钻石
        {
            int gold = Integer.valueOf(strs[1]);
            player.getBackpackManager().addResource(
                    ResourceType.GOLD_BULLION, gold, true, Reasons.ITEM_GM, Calendar.getInstance().getTime());
        }
        else if ("energy".equalsIgnoreCase(strs[0]) && strs.length == 2) //体力
        {
            int energy = Integer.valueOf(strs[1]);
            player.getBackpackManager().addResource(
                    ResourceType.ENERGY, energy, true, Reasons.ITEM_GM, Calendar.getInstance().getTime());
        }
        else if ("skillpoint".equalsIgnoreCase(strs[0]) && strs.length == 2) //技能点
        {
            int energy = Integer.valueOf(strs[1]);
            player.getBackpackManager().addResource(
                    ResourceType.SKILL_POINT, energy, true, Reasons.ITEM_GM, Calendar.getInstance().getTime());
        }
        else if ("starcnt".equalsIgnoreCase(strs[0]) && strs.length == 2) //通关的星级总数
        {
            int wood = Integer.valueOf(strs[1]);
            player.getBackpackManager().addResource(
                    ResourceType.MISSION_STAR, wood, true, Reasons.ITEM_GM, Calendar.getInstance().getTime());
        }
        else if ("honor".equalsIgnoreCase(strs[0]) && strs.length == 2) //荣誉值
        {
            int mana = Integer.valueOf(strs[1]);
            player.getBackpackManager().addResource(
                    ResourceType.HONOR, mana, true, Reasons.ITEM_GM, Calendar.getInstance().getTime());
        }
        else if ("expesoul".equalsIgnoreCase(strs[0]) && strs.length == 2) //远征币
        {
            int starSoul = Integer.valueOf(strs[1]);
            player.getBackpackManager().addResource(
                    ResourceType.EXPEDITION_COIN, starSoul, true, Reasons.ITEM_GM, Calendar.getInstance().getTime());
        }
        else if ("soulfrag".equalsIgnoreCase(strs[0]) && strs.length == 2) //灵魂碎片
        {
            int xuanjin = Integer.valueOf(strs[1]);
            player.getBackpackManager().addResource(
                    ResourceType.SOUL_FRAG, xuanjin, true, Reasons.ITEM_GM, Calendar.getInstance().getTime());
        }
        else if ("name".equalsIgnoreCase(strs[0]) && strs.length == 2) //修改角色名字
        {
            String roleName = strs[1];
            player.setRoleName(roleName);
        }
        else if ("vip".equalsIgnoreCase(strs[0]) && strs.length == 2) //设置VIP等级
        {
            int level = Integer.valueOf(strs[1]);
            if (level > 15){
                level = 15;
            }
            if (level < 0){
                level = 0;
            }
            
            player.getVipManager().setVipLevel(level);
        }

        else if ("items".equalsIgnoreCase(strs[0]) && strs.length == 4) //批量添加物品
        {
            log.info("gmadditems...");
            int starId = Integer.valueOf(strs[1]);
            int endId = Integer.valueOf(strs[2]);
            int num = Integer.valueOf(strs[3]);
            for (int i = starId; i <= endId; i++)
            {
                Item createItem = BeanFactory.createItem(i, num);
                if (createItem != null)
                {
                    ErrorCode err = player.getBackpackManager().addItem(createItem, true, Reasons.ITEM_GM);
                    if (err != null)
                    {
                        //System.out.println("添加物品失败！code = " + err.getCode());
                    }
                }
                else
                {
                    //log.info("createItem error, item id: " + i);
                }
            }
            log.info("gmadditems over ");
        }
        else if ("item".equalsIgnoreCase(strs[0]) && strs.length == 3) //添加单个物品
        {
            // 模板id
            log.info("gmadditem...");
            int id = Integer.valueOf(strs[1]);
            int num = Integer.valueOf(strs[2]);
            log.info("id=" + id);
            log.info("num=" + num);
            Item createItem = BeanFactory.createItem(id, num);
            log.info("item=" + createItem);
            if (createItem != null)
            {
                ErrorCode err = player.getBackpackManager().addItem(createItem, true, Reasons.ITEM_GM);
                if (err != null)
                {
                    log.error("添加物品失败！code = " + err.getCode());
                }
            }
            else
            {
                log.info("createItem error, item id: " + id);
            }
        }
        else if ("sendmail".equalsIgnoreCase(strs[0]) && strs.length > 2) //发送邮件
        {
            int type;
            String parameter = null;

            Mail mail = new Mail();
            String title;
            String content;
            String senderName;
            int deadLine;
            List<Resource> resList = new ArrayList<>(0);
            List<Item> itemList = new ArrayList<>(0);
            int accessory = 1;

            type = Integer.valueOf(strs[1]);
            if (type == 1)
            {
                if (strs.length >= 6)
                {
                    title = strs[2];
                    content = strs[3];
                    senderName = strs[4];
                    deadLine = (int) (System.currentTimeMillis() / 1000) + 60 * 60 * Integer.valueOf(strs[5]);
                    if (strs.length >= 7)
                        resList.addAll(MailService.parseResources(strs[6]));
                    if (strs.length >= 8)
                        itemList.addAll(MailService.parseItems(strs[7]));

                    if (resList.isEmpty() && itemList.isEmpty())
                        accessory = 0;

                    mail.setAll(title, content, senderName, 2, 0L, null, accessory, deadLine, resList, itemList);
                    MailService.getInstance().sendByType(type, parameter, mail);
                }
            }
            else
            {
                if (strs.length >= 7)
                {
                    parameter = strs[2];

                    title = strs[3];
                    content = strs[4];
                    senderName = strs[5];
                    deadLine = (int) (System.currentTimeMillis() / 1000) + 60 * 60 * Integer.valueOf(strs[6]);
                    if (strs.length >= 8)
                        resList.addAll(MailService.parseResources(strs[7]));
                    if (strs.length >= 9)
                        itemList.addAll(MailService.parseItems(strs[8]));

                    if (resList.isEmpty() && itemList.isEmpty())
                        accessory = 0;

                    mail.setAll(title, content, senderName, 2, 0L, null, accessory, deadLine, resList, itemList);
                    MailService.getInstance().sendByType(type, parameter, mail);
                }
            }
        }
       
        else if ("dailyTaskTrigger".equalsIgnoreCase(strs[0]))
        {
            player.getDailyTaskManager().mainMissionFinishedTrigger();
        }
        
        else if ("gmOpenAllFeature".equalsIgnoreCase(strs[0]))
        {
            player.getFeatureManager().gmTriggerAll();
        }
        else if ("gmOpenFeature".equalsIgnoreCase(strs[0]) && strs.length == 2)
        {
            player.getFeatureManager().gmTriggerOne(strs[1]);
        }
        
        else if ("gmAddAchievementPoint".equalsIgnoreCase(strs[0]) && strs.length == 2)
        {
            player.getBackpackManager().addResource(ResourceType.ACHIEVEMENT, Integer.valueOf(strs[1]), true, Reasons.ITEM_GM, new Date());
        }
        else if ("gmSubAchievementPoint".equalsIgnoreCase(strs[0]) && strs.length == 2)
        {
            player.getBackpackManager().subResource(ResourceType.ACHIEVEMENT, Integer.valueOf(strs[1]), true, Reasons.ITEM_GM, new Date());
        }
        
        else if ("gmrerank".equalsIgnoreCase(strs[0]))
        {
            RankDao.selectFunMission1Rank();
            RankDao.selectFunMission2Rank();
            RankDao.selectAchievementRank();
        }
        else if ("testdailytask".equalsIgnoreCase(strs[0]) && strs.length == 1)
        {
            player.getDailyTaskManager().mainMissionFinishedTrigger();
        }
        else if ("testguidesave".equalsIgnoreCase(strs[0]) && strs.length == 1)
        {
            //player.getGuideManager().testReqSaveGuideList();
        }
        else if ("testguidelist".equalsIgnoreCase(strs[0]) && strs.length == 1)
        {
            //player.getGuideManager().testReqGuidedIdList();
        }
        else if ("testguideremove".equalsIgnoreCase(strs[0]) && strs.length == 1)
        {
            //player.getGuideManager().testReqRemoveGuideList();
        }
        
        else if ("testbuymonthcard".equalsIgnoreCase(strs[0]) && strs.length == 1)
        {
            player.buyMonthCard();
        }
        else if ("testissuemonthcardaward".equalsIgnoreCase(strs[0]) && strs.length == 1)
        {
            DBView.getInstance().issueMonthCardAward();
        }
        else if ("tick5".equalsIgnoreCase(strs[0]))
        {
            player.tick5();
        }
        else if ("setrebate".equalsIgnoreCase(strs[0]))
        {
            if (strs.length >= 3)
            {
                int rebateId = Integer.valueOf(strs[1]);
                int amount = Integer.valueOf(strs[2]);
                player.getRebateManager().getRebate(rebateId).setAmount(amount);
                player.getRebateManager().sendRebateInfo();
            }
        }
        else if ("testrechargetrigger".equalsIgnoreCase(strs[0]) && strs.length == 2)
        {
            player.getVipManager().rechargeTrigger(Integer.parseInt(strs[1]));
        }
        else if ("testbuyvipgift".equalsIgnoreCase(strs[0]) && strs.length == 2)
        {
//            player.getVipManager().ReqBuyVipGift(Integer.parseInt(strs[1]));
        }
        else if ("testrecharge".equalsIgnoreCase(strs[0]) && strs.length == 3)
        {
            RechargeMessage.ReqRechargeVerify.Builder reqMsg = RechargeMessage.ReqRechargeVerify.newBuilder();
            reqMsg.setStrBillNO(strs[1]);
            reqMsg.setStrAccessToken(strs[2]);
            player.getRechargeManager().reqRecharge(reqMsg.build());
            //player.getRechargeManager().testAddLogRechargeSuccess();
        }
        else if ("testaccurecharge".equalsIgnoreCase(strs[0]) && strs.length == 2) // 测试累计充值
        {
            int value = Integer.parseInt(strs[1]);
            player.getRechargeManager().testAddAccuRecharge(value);
        }
        else if ("testreqgetfirstrecharge".equalsIgnoreCase(strs[0]) && strs.length == 2) // 测试领取首充奖励
        {
            int id = Integer.parseInt(strs[1]);
            player.getRechargeManager().reqGetFirstRechargeReward(id);
        }
        else if ("testapplyfriend".equalsIgnoreCase(strs[0]) && strs.length == 2) // 测试申请好友
        {
            String userId = strs[1];
            GameWorld.getInstance().addCommand(new ICommand()
            {
                private long selfUserId;
                private String peerUserId;

                public ICommand set(long selfUserId, String peerUserId)
                {
                    this.selfUserId = selfUserId;
                    this.peerUserId = peerUserId;
                    return this;
                }

                @Override
                public void action()
                {
                    FriendMessage.ReqApplyFriend.Builder reqMsg = FriendMessage.ReqApplyFriend.newBuilder();
                    reqMsg.setUserId(peerUserId);
//                    GameWorld.getInstance().getFriendManager().reqApplyFriend(selfUserId, reqMsg.build());
                }
            }.set(player.getUserId(), userId));
        }
        else if ("testreplyfriendapply".equalsIgnoreCase(strs[0]) && strs.length == 3) // 测试回复申请好友
        {
            String peerUserId = strs[1];
            int opt = Integer.parseInt(strs[2]);
            GameWorld.getInstance().addCommand(new ICommand()
            {
                private long selfUserId;
                private String peerUserId;
                private int opt;

                public ICommand set(long selfUserId, String peerUserId, int opt)
                {
                    this.selfUserId = selfUserId;
                    this.peerUserId = peerUserId;
                    this.opt = opt;
                    return this;
                }

                @Override
                public void action()
                {
//                    FriendMessage.ReqReplyFriendApply.Builder reqMsg = FriendMessage.ReqReplyFriendApply.newBuilder();
//                    reqMsg.setUserId(peerUserId);
//                    reqMsg.setOpt(opt);
//                    GameWorld.getInstance().getFriendManager().reqReplyFriendApply(selfUserId, reqMsg.build());
                }
            }.set(player.getUserId(), peerUserId, opt));
        }
        else if ("testallfriendinfo".equalsIgnoreCase(strs[0]) && strs.length == 1) // 测试获取所有好友信息
        {
            GameWorld.getInstance().addCommand(new ICommand()
            {
                private long selfUserId;

                public ICommand set(long selfUserId)
                {
                    this.selfUserId = selfUserId;
                    return this;
                }

                @Override
                public void action()
                {
//                    GameWorld.getInstance().getFriendManager().reqAllFriendInfo(selfUserId);
                }
            }.set(player.getUserId()));
        }
        else if ("testapplyfriendlist".equalsIgnoreCase(strs[0]) && strs.length == 1) // 测试获取好友申请列表
        {
            GameWorld.getInstance().addCommand(new ICommand()
            {
                public long selfUserId;

                public ICommand set(long selfUserId)
                {
                    this.selfUserId = selfUserId;
                    return this;
                }

                @Override
                public void action()
                {
//                    GameWorld.getInstance().getFriendManager().reqApplyFriendList(selfUserId);
                }
            }.set(player.getUserId()));
        }
        else if ("testremovefriend".equalsIgnoreCase(strs[0]) && strs.length == 2) // 测试删除好友
        {
            String peerUserId = strs[1];
            GameWorld.getInstance().addCommand(new ICommand()
            {
                private long selfUserId;
                private String peerUserId;

                public ICommand set(long selfUserId, String peerUserId)
                {
                    this.selfUserId = selfUserId;
                    this.peerUserId = peerUserId;
                    return this;
                }

                @Override
                public void action()
                {
                    FriendMessage.ReqRemoveFriend.Builder reqMsg = FriendMessage.ReqRemoveFriend.newBuilder();
                    reqMsg.setUserId(peerUserId);
//                    GameWorld.getInstance().getFriendManager().reqRemoveFriend(selfUserId, reqMsg.build());
                }
            }.set(player.getUserId(), peerUserId));
        }
        else if ("testgetpresentenergylist".equalsIgnoreCase(strs[0]) && strs.length == 1) // 测试获取好友赠送体力列表
        {
            GameWorld.getInstance().addCommand(new ICommand()
            {
                private long selfUserId;

                public ICommand set(long selfUserId)
                {
                    this.selfUserId = selfUserId;
                    return this;
                }

                @Override
                public void action()
                {
//                    GameWorld.getInstance().getFriendManager().reqGetPresentEnergyList(selfUserId);
                }
            }.set(player.getUserId()));
        }
        else if ("testpresentenergy".equalsIgnoreCase(strs[0]) && strs.length == 2) // 测试赠送体力给好友
        {
            String peerUserId = strs[1];
            GameWorld.getInstance().addCommand(new ICommand()
            {
                private long selfUserId;
                private String peerUserId;

                public ICommand set(long selfUserId, String peerUserId)
                {
                    this.selfUserId = selfUserId;
                    this.peerUserId = peerUserId;
                    return this;
                }

                @Override
                public void action()
                {
//                    FriendMessage.ReqPresentEnergy.Builder reqMsg = FriendMessage.ReqPresentEnergy.newBuilder();
//                    reqMsg.setUserId(peerUserId);
//                    GameWorld.getInstance().getFriendManager().reqPresentEnergy(selfUserId, reqMsg.build());
                }
            }.set(player.getUserId(), peerUserId));
        }
        else if ("testgetpresentenergy".equalsIgnoreCase(strs[0]) && strs.length == 2) // 测试领取赠送体力
        {
            String uuid = strs[1];
            GameWorld.getInstance().addCommand(new ICommand()
            {
                private long selfUserId;
                private String uuid;

                public ICommand set(long selfUserId, String uuid)
                {
                    this.selfUserId = selfUserId;
                    this.uuid = uuid;
                    return this;
                }

                @Override
                public void action()
                {
//                    FriendMessage.ReqGetPresentedEnergy.Builder reqMsg = FriendMessage.ReqGetPresentedEnergy.newBuilder();
//                    reqMsg.setUuid(uuid);
//                    GameWorld.getInstance().getFriendManager().reqGetPresentedEnergy(selfUserId, reqMsg.build());
                }
            }.set(player.getUserId(), uuid));
        }
        else if ("testgetallpresentenergy".equalsIgnoreCase(strs[0]) && strs.length == 1) // 测试领取所有好友赠送的体力并回赠
        {
            GameWorld.getInstance().addCommand(new ICommand()
            {
                private long selfUserId;

                public ICommand set(long selfUserId)
                {
                    this.selfUserId = selfUserId;
                    return this;
                }

                @Override
                public void action()
                {
//                    GameWorld.getInstance().getFriendManager().reqGetAllPresentedEnergy(selfUserId);
                }
            }.set(player.getUserId()));
        }
        else if ("testsearchfriend".equalsIgnoreCase(strs[0]) && strs.length == 2) // 测试搜索好友
        {
            String searchString = strs[1];
            GameWorld.getInstance().addCommand(new ICommand()
            {
                private long selfUserId;
                private String searchString;

                public ICommand set(long selfUserId, String searchString)
                {
                    this.selfUserId = selfUserId;
                    this.searchString = searchString;
                    return this;
                }

                @Override
                public void action()
                {
                    FriendMessage.ReqSearchFriend.Builder reqMsg = FriendMessage.ReqSearchFriend.newBuilder();
                    reqMsg.setSearchString(searchString);
//                    GameWorld.getInstance().getFriendManager().reqSearchFriend(selfUserId, reqMsg.build());
                }
            }.set(player.getUserId(), searchString));
        }
        else if ("testrecommandfriend".equalsIgnoreCase(strs[0]) && strs.length == 1) // 测试推介好友
        {
            GameWorld.getInstance().addCommand(new ICommand()
            {
                private long selfUserId;

                public ICommand set(long selfUserId)
                {
                    this.selfUserId = selfUserId;
                    return this;
                }

                @Override
                public void action()
                {
//                    GameWorld.getInstance().getFriendManager().reqRecommendFriend(selfUserId);
                }
            }.set(player.getUserId()));
        }
        else
        {
            log.error("Not find GM command!");
        }

        return null;
    }
}
