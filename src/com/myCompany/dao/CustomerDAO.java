package com.myCompany.dao;

import com.myCompany.bean.Customer;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

/**
 * 此接口用于规范对于customer表的常用操作
 * @author chenyaqi
 * @date 2021/2/25 - 14:22
 */
public interface CustomerDAO {
    /**
     * 将customer对象添加到数据库中
     * @param conn 连接
     * @param customer Customer对象
     */
    int insert(Connection conn, Customer customer);

    /**
     * 针对指定的id,删除表中的一条记录
     * @param conn 连接
     * @param id id
     */
    void deleteById(Connection conn, int id);

    /**
     * 针对指定的customer对象，更新表中的一条数据
     * @param conn 连接
     * @param customer Customer对象
     */
    int update(Connection conn, Customer customer);

    /**
     * 针对指定的id查询得到对应的customer对象
     * @param conn 连接
     * @param id id
     */
    Customer getCustomerById(Connection conn, int id);

    /**
     * 查询表中的所有记录构成的集合
     * @param conn 连接
     * @return Customer对象集合
     */
    List<Customer> getAll(Connection conn);

    /**
     * 返回数据表中的数据的条目数
     * @param conn 连接
     * @return 数据总数
     */
    Long getCount(Connection conn);

    /**
     * 返回数据表中的最大的资产
     * @param conn 连接
     * @return 最大资产
     */
    BigDecimal getMaxBalance(Connection conn);
}
