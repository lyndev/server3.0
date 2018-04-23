/**
 * @date 2014/5/19
 * @author ChenLong
 */
package game.server.thread.dboperator.handler;

import game.server.db.game.bean.MailBean;
import game.server.db.game.bean.RoleUpdateBean;
import game.server.db.game.dao.MailDao;
import game.server.db.game.dao.RankDao;
import game.server.db.game.dao.RoleDao;

/**
 * 请求更新玩家数据
 *
 * @author ChenLong
 */
public class ReqUpdateRoleHandler extends DBOperatorHandler
{
    private final RoleUpdateBean bean;

    public ReqUpdateRoleHandler(int lineNum, RoleUpdateBean bean)
    {
        super(lineNum);
        this.bean = bean;
    }

    public RoleUpdateBean getBean()
    {
        return bean;
    }

    /**
     * 判断请求是否可以被合并
     *
     * @param handler
     * @return
     */
    public boolean canCombine(ReqUpdateRoleHandler handler)
    {
        String aRoleId = bean.getRoleBean().getRoleId();
        String bRoleId = handler.getBean().getRoleBean().getRoleId();
        return aRoleId.equals(bRoleId);
    }
    
    public void combine(ReqUpdateRoleHandler newHandler)
    {
        this.bean.assign(newHandler.getBean());
    }

    @Override
    public void action()
    {
        RoleDao.update(bean.getRoleBean(), bean.isHasLog());
        RankDao.update(bean.getRankBean(), bean.isHasLog());
        for (MailBean mailBean : bean.getMailList())
        {
            if (mailBean.getDeleteFlag())
                MailDao.delete(mailBean.getId(), bean.isHasLog());
            else if (mailBean.getInsertFlag())
                MailDao.insert(mailBean);
            else if (mailBean.getModifyFlag())
                MailDao.update(mailBean, bean.isHasLog());
        }
    }
}
