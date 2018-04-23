/**
 * @date 2014/7/1
 * @author ChenLong
 */
package game.testrobot.robot;

import game.core.message.SMessage;
import game.message.LoginMessage;
//import game.message.LoginMessage.PingPong;
import game.message.LoginMessage.ResCreateRoleFailed;
import game.message.LoginMessage.ResLoginFailed;
import game.message.LoginMessage.ResLoginSuccess;
import game.message.LoginMessage.Role;
import game.testrobot.TestClient;
import game.testrobot.utils.MessageUtils;
import game.testrobot.utils.UniqueId;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author ChenLong
 */
public class RobotPlayer
{
    private final static Logger logger = Logger.getLogger(RobotPlayer.class);

    private transient int lineId;

    private RobotStatus status = RobotStatus.NOT_CONNECT;
    private long userId;
    private long roleId;
    private String userName;
    private String roleName;
    private int roleLevel;
    private int roleHead;
    private byte vipLevel;
    private int platformId;
    private int serverId;
    private String data;
    //private Token token;
    private IoSession session;
    private long lastConnectTime; // 上次连接/断线时间
    private long createRoleTime; // 角色创建时间
    private int roleExp;
    private int gmLevel;        // gm等级

    public void tick()
    {
        runStateMachine();
    }

    public void runStateMachine()
    {
        switch (status)
        {
            case NOT_CONNECT:
                break;
            case CONNECTED:
                //login();
                break;
            case LOGINING:
                break;
            case LOGIN_SUCCESS:
                //pingPong();
                //pingPong();
                break;
            case PINGPONG:
                break;
            default:

        }
    }

    public void login()
    {
        LoginMessage.ReqLogin.Builder reqMsg = LoginMessage.ReqLogin.newBuilder();
        reqMsg.setPlaformId(567);
        reqMsg.setServerId(731);
        reqMsg.setLoginType(2);
        reqMsg.setUserName(userName);
        reqMsg.setSign("");

        MessageUtils.send(session, new SMessage(LoginMessage.ReqLogin.MsgID.eMsgID_VALUE, reqMsg.build().toByteArray()));

        setStatus(RobotStatus.LOGINING);
    }

    public void loginFailed(ResLoginFailed resMsg)
    {
        setStatus(RobotStatus.LOGIN_FAILED);
        if (resMsg.getReason() == 2)
        {
            logger.info("login res not role, userName: [" + userName + "], create role");

            LoginMessage.ReqCreateRole.Builder reqMsg = LoginMessage.ReqCreateRole.newBuilder();
            reqMsg.setRoleName(userName);
            reqMsg.setFedId("TestRobotFGI");

            MessageUtils.send(session, new SMessage(LoginMessage.ReqCreateRole.MsgID.eMsgID_VALUE, reqMsg.build().toByteArray()));
        }
        else
        {
            logger.error("loginFailed, reason = " + resMsg.getReason());
        }
    }

    public void loginSuccess(ResLoginSuccess resMsg)
    {
        setStatus(RobotStatus.LOGIN_SUCCESS);
        Role role = resMsg.getRole();
        logger.info("loginSuccess, userName: [" + userName + "], userId: [" + role.getUserId() + "], "
                + "roleName: [" + role.getRoleName() + "], roleId: [" + role.getRoleId() + "]");

        this.userId = UniqueId.toBase10(role.getUserId());
        this.roleName = role.getRoleName();
        this.roleId = UniqueId.toBase10(role.getRoleId());
        this.roleLevel = role.getRoleLevel();

        long send = System.currentTimeMillis();
        System.out.println("ccccccccccccccc " + roleName + " time " + (send - TestClient.getInstance().getStart()));
        //TestClient.getInstance().unlock();
        TestClient.getInstance().decCount();
    }

    public void createRoleFailed(ResCreateRoleFailed resMsg)
    {
        logger.error("createRoleFailed, reason = " + resMsg.getReason());
    }

    public void pingPong()
    {
        //setStatus(RobotStatus.PINGPONG);
        reqPingPongIncrement();
    }

    public void reqPingPongIncrement()
    {
        //LoginMessage.PingPong.Builder reqMsg = LoginMessage.PingPong.newBuilder();
        //reqMsg.setValue(1);
       // MessageUtils.send(session, new SMessage(LoginMessage.PingPong.MsgID.eMsgID_VALUE, reqMsg.build().toByteArray()));
    }

    //public void resPingPongIncrement(PingPong resMsg)
    //{
//        try
//        {
//            Thread.sleep(10);
//        }
//        catch (InterruptedException ex)
//        {
//        }
        //LoginMessage.PingPong.Builder reqMsg = LoginMessage.PingPong.newBuilder();
        //reqMsg.setValue(resMsg.getValue() + 1);
        //MessageUtils.send(session, new SMessage(LoginMessage.PingPong.MsgID.eMsgID_VALUE, reqMsg.build().toByteArray()));
    //}

    public RobotStatus getStatus()
    {
        return status;
    }

    public void setStatus(RobotStatus status)
    {
        this.status = status;
    }

    public int getLineId()
    {
        return lineId;
    }

    public void setLineId(int lineId)
    {
        this.lineId = lineId;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(long roleId)
    {
        this.roleId = roleId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    public int getRoleLevel()
    {
        return roleLevel;
    }

    public void setRoleLevel(int roleLevel)
    {
        this.roleLevel = roleLevel;
    }

    public int getRoleHead()
    {
        return roleHead;
    }

    public void setRoleHead(int roleHead)
    {
        this.roleHead = roleHead;
    }

    public byte getVipLevel()
    {
        return vipLevel;
    }

    public void setVipLevel(byte vipLevel)
    {
        this.vipLevel = vipLevel;
    }

    public int getPlatformId()
    {
        return platformId;
    }

    public void setPlatformId(int platformId)
    {
        this.platformId = platformId;
    }

    public int getServerId()
    {
        return serverId;
    }

    public void setServerId(int serverId)
    {
        this.serverId = serverId;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public IoSession getSession()
    {
        return session;
    }

    public void setSession(IoSession session)
    {
        this.session = session;
    }

    public long getLastConnectTime()
    {
        return lastConnectTime;
    }

    public void setLastConnectTime(long lastConnectTime)
    {
        this.lastConnectTime = lastConnectTime;
    }

    public long getCreateRoleTime()
    {
        return createRoleTime;
    }

    public void setCreateRoleTime(long createRoleTime)
    {
        this.createRoleTime = createRoleTime;
    }

    public int getRoleExp()
    {
        return roleExp;
    }

    public void setRoleExp(int roleExp)
    {
        this.roleExp = roleExp;
    }

    public int getGmLevel()
    {
        return gmLevel;
    }

    public void setGmLevel(int gmLevel)
    {
        this.gmLevel = gmLevel;
    }

}
