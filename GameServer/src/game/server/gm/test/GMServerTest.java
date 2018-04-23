package game.server.gm.test;

import game.server.gm.GMCommandServer;

/**
 *
 * @author Administrator
 */
public class GMServerTest
{

    public static void main(String[] args) throws Exception
    {
        new GMCommandServer().getServer().accept(10000);
        System.out.println("Server started.");
    }

}
