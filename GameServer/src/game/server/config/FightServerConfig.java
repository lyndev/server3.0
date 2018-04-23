/**
 * @date 2014/7/17
 * @author ChenLong
 */
package game.server.config;

/**
 *
 * @author ChenLong
 */
public class FightServerConfig
{
    private final String ip;
    private final int port;

    public FightServerConfig(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
    }

    public String getIp()
    {
        return ip;
    }

    public int getPort()
    {
        return port;
    }
}
