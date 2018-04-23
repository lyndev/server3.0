/**
 * @date 2014/9/24
 * @author ChenLong
 */
package game.server.logic.recharge.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 *
 * @author ChenLong
 */
public class ResVerifyBean
{
    private double amount;
    private String category;
    private double currency_amount;
    private double discount_price;
    private double price;
    private int gift;
    private int item_amount;
    private String item_code;
    private String orderid;
    private int status;
    private String server_id;
    private String character_id; // roleId

    public static ResVerifyBean parseFromJson(String jsonStr)
    {
        ResVerifyBean bean = new ResVerifyBean();
        JSONObject jsonObj = JSON.parseObject(jsonStr);
        bean.amount = jsonObj.getDoubleValue("amount");
        bean.category = jsonObj.getString("category");
        bean.currency_amount = jsonObj.getDoubleValue("currency_amount");
        bean.discount_price = jsonObj.getDoubleValue("discount_price");
        bean.price = jsonObj.getDoubleValue("price");
        bean.gift = jsonObj.getIntValue("gift");
        bean.item_amount = jsonObj.getIntValue("item_amount");
        bean.item_code = jsonObj.getString("item_code");
        bean.orderid = jsonObj.getString("orderid");
        bean.status = jsonObj.getIntValue("status");
        bean.server_id = jsonObj.getString("server_id");
        bean.character_id = jsonObj.getString("character_id");
        return bean;
    }

    public double getAmount()
    {
        return amount;
    }

    public String getCategory()
    {
        return category;
    }

    public double getCurrency_amount()
    {
        return currency_amount;
    }

    public int getGift()
    {
        return gift;
    }

    public int getItem_amount()
    {
        return item_amount;
    }

    public String getItem_code()
    {
        return item_code;
    }

    public String getOrderid()
    {
        return orderid;
    }

    public double getDiscount_price()
    {
        return discount_price;
    }

    public double getPrice()
    {
        return price;
    }

    public int getStatus()
    {
        return status;
    }

    public String getServer_id()
    {
        return server_id;
    }

    public String getCharacter_id()
    {
        return character_id;
    }
}
