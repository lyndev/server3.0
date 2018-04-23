/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.analyse;

/**
 *
 * @author Administrator
 */
public class Main
{
    public static void main(String[] args)
    {
        DataAnalyzer analyzer = new DataAnalyzer();
        analyzer.loadConfig();
        analyzer.analyse();
    }
}
