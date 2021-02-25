package com.myCompany.transaction;

import com.myCompany.util.JDBCMysqlUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author chenyaqi
 * @date 2021/2/25 - 21:14
 */
public class TransactionTest {
    @Test
    public void testUpdateWithTx(){
        Connection connection = null;
        try {
            connection = JDBCMysqlUtils.getConnection();
            // 获取当前连接的隔离级别
            System.out.println(connection.getTransactionIsolation());
            // 设置数据库的隔离级别
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            //取消数据的自动提交
            connection.setAutoCommit(false);

            String sql1 = "update bank set balance = balance - 100 where name = ?";
            JDBCMysqlUtils.updateDataPro(connection,sql1,"张三");

            // 模拟网络异常
//            System.out.println(10/0);

            String sql2 = "update bank set balance = balance + 100 where name = ?";
            JDBCMysqlUtils.updateDataPro(connection,sql2,"李四");

            System.out.println("转账成功!");
            // 提交数据
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            // 修改其为自动提交状态
            // 主要针对于使用数据库连接池的使用
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JDBCMysqlUtils.closeResource(connection,null);
        }
    }
}
