package action;

import game.core.script.*;

public class ActionScript implements IScript {
    private final int msgId = 1001;

    class InCls {
        public void displayId() {
            System.out.println("InCls " + msgId);
        }
    }

    public int getId() {
        new InCls().displayId();
        return msgId;
    }

    public void action() {
        System.out.println("in ActionScript");
    }
}
