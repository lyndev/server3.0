/**
 * @date 2014/9/2
 * @author ChenLong
 */
package chenlong.gmtesttool;

import chenlong.gmtesttool.cli.MainCli;
import chenlong.gmtesttool.gui.MainFrame;

/**
 *
 * @author ChenLong
 */
public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].trim().toLowerCase().equals("-cli")) {
            MainCli.main(args);
        } else {
            MainFrame.main(args);
        }
    }
}
