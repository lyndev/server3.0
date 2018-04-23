/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.logic.line.handler;

import game.core.command.Handler;
import game.server.logic.line.GameLineManager;
import java.util.UUID;

/**
 *
 * @author Administrator
 */
public class GrabTreasureMatchHandler extends Handler
{
    private final int LineNum;
    private final UUID nodeId;
    private final int level;
    private final int itemId;
    public GrabTreasureMatchHandler(int lineNum, UUID node, int level, int itemId)
    {
        this.LineNum = lineNum;
        this.nodeId = node;
        this.level = level;
        this.itemId = itemId;
    }
    
    @Override
    public void action()
    {
        GameLineManager.getInstance().getLine(LineNum).grabTreasureMatchPlayer(nodeId, level, itemId);
    }
    
}
