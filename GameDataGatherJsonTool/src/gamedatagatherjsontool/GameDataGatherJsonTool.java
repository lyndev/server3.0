/**
 * @date 2014/8/11
 * @author ChenLong
 */
package gamedatagatherjsontool;

import org.apache.commons.codec.binary.Base64;
import game.core.util.file.TextFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class GameDataGatherJsonTool {

    private final static Logger log = Logger.getLogger(GameDataGatherJsonTool.class);

    public static void usage() {
        System.out.println("usage:\n\tjava -jar GameDataGatherJsonTool.jar filePath");
        System.exit(1);
    }

    public static byte[] compress(byte[] data) {
        byte[] output = new byte[0];

        Deflater compresser = new Deflater();

        compresser.reset();
        compresser.setInput(data);
        compresser.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!compresser.finished()) {
                int i = compresser.deflate(buf);
                bos.write(buf, 0, i);
            }
            output = bos.toByteArray();
        } catch (Exception e) {
            output = data;
            log.error(e, e);
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                log.error(e, e);
            }
        }
        compresser.end();
        return output;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        if (args.length <= 0) {
            usage();
        }
        String plainJson = TextFile.read(args[0], "UTF-8");
        byte[] compressedJson = compress(plainJson.getBytes());
        byte[] codedJson = Base64.encodeBase64(compressedJson);
        String result = new String(codedJson, "UTF-8");
        
        System.out.println(result);
    }
}
