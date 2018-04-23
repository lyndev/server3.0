/**
 * @date 2014/5/29
 * @author ChenLong
 */
package game.server.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.core.script.ScriptJavaLoader;
import game.data.GameDataManager;
import game.data.bean.q_scriptBean;
import game.server.GameServer;
import game.server.db.game.bean.RoleBean;
import game.server.db.game.dao.RoleDao;
import game.server.logic.constant.ResourceType;
import game.server.logic.line.GameLineManager;
import game.server.logic.mail.bean.MailTool;
import game.server.logic.mail.service.MailService;
import game.server.logic.player.PlayerManager;
import game.server.logic.struct.Player;
import game.server.logic.struct.Resource;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.ClientVersion;
import game.server.logic.support.DBView;
import game.server.logic.trot.TrotManager;
import game.server.logic.trot.bean.TrotInfo;
import game.server.logic.trot.bean.TrotType;
import game.server.thread.dboperator.GameDBOperator;
import game.server.thread.dboperator.handler.ReqBuyAllPlayerMonthCardHandler;
import game.server.util.GameJsonConfiger;
import game.server.world.GameWorld;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.http.api.DefaultHttpResponse;
import org.apache.mina.http.api.HttpRequest;
import org.apache.mina.http.api.HttpResponse;
import org.apache.mina.http.api.HttpStatus;

/**
 * Http消息相应
 *
 * @author ChenLong
 */
public class HttpService
{
    private final Logger log = Logger.getLogger(HttpService.class);
    private final static HttpService instance;

    static
    {
        instance = new HttpService();
    }

    public static HttpService getInstance()
    {
        return instance;
    }

    public void dispatcherRequest(IoSession session, HttpRequest httpRequest)
    {
        String command = httpRequest.getParameter("command");
        String visitor = httpRequest.getParameter("visitor=cocos2d");
        boolean processed = (command != null);
        GameHttpResponse httpResponse = new GameHttpResponse();
        StringBuilder responseContent = new StringBuilder();

        if (command != null)
        {
            switch (command.toLowerCase())
            {
                case "stopserver": // StopServer程序发送该请求  http://ip:port/?command=stopserver
                    GameServer.getInstance().executeStopTask();
                    break;
                case "reloadgamedata":
                    reloadGameData(session, httpRequest, httpResponse, responseContent);
                    break;
                case "reloadjsonfile":
                    reloadJsonFile(session, httpRequest);
                    break;
                case "reloadjavascript":
                    reloadJavaScript(session, httpRequest);
                    break;
                case "reloadclientversion":
                    ClientVersion.getInstance().load();
                    break;
                case "flushalldetachplayer":
                    flushAllDetachPlayer(session, httpRequest);
                    break;
                case "saveallonlineplayer":
                    saveAllOnlinePlayer(session, httpRequest);
                    break;
                case "setplayersavedelay":
                    setPlayerSaveDelay(session, httpRequest);
                    break;
                case "setflushdetachplayerwaterlevel":
                    setFlushDetachPlayerWaterLevel(session, httpRequest);
                    break;
                case "setflushdetachplayerwaterleveldelay":
                    setFlushDetachPlayerWaterLevelDelay(session, httpRequest);
                    break;
                case "setflushdetachplayerdelay":
                    setFlushDetachPlayerDelay(session, httpRequest);
                    break;
                case "gmlevel":
                    changeGMLevel(session, httpRequest);
                    break;
                case "notice":
                    noticePlayer(session, httpRequest);
                    break;
                case "onlinenum":
                    onlinenum(session, httpRequest);
                    break;
                case "sendmail":
                    sendMail(session, httpRequest);
                    break;
                case "closesession": // 断开所有在线玩家连接(测试用) http://ip:port/?command=closesession
                    PlayerManager.closeAllPlayerSession();
                    break;
                case "buyallplayermonthcard": // 2014.11.7 16:00 王老板要求测试时能开启全服的月卡功能, 注意: 执行此功能前需要执行flushalldetachplayer命令, 将内存中的分离状态玩家清除
                    buyAllPlayerMonthCard(session, httpRequest);
                    break;
                default:
                    log.error("unknow command: [" + command + "]");
                    processed = false;
                    break;
            }
        }

        // 拼返回Hhtml
        StringBuilder responseHtml = new StringBuilder("<html><body><h2>");
        if (processed)
        {
            responseHtml.append("命令: [").append(command).append("] 执行完成</h2>");
        }
        else
        {
            responseHtml.append("command参数错误</h2>");
        }
        responseHtml.append("<pre>").append(responseContent).append("</pre></body></html>");

        // Content-Length
        httpResponse.setContentLength(responseHtml.toString().getBytes().length);

        // html内容
        IoBuffer buf = IoBuffer.allocate(responseHtml.toString().getBytes().length).setAutoExpand(true);
        buf.put(responseHtml.toString().getBytes());
        buf.flip();

        session.write(httpResponse);
        session.write(buf);
    }

    /**
     * 重新加载 config/gameJsonConfig.txt 配置<br />
     * 例如: http://ip:port/?command=reloadjsonfile
     *
     * @param session
     * @param httpRequest
     */
    public void reloadJsonFile(IoSession session, HttpRequest httpRequest)
    {
        log.info("reloadJsonFile");
        GameJsonConfiger.getInstance().reloadJsonFile();
    }

    /**
     * 重新加载全部java脚本<br />
     * 例如:<br />
     * 重新加载所有脚本: http://ip:port/?command=reloadjavascript <br />
     * 重新加载指定脚本: http://ip:port/?command=reloadjavascript&param=1001,1005
     *
     * @param session
     * @param httpRequest
     */
    public void reloadJavaScript(IoSession session, HttpRequest httpRequest)
    {
        Map<Integer, String> scriptNames = new HashMap<>();
        String param = httpRequest.getParameter("param");
        if (param != null)
        {
            String[] token = param.split(",");
            for (String tokenElement : token)
            {
                try
                {
                    int scriptId = Integer.parseInt(tokenElement);
                    q_scriptBean bean = GameDataManager.getInstance().q_scriptContainer.getMap().get(scriptId);
                    if (bean != null)
                    {
                        scriptNames.put(scriptId, bean.getQ_script_name());
                    }
                }
                catch (NumberFormatException ex)
                {
                    log.error("NumberFormatException", ex);
                }
                catch (Exception ex)
                {
                    log.error("Exception", ex);
                }
            }
        }
        else
        {
            for (Map.Entry entry : GameDataManager.getInstance().q_scriptContainer.getMap().entrySet())
            {
                int scriptId = (Integer) entry.getKey();
                String scriptName = ((q_scriptBean) entry.getValue()).getQ_script_name();

                scriptNames.put(scriptId, scriptName);
            }
        }
        if (!scriptNames.isEmpty())
            ScriptJavaLoader.getInstance().reloadScript(scriptNames);
        else
            log.error("scriptNames is empty, maybe parameter error");
    }

    /**
     * 从内存中清除所有已分离(掉线)玩家对象<br />
     * http://ip:port/?command=flushalldetachplayer
     *
     * @param session
     * @param httpRequest
     */
    public void flushAllDetachPlayer(IoSession session, HttpRequest httpRequest)
    {
        log.info("flushAllDetachPlayer");
        GameLineManager.getInstance().flushAllDetachPlayer();
    }

    /**
     * 回存所有玩家
     *
     * @param session
     * @param httpRequest
     */
    public void saveAllOnlinePlayer(IoSession session, HttpRequest httpRequest)
    {
        log.info("saveAllOnlinePlayer");
        GameLineManager.getInstance().saveAllLinePlayerCommand();
    }

    /**
     * http://ip:port/?command=setplayersavedelay&value=20
     *
     * @param session
     * @param httpRequest
     */
    public void setPlayerSaveDelay(IoSession session, HttpRequest httpRequest)
    {
        log.info("setPlayerSaveDelay");
        String value = httpRequest.getParameter("value");
        if (value != null)
        {
            long delay = Long.parseLong(value);
            GameLineManager.getInstance().setPlayerSaveDelay(delay);
        }
    }

    /**
     * http://ip:port/?command=setflushdetachplayerwaterlevel&value=200
     *
     * @param session
     * @param httpRequest
     */
    public void setFlushDetachPlayerWaterLevel(IoSession session, HttpRequest httpRequest)
    {
        log.info("setFlushDetachPlayerWaterLevel");
        String value = httpRequest.getParameter("value");
        if (value != null)
        {
            long delay = Long.parseLong(value);
            GameLineManager.getInstance().setFlushDetachPlayerWaterLevel(delay);
        }
    }

    public void setFlushDetachPlayerWaterLevelDelay(IoSession session, HttpRequest httpRequest)
    {
        log.info("setFlushDetachPlayerWaterLevelDelay");
        String value = httpRequest.getParameter("value");
        if (value != null)
        {
            long delay = Long.parseLong(value);
            GameLineManager.getInstance().setFlushDetachPlayerWaterLevelDelay(delay);
        }
    }

    public void setFlushDetachPlayerDelay(IoSession session, HttpRequest httpRequest)
    {
        log.info("flushDetachPlayerDelay");
        String value = httpRequest.getParameter("value");
        if (value != null)
        {
            long delay = Long.parseLong(value);
            GameLineManager.getInstance().setFlushDetachPlayerDelay(delay);
        }
    }

    /**
     * 重新加载配置表<br />
     * 例如:<br />
     * 重新加载所有配置表: http://ip:port/?command=
     * <br />
     * 重新加载指定配置表: http://ip:port/?command=reloadgamedata&param=q_task,q_item
     *
     * @param session
     * @param httpRequest
     * @param httpResponse
     * @param responseContent
     */
    public void reloadGameData(IoSession session, HttpRequest httpRequest, GameHttpResponse httpResponse, StringBuilder responseContent)
    {
        String param = httpRequest.getParameter("param");

        if (param != null)
        {
            //加载部分
            String[] token = param.split(",");
            boolean reloadItems = false;
            for (String tableName : token)
            {
                if ("q_item".equals(tableName) || "q_equipment".equals(tableName))
                {
                    reloadItems = true;
                    continue;
                }
                responseContent.append(reloadSingleGameDataTable(tableName + "Container")).append("\n");
            }

            // 物品表和装备表紧密关联, 如果加载其中一张表, 则另一张表也必须重新加载, 并保证加载顺序
            if (reloadItems)
            {
                responseContent.append(reloadSingleGameDataTable("q_item" + "Container")).append("\n");
                responseContent.append(reloadSingleGameDataTable("q_equipment" + "Container")).append("\n");
                BeanTemplet.loadAllItemContainer();
            }
        }
        else
        {
            //加载所有的
            Field[] declaredFields = GameDataManager.getInstance().getClass().getDeclaredFields();
            for (Field field : declaredFields)
            {
                if (field.getName().contains("Container"))
                    responseContent.append(reloadSingleGameDataTable(field.getName())).append("\n");
            }
            // 加载完所有表后, 重新装载装备数据
            BeanTemplet.loadAllItemContainer();
        }
    }

    /**
     * 服务器监控心跳
     *
     * @param session
     * @param httpRequest
     */
    public void serverAliveHeartbeat(IoSession session, HttpRequest httpRequest)
    {
        Map<String, String> headers = new HashMap<>();
        headers.put("response", "alive");
        HttpResponse rp = new DefaultHttpResponse(httpRequest.getProtocolVersion(), HttpStatus.CLIENT_ERROR_BAD_REQUEST, headers);
        session.write(rp);
    }

    /**
     * 充值接口
     *
     * @param session
     * @param httpResuest
     */
    public void recharge(IoSession session, HttpRequest httpResuest)
    {
    }

    /**
     * 查看在线人数
     *
     * @param session
     * @param httpRequest
     */
    public void onlineNum(IoSession session, HttpRequest httpRequest)
    {
    }

    /**
     * 加载单个配置表数据
     *
     * @param fieldName GameDataManager中xxContainer
     */
    private String reloadSingleGameDataTable(String fieldName)
    {
        try
        {
            if (!fieldName.contains("Container"))
                return "ERROR TABLE NAME: [" + fieldName + "]";
            Field declaredField = GameDataManager.getInstance().getClass().getDeclaredField(fieldName);
            int hashCode = declaredField.get(GameDataManager.getInstance()).hashCode();
            Class<?> cls = declaredField.getType();
            Object newInstance = cls.newInstance();
            Method method = cls.getMethod("load");
            method.invoke(newInstance);
            declaredField.set(GameDataManager.getInstance(), newInstance);
            int hashCode2 = declaredField.get(GameDataManager.getInstance()).hashCode();
            if (hashCode != hashCode2)
                log.info("reload " + fieldName + " end");
            else
                log.info("reload " + fieldName + " is faild" + hashCode + " " + hashCode2);

            // 重新加载并初始化敏感词过滤器
            if (fieldName.contains("q_filterword"))
            {
                GameServer.initSensitiveWordFilter();
            }
        }
        catch (NoSuchFieldException | SecurityException ex)
        {
            log.error("NoSuchFieldException | SecurityException", ex);
        }
        catch (InstantiationException | IllegalAccessException ex)
        {
            log.error("InstantiationException | IllegalAccessException", ex);
        }
        catch (NoSuchMethodException | IllegalArgumentException ex)
        {
            log.error("NoSuchMethodException | IllegalArgumentException", ex);
        }
        catch (InvocationTargetException ex)
        {
            log.error("InvocationTargetException", ex);
        }
        return fieldName;
    }

    /**
     * 修改用户权限 http://ip:port/?command=gmlevel&user=userid&level= (0, 1, 2)
     *
     * @param session
     * @param httpRequest
     */
    public void changeGMLevel(IoSession session, HttpRequest httpRequest)
    {
        // 只允许内网添加GM
        String remoteAddress = session.getRemoteAddress().toString();
        if (remoteAddress.startsWith("/192.168") || remoteAddress.startsWith("/127.0.0.1"))
        {
            String userId = httpRequest.getParameter("user");
            String level = httpRequest.getParameter("level");

            if (userId == null || userId.trim().equals("")
                    || level == null || level.trim().equals(""))
            {
                response(session, httpRequest, "invalid parameters");
                return;
            }

            Player player = PlayerManager.getPlayerByUserName(userId);
            if (player != null)
            {
                player.setGmLevel(Integer.parseInt(level));
            }
            else
            {
                RoleBean bean = RoleDao.selectByUserId(userId);
                if (bean != null)
                {
                    bean.setGmLevel(Integer.parseInt(level));
                    RoleDao.update(bean, true);
                }
                else
                {
                    response(session, httpRequest, "role does't exist");
                }
            }
            // todo log
        }
    }

    private void response(IoSession session, HttpRequest httpRequest, String responseMsg)
    {
        Map<String, String> headers = new HashMap<>();
        headers.put("response", responseMsg);
        HttpResponse rp = new DefaultHttpResponse(httpRequest.getProtocolVersion(), HttpStatus.CLIENT_ERROR_BAD_REQUEST, headers);
        session.write(rp);
    }

    /**
     * 发布公告
     * http://ip:port/?command=notice&type=xx&time=xx&count=xx&interval=xx&content=str&parameter=parameter
     *
     * type: 发送类型 1 = 所有玩家，parameter被忽略 2 =
     * 针对玩家发送，parameter为要发送的玩家id，多个玩家以","分开。 （1213,1234,1124） 3 =
     * 按照玩家等级发送，parameter为：最低等级_最高等级 （1_10） 4 = 按照玩家VIP等级发送， parameter为：
     * 最低VIP等级_最高VIP等级 （1_10） 5 = 按照仙府等级发送， parameter为： 最低等级_最高等级 （1_10） 6 =
     * 按照账号创建时间发送， parameter为： xx-xx-xx_xx-xx-xx （2014-01-01_2014-01-10） 7 =
     * 按照在线时间发送， paramter为：在线多少秒 (3600) 8 = 按照竞技排名发送， parameter为： xx名_xx名
     * (10_100) 9 = 按照平台发送， parameter为： 平台ID (110_112_113)
     *
     * time: 多少秒后发送 默认为马上 count: 发送次数 默认为1次 interval: 发送间隔（秒） 默认为5秒 content:
     * 发送内容
     *
     * @param session
     * @param httpRequest
     */
    public void noticePlayer(IoSession session, HttpRequest httpRequest)
    {

        try
        {
            String strType = httpRequest.getParameter("type");
            if (strType == null || strType.trim().equalsIgnoreCase(""))
            {
                response(session, httpRequest, "invalid parameters!");
                return;
            }
            String content = URLDecoder.decode(httpRequest.getParameter("content"), "utf-8");
            if (content == null || content.trim().equalsIgnoreCase(""))
            {
                response(session, httpRequest, "invalid parameters!");
                return;
            }

            TrotInfo noticeInfo = new TrotInfo(content);
            noticeInfo.setType(TrotType.getTypeByValue(Integer.parseInt(strType)));

            String strTime = httpRequest.getParameter("time");
            if (strTime != null && !strTime.trim().equalsIgnoreCase(""))
            {
                noticeInfo.setTime(Integer.parseInt(strTime));
            }

            String count = httpRequest.getParameter("count");
            if (count != null && !count.trim().equalsIgnoreCase(""))
            {
                int c = Integer.parseInt(count);
                if (c > 10)
                {
                    c = 10;
                }
                else if (c < 0 && c != -1)
                {
                    c = 1;
                }
                //noticeInfo.getCount().set(c);
                noticeInfo.setCount(c);
            }

            String interval = httpRequest.getParameter("interval");
            if (interval != null && !interval.trim().equalsIgnoreCase(""))
            {
                noticeInfo.setInterval(Integer.parseInt(interval));
            }

            String parameter = httpRequest.getParameter("parameter");
            if (parameter != null && !parameter.trim().equalsIgnoreCase(""))
            {
                noticeInfo.setParameter(parameter);
            }

            TrotManager.getIntance().notice(noticeInfo);
        }
        catch (UnsupportedEncodingException ex)
        {
            log.error("UnsupportedEncodingException", ex);
        }
    }

    /**
     * 查看在线数, http://ip:port/?command=onlinenum
     *
     * @param session
     * @param httpRequest
     */
    public void onlinenum(IoSession session, HttpRequest httpRequest)
    {
        log.info("Online Player num = " + PlayerManager.onLinePlayerNum());
        GameWorld.getInstance().onlineNumCommand();
    }

    /**
     * 发送邮件 http://ip:port/?command=sendmail&param=jsonURLencodeString <br />
     * URLEncode/Decode工具 http://meyerweb.com/eric/tools/dencoder/ <br />
     * 例如: <br />
     * 源: <br />
     * http://ip:port/?command=sendmail&param={"content":"内容","deadLine":24,"itemStr":"50000_2","parameter":"1_10","resStr":"1_100","senderName":"发件人","title":"标题","type":3}<br
     * />
     * URLEncode后: <br />
     * http://127.0.0.1:8889/?command=sendmail&param=%7B%22content%22%3A%22%E5%86%85%E5%AE%B9%22%2C%22deadLine%22%3A24%2C%22itemStr%22%3A%2250000_2%22%2C%22parameter%22%3A%221_10%22%2C%22resStr%22%3A%221_100%22%2C%22senderName%22%3A%22%E5%8F%91%E4%BB%B6%E4%BA%BA%22%2C%22title%22%3A%22%E6%A0%87%E9%A2%98%22%2C%22type%22%3A3%7D
     *
     * @param session
     * @param httpRequest
     */
    public void sendMail(IoSession session, HttpRequest httpRequest)
    {
        log.info("Online Player num = " + PlayerManager.onLinePlayerNum());
        String command = httpRequest.getParameter("command");
        String param = httpRequest.getParameter("param");
        try
        {
            String cipherJson = URLDecoder.decode(param, "UTF-8");
            JSONObject jsonObj = JSON.parseObject(cipherJson);
//            int type = jsonObj.getIntValue("type");
//            String parameter = jsonObj.getString("parameter");
//            String title = jsonObj.getString("title");
//            String content = jsonObj.getString("content");
//            String senderName = jsonObj.getString("senderName");
//            int deadLine = jsonObj.getIntValue("deadLine");
//            String resStr = jsonObj.getString("resStr");
//            String itemStr = jsonObj.getString("itemStr");
//            
//            int ddln = (int) (System.currentTimeMillis() / 1000) + 60 * 60 * deadLine;
//            Mail mail = new Mail();
//            int accessory = 1;
//            List<Resource> resList = MailService.getInstance().parseResources(resStr);
//            List<Item> itemList = MailService.getInstance().parseItems(itemStr);
//            if (resList.isEmpty() && itemList.isEmpty())
//                accessory = 0;
//            mail.setAll(title, content, senderName, 2, 0L, "", accessory, ddln, resList, itemList);

            MailTool tool = new MailTool();
            tool.fromJson(jsonObj);
            //-------------------------------------------------------------
            //通过邮件发送的资源, 元宝不能超过10000
            List<Resource> list = tool.getMail().getResourceList();
            if (list != null)
            {
                for (Resource res : list)
                {
                    if (ResourceType.GOLD_BULLION.compare(res.getType()) && res.getAmount() >= 10000)
                    {
                        log.error("通过邮件发送的元宝数量不能超过10000, 试图发送的数量为: " + res.getAmount());
                        return; //如果超过10000, 直接返回, 不发送邮件
                    }
                }
            }
            //------------------------------------------------------------
            MailService.getInstance().sendByType(tool.getType(), tool.getParameter(), tool.getMail());

        }
        catch (UnsupportedEncodingException ex)
        {
            log.error("UnsupportedEncodingException", ex);
        }
    }

    public void buyAllPlayerMonthCard(IoSession session, HttpRequest httpRequest)
    {
        // 处理在内存中的玩家
        GameLineManager.getInstance().buyAllPlayerMonthCard();
        
        // 处理离线玩家
        List<String> offlineRoleIds = DBView.getInstance().getAllOfflinePlayerRoleId();
        GameDBOperator.getInstance().submitRequest(new ReqBuyAllPlayerMonthCardHandler(offlineRoleIds));
    }
}
