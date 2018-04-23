//package game.core.util.json;
//
//import java.lang.reflect.Field;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import net.sf.json.JSON;
//import net.sf.json.JSONObject;
//import net.sf.json.JSONSerializer;
//import net.sf.json.JsonConfig;
//import org.apache.log4j.Logger;
//
///**
// *
// * <b>JSON序列化工具的抽象类.</b>
// * <p>
// * 基于net.sf.json.JSON，子类通过继承并实现initSerializableConfig和initUnserializableConfig，可以支持更多的数据类型以及辅助功能.<br>
// * 但如果只是一般性需求，比如将一个标准的POJO转换成JSON，则建议直接使用net.sf.json.JSON.JSONObject以及net.sf.json.JSON.JSONArray.
// * <p>
// * <b>Sample:</b>
// *
// * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
// * @version 1.0.0
// */
//public abstract class JsonSerializer
//{
//
//    static final Logger LOG = Logger.getLogger(JsonSerializer.class);
//
//    /**
//     * 序列化配置
//     */
//    protected final JsonConfig serializableConfig = new JsonConfig();
//
//    /**
//     * 反序列化配置
//     */
//    protected final JsonConfig unserializableConfig = new JsonConfig();
//
//    /**
//     * 字段缓存，存储已解析的Class的字段列表，第一层key为类名，第二层key为字段名
//     */
//    private final Map<String, Map<String, Field>> fieldsCache = new ConcurrentHashMap<>();
//
//    protected JsonSerializer()
//    {
//        initSerializableConfig();
//        initUnserializableConfig();
//    }
//
//    /**
//     * 将指定对象序列化为JSON表达式.
//     *
//     * @param obj 指定对象
//     * @return
//     */
//    public final String toJSON(Object obj)
//    {
//        try
//        {
//            JSON json = JSONSerializer.toJSON(obj, serializableConfig);
//            return json.toString();
//        }
//        catch (Exception e)
//        {
//            LOG.error("JSON序列化失败！cause：", e);
//        }
//
//        return null;
//    }
//
//    /**
//     * 将指定的JSON表达式反序列化为指定对象.
//     *
//     * @param json 序列化后的JSON表达式
//     * @param clazz 指定反序列化后的对象类型
//     * @return
//     */
//    public final Object toObject(String json, Class<?> clazz)
//    {
//        try
//        {
//            JsonConfig jsonConfig = unserializableConfig.copy();
//            jsonConfig.setRootClass(clazz);
//            JSONObject object = JSONObject.fromObject(json);
//            return JSONObject.toBean(object, jsonConfig);
//        }
//        catch (Exception e)
//        {
//            LOG.error("JSON反序列化失败！json = " + json, e);
//        }
//
//        return null;
//    }
//
//    /**
//     * 获取Class中的指定字段.
//     *
//     * @param clazz 目标类
//     * @param name 字段名
//     * @return
//     */
//    protected final Field getDeclaredField(Class<?> clazz, String name)
//    {
//        if (fieldsCache.containsKey(clazz.getName()))
//        {
//            // 如果缓存中已包含该Class的字段列表，则不再反射获取
//            return fieldsCache.get(clazz.getName()).get(name);
//        }
//        else
//        {
//            Class<?> c = clazz;
//            // 建立字段映射表
//            Map<String, Field> map = new HashMap<>();
//            // 循环获取class及其父类的字段列表
//            while (c != null)
//            {
//                Field[] fields = c.getDeclaredFields();
//                for (Field field : fields)
//                {
//                    if (!map.containsKey(field.getName()))
//                    {
//                        map.put(field.getName(), field);
//                    }
//                }
//                c = c.getSuperclass();
//            }
//            fieldsCache.put(clazz.getName(), map);
//
//            return fieldsCache.get(clazz.getName()).get(name);
//        }
//    }
//
//    /**
//     * 初始化序列化配置信息.
//     */
//    protected abstract void initSerializableConfig();
//
//    /**
//     * 初始化反序列化配置信息.
//     */
//    protected abstract void initUnserializableConfig();
//
//}
