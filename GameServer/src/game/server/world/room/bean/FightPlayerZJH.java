/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.world.room.bean;
import game.core.timer.TimerEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class FightPlayerZJH extends FightPlayerBase
{
    
    public int m_cellSocre = 0;
    private boolean m_isLookCard = false;
    
    // 玩家动作计时器
    private class ActionTimer extends TimerEvent
    {
        public ActionTimer(int interval)
        {
            super(interval * 1000);
        }

        @Override
        public void run()
        {
            //TODO:动作计时器执行
        }
    }

    private List<Integer> cards = new ArrayList<>();
    public void setCards(List<Integer> cards){
        this.cards = cards;
    }
    
    public void getCards(List<Integer> cards){
        this.cards = cards;
    }
    
    //单单次下注
    public int getCellScore(){
        return this.m_cellSocre;
    }
    
    //单次下注
    public void setCellScore(int cellScore){
        this.m_cellSocre = cellScore;
    }
    
    // 设置看牌状态
    public void setIsLookCard(boolean bLookCard){
        this.m_isLookCard = bLookCard;
    }
    
    public boolean isLookcard(){
        return this.m_isLookCard;
    }

}
