package game.server.gm;

import com.alibaba.fastjson.JSONObject;
import game.core.net.communication.CommunicationS;
import game.core.net.server.IServer;
import game.core.util.SessionUtils;
import game.server.gm.codec.GMProtocolCodecFactory;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;

/**
 *
 * <b>GM指令的服务端程序.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class GMCommandServer implements IServer
{
    
    private final Logger log = Logger.getLogger(GMCommandServer.class);
    
    private final CommunicationS server;
    
    private static final String AES_KEY = "bcdaajjsgaaaabcd"; // AES对称密钥

    public GMCommandServer()
    {
        server = new CommunicationS(this, 100);
    }
    
    public CommunicationS getServer()
    {
        return server;
    }
    
    @Override
    public void doCommand(IoSession session, IoBuffer buf)
    {
        try
        {
            log.info("GMCommand from sessionId = " + session.getId() + "\t" + buf.toString());
            buf.order(ByteOrder.LITTLE_ENDIAN);
            // 读取加密标识
            short secret = buf.getShort();
//            System.out.println("secret=" + secret);
            // 读取内容长度（没用，逗比）
            short dataLen = buf.getShort();
//            System.out.println("dataLen=" + dataLen);
            // 读取消息内容
            byte[] msgData = new byte[buf.remaining()];
            buf.get(msgData);

            // 解密消息内容
            JSONObject json;
            if (secret == 1767)
            {
//                System.out.println("解密前的json：" + new String(msgData));
                long startTime = System.currentTimeMillis();
                String jsonData = decrypt(msgData, AES_KEY);
                long endTime = System.currentTimeMillis();
//                System.out.println("解密后的json：" + jsonData);
//                System.out.println("解密耗时：" + (endTime - startTime) + "ms");
                if (endTime - startTime > 100)
                {
                    log.error("解密GM指令耗时较长：" + (endTime - startTime) + "ms，jsonData：" + jsonData);
                }
                if (jsonData == null)
                {
                    log.error("GM指令解密失败！");
                    return;
                }
                json = JSONObject.parseObject(jsonData);
            }
            else
            {
                json = JSONObject.parseObject(new String(msgData, Charset.forName("UTF-8")));
            }

            // 创建消息对象
            GMCommandMessage message = new GMCommandMessage();
            message.setSecret(secret);
            message.setSession(session);
            message.setSerial(json.getLong("serial"));
            message.setCommand(json.getIntValue("command"));
            message.setData(json.getJSONObject("data"));

            // 交给handler处理消息
            GMCommandHandler handler = new GMCommandHandler();
            handler.setAttribute("gmCmd", message);
            handler.action();
        }
        catch (Exception e)
        {
            log.error("error", e);
        }
    }
    
    @Override
    public void sessionCreate(IoSession session)
    {
        log.info("sessionCreate sessionId = " + session.getId());
    }
    
    @Override
    public void sessionClosed(IoSession session)
    {
        log.info("sessionClosed sessionId = " + session.getId());
    }
    
    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
    {
        SessionUtils.closeSession(session, "回收空闲连接！");
    }
    
    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
    {
        session.close(true);
        log.error(session + " close exceptionCaught. cause: " + cause.getMessage());
    }
    
    @Override
    public ProtocolCodecFactory getCodecFactory()
    {
        return new GMProtocolCodecFactory(null, null, null);
    }
    
    private String decrypt(byte[] data, String key)
    {
        try
        {
            // 判断Key是否为16位
            if (key.length() != 16)
            {
                log.error("Key is not 16 bit.");
                return null;
            }
            
            byte[] raw = key.getBytes();
            // base64解码
            byte[] encrypted = Base64.decodeBase64(data);
            // 获取随机IV公密
            byte[] ivByte = Arrays.copyOfRange(encrypted, 0, 16);
            // 通过AES和key得到SecretKeySpec
            SecretKeySpec skp = new SecretKeySpec(raw, "AES");
            // 算法/模式/补码方式 NoPadding默认补码为0
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            // 生成IV
            IvParameterSpec iv = new IvParameterSpec(ivByte);
            // 设置IV及解密模式
            cipher.init(Cipher.DECRYPT_MODE, skp, iv);

            // 获取加密数据
            byte[] original = cipher.doFinal(Arrays.copyOfRange(encrypted, 16, encrypted.length));
            String originalString = new String(original);
            // 去除补码为0的空数据
            return originalString.trim();
        }
        catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException ex)
        {
            log.error("Data decrypt error! cause: ", ex);
        }
        
        return null;
    }
    
}
