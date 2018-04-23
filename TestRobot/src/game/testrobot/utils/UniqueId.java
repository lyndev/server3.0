package game.testrobot.utils;

/**
 *
 * @date 2014-6-25
 * @author pengmian
 */
public class UniqueId
{
    /**
     * 转化为36进制的字符串
     *
     * @param uniqueId
     * @return
     */
    public static String toBase36(long uniqueId)
    {
        return Long.toString(uniqueId, 36);
    }

    /**
     * 从36进制的字符串转换为10进制的long型
     *
     * @param str
     * @return
     */
    public static long toBase10(String str)
    {
        return Long.parseLong(str, 36);
    }

    /**
     * 解析id包含的代理id
     *
     * @param uniqueId
     * @return
     */
    public static long parsePlatform(long uniqueId)
    {
        return (0x7F & uniqueId);
    }

    /**
     * 解析id包含的服务器id
     *
     * @param uniqueId
     * @return
     */
    public static long parseServerId(long uniqueId)
    {
        return (0x1FFFF & uniqueId) >> 7;
    }

    /**
     * 解析id包换的功能id
     *
     * @param uniqueId
     * @return
     */
    public static long parseFuncType(long uniqueId)
    {
        return (0x1FFFFF & uniqueId) >> 17;
    }

    /**
     * 解析序列数
     *
     * @param uniqueId
     * @return
     */
    public static long parseSequence(long uniqueId)
    {
        return (0x1FFFFFFFFFFL & uniqueId) >> 21;
    }
}
