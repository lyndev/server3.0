package org.apache.ibatis.session;

import java.io.Closeable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.executor.BatchResult;

public interface SqlSession extends Closeable {

    public <T extends Object> T selectOne(String string);

    public <T extends Object> T selectOne(String string, Object o);

    public <E extends Object> List<E> selectList(String string);

    public <E extends Object> List<E> selectList(String string, Object o);

    public <E extends Object> List<E> selectList(String string, Object o, RowBounds rb);

    public <K extends Object, V extends Object> Map<K, V> selectMap(String string, String string1);

    public <K extends Object, V extends Object> Map<K, V> selectMap(String string, Object o, String string1);

    public <K extends Object, V extends Object> Map<K, V> selectMap(String string, Object o, String string1, RowBounds rb);

    public void select(String string, Object o, ResultHandler rh);

    public void select(String string, ResultHandler rh);

    public void select(String string, Object o, RowBounds rb, ResultHandler rh);

    public int insert(String string);

    public int insert(String string, Object o);

    public int update(String string);

    public int update(String string, Object o);

    public int delete(String string);

    public int delete(String string, Object o);

    public void commit();

    public void commit(boolean bln);

    public void rollback();

    public void rollback(boolean bln);

    public List<BatchResult> flushStatements();

    public void close();

    public void clearCache();

    public Configuration getConfiguration();

    public <T extends Object> T getMapper(Class<T> type);

    public Connection getConnection();
}
