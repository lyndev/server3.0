package game.server.gm;

import com.alibaba.fastjson.JSONObject;
import game.core.command.Handler;
import game.core.script.ScriptManager;
import game.server.logic.util.ScriptArgs;
import org.apache.log4j.Logger;

/**
 *
 * <b>GM指令的消息处理器.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class GMCommandHandler extends Handler
{

    private final Logger log = Logger.getLogger(GMCommandHandler.class);

    @Override
    public void action()
    {
        GMCommandMessage message = (GMCommandMessage) getAttribute("gmCmd");

        ScriptArgs arg = new ScriptArgs();
        arg.put(ScriptArgs.Key.OBJECT_VALUE, message);
        ScriptManager.getInstance().call(1027, arg);
                
//        switch (message.getCommand())
//        {
//            case 1001:
//                // test
//                System.out.println("call GMCommand!");
//                System.out.println("command:" + message.getCommand());
//                System.out.println("serial:" + message.getSerial());
//                System.out.println("secret:" + message.getSecret());
//                System.out.println("data:" + message.getData().toJSONString());
//                JSONObject data = new JSONObject();
//                data.put("test", 123);
//                message.getSession().write(GMCommandUtils.createMessage(message, data, 1));
//                break;
//            default:
//                log.error("Not found GMCommand! command = "
//                        + message.getCommand() + "，data = " + message.getData().toJSONString());
//                break;
//        }
    }

}
