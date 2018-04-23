package game.server.world.rank.struct;

import game.message.RankMessage;
import java.util.List;

/**
 *
 * <b>排名信息.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class RankInfo implements Cloneable
{

    private long userId; // 用户ID

    private String roleName; // 昵称

    private int roleHead; // 头像

    private int rank; // 名次

    private int rankValue; // 分数

    private int bfStar; // 战场星级

    private List<Card> cardList; // 计算名次时玩家的进攻/防守阵容

    private boolean isDel; // 删除标记，如果为true，表示应该从对应的排行榜中删除

    public RankMessage.RankInfo.Builder getMessage()
    {
        RankMessage.RankInfo.Builder message = RankMessage.RankInfo.newBuilder();
        message.setRoleHead(roleHead);
        message.setRoleName(roleName);
        message.setRank(rank);
        message.setRankValue(rankValue);

        if (cardList != null && !cardList.isEmpty())
        {
            for (Card card : cardList)
            {
                message.addCardList(card.getMessage());
            }
        }

        return message;
    }

    public void addRankValue(int value)
    {
        rankValue += value;
    }

    public void subRankValue(int value)
    {
        rankValue -= value;
        if (rankValue < 0)
            rankValue = 0;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public int getBeanId()
    {
        return (bfStar - 2) * 8 + rank;
    }
     
    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    public int getRoleHead()
    {
        return roleHead;
    }

    public void setRoleHead(int roleHead)
    {
        this.roleHead = roleHead;
    }

    public int getRank()
    {
        return rank;
    }

    public void setRank(int rank)
    {
        this.rank = rank;
    }

    public int getRankValue()
    {
        return rankValue;
    }

    public void setRankValue(int rankValue)
    {
        this.rankValue = rankValue;
    }

    public int getBfStar()
    {
        return bfStar;
    }

    public void setBfStar(int bfStar)
    {
        this.bfStar = bfStar;
    }

    public List<Card> getCardList()
    {
        return cardList;
    }

    public void setCardList(List<Card> cardList)
    {
        this.cardList = cardList;
    }

    public boolean isDel()
    {
        return isDel;
    }

    public void setIsDel(boolean isDel)
    {
        this.isDel = isDel;
    }

    public class Card implements Cloneable
    {

        private int modelId; // 模板id

        private int cardLevel; // 卡牌等级

        private int qualityLevel; // 卡牌品质

        private int starLevel; // 卡牌星级

        public RankMessage.CardInfo.Builder getMessage()
        {
            RankMessage.CardInfo.Builder message = RankMessage.CardInfo.newBuilder();
            message.setModelId(modelId);
            message.setCardLevel(cardLevel);
            message.setStarLevel(starLevel);
            message.setQualityLevel(qualityLevel);

            return message;
        }

        @Override
        public Object clone() throws CloneNotSupportedException
        {
            return super.clone();
        }

        public int getModelId()
        {
            return modelId;
        }

        public void setModelId(int modelId)
        {
            this.modelId = modelId;
        }

        public int getCardLevel()
        {
            return cardLevel;
        }

        public void setCardLevel(int cardLevel)
        {
            this.cardLevel = cardLevel;
        }

        public int getQualityLevel()
        {
            return qualityLevel;
        }

        public void setQualityLevel(int qualityLevel)
        {
            this.qualityLevel = qualityLevel;
        }

        public int getStarLevel()
        {
            return starLevel;
        }

        public void setStarLevel(int starLevel)
        {
            this.starLevel = starLevel;
        }

    }

}
