package game.core.script.action;

import game.core.script.IScript;

public class ActionScript implements IScript
{
    private final int msgId = 1001;

    @Override
    public Object call(int scriptId, Object arg)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    class InCls
    {
        public void displayId()
        {
            System.out.println("InCls mmmmmmmmm " + msgId);
        }
    }

    public int getId()
    {
        new InCls().displayId();
        System.out.println("in getId ttttttttt " + msgId);
        return msgId;
    }

    public void action()
    {
        System.out.println("in ActionScript");
    }
}
