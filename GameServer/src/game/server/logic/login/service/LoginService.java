package game.server.logic.login.service;

import game.core.message.SMessage;
import game.core.script.ScriptManager;
import game.core.util.UUIDUtils;
import game.data.bean.q_globalBean;
import game.data.bean.q_npc_heroBean;
import game.message.LoginMessage;
import game.message.PlayerMessage;
import game.server.config.ServerConfig;
import game.server.db.game.bean.MailBean;
import game.server.db.game.bean.RankBean;
import game.server.db.game.bean.RoleBean;
import game.server.db.game.bean.UserBean;
import game.server.logic.constant.CloseSocketReason;
import game.server.logic.constant.SessionKey;
import game.server.logic.line.GameLine;
import game.server.logic.line.GameLineManager;
import game.server.logic.player.PlayerManager;
import game.server.logic.struct.Player;
import game.server.logic.struct.Token;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.ClientVersion;
import game.server.logic.support.DBView;
import game.server.logic.support.RoleView;
import game.server.logic.util.ScriptArgs;
import game.server.thread.dboperator.GameDBOperator;
import game.server.thread.dboperator.handler.ReqCreatePlayerInsertHandler;
import game.server.thread.dboperator.handler.ReqSelectPlayerHandler;
import game.server.util.MessageUtils;
import game.server.util.SensitiveWordFilter;
import game.server.util.SessionUtils;
import game.server.util.UniqueId;
import game.server.util.WordUtils;
import game.server.world.login.handler.LWConnectClosedHandler;
import game.server.world.login.handler.LWLoginSuccessHandler;
import game.server.world.login.handler.LWReconnectSuccessHandler;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 *
 * <b>登录相关服务.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class LoginService
{
    public final static int LOGIN_FUNCTION = 100;

    private final Logger log = Logger.getLogger(LoginService.class);

    private final Map<String, IoSession> userSessions = new ConcurrentHashMap<>();

    private LoginService()
    {
        // 单例对象，提供私有构造器
    }

    /**
     * 获取实例对象.
     *
     * @return
     */
    public static LoginService getInstance()
    {
        return Singleton.INSTANCE.getService();
    }

    private String md5(String strVerify)
    {
        try
        {
            byte[] bytes = MessageDigest.getInstance("MD5").digest(strVerify.getBytes("UTF-8"));
            StringBuilder ret = new StringBuilder(bytes.length << 1);
            for (int a = 0; a < bytes.length; ++a)
            {
                ret.append(Character.forDigit((bytes[a] >> 4) & 0xf, 16));
                ret.append(Character.forDigit(bytes[a] & 0xf, 16));
            }
            return ret.toString();
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException ex)
        {
            log.error(ex, ex);
        }
        return null;
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton
    {
        INSTANCE;

        LoginService service;

        Singleton()
        {
            this.service = new LoginService();
        }

        LoginService getService()
        {
            return service;
        }
    }

    /**
     * 验证登录帐号密码
     *
     * @param session
     * @param reqLogin
     * @return
     */
    public boolean isLegalLogin(IoSession session, LoginMessage.ReqLogin reqLogin)
    {
        // TODO 检查消息中的sign判断是否合法
        /*
        Validate.notEmpty(reqLogin.getSign(), "sign is empty");
        Validate.notNull(reqLogin.getTime());

        if (!reqLogin.hasSign())
        {
            log.error("!reqLogin.hasSign()");
            return false;
        }

        // 登录时间30分钟内有效
        int now = (int) System.currentTimeMillis() / 1000;
        if (reqLogin.getTime() >= (now - 30 * 60) && reqLogin.getTime() <= now)
        {
            log.error("User[ " + reqLogin.getSoleId() + "]验证串已过期：[" + reqLogin.getTime() + "]，当前时间:[" + now + "]");
            return false;
        }

        String strVerify = reqLogin.getSoleId() + reqLogin.getTime() + ServerConfig.getInstance().getLoginKey();
        String md5Sign = md5(strVerify);

        if (md5Sign == null || !md5Sign.equalsIgnoreCase(reqLogin.getSign()))
        {
            log.error("User[ " + reqLogin.getUserName() + "]验证串失败：[" + reqLogin.getSign() + "][" + reqLogin.getTime() + "]");
            return false;
        }
         */
        return true;
    }

    public void loginResSelect(IoSession session, boolean hasRole, UserBean userBean, RoleBean roleBean, List<MailBean> mailList)
    {
        Validate.notNull(session);
        if (hasRole && (userBean == null || roleBean == null))
            throw new IllegalArgumentException("userBean = " + userBean + ", roleBean = " + roleBean);

        GameLine gameLine = (GameLine) session.getAttribute(SessionKey.GAME_LINE.name());
        if (gameLine == null)
        {
            log.error("gameLine == null");
            return;
        }
        String userName = (String) session.getAttribute(SessionKey.USER_NAME.name());
        if (userName == null)
        {
            log.error("user == null");
            return;
        }

        if (!hasRole) // 没有角色
        {
            loginFail(session, LoginFailReason.HAS_NOT_ROLE.getValue());
            return;
        }

        Player player = new Player();
        try
        {
            player.loadInitialize(userBean, roleBean, mailList);
        }
        catch (Exception ex) // 玩家数据初始化错误
        {
            log.error("player initialize fail, userName: ["
                    + userBean.getUserName() + "], userId: ["
                    + userBean.getUserId() + "], roleName: ["
                    + roleBean.getRoleName() + "], roleId: ["
                    + roleBean.getRoleId() + "]", ex);
            gameLine.getLoginLock().removeTransaction(userName); // 清除跨线程流程锁
            return;
        }
        player.setToken(new Token());
        player.setLineId(gameLine.getLineId());

        loginPlayer(session, player);
    }

    public void loginPlayer(IoSession session, Player player)
    {
        PlayerManager.register(session, player);
        GameLine gameLine = (GameLine) session.getAttribute(SessionKey.GAME_LINE.name());
        if (gameLine == null)
        {
            log.error("gameLine == null");
            return;
        }
        gameLine.addPlayer(player);
        player.setLastConnectTime(System.currentTimeMillis());
        if (session.isClosing()) // 在异步去GameDBOperator线程INSERT/SELECT未返回结果前session断开连接
            player.setDetachState();
        else
            player.setNormalState();
        gameLine.getLoginLock().removeTransaction(player.getUserName()); // 清除跨线程流程锁
        player.getToken().flush();

        if (session.containsAttribute(SessionKey.CLIENT_VER.name()))
            player.setClientVer((String) session.getAttribute(SessionKey.CLIENT_VER.name()));
        if (session.containsAttribute(SessionKey.CLIENT.name()))
            player.setClient((String) session.getAttribute(SessionKey.CLIENT.name()));
        if (session.containsAttribute(SessionKey.DEVICE.name()))
            player.setDevice((String) session.getAttribute(SessionKey.DEVICE.name()));
        if (session.containsAttribute(SessionKey.PLATFORM_UID.name()))
            player.setPlatform_uid((String) session.getAttribute(SessionKey.PLATFORM_UID.name()));

        loginSuccess(player);
    }

    /**
     * 登录
     *
     * @param session 连接会话对象
     * @param reqMsg 客户端发送的登录消息
     * @param verify 登录是否验证
     */
    public void login(IoSession session, LoginMessage.ReqLogin reqMsg, boolean verify)
    {
        Validate.notNull(session);
        Validate.notNull(reqMsg);

        log.debug("in login\nReqMsg=>\n" + reqMsg.toString());

        callLoginBeginScript(session, reqMsg, verify);

        if (session.containsAttribute(SessionKey.LOGIN_MSG_PROC)) // 客户端重复发送Login消息
        {
            log.error("same session multi send login msg: " + reqMsg.toString());
            return;
        }

        if (reqMsg.hasClientVer())
        {
            String clientVer = reqMsg.getClientVer();
            if (!ClientVersion.getInstance().matchingVersion(clientVer))
            {
                log.error("客户端版本太低, 服务端clientVer: [" + ClientVersion.getInstance().getClientVer() + "], 客户端clientVer: [" + clientVer + "]");
                loginFail(session, LoginFailReason.CLIENT_VERSION_NOT_MATCHING.getValue());
                return;
            }
            session.setAttribute(SessionKey.CLIENT_VER.name(), clientVer);
        }
        else
        {
            log.warn("!reqMsg.hasClientVer()");
        }

        GameLine gameLine = (GameLine) session.getAttribute(SessionKey.GAME_LINE.name());
        if (gameLine == null)
        {
            log.error("gameLine == null");
            return;
        }


        /*
        if (verify && !isLegalLogin(session, reqMsg)) // 账号验证
        {
            loginFail(session, LoginFailReason.VERIFY_FAIL.getValue()); // 1 = 账号验证未通过
            return;
        }
         */
        // 记录platformId, 
        session.setAttribute(SessionKey.PLATFORM_ID.name(), reqMsg.getPlaformId());
        session.setAttribute(SessionKey.SERVER_ID.name(), reqMsg.getServerId());

        Player player = null;
        // 是否有用户名
        if (reqMsg.hasUserName())
        {
            session.setAttribute(SessionKey.USER_NAME.name(), reqMsg.getUserName());
            String userName = reqMsg.getUserName().toLowerCase();
            if (gameLine.getLoginLock().hasTransaction(userName)){
                log.error("soleId: [" + userName + "], has transaction");
                return;
            }
        
            player = PlayerManager.getPlayerByUserName(userName);

            // 不允许登录机器人账号
            if (player != null && player.isRobot())
            {
                log.error("client try login robot, " + reqMsg.toString());
                loginFail(session, LoginFailReason.VERIFY_FAIL.getValue());
                return;
            }

            // 客户端重复发送Login消息
            if (player != null && player.getSession() == session)
            {
                log.error("client multi send login msg: " + reqMsg.toString());
                loginFail(session, LoginFailReason.UNKNOWN.getValue());
                return;
            }

            // 重复登录, 顶号
            if (player != null && player.getSession() != null && player.getSession() != session)
            {
                log.info("client repeat login, " + reqMsg.toString());
                IoSession oldSession = player.getSession();
                SessionUtils.copyAllGameAttributes(oldSession, session);
                SessionUtils.removeAllGameAttributes(oldSession);
                player.setSession(session);
                MessageUtils.closeSocket(oldSession, CloseSocketReason.REPEAT_LOGIN);
                oldSession.close(false);
                loginPlayer(session, player);
            }
        }

        // 无角创建一个角色
        if( player == null){
            LoginMessage.ReqCreateRole.Builder crtRoleMsg = LoginMessage.ReqCreateRole.newBuilder();
            if (reqMsg.hasRoleName())
            {
                crtRoleMsg.setRoleName(reqMsg.getRoleName());
            }
            else
            {
                String _crtName = "游客" + RandomUtils.nextInt(10000);
                try
                {
                    _crtName = new String(_crtName.getBytes(), "UTF-8");
                }
                catch (UnsupportedEncodingException ex)
                {
                    java.util.logging.Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
                }
                crtRoleMsg.setRoleName(_crtName);
            }

            // 服务器创建soleid
            String userName = UUID.randomUUID().toString();
            if(reqMsg.hasUserName()){
                userName = reqMsg.getUserName();
            }
            session.setAttribute(SessionKey.USER_NAME.name(), userName);
            
            crtRoleMsg.setUserName(userName);

            if (reqMsg.hasSex())
            {
                crtRoleMsg.setSex(reqMsg.getSex());
            } else {
                crtRoleMsg.setSex(0);
            }

            if (reqMsg.hasHeadURL())
            {
                crtRoleMsg.setHeadURL(reqMsg.getHeadURL());
            }
            this.createRole(session, crtRoleMsg.build());
        } else {
            loginPlayer(session, player);
        }
    }
    
    /**
     * 调用开始登录脚本
     *
     * @param session
     * @param reqMsg
     * @param verify
     */
    private void callLoginBeginScript(IoSession session, LoginMessage.ReqLogin reqMsg, boolean verify)
    {
        ScriptArgs args = new ScriptArgs();
        args.put(ScriptArgs.Key.IO_SESSION, session);
        args.put(ScriptArgs.Key.OBJECT_VALUE, reqMsg);
        args.put(ScriptArgs.Key.BOOLEAN_VALUE, verify);

        ScriptManager.getInstance().call(1006, args);
    }

    /**
     * 调用登录成功脚本
     *
     * @param session
     * @param player
     */
    private void callLoginSuccessScript(Player player)
    {
        ScriptArgs args = new ScriptArgs();
        args.put(ScriptArgs.Key.IO_SESSION, player.getSession());
        args.put(ScriptArgs.Key.PLAYER, player);
        args.put(ScriptArgs.Key.FUNCTION_NAME, "loginSuccess");

        ScriptManager.getInstance().call(1001, args);
    }

    /**
     * 调用重连接成功脚本
     *
     * @param player
     */
    private void callReconnectSuccessScript(Player player)
    {
        ScriptArgs args = new ScriptArgs();
        args.put(ScriptArgs.Key.IO_SESSION, player.getSession());
        args.put(ScriptArgs.Key.PLAYER, player);

        ScriptManager.getInstance().call(1011, args);
    }

    /**
     * 调用连接关闭脚本
     *
     * @param player
     */
    private void callConnectionCloseScript(Player player)
    {
        ScriptArgs args = new ScriptArgs();
        args.put(ScriptArgs.Key.ARG1, player);
        ScriptManager.getInstance().call(1012, args);
    }

    /**
     * 调用开始创建角色脚本
     *
     * @param session
     * @param reqMsg
     */
    private void callCreateRoleBeginScript(IoSession session, LoginMessage.ReqCreateRole reqMsg)
    {
        ScriptArgs args = new ScriptArgs();
        args.put(ScriptArgs.Key.IO_SESSION, session);
        args.put(ScriptArgs.Key.OBJECT_VALUE, reqMsg);

        ScriptManager.getInstance().call(1007, args);
    }

    private void callCreateRoleSuccessScript(IoSession session, boolean isSuccess, UserBean userBean, RoleBean roleBean, RankBean rankBean)
    {
        ScriptArgs args = new ScriptArgs();
        args.put(ScriptArgs.Key.ARG1, session);
        args.put(ScriptArgs.Key.ARG2, isSuccess);
        args.put(ScriptArgs.Key.ARG3, userBean);
        args.put(ScriptArgs.Key.ARG4, roleBean);
        args.put(ScriptArgs.Key.ARG5, rankBean);

        ScriptManager.getInstance().call(1008, args);
    }

    /**
     * 断线重连
     *
     * @param session
     * @param reqMsg 客户端发送的重连消息
     */
    public void reconnect(IoSession session, LoginMessage.ReqReconnect reqMsg)
    {
        Validate.notNull(session);
        Validate.notNull(reqMsg);
        log.info("in reconnect\nReqMsg=>\n" + reqMsg.toString());
        Validate.notEmpty(reqMsg.getUserId(), "userId is empty");
        Validate.notEmpty(reqMsg.getToken(), "token is empty");

        String userId = reqMsg.getUserId();
        String token = reqMsg.getToken();
        Player player = PlayerManager.getPlayerByUserId(userId);
        if (player == null)
        {
            log.error("重连, 会话过期, 内存中找不到该玩家");
            reconnectFail(session, ReconnetFailReason.SESSION_OUT_DATE.getValue()); // 1 = 会话已过期
            return;
        }

        String clientVer = StringUtils.EMPTY;
        if (reqMsg.hasClientVer())
        {
            clientVer = reqMsg.getClientVer();
            if (!ClientVersion.getInstance().matchingVersion(clientVer))
            {
                log.error("客户端版本太低, 服务端clientVer: [" + ClientVersion.getInstance().getClientVer() + "], 客户端clientVer: [" + clientVer + "]");
                reconnectFail(session, ReconnetFailReason.CLIENT_VERSION_NOT_MATCHING.getValue()); // 客户端版本不对
                return;
            }
        }
        else
        {
            log.warn("!reqMsg.hasClientVer()");
        }

        Token playerToken = player.getToken();
        if (!playerToken.getUUID().equals(UUIDUtils.fromCompactString(token)))
        {
            log.error("重连, 令牌不正确, playerToken: [" + playerToken.toString() + "]");
            reconnectFail(session, ReconnetFailReason.TOKEN_INVALID.getValue()); // 2 = 令牌不正确
            return;
        }
        playerToken.flush();

        SessionUtils.fillInGameAttributes(session, player);
        if (!clientVer.isEmpty())
        {
            session.setAttribute(SessionKey.CLIENT_VER.name(), clientVer);
            player.setClientVer(clientVer);
        }
        player.setNormalState(); // 置为 正常 状态
        player.setSession(session);
        player.setLastConnectTime(System.currentTimeMillis());

        DBView.getInstance().setOnline(player.getRoleId(), true); // 更新DBView里的状态

        // 同步到GameWorld
        LWReconnectSuccessHandler handler = new LWReconnectSuccessHandler(session);
        handler.setAttribute("userId", userId);
        MessageUtils.sendToGameWorld(handler);

        // 通知客户端
        reconnectSuccess(session, player);

        callReconnectSuccessScript(player); // 平台开发部需要在这里给他们发登录日志
    }

    /**
     * 客户端连接断开
     *
     * @param session
     */
    public void connectionClosed(IoSession session)
    {
        Validate.notNull(session);
        log.info("in connectionClosed");

        Player player = PlayerManager.getPlayerBySession(session);
        if (player != null)
        {
            Object userNameObj = session.getAttribute(SessionKey.USER_NAME.name());
            if (userNameObj != null)
            {
                String userName = (String) userNameObj;
                GameLine gameLine = GameLineManager.getInstance().getLine(player.getLineId());
                if (gameLine != null)
                {
                    gameLine.getCreateRoleLock().removeTransaction(userName);
                    gameLine.getLoginLock().removeTransaction(userName);
                }
            }
            if (player.getSession() == session)
            {
                player.sessionClose();
            }

            DBView.getInstance().setOnline(player.getRoleId(), false); // 更新DBView里的状态

            callConnectionCloseScript(player);

            // 同步到GameWorld
            LWConnectClosedHandler handler = new LWConnectClosedHandler(player.getLastConnectTime());
            handler.setAttribute("userId", player.getUserId());
            MessageUtils.sendToGameWorld(handler);
        }
        else
        {
            log.error("cannot find Player, session attribute " + SessionUtils.allGameAttributesToString(session));
        }
    }

    /**
     * 检查角色名是否合法
     *
     * @param userName
     * @param roleName
     * @return
     */
    private CreateRoleFailReason checkLegalRoleName(String userName, String roleName)
    {
        // 角色名是否存在
        if (DBView.getInstance().hasRoleName(roleName))
        {
            log.error("roleName has exist. userName: [" + userName + "], roleName: [" + roleName + "]");
            return CreateRoleFailReason.HAS_EXIST; // 1 = 角色名已存在
        }

        // 角色名为空判断
        if (roleName.trim().isEmpty())
        {
            log.error("roleName empty, userName: [" + userName + "], roleName: [" + roleName + "]");
            return CreateRoleFailReason.ROLE_NAME_EMPTY;
        }

        // 角色名中空格判断
        if (WordUtils.hasSpace(roleName))
        {
            log.error("roleName has space, userName: [" + userName + "], roleName: [" + roleName + "]");
            return CreateRoleFailReason.ROLE_NAME_HAS_SPACE;
        }

        // 名字是否超出14个字符
        int len = WordUtils.getAsciiCharacterLength(roleName);
        if (len > 14)
        {
            log.error("roleName over length, userName: [" + userName + "], roleName: [" + roleName + "], roleName ascii length = " + len);
            return CreateRoleFailReason.ROLE_NAME_OVER_LENGTH;
        }

        String specialChar = "!@#$%^&*()_+-=[{]}\\;:'\",<.>/?！＠＃￥％……＆×（）——＋－＝【｛】｝＼｜；：‘“，《。》、？";
        for (int i = 0; i < specialChar.length(); ++i)
        {
            char sc = specialChar.charAt(i);
            for (int j = 0; j < roleName.length(); ++j)
            {
                if (sc == roleName.charAt(j))
                {
                    log.error("role contain special character, userName: [" + userName + "], roleName: [" + roleName + "]");
                    return CreateRoleFailReason.SPECIAL_CHARACTER;
                }
            }
        }

        // 敏感词判断
        if (SensitiveWordFilter.getInstance().hasSensitiveWord(roleName))
        {
            log.error("名字有敏感字符，开发期间可以通过，正式服务器需要开启检测, userName: [" + userName + "], roleName: [" + roleName + "]");
            //return CreateRoleFailReason.SENSITIVE_WORD;
        }

        return CreateRoleFailReason.UNKNOWN;
    }

    /**
     * 检查主角ID是否合法
     *
     * @param cardId
     * @return
     */
    public CreateRoleFailReason checkMainCardId(int cardId)
    {
        q_globalBean bean = BeanTemplet.getGlobalBean(1001);
        if (bean == null)
        {
            log.error("Global varable 1001 is not exist");
            return CreateRoleFailReason.MAIN_CARID_ID_ERROR;
        }

        q_npc_heroBean attributeBean = BeanTemplet.getNpcHeroBean(cardId);
        if (attributeBean == null)
        {
            log.error("主角ID:" + cardId + " 在【英雄基础属性】配置表中不存在");
            return CreateRoleFailReason.MAIN_CARID_ID_ERROR;
        }

        String strIDs = bean.getQ_string_value();
        Validate.notEmpty(strIDs);
        String[] strArrIds = strIDs.split("_");
        for (String strId : strArrIds)
        {
            if (Integer.parseInt(strId) == cardId)
            {
                return CreateRoleFailReason.UNKNOWN;
            }
        }

        return CreateRoleFailReason.MAIN_CARID_ID_ERROR;
    }

    /**
     * 创建角色
     *
     * @param session 连接回话对象
     * @param reqMsg 客户端发送的创建角色消息
     */
    public void createRole(IoSession session, LoginMessage.ReqCreateRole reqMsg)
    {
        Validate.notNull(session);
        Validate.notNull(reqMsg);

        log.debug("in createRole\nReqMsg=>\n" + reqMsg.toString());

        String userName = (String) session.getAttribute(SessionKey.USER_NAME.name());
        if (userName == null || DBView.getInstance().hasUserName(userName))
        {
            log.error("userName = " + userName + " 已经创建过角色");
            Player player = PlayerManager.getPlayerBySession(session);
            if (player != null)
            {
                player.setRoleName(reqMsg.getRoleName());
            }
            GameLine gameLine = (GameLine) session.getAttribute(SessionKey.GAME_LINE.name());
            GameDBOperator.getInstance().submitRequest(new ReqSelectPlayerHandler(gameLine.getLineId(), session, userName));
            return;
        }

        String roleName = reqMsg.getRoleName();
        CreateRoleFailReason reason = checkLegalRoleName(userName, roleName);
        if (reason != CreateRoleFailReason.UNKNOWN)
        {
            createRoleFail(session, reason.getValue());
            return;
        }

        GameLine gameLine = (GameLine) session.getAttribute(SessionKey.GAME_LINE.name());
        if (gameLine == null)
        {
            log.error("gameLine == null");
            return;
        }
        if (gameLine.getCreateRoleLock().hasTransaction(userName))
        {
            log.error("createRole, userName: [" + userName + "], has transaction");
            return;
        }

        Integer platformId = (Integer) session.getAttribute(SessionKey.PLATFORM_ID.name());
        Integer serverId = (Integer) session.getAttribute(SessionKey.SERVER_ID.name());
        String fgi = "";
        Object fgiVal = session.getAttribute(SessionKey.FGI.name());
        if (fgiVal != null && fgiVal instanceof String)
            fgi = (String) fgiVal;
         
        if (platformId == null)
        {
            log.error("platformId = " + platformId + ", userName = " + userName);
            createRoleFail(session, CreateRoleFailReason.UNKNOWN.getValue());
            return;
        }

        // 初始user表数据
        UserBean userBean = new UserBean();
        userBean.setUserName(userName);
        userBean.setUserId(UniqueId.getUniqueIdBase36(serverId, platformId, UniqueId.FuncType.USER));

        // 初始role表数据
        RoleBean roleBean = new RoleBean();

        roleBean.setRoleId(UniqueId.getUniqueIdBase36(serverId, platformId, UniqueId.FuncType.ROLE));
        roleBean.setUserId(userBean.getUserId());
        roleBean.setRoleName(roleName);
        roleBean.setFgi(fgi);
        roleBean.setPlatformId(platformId);
        roleBean.setServerId(serverId);
        roleBean.setMiscData(StringUtils.EMPTY);
        roleBean.setRoleLevel(1); //初始等级为1
        roleBean.setSoleId(userName);
        if (reqMsg.hasSex())
        {
            roleBean.setSex(reqMsg.getSex());
        }
        if (reqMsg.hasHeadURL())
        {
            roleBean.setHeadURL(reqMsg.getHeadURL());
        }

        // 成就排行数据表
        RankBean rankBean = new RankBean();
        rankBean.setUserId(userBean.getUserId());
        rankBean.setRoleId(roleBean.getRoleId());
        rankBean.setRoleName(roleBean.getRoleName());
        rankBean.setRoleHead(roleBean.getRoleHead());
        rankBean.setRoleLevel(roleBean.getRoleLevel());
        rankBean.setAchievementPoint(0);
        rankBean.setAchievementLastModify(Calendar.getInstance().getTime());
        rankBean.setFunMission1Score(0);
        rankBean.setFunMission1LastModify(Calendar.getInstance().getTime());
        rankBean.setFunMission2Score(0);
        rankBean.setFunMission2LastModify(Calendar.getInstance().getTime());

        Player player = new Player();
        player.createInitialize(userBean, roleBean);

        roleBean.setMiscData(player.toJson().toString());

        roleBean.compress();

        gameLine.getCreateRoleLock().recordTransaction(userName, session);
        GameDBOperator.getInstance().submitRequest(new ReqCreatePlayerInsertHandler(gameLine.getLineId(), session, userBean, roleBean, rankBean));
    }

    public void createRoleInsertRes(IoSession session, boolean isSuccess, UserBean userBean, RoleBean roleBean, RankBean rankBean)
    {
        Validate.notNull(session);
        if (isSuccess && (userBean == null || roleBean == null))
            throw new IllegalArgumentException("userBean = " + userBean + ", roleBean = " + roleBean);

        GameLine gameLine = (GameLine) session.getAttribute(SessionKey.GAME_LINE.name());
        if (gameLine == null)
        {
            log.error("gameLine == null");
            return;
        }

        gameLine.getCreateRoleLock().removeTransaction(userBean.getUserName());

        if (!isSuccess)
        {
            log.error("create faile");
            createRoleFail(session, 0);
            return;
        }

        // 添加DBView
        try
        {
            RoleView roleView = new RoleView(
                    userBean.getUserId(),
                    userBean.getUserName(),
                    userBean.getSoleId(),
                    roleBean.getRoleId(),
                    roleBean.getRoleName(),
                    roleBean.getRoleLevel(),
                    roleBean.getVipLevel(),
                    roleBean.getServerId(),
                    roleBean.getPlatformId());
            DBView.getInstance().addRoleView(roleView);
        }
        catch (Throwable t)
        {
            log.error("Throwable", t);
        }

        callCreateRoleSuccessScript(session, isSuccess, userBean, roleBean, rankBean);

        loginResSelect(session, true, userBean, roleBean, new ArrayList<MailBean>());
    }

    public void wlLoginSuccess(Player player)
    {
        // 解除跨线程流程锁
        GameLine gameLine = GameLineManager.getInstance().getLine(player.getLineId());
        if (gameLine == null)
        {
            log.error("gameLine == null,UserName:" + player.getUserName());
            return;
        }
        gameLine.getLoginLock().removeTransaction(player.getUserName());
        callLoginSuccessScript(player);

        // 构建消息对象
        PlayerMessage.PlayerInfo.Builder _playerInfo = PlayerMessage.PlayerInfo.newBuilder();
        _playerInfo.setUserId(UniqueId.toBase36(player.getUserId()));
        _playerInfo.setRoleId(UniqueId.toBase36(player.getRoleId()));
        _playerInfo.setRoleLevel(player.getRoleLevel());
        _playerInfo.setRoleName(player.getRoleName());
        _playerInfo.setExp(1);
        _playerInfo.setSex(1);
        _playerInfo.setVipRemainDay(0);
        _playerInfo.setCoin(0);
        _playerInfo.setScore(0);
        _playerInfo.setGem(0);
        _playerInfo.setUserName(player.getUserName());
        

        LoginMessage.ResLoginSuccess.Builder success = LoginMessage.ResLoginSuccess.newBuilder();
        success.setPlayerInfo(_playerInfo);
        success.setToken(UUIDUtils.toCompactString(player.getToken().getUUID()));

        MessageUtils.send(player, new SMessage(LoginMessage.ResLoginSuccess.MsgID.eMsgID_VALUE, success.build().toByteArray()));

        //运营活动相关
        player.getOperActivityManager().isLoginAccumulateOpen();
    }

    private void loginSuccess(Player player)
    {
        DBView.getInstance().setOnline(player.getRoleId(), true); // 更新DBView里的状态

        // 到GameWorld登陆
        LWLoginSuccessHandler handler = new LWLoginSuccessHandler();
        handler.setAttribute("player", player);
        MessageUtils.sendToGameWorld(handler);
    }

    private void loginFail(IoSession session, int reason)
    {
        LoginMessage.ResLoginFailed.Builder resMsg = LoginMessage.ResLoginFailed.newBuilder();
        resMsg.setReason(reason);
        MessageUtils.send(session, new SMessage(LoginMessage.ResLoginFailed.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
    }

    public static void createRoleFail(IoSession session, int reason)
    {
        LoginMessage.ResCreateRoleFailed.Builder resMsg = LoginMessage.ResCreateRoleFailed.newBuilder();
        resMsg.setReason(reason);
        MessageUtils.send(session, new SMessage(LoginMessage.ResCreateRoleFailed.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
    }

    private void reconnectSuccess(IoSession session, Player player)
    {
        LoginMessage.ResReconnetSuccess.Builder resMsg = LoginMessage.ResReconnetSuccess.newBuilder();
        resMsg.setToken(player.getToken().getValue());
        MessageUtils.send(session, new SMessage(LoginMessage.ResReconnetSuccess.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
    }

    private void reconnectFail(IoSession session, int reason)
    {
        LoginMessage.ResReconnetFialed.Builder resMsg = LoginMessage.ResReconnetFialed.newBuilder();
        resMsg.setReason(reason);
        MessageUtils.send(session, new SMessage(LoginMessage.ResReconnetFialed.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
    }

    public enum LoginFailReason // 原因(1 = 账号验证未通过, 2 = 没有角色)
    {
        UNKNOWN(0),
        VERIFY_FAIL(1),
        HAS_NOT_ROLE(2),
        CLIENT_VERSION_NOT_MATCHING(4); // 客户端版本不对

        private final int value;

        LoginFailReason(int value)
        {
            this.value = value;
        }

        int getValue()
        {
            return value;
        }
    }

    public enum CreateRoleFailReason
    {
        UNKNOWN(0),
        HAS_EXIST(1), // 角色名已存在
        ROLE_NAME_EMPTY(2), // 角色名为空
        ROLE_NAME_HAS_SPACE(3), // 角色名中有空格
        ROLE_NAME_OVER_LENGTH(4), // 队伍名字超出12个字符
        SENSITIVE_WORD(5), // 帐号名或角色名是敏感词
        CDKEY_INVALID(6), // 没有激活码或错误的激活码
        SPECIAL_CHARACTER(7), // 角色名中包含特殊字符 ！!@#$%^&*()_+-=
        MAIN_CARID_ID_ERROR(8), // 主角ID有误
        ;

        private final int value;

        CreateRoleFailReason(int value)
        {
            this.value = value;
        }

        int getValue()
        {
            return value;
        }
    }

    public enum ReconnetFailReason // 失败原因(1 = 会话已过期；2 = 令牌不正确；3 = 未断开连接)
    {
        UNKNOWN(0),
        SESSION_OUT_DATE(1),
        TOKEN_INVALID(2),
        HAS_NOT_DISCONNECTION(3),
        CLIENT_VERSION_NOT_MATCHING(4); // 客户端版本不对

        private final int value;

        ReconnetFailReason(int value)
        {
            this.value = value;
        }

        int getValue()
        {
            return value;
        }
    }

}
