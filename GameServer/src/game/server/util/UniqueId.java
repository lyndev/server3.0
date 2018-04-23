package game.server.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import game.server.GameServer;
import game.server.db.game.bean.GlobalBean;
import game.server.db.game.dao.GlobalDao;
import game.server.logic.constant.GlobalTableKey;
import game.server.logic.struct.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.log4j.Logger;

/**
 *
 * @date 2014-6-25
 * @author pengmian
 */
public class UniqueId
{
    private final static Logger logger = Logger.getLogger(UniqueId.class);

    /**
     * 功能枚举
     */
    public static enum FuncType
    {
        /**
         * 角色ID
         */
        ROLE(1),
        /**
         * 用户ID
         */
        USER(2),
        /**
         * 邮件ID
         */
        MAIL(3);

        private final long val;

        FuncType(long val)
        {
            this.val = val;
        }

        public long value()
        {
            return this.val;
        }
    }

    // 序列号 ServerId --> (FuncType --> Sequence)
    private static final Map<Long, Map<Long, AtomicLong>> sequences = new HashMap<>();

    // 序列号开始
    private static final long SEQUENCE_BEGIN = 1L;

    /**
     * 初始化，读取保存的序列号
     */
    public static synchronized void init()
    {
        GlobalBean bean = GlobalDao.select(GlobalTableKey.UNIQUE_ID.getKey());
        try
        {
            if (bean != null && bean.getValue() != null && !bean.getValue().trim().equals(""))
            {
                JSONObject jsonObj = new JSONObject(JSON.parseObject(bean.getValue()));
                for (Entry<String, Object> entry : jsonObj.entrySet())
                {
                    JSONArray arr = JSON.parseArray(jsonObj.getString(entry.getKey()));
                    Map<Long, AtomicLong> map = new HashMap<>();
                    for (int a = 0; a < arr.size(); ++a)
                    {
                        JSONObject obj = arr.getJSONObject(a);
                        map.put(obj.getLong("FuncType"), new AtomicLong(obj.getLong("Sequence")));
                    }
                    sequences.put(Long.parseLong(entry.getKey()), map);
                }
            }
        }
        catch (JSONException ex)
        {
            logger.error("init UniqueId exception", ex);
        }
    }

    /**
     * 保存
     */
    public static synchronized void save()
    {
        JSONObject obj = null;
        GlobalBean bean = GlobalDao.select(GlobalTableKey.UNIQUE_ID.getKey());
        try
        {
            obj = new JSONObject(JSON.parseObject(bean.getValue()));
        }
        catch (Exception ex)
        {
            obj = new JSONObject();
        }

        try
        {
            for (Entry<Long, Map<Long, AtomicLong>> entry : sequences.entrySet())
            {
                JSONArray arr = new JSONArray();
                for (Entry<Long, AtomicLong> entry2 : entry.getValue().entrySet())
                {
                    JSONObject obj2 = new JSONObject();
                    obj2.put("FuncType", entry2.getKey());
                    obj2.put("Sequence", entry2.getValue().get());
                    arr.add(obj2);
                }
                obj.put(entry.getKey().toString(), arr.toString());
            }

            if (!obj.isEmpty())
            {
                GlobalBean newBean = new GlobalBean();
                newBean.setId(1000);
                newBean.setValue(obj.toString());

                if (GlobalDao.update(newBean) == 0)
                {
                    GlobalDao.insert(newBean);
                }
            }
        }
        catch (JSONException ex)
        {
            logger.error("save UniqueId exception", ex);
        }
    }

    /**
     * 生成唯一id (未做任何异常处理) 序列数（20位） | 功能ID（4位) | 服务器ID（10位）| 代理ID（7位）
     *
     * @param serverId 服务器id
     * @param platformId 平台id
     * @param type 功能类型
     * @return
     */
    public static synchronized long getUniqueId(int serverId, long platformId, FuncType type)
    {
        Map<Long, AtomicLong> map = sequences.get((long) serverId);
        if (map == null)
        {
            map = new HashMap<>();
            sequences.put((long) serverId, map);
        }
        AtomicLong seq = map.get(type.value());
        if (null == seq)
        {
            seq = new AtomicLong(SEQUENCE_BEGIN);
            map.put(type.value(), seq);
        }
        long ret = (platformId & 0x7FL) | ((serverId & 0x3FFL) << 7) | ((type.value() & 0xFL) << 17) | ((0x1FFFFFL & seq.getAndIncrement()) << 21);
//        logger.info("generating UniqueId = " + ret);
        return ret;
    }

    /**
     * 通过player获得uniqueId
     *
     * @param player
     * @param type
     * @return
     */
    public static synchronized long getUniqueId(Player player, FuncType type)
    {
        return getUniqueId(player.getServerId(), player.getPlatformId(), type);
    }

    /**
     * 比对结果用，未加锁。
     *
     * @param serverId 服务器id
     * @param type 功能类型
     * @return
     */
    public static synchronized long getSequence(long serverId, FuncType type)
    {
        Map<Long, AtomicLong> map = sequences.get(serverId);
        if (map == null)
        {
            return 0l;
        }
        return map.get(type.value()).get();
    }

    /**
     * 返回字符串表示的uniqueid
     *
     * @param serverId
     * @param platformId
     * @param type
     * @return
     */
    public static synchronized String getUniqueIdBase36(int serverId, long platformId, FuncType type)
    {
        return toBase36(getUniqueId(serverId, platformId, type));
    }

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

    /**
     * 启动时根据玩家数据重建sequences, 和UniqueId中的对比; 主要为了容错
     *
     * @param sequences
     */
    public static synchronized void generatedFlushSequence(Map<Long, Map<Long, Long>> genSequences)
    {
        for (Map.Entry<Long, Map<Long, Long>> serverEntry : genSequences.entrySet())
        {
            long genServerId = serverEntry.getKey();
            Map<Long, Long> genServerSequence = serverEntry.getValue();
            for (Map.Entry<Long, Long> sequenceEntry : genServerSequence.entrySet())
            {
                long genFunc = sequenceEntry.getKey();
                long genSeq = sequenceEntry.getValue();

                Map<Long, AtomicLong> serverSequence = sequences.get(genServerId);
                if (serverSequence == null)
                {
                    serverSequence = new HashMap<>();
                    sequences.put(genServerId, serverSequence);
                }

                if (!serverSequence.containsKey(genFunc))
                    serverSequence.put(genFunc, new AtomicLong(SEQUENCE_BEGIN));

                long seq = serverSequence.get(genFunc).get();
                if (seq < genSeq)
                {
                    long newSeq = genSeq + 1;
                    serverSequence.put(genFunc, new AtomicLong(newSeq));
                    logger.warn("更新UniqueId序列 serverId = " + genServerId + ", Func = " + genFunc + ", oldSeq = " + seq + ", newSeq = " + newSeq);
                }
            }
        }
    }

    public static void main(String[] args)
    {
        GameServer.getInstance();
        //UniqueId.init(.getServerId());

        long ret = UniqueId.getUniqueId(731, 1, FuncType.ROLE);

        for (int a = 0; a < 100000000; ++a)
        {
            long ret1 = UniqueId.getUniqueId(731, a, FuncType.ROLE);
            if (a % 10000 == 0)
                System.out.println(a);

            long type = UniqueId.parseFuncType(ret1);
            long agent = UniqueId.parsePlatform(ret1);
            long sid = UniqueId.parseServerId(ret1);

            long agent2 = a & 0x7F;
            long sid2 = a & 0x3FF;

            String str = UniqueId.toBase36(ret1);
            long ret2 = UniqueId.toBase10(str);
            if (ret2 != ret1 && agent != agent2 && type != FuncType.ROLE.value() && sid != sid2)
            {
                throw new IllegalArgumentException();
            }
        }
        UniqueId.save();
    }
}
