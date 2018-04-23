package org.apache.ibatis.session;

import java.sql.Connection;

public interface SqlSessionFactory {

    public SqlSession openSession();

    public SqlSession openSession(boolean bln);

    public SqlSession openSession(Connection cnctn);

    public SqlSession openSession(TransactionIsolationLevel til);

    public SqlSession openSession(ExecutorType et);

    public SqlSession openSession(ExecutorType et, boolean bln);

    public SqlSession openSession(ExecutorType et, TransactionIsolationLevel til);

    public SqlSession openSession(ExecutorType et, Connection cnctn);

    public Configuration getConfiguration();
}
