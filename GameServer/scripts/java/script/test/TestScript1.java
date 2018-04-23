/**
 * @date 2014/5/12
 * @author ChenLong
 */
package script.test;

import game.core.script.IScript;
import org.apache.log4j.Logger;

/**
 * 脚本测试1
 *
 * @author ChenLong
 */
public class TestScript1 implements IScript
{
    private Logger log = Logger.getLogger(TestScript1.class);

    @Override
    public int getId()
    {
        return 1000;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        log.info("call scriptId = " + scriptId + "arg = " + arg.toString());
        return null;
    }
}
