package game.core.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;

/**
 *
 * <b>读写大文件的工具类.</b>
 * <p>
 * 为了提高读写大文件的效率, 这里采用nio提供的内存映射机制(java.nio.MappedByteBuffer).
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class BigFile
{

    private BigFile()
    {
        throw new UnsupportedOperationException("该类不允许被实例化!");
    }

    /**
     * 读取目标文件的字节流, 将其写入数组并返回.
     *
     * @param file 目标文件
     * @return 目标文件的字节数组
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static byte[] read(File file) throws FileNotFoundException,
            IOException
    {
        byte[] results;
        // 获取输入文件的通信管道
        FileChannel fc = new FileInputStream(file).getChannel();
        try
        {
            // 将此通道的所有文件区域映射到内存
            ByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            // 获取映射的缓冲区大小
            int len = buffer.limit();
            results = new byte[len];
            for (int i = 0; i < len; i++)
                results[i] = buffer.get();
        }
        catch (IOException e)
        {
            throw new IOException(e.getMessage());
        }
        finally
        {
            try
            {
                fc.close();
            }
            catch (IOException e)
            {
                // ignore
            }
        }

        return results;
    }

    /**
     * 读取目标文件的字节流, 将其写入数组并返回.
     *
     * @param filename 目标文件的全路径
     * @return 目标文件的字节数组
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static byte[] read(String filename) throws FileNotFoundException,
            IOException
    {
        return read(new File(filename));
    }

    /**
     * 读取目标文件的字节流, 并将其写入指定的输出文件.
     *
     * @param inputFile 目标文件
     * @param outputFile 指定的输出文件
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void write(File inputFile, File outputFile)
            throws FileNotFoundException, IOException
    {
        // 获取输入文件的通信管道
        FileChannel fc = new FileInputStream(inputFile).getChannel();
        FileOutputStream out = new FileOutputStream(outputFile);
        try
        {
            // 将此通道的所有文件区域映射到内存
            ByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc
                    .size());
            // 获取映射的缓冲区大小
            int len = buffer.limit();
            for (int i = 0; i < len; i++)
                out.write(buffer.get());
            out.flush();
        }
        catch (IOException e)
        {
            throw new IOException(e.getMessage());
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                // ignore
            }
            finally
            {
                try
                {
                    fc.close();
                }
                catch (IOException e)
                {
                    // ignore
                }
            }
        }
    }

    /**
     * 读取目标文件的字节流, 并将其写入指定的输出文件.
     *
     * @param inputFilename 目标文件的全路径
     * @param outputFilename 指定的输出文件的全路径
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void write(String inputFilename, String outputFilename)
            throws FileNotFoundException, IOException
    {
        write(new File(inputFilename), new File(outputFilename));
    }

}
