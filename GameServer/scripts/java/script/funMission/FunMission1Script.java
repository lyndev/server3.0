package script.funMission;

import game.core.script.IScript;
import game.server.db.game.bean.RankBean;
import game.server.db.game.dao.RankDao;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class FunMission1Script implements IScript
{
    private static final Logger log = Logger.getLogger(FunMission1Script.class);
    
    @Override
    public int getId()
    {
        return 1009;
    }

    @Override
    public Object call(int scriptId, Object arg)
    {
        log.info("call scriptId = " + scriptId + "arg = " + arg.toString());
        fakeRank1();
        return null;
    }
    
    public void fakeRank1()
    {
        boolean useFake = true;
        if (RankDao.getFunMission1RankList() != null)
        {
            for (RankBean bean : RankDao.getFunMission1RankList())
            {
                if (bean.getFunMission1Score() != 0)
                {
                    useFake = false;
                    break;
                }
            }
        }
        
        if (useFake)
        {
            List<RankBean> fakeRank = new ArrayList<>(3);
            RankBean fakeData1 = new RankBean();
            fakeData1.setRoleLevel(1);
            fakeData1.setRoleName("王大锤");
            fakeData1.setRoleHead(1);
            fakeData1.setFunMission1Score(400);
            fakeData1.setFunMission1Rank(1);
            fakeRank.add(fakeData1);

            RankBean fakeData2 = new RankBean();
            fakeData2.setRoleLevel(1);
            fakeData2.setRoleName("王中锤");
            fakeData2.setRoleHead(2);
            fakeData2.setFunMission1Score(300);
            fakeData2.setFunMission1Rank(2);
            fakeRank.add(fakeData2);

            RankBean fakeData3 = new RankBean();
            fakeData3.setRoleLevel(1);
            fakeData3.setRoleName("王小锤");
            fakeData3.setRoleHead(3);
            fakeData3.setFunMission1Score(200);
            fakeData3.setFunMission1Rank(3);
            fakeRank.add(fakeData3);
            
            RankDao.setFunMission1Rank(fakeRank);
        }
    }
}
