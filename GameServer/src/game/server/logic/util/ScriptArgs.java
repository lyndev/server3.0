package game.server.logic.util;

import java.util.EnumMap;

/**
 *
 * <b>脚本参数对象.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class ScriptArgs
{

    private final EnumMap<Key, Object> args;

    public ScriptArgs()
    {
        args = new EnumMap<>(Key.class);
    }

    public Object get(Key key)
    {
        return args.get(key);
    }

    public void put(Key key, Object value)
    {
        args.put(key, value);
    }

    /**
     * 脚本参数键值.
     */
    public enum Key
    {
        IO_SESSION,
        PLAYER,
        CARD,
        ITEM,
        FUNCTION_NAME, // 函数名, JS脚本可用此参数指定要调用的函数
        INTEGER_VALUE, // Integer类型
        STRING_VALUE, // String类型
        BOOLEAN_VALUE, // boolean类型
        OBJECT_VALUE, // Object类型
        ARG1,
        ARG2,
        ARG3,
        ARG4,
        ARG5,
    }
}
