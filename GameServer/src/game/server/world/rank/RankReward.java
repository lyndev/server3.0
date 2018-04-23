package game.server.world.rank;

import game.core.util.ArrayUtils;
//import game.data.bean.q_battlerewardBean;
import game.server.logic.constant.ResourceType;
import game.server.logic.item.bean.Item;
import game.server.logic.mail.bean.Mail;
import game.server.logic.mail.service.MailService;
import game.server.logic.struct.Resource;
import game.server.logic.support.BeanFactory;
import game.server.logic.support.BeanTemplet;
import game.server.util.UniqueId;
import game.server.world.rank.struct.RankInfo;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author ZouZhaopeng
 */
public class RankReward
{
    private static final Logger log = Logger.getLogger(RankReward.class);
    
    public static void giveReward(List<RankInfo> rankList)
    {
        for (RankInfo rankInfo : rankList)
        {
            final int rankLevel = getRankLevel(rankInfo.getRank());
            if (rankLevel == -1)
            {
                //战场奖励分段配置有误, 跳过不发奖励
                continue;
            }
//            q_battlerewardBean bean = BeanTemplet.getBattleRewardBean(getRankBeanId(rankInfo.getBfStar(), rankLevel));
//            if (bean == null)
//                continue;
//            
//            int deadLine = (int)(System.currentTimeMillis() / 1000) + 60 * 60 * 24;
//
//            List<Resource> resList = new ArrayList<>(1);
//            final int resNum = bean.getQ_reward_gold();
//            if (resNum > 0)
//                resList.add(new Resource(ResourceType.GOLD_BULLION.value(), resNum));
//
//            List<Item> itemList = new ArrayList<>();
//            final String itemStr = bean.getQ_reward_box();
//            if (itemStr != null)
//            {
//                final String[] id_nums = itemStr.split(";");
//                if (id_nums != null)
//                {
//                    for (String each : id_nums)
//                    {
//                        final int[] id_num = ArrayUtils.parseInt(each.split("_"));
//                        if (id_num != null && id_num.length == 2 && id_num[1] > 0)
//                            itemList.add(BeanFactory.createItem(id_num[0],id_num[1]));
//                    }
//                }
//            }
//            int accessory = 1;
//            if (resList.isEmpty() && itemList.isEmpty())
//                accessory = 0;
//
//            Mail mail = new Mail();
//            mail.setAll("战场奖励", "掠夺排名奖励如下:", "系统", 1, 0L, null, accessory, deadLine, resList, itemList);

//            MailService.getInstance().sendByType(2, UniqueId.toBase36(rankInfo.getUserId()), mail);
        }
    }
    
    /**
     * 根据排名获取排名分段
     * 
     * @param rank
     * @return 正常返回排名分段, 负数表示配置错误
     */
    public static int getRankLevel(int rank)
    {
        String config = BeanTemplet.getGlobalBean(1104).getQ_string_value();
        if (config == null || config.isEmpty())
        {
            log.error("排名分段配置有误,全局配置字段为1104");
            return -1;
        }
        String[] configs = config.split(";");
        if (configs == null || configs.length == 0)
        {
            log.error("排名分段配置有误,全局配置字段为1104");
            return -1;
        }
        int[] lvl_min_max;
        for (String each : configs)
        {
            lvl_min_max = ArrayUtils.parseInt(each.split("_"));
            if (lvl_min_max == null || lvl_min_max.length < 3)
            {
                log.error("排名分段配置有误,全局配置字段为1104");
                return -1;
            }
            if (rank >=lvl_min_max[1] && rank <= lvl_min_max[2])
                return lvl_min_max[0];
        }
        return -1;
    }
    
    public static int getRankBeanId(int battleField, int rankLevel)
    {
        return (battleField - 2) * 8 + rankLevel;
    }
}
