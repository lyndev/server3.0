/**
 * @date 2014/9/4
 * @author ChenLong
 */
package game.server.logic.constant;

import com.alibaba.fastjson.JSONObject;

/**
 *
 * @author ChenLong
 */
public enum BackstageCmdResult
{
    Success(0, "执行成功"),
    NoticeParameterError(-10, "公告参数错误"),
    CannotFindNotice(-11, "找不到公告"),
    LoginGiftExist(-12, "添加失败, 登陆有奖活动id已经存在"),
    LoginGiftNotFound(-13, "取消失败, 没有找到对应的登陆有奖活动id"),
    ;

    private final int value;
    private final String desc;

    BackstageCmdResult(int value, String desc)
    {
        this.value = value;
        this.desc = desc;
    }

    public int getValue()
    {
        return value;
    }

    public String getDesc()
    {
        return desc;
    }

    public static BackstageCmdResult getBackstageError(int value)
    {
        for (BackstageCmdResult type : BackstageCmdResult.values())
        {
            if (type.getValue() == value)
                return type;
        }
        return null;
    }
    
    public JSONObject toJSONObject()
    {
        JSONObject obj = new JSONObject();
        obj.put(String.valueOf(value), desc);
        return obj;
    }
}

