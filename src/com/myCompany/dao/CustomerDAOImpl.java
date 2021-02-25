package com.myCompany.dao;

import com.myCompany.bean.Customer;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

/**
 * Customer表增删改查操作实现类
 * @author chenyaqi
 * @date 2021/2/25 - 14:38
 */
public class CustomerDAOImpl extends BaseDAO<Customer> implements CustomerDAO {
    /**
     * 插入一条数据
     * @param conn 连接
     * @param customer Customer对象
     * @return 返回插入数据数目
     */
    @Override
    public int insert(Connection conn, Customer customer) {
        String sql = "insert into customer(name,balance) values(?,?)";
        int updateCount = super.updateData(conn, sql, customer.getName(), customer.getBalance());
        return updateCount;
    }

    /**
     * 根据id删除一条数据
     * @param conn 连接
     * @param id id
     */
    @Override
    public void deleteById(Connection conn, int id) {
        String sql = "delete from customer where id = ?";
        super.updateData(conn, sql, id);
    }

    /**
     * 更新一条数据
     * @param conn 连接
     * @param customer Customer对象
     * @return 返回更新数据数目
     */
    @Override
    public int update(Connection conn, Customer customer) {
        String sql = "update customer set name = ?, balance = ? where id = ?";
        int updateCount = super.updateData(conn, sql, customer.getName(), customer.getBalance(), customer.getId());
        return updateCount;
    }

    /**
     * 根据id获取一条数据
     * @param conn 连接
     * @param id id
     * @return 返回一个Customer对象
     */
    @Override
    public Customer getCustomerById(Connection conn, int id) {
        String sql = "select id,name,balance from customer where id = ?";
        return super.queryData(conn, sql, id);
    }

    /**
     * 获取所有数据
     * @param conn 连接
     * @return 返回一个Customer对象集合
     */
    @Override
    public List<Customer> getAll(Connection conn) {
        String sql = "select id,name,balance from customer";
        return super.batchQueryData(conn, sql);
    }

    /**
     * 获取数据总数
     * @param conn 连接
     * @return 返回数据总数
     */
    @Override
    public Long getCount(Connection conn) {
        String sql = "select count(*) from customer";
        return super.getValue(conn, sql);
    }

    /**
     * 获取最大的财产数
     * @param conn 连接
     * @return 返回最大财产数
     */
    @Override
    public BigDecimal getMaxBalance(Connection conn) {
        String sql = "select max(balance) from customer";
        return super.getValue(conn, sql);
    }
}
