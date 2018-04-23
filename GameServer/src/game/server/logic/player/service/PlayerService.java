package game.server.logic.player.service;

import game.core.message.SMessage;
import game.data.bean.q_buy_costBean;
import game.data.bean.q_globalBean;
import game.data.bean.q_vipBean;
import game.message.PlayerMessage;
import game.server.logic.constant.ErrorCode;
import game.server.logic.constant.Reasons;
import game.server.logic.constant.ResourceType;
import game.server.logic.struct.Player;
import game.server.logic.struct.Resource;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.DBView;
import game.server.util.MessageUtils;
import game.server.util.SensitiveWordFilter;
import game.server.util.WordUtils;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author
 */
public class PlayerService
{
    private final static Logger log = Logger.getLogger(PlayerService.class);

    private PlayerService()
    {
    }

    /**
     * 获取实例对象.
     *
     * @return
     */
    public static PlayerService getInstance()
    {
        return Singleton.INSTANCE.getService();
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton
    {
        INSTANCE;

        PlayerService service;

        Singleton()
        {
            this.service = new PlayerService();
        }

        PlayerService getService()
        {
            return service;
        }
    }


    /**
     * 客户端请求购买体力
     *
     * @param player
     * @param msg
     */
    public void onReqBuyEnergy(Player player)//, PlayerMessage.ReqBuyEnergy msg)
    {
        if (!player.getFeatureManager().isOpen("C3001"))
        {
            log.error("购买体力，功能未开放，玩家：" + player.getRoleName());
            return;
        }

        int vipLv = player.getVipManager().getVipLevel();
        q_vipBean vipBean = BeanTemplet.getVipBean(vipLv);
        if (vipBean == null)
        {
            log.error("玩家：[" + player.getRoleName() + "],vip配置表中对应的vip等级不存在，vipLv = " + vipLv);
            return;
        }

        int buyNum = player.getEnergyBuyNum();
        int maxNum = vipBean.getQ_buy_energy();
        if (buyNum + 1 > maxNum)
        {
            log.error("玩家：[" + player.getRoleName() + "],体力购买次数已经达到最大，buyNum = " + buyNum);
            return;
        }

        q_buy_costBean costBean = BeanTemplet.getGoldCostBean(buyNum + 1);
        if (costBean == null)
        {
            log.error("玩家：[" + player.getRoleName() + "],钻石消耗配置表中对应的次数不存在，times = " + buyNum);
            return;
        }

        int cost = costBean.getQ_energy_cost();
        if (player.getGoldBullion() < cost)
        {
            log.error("玩家：[" + player.getRoleName() + "],购买体力钻石数量不够，拥有：" + player.getGoldBullion() + " 需要：" + cost);
            return;
        }

        Resource energy = player.getBackpackManager().getResource(ResourceType.ENERGY);
        if (energy.getAmount() >= energy.getMaxAmount())
        {
            log.error("购买体力，玩家拥有的体力已经达到最大上限值。amount = " + energy.getAmount() + "，maxAmount = " + energy.getMaxAmount());
            return;
        }

        // 增加已购买次数
        player.setEnergyBuyNum(buyNum + 1);
        player.setLastResetBuyNum(System.currentTimeMillis());//重置体力购买的时刻
        player.SendAttributeChangeMsg(5, player.getEnergyBuyNum());
        // 扣除钻石
        player.getBackpackManager().subResource(ResourceType.GOLD_BULLION, cost, true, Reasons.BUY_ENERGY, Calendar.getInstance().getTime());

        // 增加体力
        q_globalBean globalBean = BeanTemplet.getGlobalBean(1005);
        int eachEnergy = 120; //默认120
        if (globalBean == null)
        {
            log.error("购买体力，全局配置错误1005");
        }
        else
        {
            eachEnergy = globalBean.getQ_int_value();
        }
        player.getBackpackManager().addResource(ResourceType.ENERGY, eachEnergy, true, Reasons.BUY_ENERGY, Calendar.getInstance().getTime());

        // 发送消息
        //PlayerMessage.ResBuyTili.Builder message = PlayerMessage.ResBuyTili.newBuilder();
        //message.setNum(eachEnergy);
        //MessageUtils.send(player, new SMessage(PlayerMessage.ResBuyTili.MsgID.eMsgID_VALUE, message.build().toByteArray()));
    }

    /**
     * 购买技能点消息
     *
     * @param player
     * @param reqMsg
     */
    public void onBuySkillPoint(Player player)//, PlayerMessage.ReqBuySkillPoint reqMsg)
    {
        int vipLv = player.getVipManager().getVipLevel();
        q_vipBean vipBean = BeanTemplet.getVipBean(vipLv);
        if (vipBean == null)
        {
            log.error("玩家：[" + player.getRoleName() + "],vip配置表中对应的vip等级不存在，vipLv = " + vipLv);
            //sendBuySkillMsg(player, false);
            return;
        }

        int buyNum = player.getSkillBuyNum();
        int maxNum = vipBean.getQ_buy_skill_point();
        if (buyNum + 1 > maxNum)
        {
            log.error("玩家：[" + player.getRoleName() + "],技能购买次数已经达到最大，buyNum = " + buyNum);
            //sendBuySkillMsg(player, false);
            return;
        }

        q_buy_costBean costBean = BeanTemplet.getGoldCostBean(buyNum + 1);
        if (costBean == null)
        {
            log.info("玩家：[" + player.getRoleName() + "],购买消耗配置表数据不存在，可能超过了。times = " + buyNum);
            //这时候就要取保底项了
            costBean = BeanTemplet.getGoldCostBean(-1);
        }

        int cost = costBean.getQ_skill_point_cost();
        if (player.getGoldBullion() < cost)
        {
            log.error("玩家：[" + player.getRoleName() + "],购买技能点金币不够，拥有：" + player.getGoldBullion() + " 需要：" + cost);
            // sendBuySkillMsg(player, false);
            return;
        }

        //扣钱了
        player.setSkillBuyNum(buyNum + 1);
        player.setLastSkillBuyTime(System.currentTimeMillis());
        player.getBackpackManager().subResource(ResourceType.GOLD_BULLION, cost, true, Reasons.BUY_SKILLPOINT, new Date());
        //先把技能点重置为0，然后增加到最大
        Resource resource = player.getBackpackManager().getResource(ResourceType.SKILL_POINT);
        resource.setAmount(0);
        q_globalBean numBean = BeanTemplet.getGlobalBean(1009);
        int addNum = 10;
        if (numBean != null)
        {
            addNum = numBean.getQ_int_value();
        }
        player.getBackpackManager().addResource(ResourceType.SKILL_POINT, addNum, true, Reasons.BUY_SKILLPOINT, new Date());
        //发送体力购买次数更新消息
        player.SendAttributeChangeMsg(7, player.getSkillBuyNum());
        //sendBuySkillMsg(player, true);
    }

    public void changeHead(Player player, int demonSoutLv)
    {

    }

    public void onChangePlayerName()
    {
        /*
        Validate.notNull(reqMsg);
        Validate.notEmpty(reqMsg.getName(), "userName is empty");

        String newName = reqMsg.getName();
        String beforeName = player.getRoleName();
        // 角色名为空判断
        ErrorCode code = checkUserName(beforeName, newName);
        if (code != null)
        {
            sendChangeNameResult(player, newName, code);
            return;
        }
        player.setRoleName(newName);
        //先发送消息
        sendChangeNameResult(player, newName, null);
        //扣除消耗
        q_globalBean bean = BeanTemplet.getGlobalBean(1006);
        int cost = 100;
        if (bean == null)
        {
            log.error("全局配置表缺失。id = " + 1006);
        }
        else
        {
            cost = bean.getQ_int_value();
        }
        //扣除消耗钻石
        player.getBackpackManager().subResource(ResourceType.GOLD_BULLION, cost, true, Reasons.CHANGE_ROLENAME, new Date());

        //更新session
        IoSession session = player.getSession();
        session.setAttribute(SessionKey.USER_NAME.name(), newName);

        //更新playerManager
        PlayerManager.changeRoleName(player, beforeName);
        //更新DBView
        DBView.getInstance().changeRoleViewRoleName(beforeName, newName);
        //更新Gameworld
        GameWorld.getInstance().addCommand(new LWPlayerUpdateHandler(player));
         */
    }

    private ErrorCode checkUserName(String beforeName, String newName)
    {
        if (newName.trim().isEmpty())
        {
            log.error("修改昵称, 新的昵称是空的: [" + newName + "]");
            return ErrorCode.USERNAME_IS_EMPTY;
        }

        // 角色名中空格判断
        if (WordUtils.hasSpace(newName))
        {
            log.error("修改昵称, 新的昵称有空格: name = " + newName);
            return ErrorCode.USERNAME_HAS_SPACE;
        }

        int len = WordUtils.getAsciiCharacterLength(newName);
        if (len > 14) // 2014.9.23 李颖亮要求从原来的6个中文改成7个中文长度
        {
            log.error("名称长度不对，超过14个字符（7个汉字）: name = [" + newName + "]");
            return ErrorCode.USERNAME_LENGTH_OVER;
        }

        if (newName.equals(beforeName))
        {
            log.error("修改昵称，两个名字昵称，客户端你怎么搞的。beforeName = " + beforeName + ", newName = " + newName);
            return ErrorCode.USERNAME_IS_SAME;
        }

        String specialChar = "!@#$%^&*()_+-=[{]}\\;:'\",<.>/?！＠＃￥％……＆×（）——＋－＝【｛】｝＼｜；：‘“，《。》、？";
        for (int i = 0; i < specialChar.length(); ++i)
        {
            char sc = specialChar.charAt(i);
            for (int j = 0; j < newName.length(); ++j)
            {
                if (sc == newName.charAt(j))
                {
                    log.error("修改昵称，为什么会有特殊符号。userName: [" + newName + "]");
                    return ErrorCode.USERNAME_HAS_SPECIAL_CHAR;
                }
            }
        }

        // 敏感词判断
        //if (SensitiveWordFilter.getInstance().hasSensitiveWord(userName) || SensitiveWordFilter.getInstance().hasSensitiveWord(roleName))
        if (SensitiveWordFilter.getInstance().hasSensitiveWord(newName))
        {
            log.error("修改昵称, 新的昵称有敏感词，客户端没判断？ name = [" + newName + "]");
            return ErrorCode.USERNAME_HAS_DIRTY_WORD;
        }

        if (DBView.getInstance().hasUserName(newName))
        {
            log.error("修改昵称, 该昵称已经创建过角色。 newName = " + newName);
            return ErrorCode.USERNAME_IS_CREATED;
        }
        return null;
    }

}
