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
public class ReqCreateXFBaseInsertHandler extends DBOperatorHandler
{
    private final XFBaseBean xfBaseBean;
    private final boolean hasLog;
    public ReqCreateXFBaseInsertHandler(XFBaseBean xfBaseBean, boolean hasLog)
    {
        super(0);
        this.xfBaseBean = xfBaseBean;
        this.hasLog = hasLog;
    }

    @Override
    public void action()
    {
        XFBaseDao.insert(xfBaseBean, hasLog);
    }
}
