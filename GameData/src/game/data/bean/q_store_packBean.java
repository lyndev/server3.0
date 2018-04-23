/**
 * Auto generated, do not edit it
 *
 * q_store_pack
 */
package game.data.bean;

public class q_store_packBean
{
    private int q_id; // ID
    private String q_items; // 商品包里面的物品（itemId_数量_价格_货币类型；itemId_数量_价格_货币类型）：1-金币，2-钻石，6-荣誉值，7-远征币，
    private String q_annotation; // 介绍（策划使用）

    /**
     * get ID
     * @return
     */
    public int getQ_id()
    {
        return q_id;
    }

    /**
     * set ID
     */
    public void setQ_id(int q_id)
    {
        this.q_id = q_id;
    }

    /**
     * get 商品包里面的物品（itemId_数量_价格_货币类型；itemId_数量_价格_货币类型）：1-金币，2-钻石，6-荣誉值，7-远征币，
     * @return
     */
    public String getQ_items()
    {
        return q_items;
    }

    /**
     * set 商品包里面的物品（itemId_数量_价格_货币类型；itemId_数量_价格_货币类型）：1-金币，2-钻石，6-荣誉值，7-远征币，
     */
    public void setQ_items(String q_items)
    {
        this.q_items = q_items;
    }

    /**
     * get 介绍（策划使用）
     * @return
     */
    public String getQ_annotation()
    {
        return q_annotation;
    }

    /**
     * set 介绍（策划使用）
     */
    public void setQ_annotation(String q_annotation)
    {
        this.q_annotation = q_annotation;
    }
}
