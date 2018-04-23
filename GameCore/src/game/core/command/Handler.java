package game.core.command;

import game.core.message.RMessage;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * <b>处理客户端消息的抽象基类.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public abstract class Handler implements ICommand
{

    private RMessage message; // 待处理的消息

    private final Map<String, Object> attributes = new HashMap<>(3); // 自定义属性值

    /**
     * 获取待处理的消息.
     *
     * @return
     */
    public RMessage getMessage()
    {
        return message;
    }

    /**
     * 设置待处理的消息.
     *
     * @param message
     */
    public void setMessage(RMessage message)
    {
        this.message = message;
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

}
