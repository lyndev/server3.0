//package game.core.util.json;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Modifier;
//import java.util.Map;
//import java.util.UUID;
//import net.sf.json.JSONException;
//import net.sf.json.JSONObject;
//import net.sf.json.util.NewBeanInstanceStrategy;
//import net.sf.json.util.PropertyFilter;
//import net.sf.json.util.PropertySetStrategy;
//import org.apache.commons.beanutils.PropertyUtils;
//
///**
// *
// * <b>一个简单的JSON序列化工具.</b>
// * <p>
// * 特点：<br>
// * 1. 修饰为public或者transient的属性不参与序列化；<br>
// * 2. 支持UUID(java.util.UUID)类型的序列化和反序列化。
// * <p>
// * <b>Sample:</b>
// *
// * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
// * @version 1.0.0
// */
//public class SimpleJsonSerializer extends JsonSerializer
//{
//
//    private SimpleJsonSerializer()
//    {
//        super();
//    }
//
//    /**
//     * 获取SimpleJsonSerializer的实例对象.
//     *
//     * @return
//     */
//    public static JsonSerializer getInstance()
//    {
//        return Singleton.INSTANCE.getSerializer();
//    }
//
//    @Override
//    protected void initSerializableConfig()
//    {
//        // 设置属性序列化过滤器
//        serializableConfig.setJsonPropertyFilter(new PropertyFilter()
//        {
//            @Override
//            public boolean apply(Object source, String name, Object value)
//            {
//                try
//                {
//                    Field field = getDeclaredField(source.getClass(), name);
//                    if (field != null)
//                    {
//                        // 修饰为public或者transient的字段不参与序列化
//                        if (Modifier.isPublic(field.getModifiers())
//                                || Modifier.isTransient(field.getModifiers()))
//                        {
//                            return true;
//                        }
//                    }
//                }
//                catch (Exception e)
//                {
//                    throw new JSONException(e);
//                }
//
//                return false;
//            }
//        });
//    }
//
//    @Override
//    protected void initUnserializableConfig()
//    {
//        // 设置类实例化策略
//        unserializableConfig.setNewBeanInstanceStrategy(new NewBeanInstanceStrategy()
//        {
//            @Override
//            public Object newInstance(Class clazz, JSONObject json)
//                    throws InstantiationException, IllegalAccessException
//            {
//                if (UUID.class == clazz)
//                {
//                    return new UUID(json.getLong("mostSignificantBits"), json.getLong("leastSignificantBits"));
//                }
//                else
//                {
//                    return clazz.newInstance();
//                }
//            }
//        });
//
//        // 设置属性赋值策略
//        unserializableConfig.setPropertySetStrategy(new PropertySetStrategy()
//        {
//            @Override
//            public void setProperty(Object bean, String key, Object value)
//                    throws JSONException
//            {
//                if (bean instanceof Map)
//                {
//                    ((Map) bean).put(key, value);
//                }
//                else
//                {
//                    try
//                    {
//                        PropertyUtils.setSimpleProperty(bean, key, value);
//                    }
//                    catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
//                    {
//                        throw new JSONException(e);
//                    }
//                }
//            }
//        });
//    }
//
//    /**
//     * 用枚举来实现单例
//     */
//    private enum Singleton
//    {
//
//        INSTANCE;
//
//        JsonSerializer serializer;
//
//        Singleton()
//        {
//            this.serializer = new SimpleJsonSerializer();
//        }
//
//        JsonSerializer getSerializer()
//        {
//            return serializer;
//        }
//
//    }
//
//    public static void main(String[] args)
//    {
//        JsonSerializer js = SimpleJsonSerializer.getInstance();
//        TestBean bean = new TestBean();
//        bean.setId(UUID.randomUUID());
//        bean.setAccount(UUID.randomUUID());
//        bean.setName("莫妮卡.贝鲁齐");
//        bean.setNickName("女神");
//        bean.setAge((byte) 38);
//        bean.setPhone("10086");
//        bean.setAddress("Italy");
//        System.out.println("id=" + bean.getId());
//        System.out.println("account=" + bean.getAccount());
//        System.out.println("name=" + bean.getName());
//        System.out.println("nickName=" + bean.getNickName());
//        System.out.println("age=" + bean.getAge());
//        System.out.println("phone=" + bean.getPhone());
//        System.out.println("address=" + bean.getAddress());
//        
//        String json = js.toJSON(bean);
//        System.out.println("\njson:" + json);
//        TestBean result = (TestBean) js.toObject(json, TestBean.class);
//        System.out.println("toObject>>" + result + "\n");
//        System.out.println("id=" + result.getId());
//        System.out.println("account=" + result.getAccount());
//        System.out.println("name=" + result.getName());
//        System.out.println("nickName=" + result.getNickName());
//        System.out.println("age=" + result.getAge());
//        System.out.println("phone=" + result.getPhone());
//        System.out.println("address=" + result.getAddress());
//    }
//
//}
