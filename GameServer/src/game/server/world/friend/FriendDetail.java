package game.server.world.friend;

import game.server.world.friend.bean.Friend;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import game.data.bean.q_globalBean;
import game.message.FriendMessage;
import game.server.logic.support.BeanTemplet;
import game.server.logic.support.IJsonConverter;
import game.server.util.UniqueId;
import game.server.world.GameWorld;
import game.server.world.wplayer.WPlayer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author ChenLong
 */
public class FriendDetail implements IJsonConverter
{
    private final static Logger logger = Logger.getLogger(FriendDetail.class);
    private transient long lastSaveTime = System.currentTimeMillis();
    private transient String md5sum = StringUtils.EMPTY;
    private long roleId;
    private long userId;
    private final Map<Long, Friend> friends = new HashMap<>(); // peer userId --> Friend
    private final Set<Long> passiveIds = new HashSet<>();    // 当前玩家上线或下线需要通知的玩家Id

    public void tick5()
    {
    }

    @Override
    public JSON toJson()
    {
        JSONObject jsonObj = new JSONObject();
        { // friends
            JSONArray jsonArray = new JSONArray();
            for (Map.Entry<Long, Friend> entry : friends.entrySet())
            {
                Friend friend = entry.getValue();
                jsonArray.add(friend.toJson());
            }
            jsonObj.put("friends", jsonArray);

            //passiveIds
            jsonArray = new JSONArray();
            for (Long passiveId : passiveIds)
            {
                jsonArray.add(passiveId);
            }
            jsonObj.put("passiveIds", jsonArray);
        }
        return jsonObj;
    }

    @Override
    public void fromJson(JSON json)
    {
        do
        {
            if (json == null)
            {
                logger.error("FriendDetail fromJson json == null");
                break;
            }
            if (!(json instanceof JSONObject))
            {
                logger.error("FriendDetail fromJson json not instanceof JSONObject");
                break;
            }
            JSONObject jsonObj = (JSONObject) json;
            { // friends
                JSONArray jsonArray = jsonObj.getJSONArray("friends");
                for (int i = 0; i < jsonArray.size(); ++i)
                {
                    JSONObject friendJson = jsonArray.getJSONObject(i);
                    Friend friend = new Friend();
                    friend.fromJson(friendJson);
                    friends.put(friend.getUserId(), friend);
                }

                //passiveIds
                jsonArray = jsonObj.getJSONArray("passiveIds");
                for (int i = 0; i < jsonArray.size(); ++i)
                {
                    Long passiveId = jsonArray.getLongValue(i);
                    passiveIds.add(passiveId);
                }
            }
        } while (false);
    }

    /**
     * 好友已加满 ?
     *
     * @return
     */
    public boolean hasFullFriend()
    {
        boolean result = true;
        do
        {
            q_globalBean globalBean = BeanTemplet.getGlobalBean(2070);
            if (globalBean == null)
            {
                logger.error("无法找到全局变量2070配置信息");
                break;
            }
            int maxFriends = globalBean.getQ_int_value();
            if (maxFriends < 0)
            {
                logger.error("全局变量1151配置错误, 值为: " + maxFriends);
                break;
            }
            if (getFriendNum() >= maxFriends)
            {
                logger.error("自己好友已满");
                break;
            }
            result = false;
        } while (false);
        return result;
    }

    /**
     * 获取所有好友信息
     *
     * @return
     */
    public FriendMessage.ResAllFriendInfo.Builder getResAllFriendInfo()
    {
        FriendMessage.ResAllFriendInfo.Builder resMsg = FriendMessage.ResAllFriendInfo.newBuilder();
        if (!friends.isEmpty())
        {
            FriendMessage.Friend.Builder builder = FriendMessage.Friend.newBuilder();
            for (Map.Entry<Long, Friend> entry : friends.entrySet())
            {
                Friend friend = entry.getValue();
                WPlayer wplayer = GameWorld.getInstance().getPlayer(friend.getUserId());
                if (wplayer != null)
                {
                    long peerUserId = wplayer.getUserId();
                    builder.clear();

                    builder.setUserId(UniqueId.toBase36(peerUserId));
                    builder.setRoleName(wplayer.getRoleName());
                    builder.setRoleLevel(wplayer.getRoleLevel());
                    builder.setRoleHead(wplayer.getRoleHead());
                    builder.setIsOnline((wplayer.getSession() != null) ? 1 : 0);
                    builder.setAddFriendTime((int) (friend.getTimestamp() / 1000L));
                    builder.setType(friend.getType());
                    resMsg.addFriends(builder.build());
                }
                else
                {
                    logger.error("getResAllFriendInfo, GameWorld找不到玩家 userid = " + friend.getUserId());
                }
            }
        }
        return resMsg;
    }

    /**
     * 获取单个好友信息
     *
     * @param peerUserId
     * @return
     */
    public FriendMessage.Friend.Builder getResFriendInfo(long peerUserId)
    {
        WPlayer wplayer = GameWorld.getInstance().getPlayer(peerUserId);
        FriendMessage.Friend.Builder builder = FriendMessage.Friend.newBuilder();
        if (wplayer != null)
        {
            builder.setUserId(UniqueId.toBase36(peerUserId));
            builder.setRoleName(wplayer.getRoleName());
            builder.setRoleLevel(wplayer.getRoleLevel());
            builder.setRoleHead(wplayer.getRoleHead());
            builder.setIsOnline((wplayer.getSession() != null) ? 1 : 0);
            Friend friend = getFriend(peerUserId);
            if (friend != null)
            {
                builder.setAddFriendTime((int) (friend.getTimestamp() / 1000L));
                builder.setType(friend.getType());
            }
        }
        return builder;
    }

    /**
     * 获取好友数量
     *
     * @return
     */
    public int getFriendNum()
    {
        int cnt = 0;
        for (Friend friend : friends.values())
        {
            if (!friend.isTmpFriend())
            {
                ++cnt;
            }
        }
        return cnt;
    }

    public boolean hasFriend(long peerUserId)
    {
        return friends.containsKey(peerUserId) && !friends.get(peerUserId).isTmpFriend();
    }

    public boolean hasTmpFriend(long peerUserId)
    {
        return friends.containsKey(peerUserId) && friends.get(peerUserId).isTmpFriend();
    }

    public Friend getFriend(long peerUserId)
    {
        return friends.get(peerUserId);
    }

    public Set<Long> getAllFriendUserIdSet()
    {
        Set<Long> list = new HashSet<>();
        for (Friend friend : friends.values())
            list.add(friend.getUserId());
        return list;
    }

    public void addFriend(long peerUserId, int type)
    {
        Friend friend = new Friend();
        friend.setUserId(peerUserId);
        friend.setType(type);
        friends.put(friend.getUserId(), friend);
    }

    public void removeFriend(long peerUserId)
    {
        friends.remove(peerUserId);
    }

    /**
     * 删除所有临时好友
     */
    public void removeAllTmpFriend()
    {
        List<Long> lst = getAllTmpFriend();
        if (lst.isEmpty())
        {
            return;
        }

        for (Long userId : lst)
        {
            friends.remove(userId);
        }
    }

    /**
     * 获取所有临时好友
     *
     * @return
     */
    public List<Long> getAllTmpFriend()
    {
        List<Long> lst = new LinkedList<>();
        for (Friend friend : friends.values())
        {
            if (friend.isTmpFriend())
            {
                lst.add(friend.getUserId());
            }
        }
        return lst;
    }

    public String calcMd5Sum()
    {
        return DigestUtils.md5Hex(toJson().toJSONString());
    }

    public long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(long roleId)
    {
        this.roleId = roleId;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public long getLastSaveTime()
    {
        return lastSaveTime;
    }

    public void setLastSaveTime(long lastSaveTime)
    {
        this.lastSaveTime = lastSaveTime;
    }

    public String getMd5sum()
    {
        return md5sum;
    }

    public void setMd5sum(String md5sum)
    {
        this.md5sum = md5sum;
    }

    public void addPassivityId(Long peerUserId)
    {
        passiveIds.add(peerUserId);
    }

    public void removePassivityId(Long peerUserId)
    {
        passiveIds.remove(peerUserId);
    }

    public Set<Long> getPassivityIds()
    {
        return passiveIds;
    }
}
