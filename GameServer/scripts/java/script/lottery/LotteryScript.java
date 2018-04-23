package script.lottery;

import game.core.script.IScript;
import org.apache.log4j.Logger;



/**
 *
 * @author YangHanzhou
 */
public class LotteryScript implements IScript
{

    private final Logger log = Logger.getLogger(LotteryScript.class);
    @Override
    public int getId()
    {
        return 1004;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        return null;
      
    }
    
}
