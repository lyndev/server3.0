/**
 * @date 2014/7/23
 * @author ChenLong
 */
package game.keygenerator.util;

/**
 *
 * @author ChenLong
 */
public class MiscUtils
{
    /**
     * netbeans/eclipse等IDE下 运行/调试 关闭时IDE内部调用Process.destroy() <br />
     * 无法触发到JVM shutdown hook, 导致关闭时无法回存 <br />
     * 故在IDE环境下添加了system property: ideDebug
     *
     * @return
     */
    public static boolean isIDEEnvironment()
    {
        String val = System.getProperty("ideDebug");
        return val != null && val.equals("true");
    }
}
