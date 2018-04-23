/**
 * Auto generated, do not edit it
 *
 * q_storeConfig
 */
package game.data.bean;

public class q_storeConfigBean
{
    private int q_id; // ID（1001-1099：普通商店，2001-2099：竞技场商店，3001-3099：远征商店，）
    private int q_type; // 商店类型：（1普通商店，2竞技场商店，3远征商店）
    private int q_pos; // 商品位置：（-1不固定位置；>0为固定位置且填入相应的位置）
    private int q_level; // 等级限制（只有不固定的商品才会有限制）
    private String q_item_id; // 固定掉落的物品（物品Id_数量_价格）
    private String q_pack; // 物品掉落包（包Id_掉落的物品个数）

    /**
     * get ID（1001-1099：普通商店，2001-2099：竞技场商店，3001-3099：远征商店，）
     * @return
     */
    public int getQ_id()
    {
        return q_id;
    }

    /**
     * set ID（1001-1099：普通商店，2001-2099：竞技场商店，3001-3099：远征商店，）
     */
    public void setQ_id(int q_id)
    {
        this.q_id = q_id;
    }

    /**
     * get 商店类型：（1普通商店，2竞技场商店，3远征商店）
     * @return
     */
    public int getQ_type()
    {
        return q_type;
    }

    /**
     * set 商店类型：（1普通商店，2竞技场商店，3远征商店）
     */
    public void setQ_type(int q_type)
    {
        this.q_type = q_type;
    }

    /**
     * get 商品位置：（-1不固定位置；>0为固定位置且填入相应的位置）
     * @return
     */
    public int getQ_pos()
    {
        return q_pos;
    }

    /**
     * set 商品位置：（-1不固定位置；>0为固定位置且填入相应的位置）
     */
    public void setQ_pos(int q_pos)
    {
        this.q_pos = q_pos;
    }

    /**
     * get 等级限制（只有不固定的商品才会有限制）
     * @return
     */
    public int getQ_level()
    {
        return q_level;
    }

    /**
     * set 等级限制（只有不固定的商品才会有限制）
     */
    public void setQ_level(int q_level)
    {
        this.q_level = q_level;
    }

    /**
     * get 固定掉落的物品（物品Id_数量_价格）
     * @return
     */
    public String getQ_item_id()
    {
        return q_item_id;
    }

    /**
     * set 固定掉落的物品（物品Id_数量_价格）
     */
    public void setQ_item_id(String q_item_id)
    {
        this.q_item_id = q_item_id;
    }

    /**
     * get 物品掉落包（包Id_掉落的物品个数）
     * @return
     */
    public String getQ_pack()
    {
        return q_pack;
    }

    /**
     * set 物品掉落包（包Id_掉落的物品个数）
     */
    public void setQ_pack(String q_pack)
    {
        this.q_pack = q_pack;
    }
}
