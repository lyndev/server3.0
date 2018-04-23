/**
 * @date 2014/5/20
 * @author ChenLong
 */
package game.server.util;

/**
 * 字符判断类
 *
 * @author ChenLong
 */
public class WordUtils
{
    /**
     * Unicode字符c是否是汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c)
    {
        return (c >= 0x4e00) && (c <= 0x9fbb);
    }

    /**
     * 字符串中是否有空格
     *
     * @param s
     * @return
     */
    public static boolean hasSpace(String s)
    {
        char[] chars = s.toCharArray();
        for (char c : chars)
        {
            if (Character.isSpaceChar(c))
                return true;
        }
        return false;
    }

    /**
     * 获取ASCII字符长度, 1个汉字长度为2, 其余字符长度1
     *
     * @param s
     * @return
     */
    public static int getAsciiCharacterLength(String s)
    {
        int len = 0;
        char[] chars = s.toCharArray();
        for (char c : chars)
        {
            if (isChinese(c))
                len += 2;
            else
                len += 1;
        }
        return len;
    }
}
