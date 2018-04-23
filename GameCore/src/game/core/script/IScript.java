/**
 * @date 2014/5.12
 * @author ChenLong
 */

package game.core.script;

/**
 * java脚本接口
 * @author ChenLong
 */
public interface IScript
{
    /**
     * 获取scriptId
     * @return 
     */
    int getId();
    
    /**
     * 调用脚本
     * @param scriptId
     * @param arg 参数
     * @return 
     */
    Object call(int scriptId, Object arg);
}
