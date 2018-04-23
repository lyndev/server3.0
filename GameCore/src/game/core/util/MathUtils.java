package game.core.util;

import java.math.BigDecimal;

/**
 *
 * <b>数学运算工具类.</b>
 * <p>
 * 提供一些java.lang.Math未提供的数学函数.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.5
 */
public class MathUtils
{

    private MathUtils()
    {
        throw new UnsupportedOperationException("该类不允许被实例化!");
    }

    /**
     * 精确的加法运算.
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 精确的减法运算.
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 精确的乘法运算.
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 相对精确的除法运算.
     * <p>
     * 当发生除不尽的情况时, 精确到小数点以后10位, 以后的数字四舍五入.
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2)
    {
        return div(v1, v2, 10);
    }

    /**
     * 相对精确的除法运算.
     * <p>
     * 当发生除不尽的情况时, 由scale参数指定精度, 以后的数字四舍五入.
     *
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示需要精确到小数点以后几位
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 精确的小数位四舍五入处理.
     *
     * @param num 待四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double num, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(num));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 比较指定的两个浮点数.
     *
     * @param a 待比较的数1
     * @param b 待比较的数2
     * @return -1: a小于b; 0: 二者相等; 1: a大于b.
     */
    public static int compare(double a, double b)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(a));
        BigDecimal b2 = new BigDecimal(Double.toString(b));
        return b1.compareTo(b2);
    }

    /**
     * 获取指定浮点数的小数点位数, 尾数无意义的0不算作内, 比如1.10其小数点位数返回1, 又如1.0其小数点位数返回0.
     *
     * @param num 指定浮点数
     * @return 小数点后的位数
     */
    public static int getDecimalPlaces(double num)
    {
        if (num != 0)
        {
            String s = String.valueOf(num);
            for (int i = 0; i < s.length(); i++)
            {
                if (s.charAt(i) == '.')
                {
                    int result = s.length() - (i + 1);
                    for (int j = s.length() - 1; j > i; j--)
                    {
                        if (s.charAt(j) == '0')
                        {
                            result--;
                        }
                        else
                        {
                            break;
                        }
                    }

                    return result;
                }
            }
        }

        return 0;
    }

    /**
     * 求两数的最大公约数.
     *
     * @param a
     * @param b
     * @return
     */
    public static int gcd(int a, int b)
    {
        int temp;
        while (a % b != 0)
        {
            temp = b;
            b = a % b;
            a = temp;
        }

        return b;
    }

    /**
     * 求两数的最小公倍数.
     *
     * @param a
     * @param b
     * @return
     */
    public static int lcm(int a, int b)
    {
        return a * b / gcd(a, b);
    }

    /**
     * 求N个数的最小公倍数.
     *
     * @param arr
     * @return
     */
    public static int lcm(int[] arr)
    {
        int result = arr[0];
        for (int i = 1; i < arr.length; i++)
        {
            result = result * (arr[i] / gcd(arr[i], result));
        }

        return result;
    }

}
