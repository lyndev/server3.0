/**
 * @date 2014/10/8
 * @author ChenLong
 */
package game.server.world.friend;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.core.message.SMessage;
import game.message.FriendMessage;
import game.server.db.game.bean.FriendDataBean;
import game.server.db.game.dao.RoleDao;
import game.server.logic.constant.ErrorCode;
import game.server.logic.struct.Player;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.DBView;
import game.server.thread.dboperator.GameDBOperator;
import game.server.thread.dboperator.handler.DBOperatorHandler;
import game.server.util.MessageUtils;
import game.server.util.MiscUtils;
import game.server.util.UniqueId;
import game.server.world.GameWorld;
import game.server.world.friend.handler.ModifyFriendNumHandler;
import game.server.world.wplayer.WPlayer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author ChenLong
 */
public class FriendManager
{
    public final static int FRIEND_FUNCTION = 130;
    private final static Logger logger = Logger.getLogger(FriendManager.class);
    private static long saveInterval = 60; // 保存间隔(秒)
    private final Map<Long, FriendDetail> friendDetails = new HashMap<>(); // self userId --> FriendDetail
    private long lastSaveTimestamp = System.currentTimeMillis(); // 上次次保存时间

    static
    {
        if (MiscUtils.isIDEEnvironment())
            saveInterval = 1;
    }

    public void tick(long currentTimeMillis)
    {
        if (currentTimeMillis - lastSaveTimestamp > saveInterval * 1000L)
        {
            save();
            lastSaveTimestamp = currentTimeMillis;
        }
    }

    public void tick5()
    {
        for (FriendDetail detail : friendDetails.values())
        {
            try
            {
                detail.tick5();
            }
            catch (Throwable t)
            {
                logger.error("Throwable", t);
            }
        }
    }

    public void load()
    {
        List<FriendDataBean> list = RoleDao.selectAllFriendData();
        for (FriendDataBean dataBean : list)
        {
            long roleId = UniqueId.toBase10(dataBean.getRoleId());
            long userId = UniqueId.toBase10(dataBean.getUserId());
            String friendData = StringUtils.EMPTY;
            if (!dataBean.getFriendData().trim().isEmpty())
                friendData = dataBean.getFriendData().trim();

            FriendDetail friendDetail = new FriendDetail();
            friendDetail.setRoleId(roleId);
            friendDetail.setUserId(userId);
            if (!friendData.trim().isEmpty())
            {
                try
                {
                    JSONObject jsonObj = JSON.parseObject(friendData);
                    friendDetail.fromJson(jsonObj);
                }
                catch (Throwable t)
                {
                    logger.error("Exception", t);
                }
            }
            friendDetail.setMd5sum(friendDetail.calcMd5Sum());
            friendDetails.put(userId, friendDetail);
        }
    }

    public void save()
    {
        for (Map.Entry<Long, FriendDetail> entry : friendDetails.entrySet())
        {
            FriendDetail friendDetail = entry.getValue();
            String md5sum = friendDetail.calcMd5Sum();
            if (!md5sum.equals(friendDetail.getMd5sum()))
            {
                saveFriendDetail(friendDetail);
                friendDetail.setLastSaveTime(System.currentTimeMillis());
                friendDetail.setMd5sum(md5sum);
            }
        }
    }

    /**
     * 如果没有FriendDetail时则初始化一个(新创建角色时)
     *
     * @param userId
     */
    public void createInitialize(long userId)
    {
        if (!friendDetails.containsKey(userId)) // 新创建的帐号
        {
            WPlayer wplayer = GameWorld.getInstance().getPlayer(userId);
            if (wplayer != null)
            {
                FriendDetail detail = new FriendDetail();
                detail.setRoleId(wplayer.getRoleId());
                detail.setUserId(wplayer.getUserId());

                friendDetails.put(detail.getUserId(), detail);
            }
            else
            {
                logger.error("createInitialize cannot find WPlayer, userId = " + userId);
            }
        }
    }

    /**
     * 客户端初始化完成
     *
     * @param player
     */
    public void clientInitializeOver(Player player)
    {
        long userId = player.getUserId();
        WPlayer wplayer = GameWorld.getInstance().getPlayer(userId);
        FriendDetail friendDetail = friendDetails.get(userId);
        if (friendDetail == null)
        {
            return;
        }
        sendResAllFriendInfo(wplayer.getSession(), friendDetail.getResAllFriendInfo(), ErrorCode.OK);
    }

    /**
     * 获取所有好友信息
     *
     * @param userId
     */
    public void onReqAllFriendInfo(long userId)
    {
        do
        {
            WPlayer wplayer = GameWorld.getInstance().getPlayer(userId);
            if (wplayer == null)
            {
                logger.error("reqAllFriendInfo, GameWorld找不到玩家 userid = " + userId);
                sendResAllFriendInfo(wplayer.getSession(), null, ErrorCode.WPLAYER_NOT_FOUND);
                break;
            }
            if (wplayer.getRoleLevel() < BeanTemplet.getFriendOpenLevel())
            {
                logger.error("reqAllFriendInfo, 主角等级未达到开启好友系统等级限制 userid = " + userId
                        + ", roleLevel = " + wplayer.getRoleLevel() + ", requireRoleLevel = " + BeanTemplet.getFriendOpenLevel());
                sendResAllFriendInfo(wplayer.getSession(), null, ErrorCode.FRIEND_LEADER_LV_TOO_LOW);
                break;
            }
            FriendDetail friendDetail = friendDetails.get(userId);
            if (friendDetail == null)
            {
                logger.error("reqAllFriendInfo, 找不到好友信息 userId = " + userId);
                sendResAllFriendInfo(wplayer.getSession(), null, ErrorCode.SELF_DETAIL_EXCEPTION);
                break;
            }
            sendResAllFriendInfo(wplayer.getSession(), friendDetail.getResAllFriendInfo(), ErrorCode.OK);
        } while (false);
    }

    /**
     * 申请好友
     *
     * @param userId
     * @param reqMsg
     */
    public void onReqApplyFriend(long userId, FriendMessage.ReqApplyFriend reqMsg)
    {
        do
        {
            WPlayer wplayer = GameWorld.getInstance().getPlayer(userId);
            if (wplayer == null)
            {
                logger.error("reqApplyFriend, GameWorld找不到玩家 userid = " + userId);
                break;
            }

            long peerUserId = UniqueId.toBase10(reqMsg.getUserId());
            if (wplayer.getRoleLevel() < BeanTemplet.getFriendOpenLevel())
            {
                logger.error("reqApplyFriend, 主角等级未达到开启好友系统等级限制 userid = " + userId
                        + ", roleLevel = " + wplayer.getRoleLevel() + ", requireRoleLevel = " + BeanTemplet.getFriendOpenLevel());
                sendResApplyFriend(wplayer.getSession(), peerUserId, ErrorCode.FRIEND_LEADER_LV_TOO_LOW, userId);
                break;
            }

            if (userId == peerUserId)
            {
                logger.error("reqApplyFriend 不能添加自己为好友, userId = " + userId);
                sendResApplyFriend(wplayer.getSession(), peerUserId, ErrorCode.FRINED_CAN_NOT_ADD_SELF, userId);
                break;
            }

            FriendDetail selfDetail = friendDetails.get(userId);
            if (selfDetail == null)
            {
                logger.error("自己的好友数据异常reqApplyFriend, selfDetail == null, userId = " + userId);
                sendResApplyFriend(wplayer.getSession(), peerUserId, ErrorCode.SELF_DETAIL_EXCEPTION, userId);
                break;
            }

            FriendDetail peerDetail = friendDetails.get(peerUserId);
            if (peerDetail == null)
            {
                logger.error("待加添加好友的数据异常reqApplyFriend, peerDetail == null, peerUserId = " + peerUserId);
                sendResApplyFriend(wplayer.getSession(), peerUserId, ErrorCode.FRIEND_DETAIL_EXCEPTION, userId);
                break;
            }

            if (selfDetail.hasFriend(peerUserId))
            {
                logger.error("对方已经是好友, selfUserId = " + userId + ", peerUserId = " + peerUserId);
                sendResApplyFriend(wplayer.getSession(), peerUserId, ErrorCode.FRINED_ALREADY_FRIEND, userId);
                break;
            }

            if (reqMsg.getType() == 1 && selfDetail.hasFullFriend())
            {
                logger.error("自己好友已满");
                sendResApplyFriend(wplayer.getSession(), peerUserId, ErrorCode.FRINED_CNT_MAX, userId);
                break;
            }
            addFriend(wplayer.getSession(), userId, peerUserId, reqMsg.getType(), false);
        } while (false);
    }

    /**
     * 删除好友
     *
     * @param userId
     * @param reqMsg
     */
    public void onReqRemoveFriend(long userId, FriendMessage.ReqRemoveFriend reqMsg)
    {
        do
        {
            long peerUserId = UniqueId.toBase10(reqMsg.getUserId());
            WPlayer wplayer = GameWorld.getInstance().getPlayer(userId);
            if (wplayer == null)
            {
                logger.error("reqRemoveFriend, GameWorld找不到玩家 userid = " + userId);
                break;
            }

            if (wplayer.getRoleLevel() < BeanTemplet.getFriendOpenLevel())
            {
                logger.error("reqRemoveFriend, 主角等级未达到开启好友系统等级限制 userid = " + userId
                        + ", roleLevel = " + wplayer.getRoleLevel() + ", requireRoleLevel = " + BeanTemplet.getFriendOpenLevel());
                sendResRemoveFriend(wplayer.getSession(), peerUserId, ErrorCode.FRIEND_LEADER_LV_TOO_LOW);
                break;
            }

            FriendDetail selfDetail = friendDetails.get(userId);
            if (selfDetail == null)
            {
                logger.error("自己好友数据异常reqRemoveFriend, selfDetail == null, userId = " + userId);
                sendResRemoveFriend(wplayer.getSession(), peerUserId, ErrorCode.SELF_DETAIL_EXCEPTION);
                break;
            }

            FriendDetail peerDetail = friendDetails.get(peerUserId);
            if (peerDetail == null)
            {
                logger.error("对方好友数据异常reqRemoveFriend, peerDetail == null, userId = " + peerUserId);
                sendResRemoveFriend(wplayer.getSession(), peerUserId, ErrorCode.FRIEND_DETAIL_EXCEPTION);
                break;
            }

            if (!selfDetail.hasFriend(peerUserId) && !selfDetail.hasTmpFriend(peerUserId))
            {
                logger.error("reqRemoveFriend, 还不是好友, selfUserId = " + userId + ", peerUserId = " + peerUserId);
                sendResRemoveFriend(wplayer.getSession(), peerUserId, ErrorCode.NOT_YOU_FRIEND);
                break;
            }

            // 回复成功
            selfDetail.removeFriend(peerUserId);
            peerDetail.removePassivityId(peerUserId);
            sendResRemoveFriend(wplayer.getSession(), peerUserId, ErrorCode.OK);
            //删除聊天记录
            GameWorld.getInstance().getChatManager().getPrivateChatManager().removeChatRecord(userId, peerUserId);
            // 更新好友数量
            MessageUtils.sendToGameLine(wplayer.getLineId(), new ModifyFriendNumHandler().set(userId, selfDetail.getFriendNum()));
        } while (false);
    }

    /**
     * 搜索好友
     *
     * @param userId
     * @param reqMsg
     */
    public void onReqSearchFriend(long userId, FriendMessage.ReqSearchFriend reqMsg)
    {
        do
        {
            WPlayer wplayer = GameWorld.getInstance().getPlayer(userId);
            if (wplayer == null)
            {
                logger.error("reqSearchFriend, GameWorld找不到玩家 userid = " + userId);
                break;
            }
            if (wplayer.getRoleLevel() < BeanTemplet.getFriendOpenLevel())
            {
                logger.error("reqSearchFriend, 主角等级未达到开启好友系统等级限制 userid = " + userId
                        + ", roleLevel = " + wplayer.getRoleLevel() + ", requireRoleLevel = " + BeanTemplet.getFriendOpenLevel());
                sendResSearchFriend(wplayer.getSession(), null, ErrorCode.FRIEND_LEADER_LV_TOO_LOW);
                break;
            }
            String searchString = reqMsg.getSearchString();
            List<Long> userIds = DBView.getInstance().searchFriend(searchString);

            sendResSearchFriend(wplayer.getSession(), getFriendMessage(userIds), ErrorCode.OK);
        } while (false);
    }

    /**
     * 玩家上线后的处理
     *
     * @param userId
     */
    public void onlineProcess(long userId)
    {
        do
        {
            FriendDetail detail = friendDetails.get(userId);
            if (detail == null)
            {
                break;
            }
            Set<Long> passivityIds = detail.getPassivityIds();
            if (passivityIds == null || passivityIds.size() < 1)
            {
                break;
            }

            //向所有在线好友发送上线消息
            for (Long peerUserId : passivityIds)
            {
                WPlayer wplayer = GameWorld.getInstance().getPlayer(peerUserId);
                if (wplayer != null)
                {
                    sendNotifyMeOnline(wplayer.getSession(), userId);
                }
            }
        } while (false);
    }

    /**
     * 玩家下线后的处理
     *
     * @param userId
     */
    public void offlineProcess(long userId)
    {
        do
        {
            FriendDetail detail = friendDetails.get(userId);
            if (detail == null)
            {
                break;
            }

            //删除临时好友的被动列表
            List<Long> lst = detail.getAllTmpFriend();
            if (lst.isEmpty())
            {
                for (Long passiveId : lst)
                {
                    FriendDetail peerDetail = friendDetails.get(passiveId);
                    if (peerDetail != null)
                    {
                        peerDetail.removePassivityId(passiveId);
                    }
                }
            }

            //删除所有临时好友
            detail.removeAllTmpFriend();

            //通知所有好友
            Set<Long> passivityIds = detail.getPassivityIds();
            if (passivityIds == null || passivityIds.size() < 1)
            {
                break;
            }

            //向所有在线好友发送下线消息
            for (Long peerUserId : passivityIds)
            {
                WPlayer wplayer = GameWorld.getInstance().getPlayer(peerUserId);
                if (wplayer != null)
                {
                    sendNofityMeOffline(wplayer.getSession(), userId);
                }
            }
        } while (false);
    }

    /**
     * 添加好友
     *
     * @param session
     * @param userId
     * @param peerUserId
     * @param type
     */
    public void addFriend(IoSession session, long userId, long peerUserId, int type, boolean addSelf)
    {
        if (type == 1)
        { //添加正式好友
            FriendDetail selfDetail = friendDetails.get(userId);
            if (selfDetail == null || selfDetail.hasFriend(peerUserId))
            {
                return;
            }

            selfDetail.addFriend(peerUserId, type);
            FriendDetail detailPeer = friendDetails.get(peerUserId);
            if (detailPeer != null)
            {
                detailPeer.addPassivityId(userId);
            }
            sendResApplyFriend(session, peerUserId, ErrorCode.OK, userId);
        }
        else
        { //添加临时好友
            FriendDetail selfDetail = friendDetails.get(userId);
            FriendDetail peerDetail = friendDetails.get(peerUserId);

            //添加对方为自己的临时好友
            if (selfDetail != null && selfDetail.getFriend(peerUserId) == null)
            {
                selfDetail.addFriend(peerUserId, 0);
                if (peerDetail != null)
                {
                    peerDetail.addPassivityId(userId);
                }
                sendResApplyFriend(session, peerUserId, ErrorCode.OK, userId);
            }

            //添加自己为对方的临时好友
            if (peerDetail != null && peerDetail.getFriend(userId) == null && addSelf)
            {
                peerDetail.addFriend(userId, 0);
                if (selfDetail != null)
                {
                    selfDetail.addPassivityId(peerUserId);
                }
                WPlayer wPlayer = GameWorld.getInstance().getPlayer(peerUserId);
                if (wPlayer != null)
                {
                    sendResApplyFriend(wPlayer.getSession(), userId, ErrorCode.OK, peerUserId);
                }
            }
        }
    }

    /**
     * 获取玩家好友数量
     *
     * @param userId
     * @return
     */
    public int getFriendNum(long userId)
    {
        int num = 0;
        FriendDetail detail = friendDetails.get(userId);
        if (detail != null)
            num = detail.getFriendNum();
        else
            logger.error("getFriendNum, cannot find FriendDetail, userId = " + userId);
        return num;
    }

    /**
     * 获取所有临时好友Id
     *
     * @param userId
     * @return
     */
    public List<Long> getAllTmpFriendIds(long userId)
    {
        FriendDetail detail = friendDetails.get(userId);
        if (detail != null)
        {
            return detail.getAllTmpFriend();
        }
        return null;
    }

    private void saveFriendDetail(FriendDetail friendDetail)
    {
        GameDBOperator.getInstance().submitRequest(new DBOperatorHandler(0)
        {
            private FriendDataBean dataBean;

            public DBOperatorHandler set(FriendDetail friendDetail)
            {
                dataBean = new FriendDataBean();
                dataBean.setRoleId(UniqueId.toBase36(friendDetail.getRoleId()));
                dataBean.setFriendData(friendDetail.toJson().toJSONString());
                return this;
            }

            @Override
            public void action()
            {
                RoleDao.updateFriendData(dataBean);
            }
        }.set(friendDetail));
    }

    private List<FriendMessage.Friend> getFriendMessage(List<Long> userIds)
    {
        List<FriendMessage.Friend> list = new LinkedList<>();
        if (!userIds.isEmpty())
        {
            FriendMessage.Friend.Builder friendMsg = FriendMessage.Friend.newBuilder();
            for (Long userId : userIds)
            {
                WPlayer wplayer = GameWorld.getInstance().getPlayer(userId);
                if (wplayer != null)
                {
                    friendMsg.clear();
                    friendMsg.setUserId(UniqueId.toBase36(wplayer.getUserId()));

                    friendMsg.setRoleName(wplayer.getRoleName());
                    friendMsg.setRoleLevel(wplayer.getRoleLevel());
                    friendMsg.setRoleHead(wplayer.getRoleHead());
                    friendMsg.setIsOnline((wplayer.getSession() != null) ? 1 : 0);

                    list.add(friendMsg.buildPartial());
                }
                else
                {
                    logger.error("getFriendMessage, GameWorld找不到玩家 userid = " + userId);
                }
            }
        }
        return list;
    }

    /**
     * 发送所有好友信息
     *
     * @param session
     * @param resMsg
     */
    private void sendResAllFriendInfo(IoSession session, FriendMessage.ResAllFriendInfo.Builder resMsg, ErrorCode errorCode)
    {
        if (session == null || resMsg == null)
        {
            return;
        }

        resMsg.setErrorCode(errorCode.getCode());
        MessageUtils.send(session, new SMessage(FriendMessage.ResAllFriendInfo.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
    }

    /**
     * 发送申请好友结果
     *
     * @param session
     * @param userId
     * @param errorCode
     */
    private void sendResApplyFriend(IoSession session, Long peerUserId, ErrorCode errorCode, Long userId)
    {
        FriendMessage.ResApplyFriend.Builder resMsg = FriendMessage.ResApplyFriend.newBuilder();
        resMsg.setUserId(UniqueId.toBase36(peerUserId));
        resMsg.setErrorCode(errorCode.getCode());

        FriendDetail detail = friendDetails.get(userId);
        if (detail != null)
        {
            resMsg.setFriends(detail.getResFriendInfo(peerUserId));
        }
        MessageUtils.send(session, new SMessage(FriendMessage.ResApplyFriend.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
    }

    /**
     * 发送删除好友结果
     *
     * @param session
     * @param userId
     * @param errorCode
     */
    private void sendResRemoveFriend(IoSession session, long userId, ErrorCode errorCode)
    {
        FriendMessage.ResRemoveFriend.Builder resMsg = FriendMessage.ResRemoveFriend.newBuilder();
        resMsg.setUserId(UniqueId.toBase36(userId));
        resMsg.setErrorCode(errorCode.getCode());
        MessageUtils.send(session, new SMessage(FriendMessage.ResRemoveFriend.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
    }

    /**
     * 发送搜索好友结果
     *
     * @param session
     * @param friends
     * @param errorCode
     */
    private void sendResSearchFriend(IoSession session, List<FriendMessage.Friend> friends, ErrorCode errorCode)
    {
        FriendMessage.ResSearchFriend.Builder resMsg = FriendMessage.ResSearchFriend.newBuilder();
        if (friends != null)
        {
            resMsg.addAllFriends(friends);
        }
        resMsg.setErrorCode(errorCode.getCode());
        MessageUtils.send(session, new SMessage(FriendMessage.ResSearchFriend.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
    }

    /**
     * 发送上线通知消息
     *
     * @param session
     * @param userId
     */
    private void sendNotifyMeOnline(IoSession session, long userId)
    {
        if (session == null)
        {
            return;
        }

        FriendMessage.NotifyFriendOnline.Builder resMsg = FriendMessage.NotifyFriendOnline.newBuilder();
        resMsg.setUserId(UniqueId.toBase36(userId));
        MessageUtils.send(session, new SMessage(FriendMessage.NotifyFriendOnline.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
    }

    /**
     * 发送下线通知消息
     *
     * @param session
     * @param userId
     */
    private void sendNofityMeOffline(IoSession session, long userId)
    {
        if (session == null)
        {
            return;
        }

        FriendMessage.NotifyFriendOffline.Builder resMsg = FriendMessage.NotifyFriendOffline.newBuilder();
        resMsg.setUserId(UniqueId.toBase36(userId));
        MessageUtils.send(session, new SMessage(FriendMessage.NotifyFriendOffline.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()));
    }
}
