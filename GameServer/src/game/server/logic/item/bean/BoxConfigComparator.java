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
public class BoxConfigComparator implements Comparator<BoxConfig>
{
    @Override
    public int compare(BoxConfig o1, BoxConfig o2)
    {
        if (o1.getWeight() < o2.getWeight())
            return -1;
        else if (o1.getWeight() > o2.getWeight())
            return 1;
        else
            return 0;
    }
}
