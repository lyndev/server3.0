/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.timer;

import game.core.command.ICommand;

/**
 *
 * @author Administrator
 */
public class DailyTimerTest
{

    public static void main(String[] args)
    {
        SimpleTimerProcessor.getInstance().addEvent(new DailyTimer("baby", 9, new ICommand()
        {
            @Override
            public void action()
            {
                System.out.println("baby，我爱你，爱你，爱你，就是爱你！");
            }
        }).getTask());
    }

}
