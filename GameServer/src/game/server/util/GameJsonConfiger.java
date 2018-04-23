/**
 * @date 2014/5/22
 * @author ChenLong
 */
package game.server.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.core.util.file.TextFile;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * 读取配置GameJsonConfig
 *
 * @author ChenLong
 */
public class GameJsonConfiger
{
    private static final Logger log = Logger.getLogger(GameJsonConfiger.class);
    private static final GameJsonConfiger instance;
    private final String filePath = "config/gameJsonConfig.txt";
    private volatile JSONObject jsonObj;

    static
    {
        instance = new GameJsonConfiger();
    }

    public static GameJsonConfiger getInstance()
    {
        return instance;
    }

    public JSON getJsonObject(String key) // TODO 改为RW锁
    {
        return jsonObj.getJSONObject(key);
    }

    private GameJsonConfiger()
    {
        try
        {
            loadJsonFile(filePath);
        }
        catch (IOException ex)
        {
            log.error("IOException", ex);
        }
    }

    public final void loadJsonFile(String jsonPath) throws IOException
    {
        log.info("load Json file: [" + jsonPath + "]");
        String jsonContent = TextFile.read(jsonPath);
        JSON json = JSON.parseObject(jsonContent);
        if (json instanceof JSONObject)
        {
            jsonObj = (JSONObject) json;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    public void reloadJsonFile()
    {
        try
        {
            loadJsonFile(filePath);
        }
        catch (IOException ex)
        {
            log.error("IOException", ex);
        }
    }
}
