package game.core.util.json;

import java.util.UUID;

/**
 *
 * JSON测试Bean
 */
public class TestBean
{
    
    private UUID id;
    private UUID account;
    private String name;
    private byte age;
    private String address;
    private transient String phone;
    public String nickName;

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public UUID getAccount()
    {
        return account;
    }

    public void setAccount(UUID account)
    {
        this.account = account;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public byte getAge()
    {
        return age;
    }

    public void setAge(byte age)
    {
        this.age = age;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }
    
}
