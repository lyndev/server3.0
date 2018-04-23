package game.core.util;

import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

/**
 *
 * <b>数组操作的工具类.</b>
 * <p>
 * 此类封装了一些常用的数组操作.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.5
 */
public class ArrayUtils
{

    private ArrayUtils()
    {
        throw new UnsupportedOperationException("该类不允许被实例化!");
    }

    /**
     * 以中文作比较器, 对字符串数组进行排序.
     *
     * @param source 指定的字符串数组
     */
    @SuppressWarnings("unchecked")
    public static void sortCN(String source[])
    {
        Comparator cmp = Collator.getInstance(Locale.CHINA);
        Arrays.sort(source, cmp);
    }
    
    /**
     * 为字符串数组中的所有元素去除首尾的空格(半角).
     *
     * @param source 指定的字符串数组
     */
    public static void trim(String[] source)
    {
        if (null != source)
        {
            for (int i = 0; i < source.length; i++)
                source[i] = source[i].trim();
        }
    }
    
    /**
     * 判断字符串数组中是否包含指定的字符串(大小写敏感).
     *
     * @param source 指定的字符串数组
     * @param containedStr 要匹配的字符串, 可以为空字符串: "", 但如果为null, 则直接返回-1
     * @return 如果包含返回该字符串在数组中的索引(第一次匹配到的索引), 否则返回-1
     */
    public static int contains(String[] source, String containedStr)
    {
        if (null == containedStr)
            return -1;

        for (int i = 0; i < source.length; i++)
        {
            if (containedStr.equals(source[i]))
                return i;
        }

        return -1;
    }

    /**
     * 判断字符串数组中是否包含指定的字符串(忽略大小写检查).
     *
     * @param source 指定的字符串数组
     * @param containedStr 要匹配的字符串, 可以为空字符串: "", 但如果为null, 则直接返回-1
     * @return 如果包含返回该字符串在数组中的索引(第一次匹配到的索引), 否则返回-1
     */
    public static int containsIgnoreCase(String[] source, String containedStr)
    {
        if (null == containedStr)
            return -1;

        for (int i = 0; i < source.length; i++)
        {
            if (containedStr.equalsIgnoreCase(source[i]))
                return i;
        }

        return -1;
    }

    /**
     * 将指定数组解析为byte基本类型的数组.
     *
     * @param <T> 可转换成数值的类型
     * @param source 指定的任意类型数组
     * @return 如果指定数组中有任意一个元素的值为空或者转换失败, 返回null.
     */
    public static <T> byte[] parseByte(T[] source)
    {
        if (null == source || source.length == 0)
            return null;

        byte[] results = new byte[source.length];
        try
        {
            if (source instanceof String[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Byte.parseByte((String) source[i]);
            }
            else if (source instanceof Object[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Byte.parseByte(String.valueOf(source[i]));
            }
        }
        catch (NullPointerException | NumberFormatException e)
        {
            results = null;
        }

        return results;
    }

    /**
     * 将指定数组解析为byte包装类型的数组.
     *
     * @param <T> 可转换成数值的类型
     * @param source 指定的任意类型数组
     * @return 如果指定数组中有任意一个元素的值为空或者转换失败, 返回null.
     */
    public static <T> Byte[] parseByteWarp(T[] source)
    {
        if (null == source || source.length == 0)
            return null;

        Byte[] results = new Byte[source.length];
        try
        {
            if (source instanceof String[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Byte.parseByte((String) source[i]);
            }
            else if (source instanceof Object[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Byte.parseByte(String.valueOf((Object) source[i]));
            }
        }
        catch (NullPointerException | NumberFormatException e)
        {
            results = null;
        }

        return results;
    }

    /**
     * 将指定数组解析为short基本类型的数组.
     *
     * @param <T> 可转换成数值的类型
     * @param source 指定的任意类型数组
     * @return 如果指定数组中有任意一个元素的值为空或者转换失败, 返回null.
     */
    public static <T> short[] parseShort(T[] source)
    {
        if (null == source || source.length == 0)
            return null;

        short[] results = new short[source.length];
        try
        {
            if (source instanceof String[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Short.parseShort((String) source[i]);
            }
            else if (source instanceof Object[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Short.parseShort(String.valueOf(source[i]));
            }
        }
        catch (NullPointerException | NumberFormatException e)
        {
            results = null;
        }

        return results;
    }

    /**
     * 将指定数组解析为short包装类型的数组.
     *
     * @param <T> 可转换成数值的类型
     * @param source 指定的任意类型数组
     * @return 如果指定数组中有任意一个元素的值为空或者转换失败, 返回null.
     */
    public static <T> Short[] parseShortWarp(T[] source)
    {
        if (null == source || source.length == 0)
            return null;

        Short[] results = new Short[source.length];
        try
        {
            if (source instanceof String[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Short.parseShort((String) source[i]);
            }
            else if (source instanceof Object[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Short.parseShort(String.valueOf((Object) source[i]));
            }
        }
        catch (NullPointerException | NumberFormatException e)
        {
            results = null;
        }

        return results;
    }

    /**
     * 将指定数组解析为int基本类型的数组.
     *
     * @param <T> 可转换成数值的类型
     * @param source 指定的任意类型数组
     * @return 如果指定数组中有任意一个元素的值为空或者转换失败, 返回null.
     */
    public static <T> int[] parseInt(T[] source)
    {
        if (null == source || source.length == 0)
            return null;

        int[] results = new int[source.length];
        try
        {
            if (source instanceof String[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Integer.parseInt((String) source[i]);
            }
            else if (source instanceof Object[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Integer.parseInt(String.valueOf(source[i]));
            }
        }
        catch (NullPointerException | NumberFormatException e)
        {
            results = null;
        }

        return results;
    }

    /**
     * 将指定数组解析为int包装类型的数组.
     *
     * @param <T> 可转换成数值的类型
     * @param source 指定的任意类型数组
     * @return 如果指定数组中有任意一个元素的值为空或者转换失败, 返回null.
     */
    public static <T> Integer[] parseIntWarp(T[] source)
    {
        if (null == source || source.length == 0)
            return null;

        Integer[] results = new Integer[source.length];
        try
        {
            if (source instanceof String[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Integer.parseInt((String) source[i]);
            }
            else if (source instanceof Object[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Integer.parseInt(String.valueOf(source[i]));
            }
        }
        catch (NullPointerException | NumberFormatException e)
        {
            results = null;
        }

        return results;
    }

    /**
     * 将指定数组解析为long基本类型的数组.
     *
     * @param <T> 可转换成数值的类型
     * @param source 指定的任意类型数组
     * @return 如果指定数组中有任意一个元素的值为空或者转换失败, 返回null.
     */
    public static <T> long[] parseLong(T[] source)
    {
        if (null == source || source.length == 0)
            return null;

        long[] results = new long[source.length];
        try
        {
            if (source instanceof String[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Long.parseLong((String) source[i]);
            }
            else if (source instanceof Object[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Long.parseLong(String.valueOf(source[i]));
            }
        }
        catch (NullPointerException | NumberFormatException e)
        {
            results = null;
        }

        return results;
    }

    /**
     * 将指定数组解析为long包装类型的数组.
     *
     * @param <T> 可转换成数值的类型
     * @param source 指定的任意类型数组
     * @return 如果指定数组中有任意一个元素的值为空或者转换失败, 返回null.
     */
    public static <T> Long[] parseLongWarp(T[] source)
    {
        if (null == source || source.length == 0)
            return null;

        Long[] results = new Long[source.length];
        try
        {
            if (source instanceof String[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Long.parseLong((String) source[i]);
            }
            else if (source instanceof Object[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Long.parseLong(String.valueOf(source[i]));
            }
        }
        catch (NullPointerException | NumberFormatException e)
        {
            results = null;
        }

        return results;
    }

    /**
     * 将指定数组解析为float基本类型的数组.
     *
     * @param <T> 可转换成数值的类型
     * @param source 指定的任意类型数组
     * @return 如果指定数组中有任意一个元素的值为空或者转换失败, 返回null.
     */
    public static <T> float[] parseFloat(T[] source)
    {
        if (null == source || source.length == 0)
            return null;

        float[] results = new float[source.length];
        try
        {
            if (source instanceof String[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Float.parseFloat((String) source[i]);
            }
            else if (source instanceof Object[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Float.parseFloat(String.valueOf(source[i]));
            }
        }
        catch (NullPointerException | NumberFormatException e)
        {
            results = null;
        }

        return results;
    }

    /**
     * 将指定数组解析为float包装类型的数组.
     *
     * @param <T> 可转换成数值的类型
     * @param source 指定的任意类型数组
     * @return 如果指定数组中有任意一个元素的值为空或者转换失败, 返回null.
     */
    public static <T> Float[] parseFloatWarp(T[] source)
    {
        if (null == source || source.length == 0)
            return null;

        Float[] results = new Float[source.length];
        try
        {
            if (source instanceof String[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Float.parseFloat((String) source[i]);
            }
            else if (source instanceof Object[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Float.parseFloat(String.valueOf(source[i]));
            }
        }
        catch (NullPointerException | NumberFormatException e)
        {
            results = null;
        }

        return results;
    }

    /**
     * 将指定数组解析为double基本类型的数组.
     *
     * @param <T> 可转换成数值的类型
     * @param source 指定的任意类型数组
     * @return 如果指定数组中有任意一个元素的值为空或者转换失败, 返回null.
     */
    public static <T> double[] parseDouble(T[] source)
    {
        if (null == source || source.length == 0)
            return null;

        double[] results = new double[source.length];
        try
        {
            if (source instanceof String[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Double.parseDouble((String) source[i]);
            }
            else if (source instanceof Object[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Double.parseDouble(String.valueOf(source[i]));
            }
        }
        catch (NullPointerException | NumberFormatException e)
        {
            results = null;
        }

        return results;
    }

    /**
     * 将指定数组解析为double包装类型的数组.
     *
     * @param <T> 可转换成数值的类型
     * @param source 指定的任意类型数组
     * @return 如果指定数组中有任意一个元素的值为空或者转换失败, 返回null.
     */
    public static <T> Double[] parseDoubleWarp(T[] source)
    {
        if (null == source || source.length == 0)
            return null;

        Double[] results = new Double[source.length];
        try
        {
            if (source instanceof String[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Double.parseDouble((String) source[i]);
            }
            else if (source instanceof Object[])
            {
                for (int i = 0; i < source.length; i++)
                    results[i] = Double.parseDouble(String.valueOf(source[i]));
            }
        }
        catch (NullPointerException | NumberFormatException e)
        {
            results = null;
        }

        return results;
    }

}
