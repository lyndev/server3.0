/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.logic.item.bean;

/**
 *
 * @author Administrator
 */
public class BoxConfig
{

    private final int weight;

    private final BoxItems[] boxItems;

    public BoxConfig(int weight, BoxItems[] boxItems)
    {
        this.weight = weight;
        this.boxItems = boxItems;
    }

    public int getWeight()
    {
        return weight;
    }

    public BoxItems[] getBoxItems()
    {
        return boxItems;
    }
}
