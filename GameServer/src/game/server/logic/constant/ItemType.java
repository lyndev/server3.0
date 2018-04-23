package game.server.logic.constant;

import game.server.logic.item.bean.*;

/**
 *
 * <b>物品类型定义.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public enum ItemType
{
    /**
     * 礼包
     */
    GIFT(1, Gift.class),
    /**
     * 宝箱
     */
    BOX(2, Box.class),
    /**
     * 钥匙
     */
    KEY(3, Key.class),
    /**
     * 经验类道具
     */
    EXP(4, ExpProp.class),
    /**
     * 魂石
     */
    SOUL_STONE(5, SoulStone.class),
    /**
     * 装备
     */
    EQUIPMENT(6, Equipment.class),
    
    /**
     * 普通道具
     */
    NORMALITEM(7, NormalItem.class),
    
    /**
     * 卷轴（图纸）
     */
    DRAWING(8, Drawing.class),
    
    /**
     * 宝石
     */
    GEMS(9, Gems.class),
    
    /**
     * 装备碎片 
     */
    EQUIP_FRAG(10, EquipDebris.class),
    
    /**
     * 卷轴碎片
     */
    SCROLL_DEBRIS(11, ScrollDebris.class),
    
    
 //=======以下是万万的道具类型===================   
    
//    /**
//     * 装备材料
//     */
//    EQUIPMENT_MATERIAL(9, EquipmentMaterial.class),
////    /**
////     * 图纸
////     */ga
////    DRAWING(9, Drawing.class),
//    /**
//     * 武魂
//     */
//    HERO_SOUL(10, HeroSoul.class),
//    /**
//     * 兵书
//     */
//    WAR_BOOK(11, WarBook.class),
    /**
     * 装备强化道具
     */
    EQUIPMENT_STRENG(12, EquipmentStreng.class),
    
    /**
     * 药水：徐能强修改
     */
    POTION(13, Potion.class),
    
    /**
     * 碎片
     */
    DEBRIS(14, Debris.class),
    /**
     * 道具
     */
    PROPS(15, Prop.class),
    /**
     * 饰品碎片
     */
    JEWELRY_DEBRIS(16, JewelryDebris.class),
    /**
     * 拼图材料
     */
    JIGSAW_MATERIAL(17, JigsawMaterial.class);

    private final int value;

    private final Class<? extends Item> beanClass;

    ItemType(int value, Class<? extends Item> beanClass)
    {
        if (beanClass == null)
        {
            throw new IllegalArgumentException("beanClass is can't null!");
        }
        this.value = value;
        this.beanClass = beanClass;
    }

    public int value()
    {
        return value;
    }

    public boolean compare(int value)
    {
        return this.value == value;
    }

    public Class<? extends Item> getBeanClass()
    {
        return beanClass;
    }

}
