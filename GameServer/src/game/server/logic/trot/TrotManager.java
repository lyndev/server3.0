
package game.server.logic.trot;

import game.core.timer.TimerEvent;
import game.core.timer.SimpleTimerProcessor;
import game.core.timer.TimerEventWrapper;
import game.server.logic.player.PlayerManager;
import game.server.logic.trot.bean.TrotInfo;
import game.server.logic.trot.bean.TrotType;
import game.server.logic.trot.filter.TrotFilter;
import org.apache.log4j.Logger;


/**
 * 
 * @date   2014-7-15
 * @author pengmian
 */
public class TrotManager 
{    
    
    private static final Logger logger = Logger.getLogger(TrotManager.class);

//    ////////////////////////////////////////////////////////////////////////////
//    // 后台执行n次的广播
//    private class TrotNormalTimerEvent extends TimerEvent
//    {
//        private final TrotInfo tInfo;   // 公告信息
//                
//        public TrotNormalTimerEvent(TrotInfo tInfo, long delay)
//        {
//            super(delay);
//            this.tInfo = tInfo;
//        }
//
//        @Override
//        public void run()
//        {
//            /// 无效时，不发送
//            if (tInfo.isValid())
//            {
//                logger.info("run to here!");
//                PlayerManager.boardcastNotify(tInfo.getContent(), tInfo.getFilter());                
////                if (tInfo.getCount().decrementAndGet() > 0)
////                {
////                    SimpleTimerProcessor.getInstance().addEvent(new TrotNormalTimerEvent(tInfo, tInfo.getInterval() * 1000));
////                }
//            }
//        }
//    }
    
    ////////////////////////////////////////////////////////////////////////////
    // 后台一直执行的公告
    private class TrotLoopTimerEvent extends TimerEvent
    {
        private final TrotInfo tInfo;   // 公告信息
        
        public TrotLoopTimerEvent(TrotInfo tInfo, long initialDelay, long delay, boolean loopFixed)
        {
            super(initialDelay, delay, loopFixed);
            this.tInfo = tInfo;
        }

        @Override
        public void run()
        {
            /// 无效时，不发送
            if (tInfo.isValid())
            {
                //logger.info("run to here!");
                PlayerManager.boardcastNotify(tInfo.getContent(), tInfo.getFilter());
            }
        }
    }

    private TrotManager()
    {

    }
     
    /**
     * 单件
     * @return
     */
    public static TrotManager getIntance()
    {
        return Singleton.INSTANCE.getTrotManager();
    }
    
    /**
     * 发送公告接口  
     * @param nInfo
     */
    public void notice(TrotInfo nInfo)
    {
        nInfo.setFilter(getFilter(nInfo.getType()));
        nInfo.getFilter().setParameter(nInfo.getParameter());
        if (nInfo.getCount() == -1)
        {
            SimpleTimerProcessor.getInstance().addEvent(new TrotLoopTimerEvent(nInfo, 
                    nInfo.getTime() == 0 ? 1000 : nInfo.getTime() * 1000,
                    nInfo.getInterval() == 0 ? 1000 : nInfo.getInterval() * 1000, 
                    false));
        }
        else
        {
            if (nInfo.getCount() > 0)
            {
                TimerEventWrapper eventWrapper = new TimerEventWrapper(
                        new TrotLoopTimerEvent(nInfo, nInfo.getTime() == 0 ? 1000 : nInfo.getTime() * 1000,
                                nInfo.getInterval() == 0 ? 1000 : nInfo.getInterval() * 1000,
                                false),
                        nInfo.getCount());

                eventWrapper.start();
            }
            else
            {
                logger.error("要求执行[" + nInfo.getCount() + "]次的公告：[" + nInfo.getContent() + "]");
            }
        }
    }

    private TrotFilter getFilter(TrotType type)
    {
        try
        {
            return type.getFilter();
        }
        catch (InstantiationException | IllegalAccessException ex)
        {
            logger.error("无法实例化过滤器：[" + type.name() + "]");
        }
        return null;
    }
    
    private enum Singleton
    {

        INSTANCE;

        TrotManager trotManager;

        Singleton()
        {
            this.trotManager = new TrotManager();
        }

        TrotManager getTrotManager()
        {
            return trotManager;
        }
    }
}
