/**
 * @date 2014/11/7
 * @author ChenLong
 */
package game.server.thread.dboperator.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.server.db.game.bean.RoleBean;
import game.server.db.game.dao.RoleDao;
import game.server.logic.constant.JsonKey;
import game.server.logic.support.DBView;
import game.server.util.UniqueId;
import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class ReqBuyAllPlayerMonthCardHandler extends DBOperatorHandler
{
    private static final Logger logger = Logger.getLogger(ReqBuyAllPlayerMonthCardHandler.class);
    private final List<String> offlineRoleIds;

    public ReqBuyAllPlayerMonthCardHandler(List<String> offlineRoleIds)
    {
        super(0);
        this.offlineRoleIds = offlineRoleIds;
    }

    @Override
    public void action()
    {
        for (String roleId : offlineRoleIds)
        {
            try
            {
                long nowTime = System.currentTimeMillis();
                RoleBean roleBean = RoleDao.selectByRoleId(roleId);
                if (roleBean != null)
                {
                    roleBean.uncompress();
                    String miscData = roleBean.getMiscData();
                    JSONObject jsonObj = JSON.parseObject(miscData);
                    if (jsonObj.containsKey(JsonKey.MONTH_CARD_EXPIRY_DATE.getKey()))
                    {

                        long monthCardExpiryDate = jsonObj.getLongValue(JsonKey.MONTH_CARD_EXPIRY_DATE.getKey());
                        long lRoleId = UniqueId.toBase10(roleId);
                        boolean isNewMonthCard = false; // 新月卡还是续期?

                        if (monthCardExpiryDate < nowTime)
                            isNewMonthCard = true;

                        long startTime = Math.max(nowTime, monthCardExpiryDate); // 计算累加月卡有效期起始时间点
                        logger.info("buyMonthCard, nowTime = " + nowTime + ", monthCardExpiryDate = " + monthCardExpiryDate + ", startTime = " + startTime);

                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(startTime);
                        cal.add(Calendar.DAY_OF_MONTH, 30); // 增加月卡有效期30天

                        monthCardExpiryDate = cal.getTimeInMillis();
                        jsonObj.put(JsonKey.MONTH_CARD_EXPIRY_DATE.getKey(), monthCardExpiryDate);

                        roleBean.setMiscData(jsonObj.toJSONString());
                        roleBean.compress();
                        RoleDao.updateMiscData(roleBean);

                        DBView.getInstance().setMonthCardExpiryDate(lRoleId, monthCardExpiryDate);
                        logger.info("buyMonthCard, roleId = " + roleId
                                + ", roleName: [" + roleBean.getRoleName() + "] new monthCardExpiryDate = " + monthCardExpiryDate);

                        if (isNewMonthCard) // 2014.10.29 王老板/梁晨要求新月卡购买时就发一次奖励
                        {
                            DBView.getInstance().issueMonthCardAward(lRoleId);
                            logger.info("buyMonthCard, isNewMonthCard issueMonthCardAward");
                        }
                    }
                    else
                    {
                        logger.error("cannot find JsonKey.MONTH_CARD_EXPIRY_DATE Key");
                    }
                }
                else
                {
                    logger.warn("buyAllOfflinePlayerMonthCard cannot get RoleBean roleId = " + roleId);
                }
            }
            catch (Exception ex)
            {
                logger.error("buyAllOfflinePlayerMonthCard", ex);
            }
        }
    }
}
