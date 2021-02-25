package com.myCompany.dao;

import com.myCompany.bean.Customer;
import com.myCompany.utilPro.JDBCMysqlUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author chenyaqi
 * @date 2021/2/25 - 15:34
 */
public class CustomerDAOImplTest {
    private CustomerDAOImpl dao = new CustomerDAOImpl();

    @Test
    public void insert() {
        Connection conn = null;
        try {
            conn = JDBCMysqlUtils.getConnection();
            Customer customer = new Customer(1, "蔡徐坤", BigDecimal.valueOf(250));
            int insertCount = dao.insert(conn, customer);
            System.out.println(insertCount+"条数据添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        JDBCMysqlUtils.closeResource(conn,null);
        }
    }

    @Test
    public void deleteById() {
        Connection conn = null;
        try {
            conn = JDBCMysqlUtils.getConnection();
            dao.deleteById(conn,2);
            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCMysqlUtils.closeResource(conn,null);
        }
    }

    @Test
    public void update() {
        Connection conn = null;
        try {
            conn = JDBCMysqlUtils.getConnection();
            Customer customer = new Customer(3, "杜甫", BigDecimal.valueOf(5.8));
            int updateCount = dao.update(conn, customer);
            System.out.println(updateCount+"条数据更新成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCMysqlUtils.closeResource(conn,null);
        }
    }

    @Test
    public void getCustomerById() {
        Connection conn = null;
        try {
            conn = JDBCMysqlUtils.getConnection();
            Customer customer = dao.getCustomerById(conn, 4);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCMysqlUtils.closeResource(conn,null);
        }
    }

    @Test
    public void getAll() {
        Connection conn = null;
        try {
            conn = JDBCMysqlUtils.getConnection();
            List<Customer> customerList = dao.getAll(conn);
            customerList.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCMysqlUtils.closeResource(conn,null);
        }
    }

    @Test
    public void getCount() {
        Connection conn = null;
        try {
            conn = JDBCMysqlUtils.getConnection();
            Long daoCount = dao.getCount(conn);
            System.out.println("表中的记录数为："+daoCount);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCMysqlUtils.closeResource(conn,null);
        }
    }

    @Test
    public void getMaxBalance() {
        Connection conn = null;
        try {
            conn = JDBCMysqlUtils.getConnection();
            BigDecimal maxBalance = dao.getMaxBalance(conn);
            System.out.println("最大财产为："+maxBalance);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCMysqlUtils.closeResource(conn,null);
        }
    }
}