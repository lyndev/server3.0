package game.server.logic.keys;

import game.core.command.CommandProcessor;
import game.core.command.ICommand;
import game.server.logic.keys.handler.KeysVerifyHandler;
import org.apache.log4j.Logger;

/**
 *
 * <b>验证并使用激活码/兑换码的线程.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class KeysVerifyThread extends CommandProcessor
{

    private final Logger log = Logger.getLogger(KeysVerifyThread.class);

    public KeysVerifyThread()
    {
        super(KeysVerifyThread.class.getSimpleName());
    }

    public static KeysVerifyThread getInstance()
    {
        return Singleton.INSTANCE.getProcessor();
    }

    @Override
    protected void doCommand(ICommand command)
    {
        if (command instanceof KeysVerifyHandler)
        {
            command.action();
        }
        else
        {
            log.error("不支持的命令类型！command class: " + command.getClass().getName());
        }
    }

    @Override
    public void writeError(String message)
    {
        log.error(message);
    }

    @Override
    public void writeError(String message, Throwable t)
    {
        log.error(message, t);
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton
    {
        INSTANCE;
        KeysVerifyThread processor;

        Singleton()
        {
            this.processor = new KeysVerifyThread();
        }

        KeysVerifyThread getProcessor()
        {
            return processor;
        }
    }

}
