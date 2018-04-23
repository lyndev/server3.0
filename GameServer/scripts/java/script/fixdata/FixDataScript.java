/**
 * @date 2014/7/29
 * @author ChenLong
 */
package script.fixdata;

import game.core.script.IScript;

/**
 * 数据修复脚本, 在更新前通过命令 java -cp GameServer.jar game.fixdata.Main 执行此脚本
 *
 * @author ChenLong
 */
public class FixDataScript implements IScript
{
    @Override
    public int getId()
    {
        return 9000;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        //System.out.println("in FixDataScript");
        return null;
    }
}
