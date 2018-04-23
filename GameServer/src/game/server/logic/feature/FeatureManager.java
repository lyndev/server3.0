package game.server.logic.feature;

import game.core.message.SMessage;
import game.core.script.ScriptManager;
import game.data.bean.q_controlBean;
import game.message.NewFeatureMessage;
import game.server.logic.struct.Player;
import game.server.logic.support.BeanTemplet;
import game.server.util.MessageUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * 新功能开放
 *
 * @date 2014-7-7
 * @author pengmian
 */
public class FeatureManager
{
    private final Player owner;
    private final List<String> lockedFeatures;   // 未开启的功能列表
    private final static Logger logger = Logger.getLogger(FeatureManager.class);
    private final static String CHECK_OK = "ok";
    private final Object syncObj = new Object();
    private Set<String> openedFun; //已经开放的功能
    
    
    
    public FeatureManager(Player owner)
    {
        this.lockedFeatures = new ArrayList<>();
        this.owner = owner;
        openedFun = new HashSet<>();
    }

    /**
     * 初始化
     */
    public void clientInitializeOver()
    {
        this.lockedFeatures.clear();
        NewFeatureMessage.ResFeatureInfo.Builder builder = NewFeatureMessage.ResFeatureInfo.newBuilder();
        Iterator<q_controlBean> it = BeanTemplet.getAllControlBean().iterator();
        while (it.hasNext())
        {
            NewFeatureMessage.FeatureInfo.Builder featureInfo = NewFeatureMessage.FeatureInfo.newBuilder();
            int curState = 1;
            q_controlBean bean = (q_controlBean) it.next();
            /// 未配置开启
            String ret = isConfigureOpen(bean);
            if (!CHECK_OK.equalsIgnoreCase(ret))
            {
                lockedFeatures.add(bean.getQ_functionId());
                curState = 2;
            }
            else
            {
                /// 条件不满足
                ret = isMeeting(bean);
                if (!CHECK_OK.equalsIgnoreCase(ret))
                {
                    lockedFeatures.add(bean.getQ_functionId());
                    curState = 2;
                }else{
                    openedFun.add(bean.getQ_functionId());
                }
            }

            featureInfo.setFeatureId(bean.getQ_functionId());
            featureInfo.setFeatureState(curState);
            featureInfo.setReason(ret);       
//            if (curState != 1)
//                logger.warn("Player[" + owner.getRoleName() + "]功能[" + bean.getQ_functionId() + "]被关闭:[" + ret + "]");
            
            builder.addInfo(featureInfo);
        }

        if (builder.getInfoCount() > 0)
        {
            sendFeatureMessage(NewFeatureMessage.ResFeatureInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
    }

    /**
     * 判断某个功能是否开放
     *
     * @param featureId
     * @return
     */
    public boolean isOpen(String featureId)
    {
        if (openedFun.contains(featureId)){
            return true;
        }
        q_controlBean bean = BeanTemplet.getControlBean(featureId);        
        return isConfigureOpen(bean).equalsIgnoreCase(CHECK_OK) && isMeeting(bean).equalsIgnoreCase(CHECK_OK);
    }

    public static boolean isConfigureOpen(String funId)
    {
        return isConfigureOpen(BeanTemplet.getControlBean(funId)).equalsIgnoreCase(CHECK_OK);
    }

    /**
     * 触发新功能开放
     *
     * @param obj
     */
    public void trigger(Object obj)
    {
        synchronized (syncObj)
        {
            Iterator<String> it = lockedFeatures.iterator();
            while (it.hasNext())
            {
                String featureId = it.next();
                if (isOpen(featureId))
                {
                    sendNewFeatureMessage(featureId);
                    if(featureId.equals("C1201") || featureId.equals("C2901"))
                    {
                        owner.getStoreManager().checkUnlockStore();
                    }
                    it.remove();
                    openedFun.add(featureId);
                }
            }
        }

    }

    /**
     * 配置是否开放
     *
     * @param bean
     * @return
     */
    private static String isConfigureOpen(q_controlBean bean)
    {
        if (bean == null)
        {
            /// 未配置，默认开启
            return CHECK_OK;
        } 

        if (bean.getQ_is_open() != 1)
        {
            return BeanTemplet.getLanguage(220024).getQ_lgtext();
        }
        return CHECK_OK;
    }

    /**
     * player条件是否满足开启新功能
     *
     * @param bean
     * @return 错误的语言包ID
     */
    private String isMeeting(q_controlBean bean)
    {
        if (bean == null)
        {
            // 未配置，默认开启？
            return CHECK_OK;
        }
        
        HashMap<String, Object> scriptArg = new HashMap<>();
        scriptArg.put("player", owner);
        scriptArg.put("bean", bean);

        String ret = (String) ScriptManager.getInstance().call(1026, scriptArg);
        if (ret == null)
        {
            logger.error("脚本[" + bean.getQ_script() + "]调用出错！");
            return "bad";
        }
        
        if (!CHECK_OK.equalsIgnoreCase(ret))
        {
            return ret;
        }

        /// 脚本验证
        if (bean.getQ_script() != 0)
        {
            ret = (String)ScriptManager.getInstance().call(bean.getQ_script(), scriptArg);
            if (ret == null)
            {
                logger.error("脚本[" + bean.getQ_script() + "]调用出错！");
                return "bad";
            }
            
            if (!CHECK_OK.equalsIgnoreCase(ret))
            {
                return ret;
            }
        }
        return CHECK_OK;
    }

    /**
     * 通知客户端
     */
    private void sendFeatureMessage(int eMsgID_VALUE, byte[] toByteArray)
    {
        MessageUtils.send(owner, new SMessage(eMsgID_VALUE, toByteArray));
    }

    public void gmTriggerAll()
    {
        Iterator<String> it = lockedFeatures.iterator();
        while (it.hasNext())
        {
            sendNewFeatureMessage(it.next());
            it.remove();
        }
    }

    public void gmTriggerOne(String featureId)
    {
        Iterator<String> it = lockedFeatures.iterator();
        while (it.hasNext())
        {
            if (it.next().equalsIgnoreCase(featureId))
            {
                sendNewFeatureMessage(featureId);
                lockedFeatures.remove(featureId);
                break;
            }
        }
    }

    public void sendNewFeatureMessage(String featureId)
    {
        NewFeatureMessage.ResUpdateFeature.Builder builder = NewFeatureMessage.ResUpdateFeature.newBuilder();
        NewFeatureMessage.FeatureInfo.Builder featureInfo = NewFeatureMessage.FeatureInfo.newBuilder();
        featureInfo.setFeatureId(featureId);
        featureInfo.setFeatureState(3); // 新开启
        builder.setInfo(featureInfo);
        sendFeatureMessage(NewFeatureMessage.ResUpdateFeature.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        logger.warn("player: [" + owner.getRoleName() + "]功能：[" + featureId + "]开放");
    }
}
