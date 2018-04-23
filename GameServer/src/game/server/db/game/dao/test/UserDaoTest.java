/**
 * @date 2014/4/14
 * @author ChenLong
 */
package game.server.db.game.dao.test;

import game.core.util.UUIDUtils;
import game.server.db.game.bean.UserBean;
import game.server.db.game.bean.UserRoleBean;
import game.server.db.game.dao.UserDao;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author ChenLong
 */
public class UserDaoTest
{
    public static void testUserInsert()
    {
        UserBean userBean = new UserBean();
        userBean.setUserName("Chen龙");

        for (int i = 0; i < 10; ++i)
        {
            userBean.setUserId(UUIDUtils.toCompactString(UUID.randomUUID()));

            UserDao.insert(userBean);
        }
    }

    public static void testUserSelect()
    {
        UserBean userBean = UserDao.selectByUserName("chen龙");
        System.out.println(userBean.getUserName() + "\t" + userBean.getUserId());
        UUID roleId = UUIDUtils.fromCompactString(userBean.getUserId());
        System.out.println(roleId);
    }

    public static void testUserSelectJoin()
    {
        UserRoleBean userRoleBean = UserDao.selectJoinByName("35898");
        System.out.println("userRoleBean:\n" + userRoleBean.toString());
    }

    public static void testUserInsertBatch()
    {
        List<List<UserBean>> list = new LinkedList<>();
        for (int i = 0; i < 100; ++i)
        {
            List<UserBean> ulist = new LinkedList<>();
            list.add(ulist);
            for (int j = 0; j < 1000; ++j)
            {
                UserBean userBean = new UserBean();
                userBean.setUserName("Chen龙" + j);
                userBean.setUserId(UUIDUtils.toCompactString(UUID.randomUUID()));
                ulist.add(userBean);
            }
        }
        System.out.println("Start Insert");
        long start = System.currentTimeMillis();
        UserDao.insertListBatch(list);
        long end = System.currentTimeMillis();
        System.out.println("Complete!\n" + start + "\t" + end + "\t" + (end - start));
    }
}
