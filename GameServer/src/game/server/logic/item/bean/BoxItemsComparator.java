/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.logic.item.bean;

import java.util.Comparator;

/**
 *
 * @author Administrator
 */
public class BoxItemsComparator implements Comparator<BoxItems>
{
    @Override
    public int compare(BoxItems o1, BoxItems o2)
    {
        if (o1.getWeight() < o2.getWeight())
            return -1;
        else if (o1.getWeight() > o2.getWeight())
            return 1;
        else
            return 0;
    }
}
