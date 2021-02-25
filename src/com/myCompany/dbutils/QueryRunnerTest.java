package com.myCompany.dbutils;

import com.myCompany.bean.Customer;
import com.myCompany.utilPro.JDBCMysqlUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * commons-dbutils 是 Apache 组织提供的一个开源 JDBC工具类库，它是对JDBC的简单封装，学习成本极低，并且使用dbutils能极大简化jdbc编码的工作量，同时也不会影响程序的性能。
 * @author chenyaqi
 * @date 2021/2/25 - 18:30
 */
public class QueryRunnerTest {
    @Test
    public void testInsert() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection connection = JDBCMysqlUtils.getConnection();
        String sql = "insert into customer(name,balance) values(?,?)";
        int insertCount = runner.update(connection, sql, "杨幂", BigDecimal.valueOf(1000));
        System.out.println("插入了"+insertCount+"条记录");
        JDBCMysqlUtils.closeResource(connection,null);
    }

    /**
     * BeanHandler:是ResultSetHandler接口的实现类，用于封装表中的一条记录
     */
    @Test
    public void testQuery() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection connection = JDBCMysqlUtils.getConnection();
        String sql = "select id,name,balance from customer where id = ?";
        BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
        Customer customer = runner.query(connection, sql, handler, 2);
        System.out.println(customer);

        JDBCMysqlUtils.closeResource(connection,null);
    }

    /**
    *BeanListHandler:是ResultSetHandler接口的实现类，用于封装表中的多条记录
     */
    @Test
    public void testQuery1() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection connection = JDBCMysqlUtils.getConnection();
        String sql = "select id,name,balance from customer where id < ?";
        BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
        List<Customer> customerList = runner.query(connection, sql, handler, 6);
        customerList.forEach(System.out::println);

        JDBCMysqlUtils.closeResource(connection,null);
    }

    /**
     *MapHandler:是ResultSetHandler接口的实现类,对应表中的一条记录，将字段及相应的字段值作为map中的key和value
     */
    @Test
    public void testQuery3() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection connection = JDBCMysqlUtils.getConnection();
        String sql = "select id,name,balance from customer where id = ?";
        MapHandler handler = new MapHandler();
        Map<String, Object> map = runner.query(connection, sql, handler, 5);
        System.out.println(map);

        JDBCMysqlUtils.closeResource(connection,null);
    }

    /**
     *MapListHandler:是ResultSetHandler接口的实现类,对应表中的多条记录，将字段及相应的字段值作为map中的key和value
     * 将这些map添加到list中
     */
    @Test
    public void testQuery4() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection connection = JDBCMysqlUtils.getConnection();
        String sql = "select id,name,balance from customer where id <= ?";
        MapListHandler handler = new MapListHandler();
        List<Map<String, Object>> mapList = runner.query(connection, sql, handler, 5);
        mapList.forEach(System.out::println);

        JDBCMysqlUtils.closeResource(connection,null);
    }

    /**
     *ScalarHandler:是ResultSetHandler接口的实现类,用于查询特殊值
     */
    @Test
    public void testQuery5() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection connection = JDBCMysqlUtils.getConnection();
        String sql = "select count(*) from customer";
        ScalarHandler handler = new ScalarHandler();
        long count = (long)runner.query(connection, sql, handler);
        System.out.println(count);

        JDBCMysqlUtils.closeResource(connection,null);
    }

    /**
     *ScalarHandler:是ResultSetHandler接口的实现类,用于查询特殊值
     */
    @Test
    public void testQuery6() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection connection = JDBCMysqlUtils.getConnection();
        String sql = "select max(name) from customer";
        ScalarHandler handler = new ScalarHandler();
        String name = (String)runner.query(connection, sql, handler);
        System.out.println(name);

        JDBCMysqlUtils.closeResource(connection,null);
    }

    /**
     * 自定义ResultSetHandler的实现类
     */
    @Test
    public void testQuery7() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection connection = JDBCMysqlUtils.getConnection();
        String sql = "select id,name,balance from customer where id = ?";
        ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>() {
            @Override
            public Customer handle(ResultSet resultSet) throws SQLException {
                if (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    BigDecimal balance = resultSet.getBigDecimal("balance");
                    Customer customer = new Customer(id,name,balance);
                    return  customer;
                }
                return null;
            }
        };

        Customer query = runner.query(connection, sql, handler, 4);
        System.out.println(query);

        JDBCMysqlUtils.closeResource(connection,null);
    }

}
