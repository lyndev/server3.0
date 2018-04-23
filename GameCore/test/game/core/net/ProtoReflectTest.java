/*
 * 2014/3/21 14:55 by ChenLong
 * 反射构造ProtoBuf Message对象Demo程序
 *
 */
package game.core.net;

import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import game.core.message.Player;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 * @author ChenLong
 */
public class ProtoReflectTest
{
    /**
     * 反射构造ProtoBuf Message对象
     *
     * @param dmsg
     * @param TMsg
     * @param TBuilder
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Message getMessage(DynamicMessage dmsg, Class<?> TMsg, Class<?> TBuilder) throws IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
    {

        Class[] args = new Class[0];

        // 获取并调用newBuilder方法
        Method newBuilder = TMsg.getDeclaredMethod("newBuilder", args);
        Object[] newBuilderArgsType = new Class[0];
        Object builder = newBuilder.invoke(null, newBuilderArgsType);

        // 获取并调用setField方法
        Class[] setFieldArgsType = new Class[2];
        setFieldArgsType[0] = Descriptors.FieldDescriptor.class;
        setFieldArgsType[1] = Object.class;
        Method setField = TBuilder.getSuperclass().getDeclaredMethod("setField", setFieldArgsType);

        // 向newBuilder出来的Message填值
        for (Map.Entry entry : dmsg.getAllFields().entrySet())
            setField.invoke(builder, (Descriptors.FieldDescriptor) entry.getKey(), entry.getValue());

        // 获取并调用build方法
        Method buildMethod = TBuilder.getDeclaredMethod("build", args);
        Object[] buildMethodParam = new Object[0];
        Object msg = buildMethod.invoke(builder, buildMethodParam);

        return (Message) msg;
    }

    public static void main(String[] args) throws InvalidProtocolBufferException
    {
        Player.Person.Builder builder = Player.Person.newBuilder();
        builder.setId(1);
        builder.setName("奔跑的猪");
        builder.setEmail("wjvonline@163.com");

        Player.Person person = builder.build();
        System.out.println("\n序列化>>>");
        byte[] result = person.toByteArray();
        for (byte b : result)
        {
            System.out.print(b);
        }
        String typeName = Player.Person.getDescriptor().getName();
        System.out.println("\n\ntypeName=" + typeName);
        System.out.println(Player.getDescriptor().findMessageTypeByName(typeName));

        System.out.println("动态反序列化>>>");

        Descriptors.Descriptor descriptor = Player.getDescriptor().findMessageTypeByName(typeName);

        DynamicMessage dmsg = DynamicMessage.parseFrom(descriptor, result).toBuilder().build();
        try
        {
            Message msg = getMessage(dmsg, Player.Person.class, Player.Person.Builder.class);
            Player.Person pp = (Player.Person) msg;
            System.out.println("msg:\t" + msg.toString());
            System.out.println("pp:\t" + pp.toString());
        }
        catch (IllegalAccessException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException e)
        {
        }
    }
}
