package game.core.util;

import java.util.Random;

/**
 *
 * <b>一个简单的伪随机数生成器.</b>
 * <p>
 * 基于java.util.Random, 提供它所不具备的一些按条件生成的方法.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.1
 */
public class SimpleRandom
{

    private final Random random;

    /**
     * 创建一个新的随机数生成器.
     */
    public SimpleRandom()
    {
        random = new Random();
    }

    /**
     * 使用单个long种子创建一个新随机数生成器.
     *
     * @param seed 初始种子
     */
    public SimpleRandom(long seed)
    {
        random = new Random(seed);
    }

    /**
     * 返回java.util.Random实例.
     *
     * @return
     */
    public Random getRandom()
    {
        return random;
    }

    /**
     * 随机生成一个在指定范围内的整数.
     *
     * @param min 随机数生成范围的最小值
     * @param max 随机数生成范围的最大值
     * @return
     * @throws IllegalArgumentException min小于max
     */
    public int next(int min, int max)
    {
        if (max > min)
            return random.nextInt(max) % (max - min + 1) + min;
        else if (min == max)
            return min;
        else
            throw new IllegalArgumentException("max必须大于等于min! min = " + min + "; max = " + max);
    }

    /**
     * 随机生成一组在指定范围内的整数, 且保证不会重复(生成范围较大时不推荐使用).
     *
     * @param min 随机数生成范围的最小值
     * @param max 随机数生成范围的最大值
     * @param num 随机数生成个数, 必须在生成范围之内(即num不能大于max-min+1)
     * @return
     * @throws IllegalArgumentException min小于max或者num大于max-min+1
     */
    public int[] getArrays(int min, int max, int num)
    {
        if (max < min)
        {
            throw new IllegalArgumentException(
                    "max必须大于等于min! min = " + min + "; max = " + max);
        }

        int amount = max - min + 1;
        if (num > amount)
        {
            throw new IllegalArgumentException(
                    "num必须小于max-min! num = " + num + "; max-min+1 = " + amount);
        }

        if (amount <= 1)
        {
            return new int[]
            {
                next(min, max)
            };
        }

        // 生成一组不重复的随机种子
        int[] seed = new int[amount];
        for (int i = 0, j = min; j <= max; i++, j++)
        {
            seed[i] = j;
        }

        int[] result = new int[num];
        for (int i = 0; i < num; i++)
        {
            int j = random.nextInt(seed.length - i);
            result[i] = seed[j];
            seed[j] = seed[seed.length - 1 - i];
        }

        return result;
    }

    /**
     * 根据既定几率, 计算是否触发某事件.
     *
     * @param rate 几率值, 等于0时, 始终返回false; 大于等于100时, 始终返回true; 其余情况按几率计算:<br>
     * 1 ~ 99: 表示按1% ~ 99%的几率触发, 可以带小数点<br>
     * 小于0时, 小数点后面每多1位有效数, 几率以100 * 10的倍数计算, 如:<br>
     * 0.1 = 千分之1; 0.01 = 万分之1; 0.11 = 万分之11; 1.11 = 万分之111; 11.11 = 万分之1111...
     * @return true表示触发, 反之未触发.
     */
    public boolean probability(double rate)
    {
        if (rate <= 0.000000001)
            return false;
        if (rate >= 100.00)
            return true;

        // 获取几率小数点位数
        int len = MathUtils.getDecimalPlaces(rate);
        int value;
        int limit;
        if (len > 0)
        {
            value = (int) (rate * (int) Math.pow(10, len));
            limit = 100 * (int) Math.pow(10, len);
        }
        else
        {
            // 没有小数位数时, 直接按百分比计算
            value = (int) rate;
            limit = 100;
        }

        return next(1, limit) <= value;
    }

    public static void main(String[] args)
    {
        SimpleRandom random = new SimpleRandom();
//        int min = 1, max = 3;
//        int one = 0, two = 0, three = 0;
//        for (int i = 0; i < 100000; i++)
//        {
//            int num = random.next(min, max);
//            if (num == 1)
//                one++;
//            else if (num == 2)
//                two++;
//            else if (num == 3)
//                three++;
//        }
//        System.out.println("one count: " + one);
//        System.out.println("two count: " + two);
//        System.out.println("three count: " + three);

        long beginTime = System.currentTimeMillis();
        int count1 = 0;
        byte rate1 = 1;
        for (int i = 0; i < 1000000; i++)
        {
            if (random.probability(rate1))
            {
                count1++;
            }
        }
        System.out.println(rate1 + "% Successful count: " + count1);
        long endTime = System.currentTimeMillis();
        System.out.println("cost time " + (endTime - beginTime) + "ms");

        long beginTime2 = System.currentTimeMillis();
        int count2 = 0;
        float rate2 = 1.5f;
        for (int i = 0; i < 1000000; i++)
        {
            if (random.probability(rate2))
            {
                count2++;
            }
        }
        System.out.println(rate2 + "% Successful count: " + count2);
        long endTime2 = System.currentTimeMillis();
        System.out.println("cost time " + (endTime2 - beginTime2) + "ms");
    }
    
}
