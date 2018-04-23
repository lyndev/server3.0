package game.core.db;

import game.core.logging.Log;

/**
 *
 * @author Administrator
 */
public class UserLog extends Log
{

    private Integer id;
    private String description;

    public UserLog() {
        super(UserLogHandler.class);
    }
    
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

}
