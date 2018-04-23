/**
 * @date 2014/9/24
 * @author ChenLong
 */
package game.server.logic.recharge.bean;

import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class ItemCode
{
    private final static Logger logger = Logger.getLogger(ItemCode.class);

    private int goodsType;
    private int recommend;

    public static ItemCode parseFromString(String str)
    {
        ItemCode itemCode = new ItemCode();
        boolean success = true;
        do
        {
            if (str == null || str.trim().isEmpty())
            {
                success = false;
                logger.error("ItemCode str isEmpty");
                break;
            }
            String[] tokens1 = str.split(";");
            for (String token1 : tokens1)
            {
                if (!success)
                    break;

                String[] tokens2 = token1.split("_");
                if (tokens2.length >= 2)
                {
                    String key = tokens2[0];
                    String value = tokens2[1];
                    switch (key)
                    {
                        case "recommend":
                            itemCode.recommend = Integer.parseInt(value);
                            break;
                        case "goodsType":
                            itemCode.goodsType = Integer.parseInt(value);
                            break;
                        default:
                            logger.warn("ItemCode parseFromString unknow key: [" + key + "]");
                            break;
                    }
                }
                else
                {
                    success = false;
                    logger.error("tokens2.length < 2");
                    break;
                }
            }
        } while (false);
        return success ? itemCode : null;
    }

    public int getGoodsType()
    {
        return goodsType;
    }

    public int getRecommend()
    {
        return recommend;
    }
}
