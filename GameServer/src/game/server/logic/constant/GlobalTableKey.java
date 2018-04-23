/**
 * @date 2014/8/6
 * @author ChenLong
 */
package game.server.logic.constant;

/**
 * global表key
 *
 * @author ChenLong
 */
public enum GlobalTableKey
{
    UNIQUE_ID(1000, "唯一Key生成序列"),
    SCHEDULE_MAIL(1001, "定时邮件"),
    NOTICE(1002, "定时公告"),
    ARENA_LUCKY_NOW(1003, "竞技场本轮幸运排名"),
    ARENA_LUCKY_LAST(1004, "竞技场上轮幸运排名"),
    CLIENT_VERSION(1005, "客户端版本配置"),
    LOGIN_GIFT(1006, "登陆有奖活动"),
    OPER_ACTIVITY(1007, "运营活动"),
    ;

    private final int key;
    private final String desc; // 描述

    GlobalTableKey(int key, String desc)
    {
        this.key = key;
        this.desc = desc;
    }

    public int getKey()
    {
        return key;
    }

    public String getDesc()
    {
        return desc;
    }
}
