package com.myCompany.utilPro;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author chenyaqi
 * @date 2021/2/25 - 17:14
 */
public class JDBCMysqlUtils {

    private static DataSource source;

    static {
        try {
            Properties pros = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            pros.load(is);
            source = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用druid的数据库连接池技术
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = source.getConnection();
        return conn;
    }

    /**
     * 使用dbutils里的Dbutils类下的closeQuietly关闭连接和Statement的操作
     *
     * @param conn Connection对象
     * @param ps   Statement对象
     * @author chen
     * @date 2021-2-20 16:22
     */
    public static void closeResource(Connection conn, Statement ps) {
//        try {
//            if (conn != null) {
//                conn.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        try {
//            if (ps != null) {
//                ps.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        DbUtils.closeQuietly(conn);
        DbUtils.closeQuietly(ps);
    }

    /**
     *  使用dbutils里的Dbutils类下的closeQuietly关闭连接和Statement的操作
     *
     * @param conn Connection对象
     * @param ps   Statement对象
     * @param rs   ResultSet 对象
     */
    public static void closeResource(Connection conn, Statement ps, ResultSet rs) {
//        try {
//            if (ps != null)
//                ps.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        try {
//            if (conn != null)
//                conn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        try {
//            if (rs != null)
//                rs.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        DbUtils.closeQuietly(conn);
        DbUtils.closeQuietly(ps);
        DbUtils.closeQuietly(rs);
    }
}
