package game.server.logic.item.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.server.logic.struct.Player;

/**
 *
 * <b>装备.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author ZouZhaopeng  徐能强
 * @version 1.1.0
 */
public final class Equipment extends Item
{
    private int pos;

    @Override
    public JSON toJson()
    {
        JSONObject obj = (JSONObject) super.toJson();
        obj.put("pos", pos);
        obj.put("equipId", getId());
        return obj;
    }
    
    @Override
    public void fromJson(JSON json)
    {
        JSONObject jsonObj = (JSONObject) json;
        this.setPos(jsonObj.getIntValue("pos"));
        this.setId(jsonObj.getIntValue("equipId"));
    }
    
    @Override
    public Equipment clone() throws CloneNotSupportedException
    {
        return (Equipment)super.clone();
    }
    
    public int getPos()
    {
        return pos;
    }

    public void setPos(int pos)
    {
        this.pos = pos;
    }

    /**
     * 啥都不说了, 直接变成最好的装备, GM命令就是叼
     * @param player
     */
    public void gmBest(Player player)
    {
        
    }      
}   




//    //====================以下是万万的装备系统===================
//
//    private int qualityExp; //(品质->精炼)精炼经验, 初始为0
//    private int equipLevel; //(等级->强化)装备等级, 初始为0
//
//    public int getQualityExp()
//    {
//        return qualityExp;
//    }
//
//    public void setQualityExp(int qualityExp)
//    {
//        this.qualityExp = qualityExp;
//    }
//
//    public int getEquipLevel()
//    {
//        return equipLevel;
//    }
//
//    public void setEquipLevel(int equipLevel)
//    {
//        this.equipLevel = equipLevel;
//    }
//
//    //装备分类: 武器, 盔甲, 饰品 
//    public int getEquipType()
//    {
//        return -1;
//        //徐能强注释
//        //return BeanTemplet.getEquipmentBean(getId()).getQ_equipment_position();
//    }
//
////    //初始品质
////    public int getOriginalQuality()
////    {
////        return 0; //todo, 根据配置
////    }
////    
////    //品质等级
////    public int getQualityLevel()
////    {
////        return 0; //todo, 根据配置和经验, 算出品质等级
////    }
////    
////    //扣除品质等级后剩余的经验
////    public int getQualityExpLeft()
////    {
////        return 0;
////    }
//
//
//    /**
//     * 获取装备在升级过程中消耗的金币
//     *
//     * @return
//     */
//    public int getLevelupCost()
//    {
//        int amount = 0;
//        for (int index = 1; index <= equipLevel; ++index)
//        {
//            amount += EquipmentService.getInstance().getLevelupMoney(index);
//        }
//        return amount;
//    }
//
//    /**
//     * 获取装备在精炼过程中消耗的金币
//     *
//     * @return
//     */
//    public int getRefineCost()
//    {
//        int amount = 0;
//        for (int index = 1; index <= qualityExp; ++index)
//        {
//            amount += EquipmentService.getInstance().getRefineMoney(getId() + "_" + index);
//        }
//        return amount;
//    }
//
//    /**
//     * 获取装备当前精炼的等级
//     *
//     * @return
//     */
//    public int getRefineLevel()
//    {
//        int amount = 0;
//        for (int index = 1; index <= qualityExp; ++index)
//        {
//            q_equipment_refiningBean bean = BeanTemplet.getEquipmentRefineBean(getId() + "_" + index);
//            if (bean != null && qualityExp >= bean.getQ_experience())
//            {
//                ++amount;
//            }
//            else
//            {
//                break;
//            }
//        }
//        return amount;
//    }
//
//    public q_equipmentBean getBean()
//    {
//        q_equipmentBean bean = BeanTemplet.getEquipmentBean(getId());
//        if (bean == null)
//        {
//            LOG.error("装备配置缺失, 装备id = " + getId());
//        }
//        return bean;
//    }
//
//    /**
//     * 获取装备位置.
//     *
//     * @return 1 = 盔甲; 2 = 武器; 3 = 饰品
//     */
//    public int getPosition()
//    {
//        return BeanTemplet.getItemBean(getId()).getQ_equipment_position();
//    }
//
//    /**
//     * 比较两件装备哪件更好
//     *
//     * @param other
//     * @return 更好true, 相同或者更差false
//     */
//    public boolean isBetterThan(Equipment other)
//    {
//        q_equipmentBean bean = getBean();
//        q_equipmentBean otherBean = other.getBean();
//        if (bean == null || otherBean == null)
//            return false;
//
//        if (bean.getQ_default() > otherBean.getQ_default())
//            return true;
//        else if (bean.getQ_default() < otherBean.getQ_default())
//            return false;
//        
//        else if (equipLevel > other.equipLevel)
//            return true;
//        else if (equipLevel < other.equipLevel)
//            return false;
//
//        else return qualityExp > other.qualityExp;
//    }
//
//    /**
//     * 啥都不说了, 直接变成最好的装备, GM命令就是叼
//     * @param player
//     */
//    public void gmBest(Player player)
//    {
//        equipLevel = player.getRoleLevel();
//        qualityExp = 30;
//    }
//}
