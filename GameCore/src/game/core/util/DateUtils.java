package game.core.util;

import java.util.Date;

/**
 *
 * <b>日期操作的工具类.</b>
 * <p>
 * 此类封装了一些通用的日期类型的功能.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.9
 */
public class DateUtils
{

    // 天数的最小单位的毫秒表示值
    private static final long MIN_UNIT_DAY = 24 * 60 * 60 * 1000;

    // 小时数的最小单位的毫秒表示值
    private static final long MIN_UNIT_HOUR = 60 * 60 * 1000;

    // 分钟数的最小单位的毫秒表示值
    private static final long MIN_UNIT_MINUTE = 60 * 1000;

    private DateUtils()
    {
        throw new UnsupportedOperationException("该类不允许被实例化!");
    }

    /**
     * 比较指定两个时间的顺序(精确到毫秒).
     *
     * @param date1 指定时间①
     * @param date2 指定时间②
     * @return 如果指定时间①等于指定时间②, 则返回0;<br>
     * 如果指定时间①在指定时间②之前, 则返回-1;<br>
     * 如果指定时间①在指定时间②之后,则返回1.
     */
    public static int compareTo(Date date1, Date date2)
    {
        return compareTo(date1.getTime(), date2.getTime());
    }

    /**
     * 比较指定两个时间的顺序(精确到毫秒).
     *
     * @param time1 指定时间①, 以从历元至现在所经过的UTC毫秒数形式.
     * @param time2 指定时间②, 以从历元至现在所经过的UTC毫秒数形式.
     * @return 如果指定时间①等于指定时间②, 则返回0;<br>
     * 如果指定时间①在指定时间②之前, 则返回-1;<br>
     * 如果指定时间①在指定时间②之后,则返回1.
     */
    public static int compareTo(long time1, long time2)
    {
        return time1 < time2 ? -1 : time1 == time2 ? 0 : 1;
    }

    /**
     * 判断指定时间①是否与指定时间②相等(精确到毫秒).
     *
     * @param date1 指定时间①
     * @param date2 指定时间②
     * @return 如果指定时间①与指定时间②相等返回true, 否则返回false.
     */
    public static boolean equals(Date date1, Date date2)
    {
        return equals(date1.getTime(), date2.getTime());
    }

    /**
     * 判断指定时间①是否与指定时间②相等(精确到毫秒).
     *
     * @param time1 指定时间①, 以从历元至现在所经过的UTC毫秒数形式.
     * @param time2 指定时间②, 以从历元至现在所经过的UTC毫秒数形式.
     * @return 如果指定时间①与指定时间②相等返回true, 否则返回false.
     */
    public static boolean equals(long time1, long time2)
    {
        return compareTo(time1, time2) == 0;
    }

    /**
     * 判断当前时间是否在指定时间之后(精确到毫秒).
     *
     * @param date 指定时间
     * @return 如果当前时间在指定时间之后返回true, 否则返回false.
     */
    public static boolean afterNow(Date date)
    {
        return afterNow(date.getTime());
    }

    /**
     * 判断当前时间是否在指定时间之后(精确到毫秒).
     *
     * @param time 指定时间, 以从历元至现在所经过的UTC毫秒数形式.
     * @return 如果当前时间在指定时间之后返回true, 否则返回false.
     */
    public static boolean afterNow(long time)
    {
        return compareTo(System.currentTimeMillis(), time) > 0;
    }

    /**
     * 判断指定时间①是否在指定时间②之后(精确到毫秒).
     *
     * @param date1 指定时间①
     * @param date2 指定时间②
     * @return 如果指定时间①在指定时间②之后返回true, 否则返回false.
     */
    public static boolean after(Date date1, Date date2)
    {
        return after(date1.getTime(), date2.getTime());
    }

    /**
     * 判断指定时间①是否在指定时间②之后(精确到毫秒).
     *
     * @param time1 指定时间①, 以从历元至现在所经过的UTC毫秒数形式.
     * @param time2 指定时间②, 以从历元至现在所经过的UTC毫秒数形式.
     * @return 如果指定时间①在指定时间②之后返回true, 否则返回false.
     */
    public static boolean after(long time1, long time2)
    {
        return compareTo(System.currentTimeMillis(), time2) > 0;
    }

    /**
     * 判断当前时间是否在指定时间之前(精确到毫秒).
     *
     * @param date 指定时间
     * @return 如果当前时间在当前时间之前返回true, 否则返回false.
     */
    public static boolean beforeNow(Date date)
    {
        return beforeNow(date.getTime());
    }

    /**
     * 判断当前时间是否在指定时间之前(精确到毫秒).
     *
     * @param time 指定时间, 以从历元至现在所经过的UTC毫秒数形式.
     * @return 如果当前时间在指定时间之前返回true, 否则返回false.
     */
    public static boolean beforeNow(long time)
    {
        return compareTo(System.currentTimeMillis(), time) < 0;
    }

    /**
     * 判断指定时间①是否在指定时间②之前(精确到毫秒).
     *
     * @param date1 指定时间①
     * @param date2 指定时间②
     * @return 如果指定时间①在指定时间②之前返回true, 否则返回false.
     */
    public static boolean before(Date date1, Date date2)
    {
        return before(date1.getTime(), date2.getTime());
    }

    /**
     * 判断指定时间①是否在指定时间②之前(精确到毫秒).
     *
     * @param time1 指定时间①, 以从历元至现在所经过的UTC毫秒数形式.
     * @param time2 指定时间②, 以从历元至现在所经过的UTC毫秒数形式.
     * @return 如果指定时间①在指定时间②之前返回true, 否则返回false.
     */
    public static boolean before(long time1, long time2)
    {
        return compareTo(time1, time2) < 0;
    }

    /**
     * 将指定时间换算为天数返回.
     *
     * @param date 指定时间
     * @return 转换后的天数, 如果返回0, 表示其值不足以换算为1天.
     */
    public static long getDays(Date date)
    {
        return getDays(date.getTime());
    }

    /**
     * 将指定时间换算为天数返回.
     *
     * @param time 指定时间, 以从历元至现在所经过的UTC毫秒数形式.
     * @return 转换后的天数, 如果返回0, 表示其值不足以换算为1天.
     */
    public static long getDays(long time)
    {
        if (time >= MIN_UNIT_DAY)
            return time / MIN_UNIT_DAY;
        else
            return 0;
    }

    /**
     * 将指定时间换算为小时数返回.
     *
     * @param date 指定时间
     * @return 转换后的小时数, 如果返回0, 表示其值不足以换算为1小时.
     */
    public static long getHours(Date date)
    {
        return getHours(date.getTime());
    }

    /**
     * 将指定时间换算为小时数返回.
     *
     * @param time 指定时间, 以从历元至现在所经过的UTC毫秒数形式.
     * @return 转换后的小时数, 如果返回0, 表示其值不足以换算为1小时.
     */
    public static long getHours(long time)
    {
        if (time >= MIN_UNIT_HOUR)
            return time / MIN_UNIT_HOUR;
        else
            return 0;
    }

    /**
     * 将指定时间换算为分钟数返回.
     *
     * @param date 指定时间
     * @return 转换后的分钟数, 如果返回0, 表示其值不足以换算为1分钟.
     */
    public static long getMinutes(Date date)
    {
        return getMinutes(date.getTime());
    }

    /**
     * 将指定时间换算为分钟数返回.
     *
     * @param time 指定时间, 以从历元至现在所经过的UTC毫秒数形式.
     * @return 转换后的分钟数, 如果返回0, 表示其值不足以换算为1分钟.
     */
    public static long getMinutes(long time)
    {
        if (time >= MIN_UNIT_MINUTE)
            return time / MIN_UNIT_MINUTE;
        else
            return 0;
    }

    /**
     * 将指定时间换算为秒数返回.
     *
     * @param date 指定时间
     * @return 转换后的秒数, 如果返回0, 表示其值不足以换算为1秒.
     */
    public static long getSeconds(Date date)
    {
        return getSeconds(date.getTime());
    }

    /**
     * 将指定时间换算为秒数返回.
     *
     * @param time 指定时间, 以从历元至现在所经过的UTC毫秒数形式.
     * @return 转换后的秒数, 如果返回0, 表示其值不足以换算为1秒.
     */
    public static long getSeconds(long time)
    {
        if (time >= 1000)
            return time / 1000;
        else
            return 0;
    }

    /**
     * 将指定时间换算为天、小时、分钟以及秒数返回.
     * <p>
     * 注意: 本方法并不是getDays()等方法的集合, 或便捷入口. 它的目的是将指定时间的毫秒数换算后得到更详尽的信息(即精确到秒).
     *
     * @param date 指定日期对象
     * @return 转换后的详细信息, 取值依次为: 天数, 小时数, 分钟数, 秒数.<br>
     * 注意：该方法不会返回null, 如果任一元素的值为0, 则表示该部分不足以换算为其最小单位.
     */
    public static long[] getTimes(Date date)
    {
        return getTimes(date.getTime());
    }

    /**
     * 将指定时间换算为天、小时、分钟以及秒数返回.
     * <p>
     * 注意: 本方法并不是getDays()等方法的集合, 或便捷入口. 它的目的是将指定时间的毫秒数换算后得到更详尽的信息(即精确到秒).
     *
     * @param time 指定时间, 以从历元至现在所经过的UTC毫秒数形式.
     * @return 转换后的详细信息, 取值依次为: 天数, 小时数, 分钟数, 秒数.<br>
     * 注意：该方法不会返回null, 如果任一元素的值为0, 则表示该部分不足以换算为其最小单位.
     */
    public static long[] getTimes(long time)
    {
        // 取值依次为: day, hour, minute, second.
        long[] dateTime =
        {
            0, 0, 0, 0
        };
        // 计算天数
        if (time >= MIN_UNIT_DAY)
        {
            dateTime[0] = time / MIN_UNIT_DAY;
            time = time % MIN_UNIT_DAY;
        }
        // 计算剩余小时数
        if (time >= MIN_UNIT_HOUR)
        {
            dateTime[1] = time / MIN_UNIT_HOUR;
            time = time % MIN_UNIT_HOUR;
        }
        // 计算剩余分钟数
        if (time >= MIN_UNIT_MINUTE)
        {
            dateTime[2] = time / MIN_UNIT_MINUTE;
            time = time % MIN_UNIT_MINUTE;
        }
        // 计算剩余秒数
        if (time >= 1000)
            dateTime[3] = time / 1000;

        return dateTime;
    }

    /**
     * 计算并返回开始时间距结束时间相差的天数.
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 如果返回0, 表示两者相等或者相差不到1天; 如果返回-1, 表示startDate不在endDate之前, 无法作比较.
     * @throws NullPointerException 如果startDate或者endDate为null
     */
    public static long getDiffDays(Date startDate, Date endDate)
    {
        if (null == startDate || null == endDate)
            throw new NullPointerException("startDate or endDate");
        if (startDate.equals(endDate))
            return 0;
        if (!startDate.before(endDate))
            return -1;

        return getDays(endDate.getTime() - startDate.getTime());
    }

    /**
     * 计算并返回开始时间距结束时间相差的天数.
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 如果返回0, 表示两者相等或者相差不到1天; 如果返回-1, 表示startTime不在endTime之前, 无法作比较.
     * @throws IllegalArgumentException 如果startTime或者endTime小于等于0
     */
    public static long getDiffDays(long startTime, long endTime)
    {
        if (startTime <= 0 || endTime <= 0)
            throw new IllegalArgumentException("startTime or endTime <= 0");
        if (equals(startTime, endTime))
            return 0;
        if (!before(startTime, endTime))
            return -1;

        return getDays(endTime - startTime);
    }

    /**
     * 计算并返回开始时间距结束时间相差的小时数.
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 如果返回0, 表示两者相等或者相差不到1小时; 如果返回-1, 表示startDate不在endDate之前, 无法作比较.
     * @throws NullPointerException 如果startDate或者endDate为null
     */
    public static long getDiffHours(Date startDate, Date endDate)
    {
        if (null == startDate || null == endDate)
            throw new NullPointerException("startDate or endDate");
        if (startDate.equals(endDate))
            return 0;
        if (!startDate.before(endDate))
            return -1;

        return getHours(endDate.getTime() - startDate.getTime());
    }

    /**
     * 计算并返回开始时间距结束时间相差的小时数.
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 如果返回0, 表示两者相等或者相差不到1小时; 如果返回-1, 表示startTime不在endTime之前, 无法作比较.
     * @throws IllegalArgumentException 如果startTime或者endTime小于等于0
     */
    public static long getDiffHours(long startTime, long endTime)
    {
        if (startTime <= 0 || endTime <= 0)
            throw new IllegalArgumentException("startTime or endTime <= 0");
        if (equals(startTime, endTime))
            return 0;
        if (!before(startTime, endTime))
            return -1;

        return getHours(endTime - startTime);
    }

    /**
     * 计算并返回开始时间距结束时间相差的分钟数.
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 如果返回0, 表示两者相等或者相差不到1分钟; 如果返回-1, 表示startDate不在endDate之前, 无法作比较.
     * @throws NullPointerException 如果startDate或者endDate为null
     */
    public static long getDiffMinutes(Date startDate, Date endDate)
    {
        if (null == startDate || null == endDate)
            throw new NullPointerException("startDate or endDate");
        if (startDate.equals(endDate))
            return 0;
        if (!startDate.before(endDate))
            return -1;

        return getMinutes(endDate.getTime() - startDate.getTime());
    }

    /**
     * 计算并返回开始时间距结束时间相差的分钟数.
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 如果返回0, 表示两者相等或者相差不到1分钟; 如果返回-1, 表示startTime不在endTime之前, 无法作比较.
     * @throws IllegalArgumentException 如果startTime或者endTime小于等于0
     */
    public static long getDiffMinutes(long startTime, long endTime)
    {
        if (startTime <= 0 || endTime <= 0)
            throw new IllegalArgumentException("startTime or endTime <= 0");
        if (equals(startTime, endTime))
            return 0;
        if (!before(startTime, endTime))
            return -1;

        return getMinutes(endTime - startTime);
    }

    /**
     * 计算并返回开始时间距结束时间相差的秒数.
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 如果返回0, 表示两者相等或者相差不到1秒; 如果返回-1, 表示startDate不在endDate之前, 无法作比较.
     * @throws NullPointerException 如果startDate或者endDate为null
     */
    public static long getDiffSeconds(Date startDate, Date endDate)
    {
        if (null == startDate || null == endDate)
            throw new NullPointerException("startDate or endDate");
        if (startDate.equals(endDate))
            return 0;
        if (!startDate.before(endDate))
            return -1;

        return getSeconds(endDate.getTime() - startDate.getTime());
    }

    /**
     * 计算并返回开始时间距结束时间相差的秒数.
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 如果返回0, 表示两者相等或者相差不到1秒; 如果返回-1, 表示startTime不在endTime之前, 无法作比较.
     * @throws IllegalArgumentException 如果startTime或者endTime小于等于0
     */
    public static long getDiffSeconds(long startTime, long endTime)
    {
        if (startTime <= 0 || endTime <= 0)
            throw new IllegalArgumentException("startTime or endTime <= 0");
        if (equals(startTime, endTime))
            return 0;
        if (!before(startTime, endTime))
            return -1;

        return getSeconds(endTime - startTime);
    }

    /**
     * 计算并返回开始时间距结束时间相差的天、小时、分钟以及秒数.
     * <p>
     * 注意: 本方法并不是getDiffDays()等方法的集合, 或便捷入口. 它的目的是获取两个日期之间相差时间的更详尽信息(即精确到秒).
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return startDate距endDate相差的详细信息, 取值依次为: 天数, 小时数, 分钟数, 秒数.<br>
     * 同时, 该方法不会返回null, 如果任一元素的值为0, 则表示该部分不足以换算为其最小单位.
     * @throws NullPointerException 如果startDate或者endDate为null
     * @throws IllegalArgumentException 如果startDate不在endDate之前
     */
    public static long[] getDiffTimes(Date startDate, Date endDate)
    {
        if (null == startDate || null == endDate)
            throw new NullPointerException("startDate or endDate");
        if (!startDate.before(endDate))
            throw new IllegalArgumentException("开始时间必须在结束时间之前!");

        return getTimes(endDate.getTime() - startDate.getTime());
    }

    /**
     * 计算并返回开始时间距结束时间相差的天、小时、分钟以及秒数.
     * <p>
     * 注意: 本方法并不是getDiffDays()等方法的集合, 或便捷入口. 它的目的是获取两个日期之间相差时间的更详尽信息(即精确到秒).
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return startTime距endTime相差的详细信息, 取值依次为: 天数, 小时数, 分钟数, 秒数.<br>
     * 同时, 该方法不会返回null, 如果任一元素的值为0, 则表示该部分不足以换算为其最小单位.
     * @throws IllegalArgumentException 如果startTime或者endTime小于等于0<br>
     * 如果startTime不在endTime之前
     */
    public static long[] getDiffTimes(long startTime, long endTime)
    {
        if (startTime <= 0 || endTime <= 0)
            throw new IllegalArgumentException("startTime or endTime <= 0");
        if (!before(startTime, endTime))
            throw new IllegalArgumentException("开始时间必须在时间日期之前!");

        return getTimes(endTime - startTime);
    }

}
