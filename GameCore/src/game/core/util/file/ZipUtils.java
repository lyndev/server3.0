package game.core.util.file;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.apache.log4j.Logger;

/**
 * 
 * <b>zip格式的压缩&解压工具类.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 * 
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public abstract class ZipUtils
{

    private static final Logger LOG = Logger.getLogger(ZipUtils.class);

    /**
     * 执行压缩.
     *
     * @param data 待压缩数据
     * @return 压缩后的数据
     */
    public static byte[] compress(byte[] data)
    {
        byte[] output = new byte[0];

        Deflater compresser = new Deflater();
        compresser.reset();
        compresser.setInput(data);
        compresser.finish();

        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        try
        {
            byte[] buf = new byte[1024];
            while (!compresser.finished())
            {
                int i = compresser.deflate(buf);
                bos.write(buf, 0, i);
            }
            output = bos.toByteArray();
        } catch (Exception e)
        {
            output = data;
            LOG.error(e, e);
        } finally
        {
            try
            {
                bos.close();
            } catch (IOException e)
            {
                LOG.error(e, e);
            }
        }
        compresser.end();

        return output;
    }

    /**
     * 执行压缩.
     *
     * @param data 待压缩数据
     * @param os 输出流
     */
    public static void compress(byte[] data, OutputStream os)
    {
        DeflaterOutputStream dos = new DeflaterOutputStream(os);

        try
        {
            dos.write(data, 0, data.length);
            dos.finish();
            dos.flush();
        } catch (IOException e)
        {
            LOG.error(e, e);
        }
    }

    /**
     * 执行解压.
     *
     * @param data 待压缩的数据
     * @return 解压后的数据
     */
    public static byte[] decompress(byte[] data)
    {
        byte[] output = new byte[0];

        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);

        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
        try
        {
            byte[] buf = new byte[1024];
            while (!decompresser.finished())
            {
                int i = decompresser.inflate(buf);
                o.write(buf, 0, i);
            }
            output = o.toByteArray();
        } catch (DataFormatException e)
        {
            output = data;
            LOG.error(e, e);
        } finally
        {
            try
            {
                o.close();
            } catch (IOException e)
            {
                LOG.error(e, e);
            }
        }
        decompresser.end();
        
        return output;
    }

    /**
     * 执行解压.
     *
     * @param is 输入流
     * @return 解压后的数据
     */
    public static byte[] decompress(InputStream is)
    {
        InflaterInputStream iis = new InflaterInputStream(is);
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        try
        {
            int i = 1024;
            byte[] buf = new byte[i];

            while ((i = iis.read(buf, 0, i)) > 0)
            {
                os.write(buf, 0, i);
            }
        } catch (IOException e)
        {
            LOG.error(e, e);
        }
        
        return os.toByteArray();
    }
    
}
