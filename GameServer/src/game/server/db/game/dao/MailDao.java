package game.server.db.game.dao;

import game.server.db.DBFactory;
import game.server.db.game.bean.MailBean;
import java.util.List;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Administrator
 */
public class MailDao
{
    public static int insert(MailBean mail)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int rows = session.insert("mail.insert", mail);
            DBFactory.GAME_DB.getLogger().info("插入邮件：" + mail.getId());
            session.commit();
            return rows;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    public static int updateNoLog(MailBean mailBean)
    {
        return update(mailBean, false);
    }

    public static int updateWithLog(MailBean mailBean)
    {
        return update(mailBean, true);
    }

    public static int update(MailBean mailBean, boolean hasLog)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int rows = session.update("mail.update", mailBean);
            if (hasLog)
                DBFactory.GAME_DB.getLogger().info("回存邮件 Id: [" + mailBean.getId() + "], "
                        + "senderName: [" + mailBean.getSenderName() + "], " + "type: [" + mailBean.getType() + "], "
                        + "receiverId: [" + mailBean.getReceiverId() + "], " + "receiverName: [" + mailBean.getReceiverId() + "], "
                        + "accessory: [" + mailBean.getAccessory() + "], " + "isRead: [" + mailBean.getIsRead() + "], "
                        + "sendTime: [" + mailBean.getDeadLine() + "], " + "mailData: [" + mailBean.getMailData() + "]");

            session.commit();
            return rows;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    public static int deleteWithLog(String mailId)
    {
        return delete(mailId, true);
    }

    public static int deleteNoLog(String mailId)
    {
        return delete(mailId, false);
    }

    /**
     * 删除指定id的邮件
     *
     * @param id
     * @param hasLog
     * @return 失败返回0, 成功返回非0
     */
    public static int delete(String id, boolean hasLog)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            int rows = session.delete("mail.delete", id);
            if (hasLog)
                DBFactory.GAME_DB.getLogger().info("删除邮件 Id: [" + id + "]");

            session.commit();
            return rows;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return 0;
    }

    public static MailBean selectByMailId(String id)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            MailBean mailBean = (MailBean) session.selectOne("mail.selectByMailId", id);
            return mailBean;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

    public static List<MailBean> selectByRoleId(String receiverId)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            List<MailBean> mailList = session.selectList("mail.selectByRoleId", receiverId);
            return mailList;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

    public static List<MailBean> selectByRoleName(String recerverName)
    {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession())
        {
            List<MailBean> mailList = session.selectList("mail.selectByRoleName", recerverName);
            return mailList;
        }
        catch (Exception e)
        {
            DBFactory.GAME_DB.getLogger().error(e);
        }
        return null;
    }

}
