package script.feature;

import game.core.script.IScript;
import game.data.bean.q_controlBean;
import game.data.bean.q_languageBean;
import game.data.bean.q_main_missionBean;
import game.data.bean.q_taskBean;
import game.server.logic.struct.Player;
import game.server.logic.support.BeanTemplet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * 
 * @date   2014-8-6
 * @author pengmian
 */
public class FeatureCheckScript implements IScript
{

    private static final Logger logger = Logger.getLogger("FeatureCheckScript.class");

    @Override
    public int getId()
    {
        return 1026;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object call(int scriptId, Object arg)
    {
        String reason = "ok";
//        String errorReason = "no open";
        do
        {
            if (arg instanceof Map)
            {
                Map<String, Object> map = (HashMap) arg;
                if (map.isEmpty())
                {
                    logger.error("map.isEmpty()");
                    reason = BeanTemplet.getLanguage(210026).getQ_lgtext();
                    break;
                }

                Player owner = (Player) map.get("player");
                if (owner == null)
                {
                    logger.error("player == null！");
                    reason = BeanTemplet.getLanguage(210026).getQ_lgtext();
                    break;
                }

                q_controlBean bean = (q_controlBean) map.get("bean");
                if (bean == null)
                {
                    logger.error("bean == null");
                    reason = BeanTemplet.getLanguage(210026).getQ_lgtext();
                    break;
                }
   
                // 主角等级
                if (owner.getRoleLevel() < bean.getQ_act_level())
                {
                    reason = String.format(BeanTemplet.getLanguage(210027).getQ_lgtext(), bean.getQ_act_level());
                    break;
                }
                
                // VIP等级
                if (owner.getVipManager().getVipLevel() < bean.getQ_vip_level())
                {
                    reason = String.format(BeanTemplet.getLanguage(210028).getQ_lgtext(), bean.getQ_vip_level());
                    break;
                }
            }
            else
            {
                reason = BeanTemplet.getLanguage(220024).getQ_lgtext();
            }
        } while (false);

        return reason;

    }

}
