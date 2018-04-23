/**
 * @date 2014/7/1
 * @author ChenLong
 */
package game.testrobot.line;

import game.core.command.ICommand;
import game.core.timer.TimerEvent;
import game.core.timer.SimpleTimerProcessor;
import game.testrobot.robot.RobotPlayer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class LineManager
{
    private final static Logger logger = Logger.getLogger(LineManager.class);
    private final static LineManager instance;
    private final Map<Integer, Line> lines = new ConcurrentHashMap<>(); // lindId --> GameLine
    private int lineNum = 0;

    static
    {
        instance = new LineManager();
    }

    private LineManager()
    {
        initLines();
        addLineManagerTick();
    }

    public static LineManager getInstance()
    {
        return instance;
    }

    public int randLine(String userName)
    {
        Validate.notNull(userName);
        return Math.abs(userName.hashCode()) % lineNum;
    }
    
    public Line getLine(int lineId)
    {
        return lines.get(lineId);
    }

    public void addCommand(int lineId, ICommand command)
    {
        Line line = lines.get(lineId);
        if (line != null)
        {
            line.addCommand(command);
        }
        else
        {
            logger.error("cannot find GameLine, lineId = " + lineId);
        }
    }

    private void initLines()
    {
        //lineNum = 2 * Runtime.getRuntime().availableProcessors(); // 线数量 = 2 * CPU核心数
        lineNum = Runtime.getRuntime().availableProcessors(); // 线数量 = CPU核心数
        logger.info("create " + lineNum + " Line");
        for (int i = 0; i < lineNum; ++i)
        {
            lines.put(i, new Line(i));
        }
    }

    private void addLineManagerTick()
    {
        SimpleTimerProcessor.getInstance().addEvent(new LineManagerTick());
    }

    private class LineManagerTick extends TimerEvent
    {
        public LineManagerTick()
        {
            super(250, 1 * 250, true); // 间隔1秒
        }

        @Override
        public void run()
        {
            for (Map.Entry<Integer, Line> entry : lines.entrySet())
            {
                Line line = entry.getValue();
                line.addCommand(new LineTick(line));
            }
        }
    }

    private class LineTick implements ICommand
    {
        private final Line line;

        public LineTick(Line line)
        {
            this.line = line;
        }

        @Override
        public void action()
        {
            line.tick();
        }
    }
}
