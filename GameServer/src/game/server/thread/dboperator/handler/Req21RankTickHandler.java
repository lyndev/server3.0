package game.server.thread.dboperator.handler;

import game.server.db.game.dao.RankDao;

/**
 *
 * @author Administrator
 */
public class Req21RankTickHandler extends DBOperatorHandler
{

    public Req21RankTickHandler()
    {
        super(0);
    }

    @Override
    public void action()
    {
        RankDao.selectAchievementRank();
        RankDao.selectFunMission1Rank();
        RankDao.selectFunMission2Rank();
    }

}
