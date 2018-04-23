/**
 * @date 2014/7/24
 * @author ChenLong
 */
package game.server.config;

/**
 * MongoDB配置
 *
 * @author ChenLong
 */
public class MongoDBConfig
{
    private final String gameId;
    private final String mongodbHost;
    private final int mongodbPort;
    private final String mongodbName;
    private final String mongodbUser;
    private final String mongodbPasswd;

    public MongoDBConfig(String gameId, String mongodbHost, int mongodbPort, String mongodbName, String mongodbUser, String mongodbPasswd)
    {
        this.gameId = gameId;
        this.mongodbHost = mongodbHost;
        this.mongodbPort = mongodbPort;
        this.mongodbName = mongodbName;
        this.mongodbUser = mongodbUser;
        this.mongodbPasswd = mongodbPasswd;
    }

    public String getGameId()
    {
        return gameId;
    }

    public String getMongodbHost()
    {
        return mongodbHost;
    }

    public int getMongodbPort()
    {
        return mongodbPort;
    }

    public String getMongodbName()
    {
        return mongodbName;
    }

    public String getMongodbUser()
    {
        return mongodbUser;
    }

    public String getMongodbPasswd()
    {
        return mongodbPasswd;
    }
}
