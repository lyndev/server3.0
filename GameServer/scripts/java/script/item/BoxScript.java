package script.item;

import game.core.message.SMessage;
import game.core.script.IScript;
import game.core.util.SimpleRandom;
import game.data.bean.q_languageBean;
import game.message.BackpackMessage;
import game.server.logic.constant.ErrorCode;
import game.server.logic.constant.Reasons;
import game.server.logic.item.bean.Box;
import game.server.logic.item.bean.BoxConfig;
import game.server.logic.item.bean.BoxConfigComparator;
import game.server.logic.item.bean.BoxItems;
import game.server.logic.item.bean.BoxItemsComparator;
import game.server.logic.item.bean.Item;
import game.server.logic.struct.Player;
import game.server.logic.support.BeanFactory;
import game.server.logic.support.BeanTemplet;
import game.server.logic.trot.TrotManager;
import game.server.logic.trot.bean.TrotInfo;
import game.server.logic.util.ScriptArgs;
import game.server.util.MessageUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * <b>宝箱脚本类.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class BoxScript implements IScript
{

    private final Logger log = Logger.getLogger(BoxScript.class);

    private final SimpleRandom random = new SimpleRandom();

    // 公告物品名字颜色
    private final String itemNameColor[] = new String[]
    {
        "{ffffff}", // 白色0
        "{99cc00}", // 绿色1
        "{49def8}", // 蓝色2
        "{fb87ea}", // 紫色3
        "{0f9f38}"  // 橙色4
    };

    // 需要公告的物品ID
    private final int[] noticeItems = new int[]
    {
        130003, 130007, 130011, 130018, 130019, 130020
    };

    // 普通宝箱的物品ID
    private final int box1_id = 30001;
    // 精致宝箱的物品ID
    private final int box2_id = 30002;
    // 豪华宝箱的物品ID
    private final int box3_id = 30003;

    // 开启普通宝箱默认给予的物品ID（但不弹出显示）
    private final int box1_default_id = 20005;
    // 开启普通宝箱默认给予的物品数量
    private final int box1_default_num = 1;
    // 开启精致宝箱默认给予的物品ID（但不弹出显示）
    private final int box2_default_id = 20005;
    // 开启精致宝箱默认给予的物品数量
    private final int box2_default_num = 1;
    // 开启豪华宝箱默认给予的物品ID（但不弹出显示）
    private final int box3_default_id = 20005;
    // 开启豪华宝箱默认给予的物品数量
    private final int box3_default_num = 1;

    // 配置普通宝箱
    private final BoxConfig[] box1 = new BoxConfig[]
    {
        // 设置一级权重
        new BoxConfig(70, new BoxItems[]
        {
            // 设置二级权重
            new BoxItems(50, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            }),
            // 设置二级权重
            new BoxItems(50, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            })
        }),
        // 设置一级权重
        new BoxConfig(20, new BoxItems[]
        {
            // 设置二级权重
            new BoxItems(50, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            }),
            // 设置二级权重
            new BoxItems(50, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            })
        }),
        // 设置一级权重
        new BoxConfig(10, new BoxItems[]
        {
            // 设置二级权重
            new BoxItems(80, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            }),
            // 设置二级权重
            new BoxItems(20, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            }),
        }),
    };

    // 配置精致宝箱
    private final BoxConfig[] box2 = new BoxConfig[]
    {
        // 设置一级权重
        new BoxConfig(72, new BoxItems[]
        {
            // 设置二级权重
            new BoxItems(50, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            }),
            // 设置二级权重
            new BoxItems(50, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            }),
        }),
        // 设置一级权重
        new BoxConfig(20, new BoxItems[]
        {
            // 设置二级权重
            new BoxItems(50, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            }),
            // 设置二级权重
            new BoxItems(50, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            })
        }),
        // 设置一级权重
        new BoxConfig(8, new BoxItems[]
        {
            // 设置二级权重
            new BoxItems(15, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            }),
            // 设置二级权重
            new BoxItems(35, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            }),
            // 设置二级权重
            new BoxItems(25, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            }),
            // 设置二级权重
            new BoxItems(25, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            })
        }),
    };

    // 配置豪华宝箱
    private final BoxConfig[] box3 = new BoxConfig[]
    {
        // 设置一级权重
        new BoxConfig(84, new BoxItems[]
        {
            // 设置二级权重
            new BoxItems(50, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            }),
            // 设置二级权重
            new BoxItems(50, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            })
        }),
        // 设置一级权重
        new BoxConfig(8, new BoxItems[]
        {
            // 设置二级权重
            new BoxItems(15, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015

            }),
            // 设置二级权重
            new BoxItems(35, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            }),
            // 设置二级权重
            new BoxItems(25, new int[]
            {
                // 设置随机开出的物品ID
                20001
            }),
            // 设置二级权重
            new BoxItems(25, new int[]
            {
                // 设置随机开出的物品ID
                20002
            })
        }),
        // 设置一级权重
        new BoxConfig(8, new BoxItems[]
        {
            // 设置二级权重
            new BoxItems(45, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            }),
            // 设置二级权重
            new BoxItems(30, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            }),
            // 设置二级权重
            new BoxItems(20, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            }),
            // 设置二级权重
            new BoxItems(5, new int[]
            {
                // 设置随机开出的物品ID
                20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015
            })
        }),
    };

    public BoxScript()
    {
        sort(box1);
        sort(box2);
        sort(box3);
    }

    @Override
    public int getId()
    {
        return 1005;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        log.info("Call BoxScript...");
        // 解析参数
        ScriptArgs args = (ScriptArgs) arg;
        Object arg1 = args.get(ScriptArgs.Key.ITEM);
        if (!(arg1 instanceof Box))
        {
            log.error("宝箱脚本参数类型错误！ScriptArgs.Key.ITEM class: " + arg1.getClass());
            return null;
        }
        Object arg2 = args.get(ScriptArgs.Key.PLAYER);
        if (!(arg2 instanceof Player))
        {
            log.error("宝箱脚本参数类型错误！ScriptArgs.Key.PLAYER class: " + arg2.getClass());
            return null;
        }

        Box box = (Box) arg1;
        Player player = (Player) arg2;

        BoxConfig[] configs = getBoxConfigs(box);
        if (configs == null)
        {
            log.error("没有找到宝箱对应的配置信息！boxId = " + box.getId());
            return null;
        }

        if (box.getNum() > 10)
        {
            log.error("一次性打开的宝箱数量过多！useNum = " + box.getNum());
            return null;
        }

        boolean isNotice = false;
        StringBuilder itemNames = new StringBuilder();
        List<Item> items = new ArrayList<>(box.getNum());
        for (int i = 0; i < box.getNum(); i++)
        {
            Integer itemId = open(box, configs);
            if (itemId != null)
            {
                Item item = BeanFactory.createItem(itemId, 1); // 每个箱子只能出1个物品
                if (item != null)
                {
                    items.add(item);
                    if (needNotice(item))
                    {
                        isNotice = true;
                        if (item.getDefault() - 1 < 0 || item.getDefault() - 1 >= 5)
                        {
                            itemNames.append(itemNameColor[0]);
                        }
                        else
                        {
                            itemNames.append(itemNameColor[item.getDefault() - 1]);
                        }

                        itemNames.append(item.getName());
                        itemNames.append("{ffffff}");
                        itemNames.append(" ");
                    }

                    // 添加开启豪华宝箱的日常任务触发器
                    if (box.getId() == box3_id)
                    {
//                        player.getDailyTaskManager().openLuxuryTrigger();
                    }
                }
                else
                {
                    log.error("宝箱开出的物品不存在！boxId = " + box.getId() + "，itemId = " + itemId);
                }
            }
            else
            {
                log.error("宝箱没有开出任何物品！boxId = " + box.getId());
            }
        }

        ErrorCode error = player.getBackpackManager().addItem(items, true, Reasons.OPEN_BOXES);
        if (error != null)
        {
            log.error("将宝箱开出的物品放入玩家背包出错！errorCode = " + error.getCode());
            return null;
        }

        // 向玩家发送开出的物品信息，用于客户端播放开箱动画
        MessageUtils.send(player, new SMessage(
                BackpackMessage.ResOpenBox.MsgID.eMsgID_VALUE,
                getMessage(box, items).build().toByteArray()));

        // 发送公告
        if (isNotice)
        {
            q_languageBean language = BeanTemplet.getLanguage(170029);
            TrotManager.getIntance().notice(new TrotInfo(String.format(language.getQ_lgtext(), player.getRoleName(), itemNames.toString())));
        }

        // 给予默认物品，不在宝箱开启动画中播放，不进行公告
        Item item = getDefaultItem(box);
        if (item == null)
        {
            log.error("没有找到宝箱默认物品！boxId = " + box.getId());
        }
        else
        {
            error = player.getBackpackManager().addItem(item, true, Reasons.OPEN_BOXES);
            if (error != null)
            {
                log.error("将宝箱默认物品放入玩家背包出错！errorCode = " + error.getCode());
            }
        }

        if (box.getNum() != items.size())
        {
            log.error("宝箱开出的物品数量与箱子数量不符！boxId：" + box.getId()
                    + "，boxNum：" + box.getNum() + "，openNum：" + items.size());
            for (Item temp : items)
            {
                log.error("Open ItemId = " + temp.getId() + " Num = " + temp.getNum());
            }
        }

        return items;
    }

    // 将宝箱配置按概率大小排序（从小到大）
    private void sort(BoxConfig[] configs)
    {
        Arrays.sort(configs, new BoxConfigComparator());

        for (BoxConfig config : configs)
        {
            Arrays.sort(config.getBoxItems(), new BoxItemsComparator());
        }
    }

    // 获取指定宝箱的配置信息
    private BoxConfig[] getBoxConfigs(Box box)
    {
        switch (box.getId())
        {
            case box1_id:
                return box1;
            case box2_id:
                return box2;
            case box3_id:
                return box3;
            default:
                return null;
        }
    }

    // 获取开启后默认给予的物品
    private Item getDefaultItem(Box box)
    {
        switch (box.getId())
        {
            case box1_id:
                return BeanFactory.createItem(box1_default_id, box1_default_num * box.getNum());
            case box2_id:
                return BeanFactory.createItem(box2_default_id, box2_default_num * box.getNum());
            case box3_id:
                return BeanFactory.createItem(box3_default_id, box3_default_num * box.getNum());
            default:
                return null;
        }
    }

    private Integer open(Box box, BoxConfig[] configs)
    {
//        log.info("Open box. boxId = " + box.getId());
        int minNum, maxNum = 0;
        int ranNum = random.next(1, 100);

        for (BoxConfig config : configs)
        {
            minNum = maxNum + 1;
            maxNum = maxNum + config.getWeight();
//            System.out.println("weight=" + config.getWeight() + ". minNum=" + minNum + ", maxNum=" + maxNum);
            if (ranNum >= minNum && ranNum <= maxNum)
            {
//                System.out.println(config.getWeight());
                maxNum = 0;
                ranNum = random.next(1, 100);
                for (BoxItems boxItems : config.getBoxItems())
                {
                    minNum = maxNum + 1;
                    maxNum = maxNum + boxItems.getWeight();
                    if (ranNum >= minNum && ranNum <= maxNum)
                    {
                        return boxItems.getItemIds()[
                                random.next(1, boxItems.getItemIds().length) - 1];
                    }
                }
                break;
            }
        }

        return null;
    }

    private BackpackMessage.ResOpenBox.Builder getMessage(Box box, List<Item> openItems)
    {
        BackpackMessage.ResOpenBox.Builder message = BackpackMessage.ResOpenBox.newBuilder();
        message.setBoxId(box.getId());
        message.setBoxNum(box.getNum());

        switch (box.getId())
        {
            case box1_id:
                message.setBoxType(1);
                break;
            case box2_id:
                message.setBoxType(2);
                break;
            case box3_id:
                message.setBoxType(3);
                break;
            default:
                message.setBoxType(0);
                break;
        }

        for (Item item : openItems)
        {
            BackpackMessage.Item.Builder itemMsg = BackpackMessage.Item.newBuilder();
            itemMsg.setId(item.getId());
            itemMsg.setNum(item.getNum());
            itemMsg.setType(item.getType());
            message.addItems(itemMsg);
        }

        return message;
    }

    private boolean needNotice(Item item)
    {
        for (int id : noticeItems)
        {
            if (id == item.getId())
            {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args)
    {
        BoxScript bs = new BoxScript();
        for (int i = 0; i < 100; i++)
        {
            System.out.println("itemId = " + bs.open(null, bs.box3));
        }
    }

}
