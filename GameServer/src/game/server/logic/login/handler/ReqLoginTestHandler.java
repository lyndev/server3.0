/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.logic.login.handler;

import game.core.command.Handler;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author Administrator
 */
public class ReqLoginTestHandler extends Handler
{
    private final Logger log = Logger.getLogger(ReqLoginTestHandler.class);
    
    @Override
    public void action()
    {
        IoSession session = getMessage().getSession();
    }
}
