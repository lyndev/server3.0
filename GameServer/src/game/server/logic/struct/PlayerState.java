/**
 * @date 2014/4/24
 * @author ChenLong
 */
package game.server.logic.struct;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * <b>玩家状态类</b>
 *
 * @author ChenLong
 */
public class PlayerState
{
    public static enum State
    {
        NULL_START,
        LOGINING, // 登陆中
        NORMAL, // 已登录, 正常游戏中
        DETACH, // 已分离(和客户端连接暂时断开, 但Player对象还在内存中)
        NULL_END,
    }
    private final static String LAST_CHANGE_STATE_TIME = "GAME_LAST_CHANGE_STATE_TIME"; // 状态切换时间戳
    private State state;
    private final Map<String, Object> attributes = new HashMap<>(); // 自定义属性值

    public PlayerState()
    {
        state = State.NULL_START;
    }

    public State getState()
    {
        return state;
    }

    public long getLastChangeStateTime()
    {
        Object obj = attributes.get(LAST_CHANGE_STATE_TIME);
        if (obj != null && obj instanceof Long)
            return (Long)obj;
        else
            return 0;
    }

    /**
     * 设置状态, 并清空attributes
     *
     * @param state
     * @return
     */
    public PlayerState setState(State state)
    {
        this.state = state;
        attributes.clear();
        attributes.put(LAST_CHANGE_STATE_TIME, System.currentTimeMillis());
        return this;
    }

    /**
     * 获取自定义属性.
     *
     * @param name 属性名
     * @return
     */
    public Object getAttribute(String name)
    {
        return attributes.get(name);
    }

    /**
     * 设置自定义属性.
     *
     * @param name 属性名
     * @param value 属性值
     * @return 与name关联的旧值；如果此前没有该name的自定义属性值，则返回null.
     */
    public Object setAttribute(String name, Object value)
    {
        return attributes.put(name, value);
    }

    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
