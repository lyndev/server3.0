/**
 * @date 2014/7/8
 * @author ChenLong
 */
package game.server.db.game.bean;

import java.util.List;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

/**
 * 玩家回存数据Bean
 *
 * @author ChenLong
 */
public class RoleUpdateBean
{
    private static final Logger logger = Logger.getLogger(RoleUpdateBean.class);

    private UserBean userBean;
    private RoleBean roleBean;
    private RankBean rankBean;
    private List<MailBean> mailList;
    private boolean hasLog;

    public RoleUpdateBean(UserBean userBean, RoleBean roleBean, RankBean rankBean, List<MailBean> mailList)
    {
        this(userBean, roleBean, rankBean, mailList, false);
    }

    public RoleUpdateBean(UserBean userBean, RoleBean roleBean, RankBean rankBean, List<MailBean> mailList, boolean hasLog)
    {
        this.userBean = userBean;
        this.roleBean = roleBean;
        this.rankBean = rankBean;
        this.mailList = mailList;
        this.hasLog = hasLog;
    }

    public void assign(RoleUpdateBean newBean)
    {
        if (this != newBean)
        {
            this.userBean = newBean.getUserBean();
            this.roleBean = newBean.getRoleBean();
            this.rankBean = newBean.getRankBean();
            //this
            this.hasLog = newBean.isHasLog();
        }
        else
        {
            logger.error("should not this situation, bean: " + this.toString());
        }
    }

    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public UserBean getUserBean()
    {
        return userBean;
    }

    public RoleBean getRoleBean()
    {
        return roleBean;
    }

    public RankBean getRankBean()
    {
        return rankBean;
    }

    public List<MailBean> getMailList()
    {
        return mailList;
    }

    public void setMailList(List<MailBean> mailList)
    {
        this.mailList = mailList;
    }

    public boolean isHasLog()
    {
        return hasLog;
    }
}
