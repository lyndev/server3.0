/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.net;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import game.core.message.Player;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Protubuf测试类.
 * 
 * @author wangJingWei
 */
public class TestMsg
{

    public static void main(String[] args) throws Exception
    {
        Player.Person.Builder builder = Player.Person.newBuilder();    
        builder.setId(1);
        builder.setName("奔跑的猪");
        builder.setEmail("wjvonline@163.com");
        
        Player.Person person = builder.build();
        System.out.println("\n序列化>>>");
        byte[] result = person.toByteArray();
        for (byte b : result) {
            System.out.print(b);
        }
//        String typeName = Player.Person.getDescriptor().getName();
//        System.out.println("\n\ntypeName=" + typeName);
//        System.out.println(Player.getDescriptor().findMessageTypeByName(typeName));
//        System.out.println("msgId=" + Player.Person.MsgID.eMsgID_VALUE);
        
        System.out.println("\n反序列化>>>");
        Player.Person msg;
        try
        {
            msg = Player.Person.parseFrom(result);
            System.out.println(msg);
        }
        catch (InvalidProtocolBufferException e)
        {
            System.out.println(e.getMessage());
        }
        
        System.out.println("动态反序列化>>>");
        Player.Person newMsg = (Player.Person) parse(Player.Person.class, result);
        System.out.println(newMsg);
//        Descriptor msgType = DescriptorProtos.getDescriptor().findMessageTypeByName(typeName);
//        System.out.println("消息类型：" + msgType);
//        DynamicMessage newMsg = DynamicMessage.parseFrom(Player.Person.getDescriptor(), result);
//        System.out.println(newMsg);
//        Player.Person person = (Player.Person) newMsg;
    }
    
    public static Object parse(Class<? extends GeneratedMessage> clazz, byte[] data) 
            throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
         // 获取并调用parseFrom方法
        Method method = clazz.getDeclaredMethod("parseFrom", byte[].class);
        return method.invoke(null, data);
    }

}
