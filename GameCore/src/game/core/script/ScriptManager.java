/**
 * @date 2014/5/12
 * @author ChenLong
 */
package game.core.script;

import game.core.exception.ReinitializedException;
import game.core.exception.UninitializedException;
import java.io.FileNotFoundException;
import java.util.Map;
import javax.script.ScriptException;
import org.apache.log4j.Logger;

/**
 * 脚本管理类 同时支持调用java/class脚本和javascript脚本
 *
 * @author ChenLong
 */
public class ScriptManager
{
    private final Logger log = Logger.getLogger(ScriptManager.class);
    private boolean hasInitialized = false;

    private ScriptManager()
    {
    }

    /**
     * 初始化脚本管理器
     *
     * @param javaPath java脚本所在目录
     * @param classPath java脚本编译后class所在目录
     * @param scriptNames 需要加载的脚本集合
     * @param jsPath js脚本所在目录
     * @throws FileNotFoundException
     * @throws ScriptException
     */
    public synchronized void initialize(String javaPath, String classPath, Map<Integer, String> scriptNames, String jsPath) throws FileNotFoundException, ScriptException
    {
        if (hasInitialized)
            throw new ReinitializedException("reinitialize ScriptManager");
        { // init and load java/class script
            ScriptJavaLoader.getInstance().initialize(javaPath, classPath);
            ScriptJavaLoader.getInstance().loadScript(scriptNames);
        }
        { // init JavaScript script
            JSLoader.getInstance().initialize(jsPath);
        }
        hasInitialized = true;
    }

    /**
     * 调用脚本
     *
     * @param scriptId 脚本Id
     * @param arg 参数
     * @return
     */
    public Object call(int scriptId, Object arg)
    {
        try
        {
            return ScriptJavaLoader.getInstance().call(scriptId, arg);
        }
        catch (Throwable ex)
        {
            log.error("call script error! scriptId = " + scriptId, ex);
            return null;
        }
    }

    /**
     * 调用JS脚本
     *
     * @param function 函数名
     * @param arg 参数
     * @return
     */
    public Object callJS(String function, Object arg)
    {
        try
        {
            return JSLoader.getInstance().call(function, arg);
        }
        catch (UninitializedException | ScriptException | NoSuchMethodException ex)
        {
            log.error("Call JS script error! function = " + function, ex);
            return null;
        }
    }

    public static ScriptManager getInstance()
    {
        return Singleton.INSTANCE.getManager();
    }

    private enum Singleton
    {
        INSTANCE;

        ScriptManager manager;

        Singleton()
        {
            this.manager = new ScriptManager();
        }

        ScriptManager getManager()
        {
            return manager;
        }
    }
}
