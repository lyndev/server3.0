package game.core.logging;

import game.core.command.ICommand;

/**
 *
 * <b>日志处理器的抽象基类.</b>
 * <p>
 * 每个Handler应负责将日志写入到数据库或是文件系统.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public abstract class LogHandler implements ICommand
{

    private Log log; // 待处理的日志

    public Log getLog()
    {
        return log;
    }

    public void setLog(Log log)
    {
        this.log = log;
    }

}
