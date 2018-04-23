/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.logic.item.bean;

import game.core.script.ScriptManager;
import game.server.logic.util.ScriptArgs;

/**
 * 策划增加“道具类型”的道具
 *
 * @author YangHanzhou
 */
public final class Prop extends Item
{
    @Override
    public boolean doUse(Object target)
    {
        // 七宝仙府调用脚本
        if (159999 == this.getId())
        {
            ScriptArgs args = new ScriptArgs();
            args.put(ScriptArgs.Key.ITEM, this);
            args.put(ScriptArgs.Key.PLAYER, target);

            return ScriptManager.getInstance().call(getScriptId(), args) != null;
        }
        else
        {
            return super.doUse(target);
        }
    }
}
