/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.server.logic.constant;

/**
 *
 * @author YangHanzhou
 */
public enum BuildingState
{
    /**
     * 普通状态
     */
    BUILD_COMMON(1),
    /**
     * 修建中状态
     */
    BUILD_BUILDING(2),
    /**
     * 修建完成状态
     */
    BUILD_OVER(3);
 

    private final int value;

    BuildingState(int value)
    {
        this.value = value;
    }

    public int value()
    {
        return value;
    }

    public boolean compare(int value)
    {
        return this.value == value;
    }

    public static BuildingState getType(int value)
    {
        for (BuildingState type : BuildingState.values())
        {
            if (type.value() == value)
                return type;
        }
        return null;
    }
}

