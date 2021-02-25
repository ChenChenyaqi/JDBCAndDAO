package com.myCompany.dao;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 封装了针对于数据表的通用操作
 *
 * @author chenyaqi
 * @date 2021/2/25 - 12:24
 */
public abstract class BaseDAO<T> {
    private Class<T> clazz = null;
    private QueryRunner runner = new QueryRunner();

    {
        // 获取当前BaseDAO的子类继承的父类中的泛型
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) genericSuperclass;
        Type[] typeArguments = paramType.getActualTypeArguments();// 获取父类的泛型参数
        clazz = (Class<T>)typeArguments[0];//泛型的第一个参数
    }

    /**
     * 通用的增删改操作（考虑上事务）
     *
     * @param sql  mysql语句
     * @param args 填充的参数
     * @return 最后一个操作的行数
     */
    public int updateData(Connection conn, String sql, Object... args) {//sql中占位符的个数与可变形参的长度一致
        int updateCount = 0;
        try {
            updateCount = runner.update(conn, sql, args);
            return updateCount;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 表的通用的查询操作（返回单个对象）(考虑上事务)
     *
     * @param sql  MySQL语句
     * @param args 参数
     * @return Student 对象
     */
    public T queryData(Connection conn, String sql, Object... args) {
        BeanHandler<T> handler = new BeanHandler<T>(clazz);
        try {
            T query = runner.query(conn, sql, handler, args);
            return query;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 表的通用的查询操作（返回多个对象）（考虑上事务）
     *
     * @param sql   mysql语句
     * @param args  参数
     * @return 返回一个泛型List
     */
    public List<T> batchQueryData(Connection conn,  String sql, Object... args) {
        BeanListHandler<T> handler = new BeanListHandler<T>(clazz);
        try {
            List<T> query = runner.query(conn, sql, handler, args);
            return query;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用于查询特殊值的通用方法
     * @param conn 连接
     * @param sql mysql语句
     * @param args 填充参数
     * @param <E> 泛型
     * @return 返回一个特殊类型对象
     */
    public <E> E getValue(Connection conn, String sql, Object... args) {
        ScalarHandler handler = new ScalarHandler();
        try {
            Object query = runner.query(conn, sql, handler, args);
            return (E)query;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
