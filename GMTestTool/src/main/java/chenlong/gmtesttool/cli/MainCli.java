/**
 * @date 2014/9/2
 * @author ChenLong
 */
package chenlong.gmtesttool.cli;

import chenlong.gmtesttool.function.client.INotify;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class MainCli implements INotify {

    private final static Logger logger = Logger.getLogger(MainCli.class);

    public static void main(String[] args) {
        if (args.length == 3) {
            String add = args[1];
            String[] tokens = add.split(":");
            String jsonStr = args[2];
            
        } else {
            usage();
        }
    }

    private static void usage() {
        System.out.println("usage: xxx.jar -cli ip:port jsonStr");
    }

    @Override
    public void connected() {
        logger.info("已连接");
    }

    @Override
    public void disconnected() {
        logger.info("断开连接");
    }

    @Override
    public void received(String jsonStr) {
        System.out.println("返回:\n" + jsonStr);
    }
}
