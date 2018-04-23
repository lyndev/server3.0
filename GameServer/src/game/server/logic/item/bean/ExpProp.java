package game.server.logic.item.bean;

import game.server.logic.support.BeanTemplet;

/**
 *
 * <b>经验类道具.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public final class ExpProp extends Item
{

    @Override
    public boolean doUse(Object target)
    {
        return false; // 经验类道具的使用请参见BackpackManager.expPropUse
    }

    /**
     * get 使用后增加的经验值
     *
     * @return
     */
    public int getAddExp()
    {
        return BeanTemplet.getItemBean(getId()).getQ_add_exp();
    }

}
