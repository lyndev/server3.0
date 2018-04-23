/**
 * @date 2014/4/14
 * @author ChenLong
 */
package game.server.db.game.dao.test;

import game.core.util.UUIDUtils;
import game.server.db.game.bean.RoleBean;
import game.server.db.game.dao.RoleDao;
import java.util.UUID;

/**
 *
 * @author ChenLong
 */
public class RoleDaoTest
{
    public static void testRoleInsert()
    {
        RoleBean roleBean = new RoleBean();
        roleBean.setRoleId(UUIDUtils.toCompactString(UUID.randomUUID()));
        roleBean.setUserId(UUIDUtils.toCompactString(UUID.randomUUID()));
        roleBean.setRoleName("Chen楼兰之地");
        roleBean.setPlatformId(200);
        roleBean.setServerId(500);
        roleBean.setMiscData("askdjnskdjncsdkjncskacnsakcnsdkjnkjnk23erfjerjejeorgjeorgjerogjnvkdnvdkfvj");

        RoleDao.insert(roleBean);
    }

    public static void testRoleSelectByRoleName()
    {
        RoleBean roleBean = RoleDao.selectByRoleName("Chen楼兰之地");
        System.out.println(roleBean.getRoleId() + "\t" + roleBean.getRoleName());
    }

    public static void testRoleSelectByRoleId()
    {
        UUID roleId = UUIDUtils.fromCompactString("873bbfe45b0540ed80ef5ad5908edc0d");
        RoleBean roleBean = RoleDao.selectByRoleId(roleId);
        System.out.println(roleBean.getRoleId() + "\t" + roleBean.getRoleName());
    }
}
