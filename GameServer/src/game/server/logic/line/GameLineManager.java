/**
 * @date 2014/4/28
 * @author ChenLong
 */
package game.server.logic.line;

import game.core.command.ICommand;
import game.core.timer.TimerEvent;
import game.core.timer.SimpleTimerProcessor;
import game.server.GameServer;
import game.server.logic.line.handler.AcrossDayTickHandler;
import game.server.logic.line.handler.GameLineTickHandler;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class GameLineManager
{
    private final Logger log = Logger.getLogger(GameLineManager.class);
    private final Map<Integer, GameLine> idGameLines = new ConcurrentHashMap<>(); // lindId --> GameLine
    private int lineNum = 0;

    private GameLineManager()
    {
        initGameLine();
        addGameLineTickTimer();
    }

    public void init()
    {

    }

    public void addCommand(int lineId, ICommand command)
    {
        GameLine gameLine = idGameLines.get(lineId);
        if (gameLine != null)
        {
            gameLine.addCommand(command);
        }
        else
        {
            log.error("cannot find GameLine, lineId = " + lineId);
        }
    }

    /**
     * 随机选线
     *
     * @return
     */
    public GameLine randomLine()
    {
        int randomLineId = RandomUtils.nextInt() % idGameLines.size();
        return idGameLines.get(randomLineId);
    }

    /**
     * 随机选线
     *
     * @param str
     * @return
     */
    public GameLine chooseLineByUserName(String str)
    {
        int randomLineId = Math.abs(str.hashCode()) % idGameLines.size();
        return idGameLines.get(randomLineId);
    }

    /**
     * 随机选线
     *
     * @param str 角色唯一服务器表示
     * @return
     */
    public GameLine chooseLineBySoleid(String str)
    {
        int randomLineId = Math.abs(str.hashCode()) % idGameLines.size();
        return idGameLines.get(randomLineId);
    }
    
    public GameLine getLine(int lineId)
    {
        return idGameLines.get(lineId);
    }

    /**
     * 停止并等待停止
     */
    public void stopAndAwaitStop()
    {
        saveAllLinePlayerCommand(); // 请求各线回存玩家数据
        stop();
        awaitStop();
    }

    /**
     * 返回线数量
     *
     * @return
     */
    public int getLineNum()
    {
        return lineNum;
    }

    /**
     * 根据CPU核心数创建线
     */
    private void initGameLine()
    {
        lineNum = Runtime.getRuntime().availableProcessors(); // 线数量 = CPU核心数
        log.info("create " + lineNum + " GameLine");
        for (int i = 0; i < lineNum; ++i)
        {
            idGameLines.put(i, new GameLine(i));
        }
    }

    /**
     * 注册检查Detach(分离)玩家定时器
     */
    private void addGameLineTickTimer()
    {
        SimpleTimerProcessor.getInstance().addEvent(new GameLineTick());
    }

    /**
     * 请求各线回存玩家数据
     */
    public void saveAllLinePlayerCommand()
    {
        List<GameLine> lines = new ArrayList<>(lineNum);
        lines.addAll(idGameLines.values());
        for (GameLine gameLine : lines)
        {
            gameLine.saveAllPlayerCommand();
        }
    }

    public void tick5Command()
    {
        List<GameLine> lines = new ArrayList<>(lineNum);
        lines.addAll(idGameLines.values());
        for (GameLine gameLine : lines)
        {
            gameLine.tick5Command();
        }
    }

    private void stop()
    {
        List<GameLine> lines = new ArrayList<>(lineNum);
        lines.addAll(idGameLines.values());
        for (GameLine gameLine : lines)
        {
            gameLine.stop(false);
        }
    }

    private void awaitStop()
    {
        List<GameLine> lines = new ArrayList<>(lineNum);
        lines.addAll(idGameLines.values());
        for (GameLine gameLine : lines)
        {
            gameLine.awaitStop();
        }
    }

    public class GameLineTick extends TimerEvent
    {
        public GameLineTick()
        {
            super(1000, 1000, true); // 间隔1秒
        }

        @Override
        public void run()
        {
            {
                int timeStamp = (int) (System.currentTimeMillis() / 1000L);
//                if (timeStamp % 30 == 0)
//                    log.info("GameLineTick alive, timeStamp = " + timeStamp);
            }
            checkAcrossDay();
            checkPerTick();
        }
    }

    private void checkPerTick()
    {
        for (Entry<Integer, GameLine> entry : idGameLines.entrySet())
        {
            int lineId = entry.getKey();
            GameLine gameLine = entry.getValue();
            addCommand(lineId, new GameLineTickHandler(gameLine));
        }
    }

    private void checkAcrossDay()
    {
        long lastAcrossDayTime = GameServer.getInstance().getLastAcrossDayTime();
        if (!DateUtils.isSameDay(new Date(lastAcrossDayTime), new Date()))
        {
            log.info("AcrossDay, " + DateFormatUtils.format(new Date(), DateFormatUtils.ISO_DATETIME_FORMAT.getPattern()));
            for (Entry<Integer, GameLine> entry : idGameLines.entrySet())
            {
                int lineId = entry.getKey();
                GameLine gameLine = entry.getValue();
                addCommand(lineId, new AcrossDayTickHandler(gameLine));
            }
            GameServer.getInstance().setLastAcrossDayTime(System.currentTimeMillis());
        }
    }

    public void flushAllDetachPlayer()
    {
        List<GameLine> lines = new ArrayList<>();
        lines.addAll(idGameLines.values());
        for (GameLine line : lines)
        {
            line.flushAllDetachPlayerCommand();
        }
    }

    public void setPlayerSaveDelay(long playerSaveDelay)
    {
        List<GameLine> lines = new ArrayList<>();
        lines.addAll(idGameLines.values());
        for (GameLine line : lines)
        {
            line.setPlayerSaveDelayCommand(playerSaveDelay);
        }
    }

    public void setFlushDetachPlayerWaterLevel(long flushDetachPlayerWaterLevel)
    {
        List<GameLine> lines = new ArrayList<>();
        lines.addAll(idGameLines.values());
        for (GameLine line : lines)
        {
            line.setFlushDetachPlayerWaterLevelCommand(flushDetachPlayerWaterLevel);
        }
    }

    public void setFlushDetachPlayerWaterLevelDelay(long flushDetachPlayerWaterLevelDelay)
    {
        List<GameLine> lines = new ArrayList<>();
        lines.addAll(idGameLines.values());
        for (GameLine line : lines)
        {
            line.setFlushDetachPlayerWaterLevelDelayCommand(flushDetachPlayerWaterLevelDelay);
        }
    }

    public void setFlushDetachPlayerDelay(long flushDetachPlayerDelay)
    {
        List<GameLine> lines = new ArrayList<>();
        lines.addAll(idGameLines.values());
        for (GameLine line : lines)
        {
            line.setFlushDetachPlayerDelayCommand(flushDetachPlayerDelay);
        }
    }
    
    public void buyAllPlayerMonthCard()
    {
        List<GameLine> lines = new ArrayList<>();
        lines.addAll(idGameLines.values());
        for (GameLine line : lines)
        {
            line.buyAllPlayerMonthCardCommand();
        }
    }

    public static GameLineManager getInstance()
    {
        return GameLineManager.Singleton.INSTANCE.getManager();
    }

    private enum Singleton
    {

        INSTANCE;

        GameLineManager manager;

        Singleton()
        {
            this.manager = new GameLineManager();
        }

        GameLineManager getManager()
        {
            return manager;
        }
    }
}
