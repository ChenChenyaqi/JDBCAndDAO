package JDBCUtils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Properties;

/**
 * 操作数据库的工具类
 *
 * @author chen Email:15229293783@163.com
 * @version 1.0
 * @date 2021-02-20 15:48
 */
public class JDBCMysqlUtils {
    /**
     * 获取数据库的连接
     *
     * @return Connection对象
     * @throws Exception
     * @author chen
     * @date 2021-2-20 16:19
     */
    public static Connection getConnection() throws Exception {
        // 1.读取配置文件中的4个基本信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        pros.load(is);

        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");
        String user = pros.getProperty("user");
        String password = pros.getProperty("password");

        // 2.加载驱动
        Class.forName(driverClass);

        // 3.获取连接
        Connection conn = DriverManager.getConnection(url, user, password);

        return conn;
    }

    /**
     * 关闭连接和Statement的操作
     *
     * @param conn Connection对象
     * @param ps   Statement对象
     * @author chen
     * @date 2021-2-20 16:22
     */
    public static void closeResource(Connection conn, Statement ps) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭资源操作
     *
     * @param conn Connection对象
     * @param ps   Statement对象
     * @param rs   ResultSet 对象
     */
    public static void closeResource(Connection conn, Statement ps, ResultSet rs) {
        try {
            if (ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通用的增删改操作
     *
     * @param sql  mysql语句
     * @param args 替换参数
     */
    public static void update(String sql, Object... args) {//sql中占位符的个数与可变形参的长度一致
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            // 1.获取数据库连接
            conn = JDBCMysqlUtils.getConnection();
            // 2.预编译sql语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            // 3.填充占位符
            int argsLength = args.length;
            for (int i = 0; i < argsLength; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5.关闭资源
            JDBCMysqlUtils.closeResource(conn, ps);
            System.out.println("操作成功！");
        }
    }

    /**
     * 对Student表的通用的查询操作
     *
     * @param sql  MySQL语句
     * @param args 参数
     * @return Student 对象
     */
    public static <T> T  queryStudent(Class<T> clazz,String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet re = null;

        try {
            conn = JDBCMysqlUtils.getConnection();
            ps = conn.prepareStatement(sql);

            int argsLength = args.length;
            for (int i = 0; i < argsLength; i++) {
                ps.setObject(i + 1, args[i]);
            }
            re = ps.executeQuery();

            // 获取结果集的元数据
            ResultSetMetaData rsmd = re.getMetaData();
            // 通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            if (re.next()) {
                T t = clazz.getDeclaredConstructor().newInstance();
                // 处理一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    // 获取每个列值：通过ResultSet
                    Object columnValue = re.getObject(i + 1);

                    // 通过ResultSetMetaData
                    // 获取每个列的列名：getColumnName()  // 不推荐使用
                    // 获取每个列的别名：getColumnLabel() // 针对表的名字与java属性的名字不同的情况
                    //String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //给student对象指定的columnName属性，赋值为columnValue,通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCMysqlUtils.closeResource(conn, ps, re);
        }
        return null;
    }
}
