package game.core.util.file;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * <b>文件执行预定义操作的结果集.</b>
 * <p>
 * 此类用于在执行某些文件操作时, 设置执行结果的详细信息. 如删除目录下所有文件时, 可将失败的文件或目录添加至队列, 并记录成功删除的文件和目录数.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class FileResultSet
{

    /**
     * 成功执行预定义操作的文件总数
     */
    private int totalFiles;

    /**
     * 成功执行预定义操作的目录总数
     */
    private int totalDirectory;

    /**
     * 执行预定义操作时失败的文件队列
     */
    private Queue<File> failFiles;

    /**
     * 获取成功执行预定义操作的文件总数.
     *
     * @return 文件总数
     */
    public int getTotalFiles()
    {
        return totalFiles;
    }

    /**
     * 设置成功执行预定义操作的文件总数.
     *
     * @param total 文件总数
     */
    protected void setTotalFiles(int total)
    {
        totalFiles = total;
    }

    /**
     * 获取成功执行预定义操作的目录总数.
     *
     * @return 目录总数
     */
    public int getTotalDirectory()
    {
        return totalDirectory;
    }

    /**
     * 设置成功执行预定义操作的目录总数.
     *
     * @param total 目录总数
     */
    protected void setTotalDirectory(int total)
    {
        totalDirectory = total;
    }

    /**
     * 获取执行预定义操作时失败的文件队列, 队列类型: FIFO.
     *
     * @return 失败的文件(目录)对象所组成的队列, 如果没有失败的文件(目录), 则返回null.
     */
    public Queue<File> getFailFiles()
    {
        return failFiles;
    }

    /**
     * 添加执行预定义操作时失败的文件(目录)进队列, 队列类型: FIFO.
     * <p>
     * 一旦调用此方法, 则表示执行预定义操作时出现了异常, 切记!
     *
     * @param file 失败的文件对象
     */
    protected void addFileFailed(File file)
    {
        if (null == failFiles)
            failFiles = new LinkedList<>();
        failFiles.add(file);
    }

    /**
     * 是否成功执行预定义操作, 包括所有的流程.
     *
     * @return 所有的操作都执行成功时返回true, 否则返回false.
     */
    public boolean isSuccessful()
    {
        return null == failFiles;
    }

}
