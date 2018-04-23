/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.world.operActivity.handler;

import game.core.command.Handler;
import game.server.world.operActivity.OperActivityWorldService;

/**
 * 检查是否有新活动
 * @author Administrator
 */
public class LWCheckNewActivityHandler extends Handler
{
    private final long userId;
    
    public LWCheckNewActivityHandler(long userId){
        this.userId = userId;
    }
    
    @Override
    public void action(){
        OperActivityWorldService.getInstance().checkHaveNewActivity(userId);
    }

}
