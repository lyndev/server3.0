/**
 * @date 2014/5/20
 * @author ChenLong
 */

package game.server.logic.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 * 登录事务锁
 * 登录需要先在GameLine线程处理, 然后请求GameDBOperator线程查询数据库, 返回结果到GameLine处理
 * 这过程中一个玩家只允许有一个处理流
 * @author ChenLong
 */
public class LoginTransactionLock
{
    public final static int TIMEOUT_MILLIS = 40 * 1000; // 事务超时时间20秒, 避免因失误导致长时间锁定
    private final Logger log = Logger.getLogger(LoginTransactionLock.class);
    private final Map<String, LoginTransaction> transactions = new HashMap<>();

    /**
     * 是否已有事务?
     * @param userName
     * @return
     */
    public boolean hasTransaction(String userName)
    {
        return transactions.containsKey(userName);
    }

    /**
     * 记录事务
     * @param userName
     * @param session
     */
    public void recordTransaction(String userName, IoSession session)
    {
        LoginTransaction transaction = new LoginTransaction(userName, session);
        transactions.put(userName, transaction);
    }

    public void removeTransaction(String userName)
    {
        transactions.remove(userName);
    }
    
    /**
     * 定时器"心跳", 清除超时事务
     * @param currentTimeMillis
     */
    public void tick(long currentTimeMillis)
    {
        List<String> removeList = new LinkedList<>();
        for (Map.Entry<String, LoginTransaction> entry : transactions.entrySet())
        {
            String userName = entry.getKey();
            LoginTransaction transaction = entry.getValue();
            if (currentTimeMillis > transaction.timeout)
                removeList.add(userName);
        }
        
        for (String userName : removeList)
        {
            log.warn("tick removeTransaction, userName: [" + userName + "]");
            removeTransaction(userName);
        }
    }

    public static class LoginTransaction
    {
        public String userName;
        public IoSession session;
        public final long timeout = System.currentTimeMillis() + LoginTransactionLock.TIMEOUT_MILLIS; // 到期时间(事务超时清除处理)

        LoginTransaction(String userName, IoSession session)
        {
            this.userName = userName;
            this.session = session;
        }
    }
}
