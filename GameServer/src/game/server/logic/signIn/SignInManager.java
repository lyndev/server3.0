/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.server.logic.signIn;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haowan.logger.service.ServicesFactory;
import game.core.command.ICommand;
import game.core.message.SMessage;
import game.data.bean.q_qiandaoBean;
import game.message.SignInMessage;
import game.message.SignInMessage.ReqExtraGet;
import game.message.SignInMessage.ReqSignIn;
import game.server.logic.constant.AchievementType;
import game.server.logic.constant.ErrorCode;
import game.server.logic.constant.Reasons;
import game.server.logic.constant.ResourceType;
import game.server.logic.item.bean.Item;
import game.server.logic.struct.Player;
import game.server.logic.support.BeanFactory;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.IJsonConverter;
import game.server.thread.BackLogProcessor;
import game.server.util.MessageUtils;
import game.server.util.MiscUtils;
import game.server.util.UniqueId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;


/**
 *
 * @author YangHanzhou
 */
public class SignInManager implements IJsonConverter
{
    private final static Logger log = Logger.getLogger(SignInManager.class);
    private final transient Player owner;
    private boolean isSignIn;                       // 今日是否已经签到过 （false: 没有签到   true: 已经签到）
    private int signInNum;                          // 每月签到累计次数
    private long lastResetTime;                     // 上次签到重置时间
    private int month;                              // 签到当前月份  
    private Map<Integer, Boolean> extraSignedMap;   // 补签情况<K:签到第几天，V:是否补签>
//    private boolean isExtraSigned;                  //今日是否补领过
    
    public SignInManager(Player owner)
    {
        this.owner = owner;
        this.isSignIn = false;
        this.signInNum = 0;
        this.lastResetTime = 0;
//        this.isExtraSigned = true;
        this.extraSignedMap = new HashMap<>();
        
        Calendar current = Calendar.getInstance();
        this.month = current.get(Calendar.MONTH) + 1;
    }
    
    public void tick5(){
        resetSignInNum();
    }

    /**
     *处理签到消息
     * @param msg
     */
    public void onReqSignIn(ReqSignIn msg)
    {
        SignInMessage.ResSignInResult.Builder message = SignInMessage.ResSignInResult.newBuilder();
        ErrorCode result = reqSignIn(msg);
        message.setIsSignIn(this.isSignIn);
        message.setNum(this.signInNum);
        boolean bRst = extraSignedMap.containsKey(msg.getNum()) ? extraSignedMap.get(msg.getNum()):true ;
        message.setIsExtraSignIn(bRst);
        message.setECode(result.getCode());
        if (result == ErrorCode.OK){
            //签到成就触发
            
        }
        MessageUtils.send(owner, new SMessage(SignInMessage.ResSignInResult.MsgID.eMsgID_VALUE, message.build().toByteArray()));
    }
    
    /**
     * 请求补领
     * @param reqMsg 
     */
    public void onReqExtraGet(ReqExtraGet reqMsg){
        SignInMessage.ResResExtraGet.Builder rst = SignInMessage.ResResExtraGet.newBuilder();  
        do
        {   //是否能够补领
            if (!extraSignedMap.containsKey(reqMsg.getNum()) || extraSignedMap.get(reqMsg.getNum())){
                rst.setECode(ErrorCode.SIGN_CAN_NOT_EXTRA_GET.getCode());
                break;
            }
           
            //有没有VIP双倍
            String strAward = getAward(this.month, reqMsg.getNum());
            String[] arrAward = strAward.split("_");
            if (arrAward.length != 4){
                rst.setECode(ErrorCode.SIGN_CAN_NOT_EXTRA_GET.getCode());
                break;
            }
            
            //是否达到双倍需要的VIP等级
            int vipDouble = Integer.valueOf(arrAward[3]);
            if (owner.getVipManager().getVipLevel() < vipDouble){
                rst.setECode(ErrorCode.SIGN_CAN_NOT_EXTRA_GET.getCode());
                break; 
            }
            
            //发奖励
            int type = Integer.valueOf(arrAward[0]);
            int id   = Integer.valueOf(arrAward[1]);
            int num  = Integer.valueOf(arrAward[2]);
            ErrorCode code = giveAward(type, id, num);
            if (code != ErrorCode.OK){
                rst.setECode(code.getCode());
                break;
            }
            
//            isExtraSigned = true;
            extraSignedMap.put(reqMsg.getNum(), true);
            rst.setECode(ErrorCode.OK.getCode());
        } while (false);
        boolean bRst = extraSignedMap.containsKey(reqMsg.getNum()) ? extraSignedMap.get(reqMsg.getNum()):true ;
        rst.setNum(reqMsg.getNum());
        rst.setIsExtraSignIn(bRst);
        MessageUtils.send(owner, new SMessage(SignInMessage.ResResExtraGet.MsgID.eMsgID_VALUE, rst.build().toByteArray()));
    }
    
    
    /**
     *重置签到标记
     */
    public void resetSignInNum()
    {
        clientInitializeOver();
    }
    
    public void clientInitializeOver()
    {
        // 是否停服跨天导致签到次数没有重置
        Calendar current = Calendar.getInstance();
        int currentDay = current.get(Calendar.DAY_OF_MONTH);
        int currentMonth = current.get(Calendar.MONTH) + 1;
        int currentHour = current.get(Calendar.HOUR_OF_DAY);
        
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(lastResetTime);
        int lastDay = cal.get(Calendar.DAY_OF_MONTH);
        int lastMonth = cal.get(Calendar.MONTH) + 1;
        this.month = lastMonth;
        
        // 从未签到过
        if (lastResetTime == 0)
        {
            if (currentDay == 1 && currentHour < 5)
            {
                month = (currentMonth - 1) > 0 ? (currentMonth - 1) : 12;
            }
            else
            {
                month = currentMonth;
            }
            
            isSignIn = false;
            signInNum = 0;
        }
        else
        {
            // 跨天（5点）
            if (MiscUtils.across5(lastResetTime, System.currentTimeMillis()))
            {
                this.setIsSignIn(false);
//                this.isExtraSigned = true;

                // 翻月
                if (currentMonth != lastMonth)
                {
                    if ((currentDay == 1 && currentHour >= 5) || currentDay > 1)
                    {
                        signInNum = 0;
                        month = currentMonth;
                    }
                }
                else
                {
                    if (currentDay == 1 && currentHour >= 5)
                    {
                        signInNum = 0;
                        month = currentMonth;
                    }
                }
            }
        }
        
        SignInMessage.ResSignInInfo.Builder message = SignInMessage.ResSignInInfo.newBuilder();
        message.setIsSignIn(isSignIn);
        message.setNum(signInNum);
        SignInMessage.ExtraSignInfo.Builder msg = SignInMessage.ExtraSignInfo.newBuilder();
        for (Map.Entry<Integer,Boolean> iterMap:extraSignedMap.entrySet()){
            msg.clear();
            msg.setDay(iterMap.getKey());
            msg.setCanExtraSigned(iterMap.getValue());
            message.addExtraInfo(msg.build());
            
        }
        message.setMonth(month);
        MessageUtils.send(owner, new SMessage(SignInMessage.ResSignInInfo.MsgID.eMsgID_VALUE, message.build().toByteArray()));
    }
    
    private ErrorCode reqSignIn(ReqSignIn msg)
    {
        if (this.isSignIn == true)
        {
            log.error("今日重复签到");
            return ErrorCode.SIGN_REPEATED;
        }
        
        if (msg.getNum() != this.signInNum + 1)
        {
            log.error("客户端签到次数错误 当前签到次数："+ msg.getNum() + " 服务器签到次数："+ this.signInNum);
            return ErrorCode.SIGN_TIMES_ERROR;
        }
        
        // 领取签到奖励
        String award = getAward(this.month, this.signInNum + 1);
        if (award == null)
        {
            log.error("签到奖励数据错误，签到奖励月："+ this.month +" 次数："+(this.signInNum + 1));
            return ErrorCode.SIGN_AWARD_EMPTY;
        }
        String[] split = award.split("_");
        if (split.length != 3 && split.length != 4)
        {
            log.error("签到奖励数据格式错误,签到奖励月："+ this.month +" 次数："+(this.signInNum + 1));
            return ErrorCode.SIGN_AWARD_FORMAT_ERROR;
        }
        int type = Integer.valueOf(split[0]);
        int id   = Integer.valueOf(split[1]);
        int num  = Integer.valueOf(split[2]);
        if (split.length == 4){
            int vipDouble = Integer.valueOf(split[3]);
            if (owner.getVipManager().getVipLevel() >= vipDouble){
                num = num * 2;
                extraSignedMap.put(this.signInNum + 1, true);
            }else{
                extraSignedMap.put(this.signInNum + 1, false);
//                isExtraSigned = false;
            }
        }
        ErrorCode code = giveAward(type, id, num);
        if (code != ErrorCode.OK){
            return code;
        }
        
        this.setIsSignIn(true);
        this.setLastResetTime(System.currentTimeMillis());
        this.setSignInNum(signInNum + 1);
        // log
        addLogSignIn(owner);
        return ErrorCode.OK;
    }
    
    /**
     * 发放奖励
     * @param type
     * @param id
     * @param num
     * @return 
     */
    private ErrorCode giveAward(int type, int id, int num){
        switch (type)
        {
            case 1:
                owner.getBackpackManager().addResource(ResourceType.GOLD_BULLION, num, true, Reasons.ITEM_SIGNIN, Calendar.getInstance().getTime());
                break;
            case 2:
                owner.getBackpackManager().addResource(ResourceType.GOLD, num, true, Reasons.ITEM_SIGNIN, Calendar.getInstance().getTime());
                break;
            case 3:

                break;
            case 4:
                Item createItem = BeanFactory.createItem(id, num);
                owner.getBackpackManager().addItem(createItem, true, Reasons.ITEM_SIGNIN);
                break;
            default:
                log.error("签到奖励数据格式错误,数据类型："+ type);
                return ErrorCode.SIGN_AWARD_FORMAT_ERROR;  
        } 
        return ErrorCode.OK;
    }
    
    
    public boolean isIsSignIn()
    {
        return isSignIn;
    }

    public void setIsSignIn(boolean isSignIn)
    {
        this.isSignIn = isSignIn;
    }

    public int getSignInNum()
    {
        return signInNum;
    }

    public void setSignInNum(int signInNum)
    {
        this.signInNum = signInNum;
    }

    public long getLastResetTime()
    {
        return lastResetTime;
    }

    public void setLastResetTime(long lastResetTime)
    {
        this.lastResetTime = lastResetTime;
    }

    public int getMonth()
    {
        return month;
    }

    public void setMonth(int month)
    {
        this.month = month;
    }

    private String getAward(int month, int date)
    {
        q_qiandaoBean signInBean = BeanTemplet.getSignInBean(month);
        if (signInBean == null)
        {
            return null;
        }
        switch (date)
        {
            case 1:
                return signInBean.getQ_item1();
            case 2:
                return signInBean.getQ_item2();
            case 3:
                return signInBean.getQ_item3();
            case 4:
                return signInBean.getQ_item4();
            case 5:
                return signInBean.getQ_item5();
            case 6:
                return signInBean.getQ_item6();
            case 7:
                return signInBean.getQ_item7();
            case 8:
                return signInBean.getQ_item8();
            case 9:
                return signInBean.getQ_item9();
            case 10:
                return signInBean.getQ_item10();
            case 11:
                return signInBean.getQ_item11();
            case 12:
                return signInBean.getQ_item12();
            case 13:
                return signInBean.getQ_item13();
            case 14:
                return signInBean.getQ_item14();
            case 15:
                return signInBean.getQ_item15();
            case 16:
                return signInBean.getQ_item16();
            case 17:
                return signInBean.getQ_item17();
            case 18:
                return signInBean.getQ_item18();
            case 19:
                return signInBean.getQ_item19();
            case 20:
                return signInBean.getQ_item20();
            case 21:
                return signInBean.getQ_item21();
            case 22:
                return signInBean.getQ_item22();
            case 23:
                return signInBean.getQ_item23();
            case 24:
                return signInBean.getQ_item24();
            case 25:
                return signInBean.getQ_item25();
            case 26:
                return signInBean.getQ_item26();
            case 27:
                return signInBean.getQ_item27();
            case 28:
                return signInBean.getQ_item28();
            case 29:
                return signInBean.getQ_item29();
            case 30:
                return signInBean.getQ_item30();
            case 31:
                return signInBean.getQ_item31();
            default:
                return null;             
        }
    }
    
    private void addLogSignIn(Player player)
    {
        BackLogProcessor.getInstance().addCommand(new ICommand()
        {
            private String fgi;
            private String serverId;
            private String roleId;
            private String fedId;
            private String sign;
            private Date date;

            public ICommand set(String fgi,
                    String serverId,
                    String roleId,
                    String fedId,
                    String sign,
                    Date date)
            {
                this.fgi = fgi;
                this.serverId = serverId;
                this.roleId = roleId;
                this.fedId = fedId;
                this.sign = sign;
                this.date = date;
                return this;
            }

            @Override
            public void action()
            {
                ServicesFactory.getSingleLogService().addLogSign(
                        fgi,
                        serverId,
                        roleId,
                        fedId,
                        sign,
                        Long.toString(date.getTime() / 1000));
            }
        }.set(player.getFgi(),
                Integer.toString(player.getServerId()),
                UniqueId.toBase36(player.getRoleId()),
                player.getFedId(),
                Integer.toString(player.getSignInManager().getSignInNum()),
                new Date()));
    }
    
    @Override
    public JSON toJson()
    {
        JSONObject data = new JSONObject();
        data.put("isSignIn", isSignIn);
        data.put("signInNum", signInNum);
        data.put("lastResetTime", lastResetTime);
        
        JSONArray array = new JSONArray();
        for (Map.Entry<Integer, Boolean> iter:extraSignedMap.entrySet()){
            if (!iter.getValue()){
                array.add(iter.getKey());
            }
        }
        data.put("signedMap", array);
        return data;
    }

    @Override
    public void fromJson(JSON json)
    {
        if (json == null)
        {
            return;
        }
        JSONObject data = (JSONObject) json;
        this.setIsSignIn(data.getBoolean("isSignIn"));
        this.setSignInNum(data.getIntValue("signInNum"));
        this.setLastResetTime(data.getLongValue("lastResetTime"));
        if (data.containsKey("signedMap")){
            JSONArray array = data.getJSONArray("signedMap");
            for (Object arrObj:array){
                extraSignedMap.put((Integer)arrObj, false);
            }
        }
    }
}
