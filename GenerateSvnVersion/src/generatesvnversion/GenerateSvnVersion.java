/**
 * @date 2014/9/23
 * @author ChenLong
 */
package generatesvnversion;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 *
 * @author ChenLong
 */
public class GenerateSvnVersion {

    public static void usage() {
        System.out.println("usage:\n\tGenerateSvnVersion directory");
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            usage();
            System.exit(1);
        }
        String dir = args[0];
        String exec = "svnversion -n " + dir;
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(exec);
        InputStream in = proc.getInputStream();
        InputStreamReader isr = new InputStreamReader(in, Charset.forName("UTF-8"));
        StringBuilder sb = new StringBuilder();
        while (true) {
            int c = isr.read();
            if (c != -1) {
                sb.append(Character.toChars(c));
            } else {
                break;
            }
        }
        System.out.println(sb.toString());
    }
}
