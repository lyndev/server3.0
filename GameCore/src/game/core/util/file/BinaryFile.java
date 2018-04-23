package game.core.util.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * <b>读写二进制文件的工具类.</b>
 * <p>
 * 用于读取或写入诸如图像数据之类的原始字节流文件.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class BinaryFile
{

    private BinaryFile()
    {
        throw new UnsupportedOperationException("该类不允许被实例化!");
    }

    /**
     * 读取目标文件的字节流, 将其写入数组并返回.
     *
     * @param file 目标文件
     * @param bufferSize 设置缓冲区大小(最小支持1KB. 单位: 字节)
     * @return 目标文件的字节数组
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static byte[] read(File file, int bufferSize)
            throws FileNotFoundException, IOException
    {
        if (bufferSize < 1024)
            bufferSize = 1024;

        FileInputStream in = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream(bufferSize);
        byte[] b = new byte[bufferSize];
        int len;
        try
        {
            while ((len = in.read(b)) != -1)
                out.write(b, 0, len);
            out.flush();

            return out.toByteArray();
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
                    in.close();
                }
                catch (IOException e)
                {
                    // ignore
                }
            }
        }

    }

    /**
     * 读取目标文件的字节流, 将其写入数组并返回.
     *
     * @param filename 目标文件的全路径
     * @param bufferSize 设置缓冲区大小(最小支持1KB. 单位: 字节)
     * @return 目标文件的字节数组
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static byte[] read(String filename, int bufferSize)
            throws FileNotFoundException, IOException
    {
        return read(new File(filename), bufferSize);
    }

    /**
     * 将目标字节流写入指定的输出文件.
     *
     * @param inputBytes 目标字节数组
     * @param outputFile 指定的输出文件
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void write(byte[] inputBytes, File outputFile)
            throws FileNotFoundException, IOException
    {
        FileOutputStream out = new FileOutputStream(outputFile);
        try
        {
            if (inputBytes != null)
            {
                for (byte b : inputBytes)
                    out.write(b);
            }
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
        }
    }

    /**
     * 将目标字节流写入指定的输出文件.
     *
     * @param inputBytes 目标字节数组
     * @param outputFilename 指定的输出文件的全路径
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void write(byte[] inputBytes, String outputFilename)
            throws FileNotFoundException, IOException
    {
        write(inputBytes, new File(outputFilename));
    }

    /**
     * 读取目标文件的字节流, 并将其写入指定的输出文件.
     *
     * @param inputFile 目标文件
     * @param outputFile 指定的输出文件
     * @param bufferSize 设置缓冲区大小(最小支持1KB. 单位: 字节)
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void write(File inputFile, File outputFile, int bufferSize)
            throws FileNotFoundException, IOException
    {
        if (bufferSize < 1024)
        {
            bufferSize = 1024;
        }

        FileInputStream in = new FileInputStream(inputFile);
        FileOutputStream out = new FileOutputStream(outputFile);
        byte[] b = new byte[bufferSize];
        int len;
        try
        {
            while ((len = in.read(b)) != -1)
                out.write(b, 0, len);
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
                    in.close();
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
     * @param bufferSize 设置缓冲区大小(最小支持1KB. 单位: 字节)
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void write(String inputFilename, String outputFilename,
            int bufferSize) throws FileNotFoundException, IOException
    {
        write(new File(inputFilename), new File(outputFilename), bufferSize);
    }

}
