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
public class BoxItems
{

    private final int weight;

    private final int[] itemIds;

    public BoxItems(int weight, int[] itemIds)
    {
        this.weight = weight;
        this.itemIds = itemIds;
    }

    public int getWeight()
    {
        return weight;
    }

    public int[] getItemIds()
    {
        return itemIds;
    }
}
