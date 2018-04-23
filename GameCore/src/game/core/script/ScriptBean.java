/**
 * @date 2014/3/26 19:02
 * @author ChenLong
 */

package game.core.script;

/**
 * <b>脚本Bean</b>
 * @author ChenLong
 */
public class ScriptBean
{
    private volatile int id; // 脚本ID
    private volatile String name; // 脚本类名(全名)
    private volatile IScript script; // 脚本实例
    private long javaFileTimestamp; // .java文件时间戳
    private long classFileTimestamp; // .class文件时间戳

    /*
    public ScriptBean(int id, String name, IScript script)
    {
        this.id = id;
        this.name = name;
        this.script = script;
    }
    */

    public int getId()
    {
        return id;
    }

    public ScriptBean setId(int id)
    {
        this.id = id;
        return this;
    }

    public String getName()
    {
        return name;
    }

    public ScriptBean setName(String name)
    {
        this.name = name;
        return this;
    }

    public IScript getScript()
    {
        return script;
    }

    public ScriptBean setScript(IScript script)
    {
        this.script = script;
        return this;
    }

    public long getJavaFileTimestamp()
    {
        return javaFileTimestamp;
    }

    public ScriptBean setJavaFileTimestamp(long javaFileTimestamp)
    {
        this.javaFileTimestamp = javaFileTimestamp;
        return this;
    }

    public long getClassFileTimestamp()
    {
        return classFileTimestamp;
    }

    public ScriptBean setClassFileTimestamp(long classFileTimestamp)
    {
        this.classFileTimestamp = classFileTimestamp;
        return this;
    }
}
