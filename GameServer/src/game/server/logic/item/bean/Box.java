package game.server.logic.item.bean;

import game.core.message.SMessage;
import game.core.util.SimpleRandom;
import game.data.bean.q_boxBean;
import game.data.bean.q_box_itemsBean;
import game.message.BackpackMessage;
import game.server.logic.constant.Reasons;
import game.server.logic.struct.Player;
import game.server.logic.support.BeanFactory;
import game.server.logic.support.BeanTemplet;
import game.server.util.MessageUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * <b>宝箱.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">XU</a>
 * @version 1.0.0
 */
public final class Box extends Item
{
    private static final Logger LOG = Logger.getLogger(Box.class);
//    @Override
//    public boolean doUse(Object target)
//    {
//        ScriptArgs args = new ScriptArgs();
//        args.put(ScriptArgs.Key.ITEM, this);
//        args.put(ScriptArgs.Key.PLAYER, target);
//
//        if (ScriptManager.getInstance().call(getScriptId(), args) != null)
//        {
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//    }

    @Override
    public boolean doUse(Object target)
    {
        int boxId = getScriptId();
        q_boxBean boxBean = BeanTemplet.getBoxBean(boxId);
        if (boxBean == null)
        {
            LOG.error("宝箱配置表数据缺失。id = " + boxId);
            return false;
        }
        String strWeight = boxBean.getQ_weight();
        if (strWeight.isEmpty() || strWeight.equals(""))
        {
            LOG.error("宝箱配置表q_weight字段数据为空。id = " + boxId);
            return false;
        }
        String[] arrWeight = strWeight.split(";");
        //先扫描，计算出权重总值
        int totalWeight = 0;
        int boxPackIds[] = new int[arrWeight.length];
        int weights[] = new int[arrWeight.length];
        int itemNums[] = new int[arrWeight.length];
        for (int i = 0; i < arrWeight.length; i++)
        {
            String[] id_weight_num = arrWeight[i].split("_");
            boxPackIds[i] = Integer.valueOf(id_weight_num[0]);
            weights[i] = Integer.valueOf(id_weight_num[1]);
            itemNums[i] = Integer.valueOf(id_weight_num[2]);
            totalWeight += weights[i];
        }
        if (weights.length <= 0 && totalWeight <= 0)
        {
            LOG.error("宝箱配置表q_weight字段数据为空。id = " + boxId);
            return false;
        }
        SimpleRandom random = new SimpleRandom();
        int nRandomWeight = random.next(1, totalWeight);
        int boxPackId = 0;  //宝箱道具列表ID
        int itemNum = 0;    //道具数量
        int addWeight = 0;  //权重累加
        for (int i = 0; i < weights.length; i++)
        {
            addWeight += weights[i];
            if (addWeight >= nRandomWeight)
            {
                boxPackId = boxPackIds[i];
                itemNum = itemNums[i];
                break;
            }
        }

        if (boxPackId == 0 || itemNum == 0)
        {
            LOG.error("fuck，怎么没有随机出宝箱道具包出来。id = " + boxId);
            return false;
        }

        List<Item> items = randomItems(boxPackId, itemNum);
        if (items != null && !items.isEmpty())
        {
            //去一下重
            items = overlapItems(items);
            Player player = (Player) target;
            //发送宝箱消息打开
            sendOpenBoxMessage(player, 1, items);
            //添加资源
            player.getBackpackManager().addItem(items, true, Reasons.OPEN_BOXES);
            return true;
        }
        return false;
    }

    private List<Item> randomItems(int boxItemId, int num)
    {
        q_box_itemsBean boxItemBean = BeanTemplet.getBoxItemBean(boxItemId);
        if (boxItemBean == null)
        {
            LOG.error("宝箱道具包表数据缺失。id = " + boxItemId);
            return null;
        }
        String strItems = boxItemBean.getQ_items();
        if (strItems.isEmpty() || strItems.equals(""))
        {
            LOG.error("宝箱道具包表数据配置错误。id = " + boxItemId);
            return null;
        }

        String[] arrItems = strItems.split(";");
        int[] itemWeights = new int[arrItems.length];
        int totalItemWeight = 0;
        int[] itemIds = new int[arrItems.length];
        for (int i = 0; i < arrItems.length; i++)
        {
            String[] id_weight = arrItems[i].split("_");
            itemIds[i] = Integer.valueOf(id_weight[0]);
            itemWeights[i] = Integer.valueOf(id_weight[1]);
            totalItemWeight += itemWeights[i];
        }
        SimpleRandom random = new SimpleRandom();
        List<Item> result = new ArrayList<>();
        //随机num次
        for (int n = 0; n < num; n++)
        {
            int nRandomWeight = random.next(1, totalItemWeight);
            int addWeight = 0;  //权重累加
            for (int i = 0; i < itemWeights.length; i++)
            {
                addWeight += itemWeights[i];
                if (addWeight >= nRandomWeight)
                {
                    Item item = BeanFactory.createItem(itemIds[i], 1);
                    if (item != null)
                    {
                        result.add(item);
                    }
                    break;
                }
            }
        }
        return result;
    }

    private List<Item> overlapItems(List<Item> items)
    {
        List<Item> result = new ArrayList<>();
        for (Item item : items)
        {
            boolean isRepeat = false;
            for (Item item1 : result)
            {
                if (item.getId() == item1.getId())
                {
                    item1.setNum(item1.getNum() + item.getNum());
                    isRepeat = true;
                    break;
                }
            }
            if (!isRepeat)
            {
                result.add(item);
            }
        }
        return result;
    }

    private void sendOpenBoxMessage(Player player, int boxNum, List<Item> openItems)
    {
        BackpackMessage.ResOpenBox.Builder message = BackpackMessage.ResOpenBox.newBuilder();
        message.setBoxId(getId());
        message.setBoxNum(boxNum);
        for (Item item : openItems)
        {
            message.addItems(item.getMessageBuilder());
        }
        // 向玩家发送开出的物品信息，用于客户端播放开箱动画
        MessageUtils.send(player, new SMessage(
                BackpackMessage.ResOpenBox.MsgID.eMsgID_VALUE,
                message.build().toByteArray()));
    }
}
