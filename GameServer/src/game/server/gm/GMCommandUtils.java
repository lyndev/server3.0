package game.server.gm;

import com.alibaba.fastjson.JSONObject;

/**
 *
 * <b>GM指令工具类.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class GMCommandUtils
{

    /**
     * 创建服务端回复的GM指令消息.
     *
     * @param receved 服务端接收到的GM指令
     * @param data 要发送的指令数据
     * @param code GM指令的执行结果
     * @return
     */
    public static GMCommandMessage createMessage(
            GMCommandMessage receved, JSONObject data, int code)
    {
        GMCommandMessage message = new GMCommandMessage();
        message.setCommand(receved.getCommand());
        message.setSerial(receved.getSerial());
        message.setSecret((short) 1056); // GM->平台的消息数据不作加密

        JSONObject json = new JSONObject();
        json.put("command", message.getCommand());
        json.put("serial", message.getSerial());
        json.put("data", data);
        json.put("code", code);
        message.setData(json);

        return message;
    }

}
