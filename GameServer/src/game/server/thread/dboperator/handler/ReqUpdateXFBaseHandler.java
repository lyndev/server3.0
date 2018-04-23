/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.server.thread.dboperator.handler;

import game.server.db.game.bean.XFBaseBean;
import game.server.db.game.dao.XFBaseDao;

/**
 *
 * @author YangHanzhou
 */
public class ReqUpdateXFBaseHandler extends DBOperatorHandler
{
    private final XFBaseBean xfBaseBean;
    private final boolean hasLog;
    public ReqUpdateXFBaseHandler(XFBaseBean xfBaseBean, boolean hasLog)
    {
        super(0);
        this.xfBaseBean = xfBaseBean;
        this.hasLog = hasLog;
    }
    
    /**
     * 判断请求是否可以被合并
     * @param handler
     * @return
     */
    public boolean canCombine(ReqUpdateXFBaseHandler handler)
    {
        return xfBaseBean.getBuildId().equals(handler.xfBaseBean.getBuildId());
    }

    @Override
    public void action()
    {
        if (hasLog)
        {
            XFBaseDao.updateWithLog(xfBaseBean);
        }
        else
        {
            XFBaseDao.updateNoLog(xfBaseBean);
        }
    }
    
}
