
import game.core.exception.UninitializedException;
import game.core.script.ScriptManager;
import game.server.logic.util.ScriptArgs;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import javax.script.ScriptException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class Test
{
    // 脚本测试
    public void testScript()
    {
        try
        {
            Map<Integer, String> scriptNames = new HashMap<>();
            scriptNames.put(1001, "script.test.TestScript1");
            ScriptManager.getInstance().initialize("scripts/java", "scriptBin", scriptNames, "scripts/js");
            {
                for (int i = 0; i < 100; ++i)
                {
                    ScriptArgs args = new ScriptArgs();
                    args.put(ScriptArgs.Key.FUNCTION_NAME, "loginSuccess");
                    ScriptManager.getInstance().call(0, args);
                    if (i % 5 == 0)
                    {
                        ScriptManager.getInstance().callJS("evalFile", "item/item.js");
                        ScriptManager.getInstance().callJS("evalFile", "login/loginSuccess.js");
                    }
                    try
                    {
                        Thread.sleep(1 * 1000);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        }
        catch (FileNotFoundException | ScriptException | UninitializedException ex)
        {
            ex.printStackTrace();
        }
    }
}
