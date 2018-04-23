/**
 * @date 2014/7/8
 * @author ChenLong
 */
package game.server.util;

import game.core.util.ZLibUtils;
import game.data.bean.q_globalBean;
import game.server.logic.support.BeanTemplet;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class MiscUtils
{
    private static final Logger logger = Logger.getLogger(MiscUtils.class);

    /**
     * netbeans/eclipse等IDE下 运行/调试 关闭时IDE内部调用Process.destroy() <br />
     * 无法触发到JVM shutdown hook, 导致关闭时无法回存 <br />
     * 故在IDE环境下添加了system property: ideDebug
     *
     * @return
     */
    public static boolean isIDEEnvironment()
    {
        String val = System.getProperty("ideDebug");
        return val != null && val.equals("true");
    }

    public static String md5(String inStr) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        byte[] bytes = MessageDigest.getInstance("MD5").digest(inStr.getBytes("UTF-8"));
        StringBuilder ret = new StringBuilder(bytes.length << 1);
        for (int a = 0; a < bytes.length; ++a)
        {
            ret.append(Character.forDigit((bytes[a] >> 4) & 0xf, 16));
            ret.append(Character.forDigit(bytes[a] & 0xf, 16));
        }
        return ret.toString();
    }

    /**
     * 将文本zip压缩后转base64
     *
     * @param plainText
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String compressText(String plainText) throws UnsupportedEncodingException
    {
        if (plainText.trim().isEmpty())
            throw new IllegalArgumentException();

        byte[] compressed = ZLibUtils.compress(plainText.getBytes());
        byte[] base64Data = Base64.encodeBase64(compressed);
        return new String(base64Data, "UTF-8");
    }

    /**
     * 以上方法的反向过程
     *
     * @param compressedText
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String uncompressText(String compressedText) throws UnsupportedEncodingException
    {
        if (compressedText.trim().isEmpty())
            throw new IllegalArgumentException();

        byte[] base64Data = Base64.decodeBase64(compressedText.getBytes());
        byte[] decompressed = ZLibUtils.decompress(base64Data);
        return new String(decompressed, "UTF-8");
    }

    public static void logError(String s, Throwable t)
    {
        logger.error(s, t);
    }

    /**
     * 以凌晨5点为分界, 判断是否跨天
     *
     * @param before
     * @param after
     * @return 跨天true, 反之false
     */
    public static boolean across5(long before, long after)
    {
        boolean result = false;

        if (before <= after)
        {
            Calendar beforeCal = Calendar.getInstance();
            beforeCal.setTimeInMillis(before);
            beforeCal.add(Calendar.HOUR_OF_DAY, -5);

            Calendar afterCal = Calendar.getInstance();
            afterCal.setTimeInMillis(after);
            afterCal.add(Calendar.HOUR_OF_DAY, -5);

            result = !DateUtils.isSameDay(beforeCal, afterCal);
        }
        else
        {
            logger.error("before > after, before = " + before + ", after = " + after);
        }
        return result;
    }

    public static boolean across5(Date before, Date after)
    {
        return across5(before.getTime(), after.getTime());
    }

    /**
     * 已凌晨5点为界, 判断是否是同一天
     *
     * @param t1
     * @param t2
     * @return
     */
    public static boolean isSameDayBy5(long t1, long t2)
    {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(t1);
        c1.add(Calendar.HOUR_OF_DAY, -5);

        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(t2);
        c2.add(Calendar.HOUR_OF_DAY, -5);

        return DateUtils.isSameDay(c1, c2);
    }
    
    /**
     *夺宝等级匹配阶数
     * @param level
     * @return
     */
    public static int getLevelRank(int level)
    {
        q_globalBean globalBean = BeanTemplet.getGlobalBean(1179);
        if (globalBean == null || globalBean.getQ_string_value() == null)
        {
            if (level < 20)
            {
                return 0;
            }
            else if (level >= 20 && level < 50)
            {
                return 20;
            }
            else if(level >= 50 && level < 70)
            {
                return 50;
            }
            else 
            {
                return 70;
            }
        }
        else
        {
            int minLv = 0;
            int maxLv = 0;
            String q_string_value = globalBean.getQ_string_value();
            String[] split = q_string_value.split("_");
            for (int i = 0; i < split.length; i++)
            {
                maxLv = Integer.valueOf(split[i]);
                if (level >= minLv && level < maxLv)
                {
                    return minLv;
                }
                minLv = maxLv;
            }
            return minLv;
        }
    }
    /**
     * 获取当前时间的unix timestamp
     * 
     * @return 
     */
    public static int getTimestamp()
    {
        return getTimestamp(System.currentTimeMillis());
    }
    
    /**
     * 转换指定毫秒到对应的秒(timestamp)
     * 
     * @param timeInMillis 毫秒时间
     * @return 秒时间
     */
    public static int getTimestamp(long timeInMillis)
    {
        return (int)(timeInMillis / 1000L);
    }
}
