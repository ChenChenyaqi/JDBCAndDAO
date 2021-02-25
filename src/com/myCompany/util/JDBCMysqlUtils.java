package com.myCompany.util;

import com.myCompany.bean.Student;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
     */
    public static int updateData(String sql,Object ... args) {//sql中占位符的个数与可变形参的长度一致
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
            // 如果是一个查询操作，有返回结果，则返回true；如果是一个增删改操作，没有返回结果，则返回false
            // ps.execute()
            // 返回操作表中的行数
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5.关闭资源
            JDBCMysqlUtils.closeResource(conn, ps);
        }
        return 0;
    }

    /**
     * 通用的增删改操作（考虑上事务）
     * @param sql mysql语句
     * @param args 填充的参数
     * @return 最后一个操作的行数
     */
    public static int updateDataPro(Connection conn, String sql,Object ... args) {//sql中占位符的个数与可变形参的长度一致
        PreparedStatement ps = null;
        try {
            // 1.预编译sql语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            // 2.填充占位符
            int argsLength = args.length;
            for (int i = 0; i < argsLength; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 3.执行
            // 如果是一个查询操作，有返回结果，则返回true；如果是一个增删改操作，没有返回结果，则返回false
            // ps.execute()
            // 返回操作表中的行数
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 4.关闭资源
            JDBCMysqlUtils.closeResource(null, ps);
        }
        return 0;
    }

    /**
     * 批量插入数据
     */
    public static void insertData(String sql, Object[][] obj)  {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JDBCMysqlUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            int length = obj.length;
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < obj[0].length; j++) {
                    preparedStatement.setObject(j+1,obj[i][j]);
                }
                preparedStatement.execute();
                preparedStatement = connection.prepareStatement(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCMysqlUtils.closeResource(connection,preparedStatement);
        }
    }

    /**
     * 表的通用的查询操作（获取单个对象）
     *
     * @param sql  MySQL语句
     * @param args 参数
     * @return Student 对象
     */
    public static <T> T  queryData(Class<T> clazz,String sql, Object... args) {
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

                    //给t对象指定的columnName属性，赋值为columnValue,通过反射
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

    /**
     * 表的通用的查询操作（获取单个对象）(考虑上事务)
     *
     * @param sql  MySQL语句
     * @param args 参数
     * @return Student 对象
     */
    public static <T> T  queryDataPro(Connection conn,Class<T> clazz,String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet re = null;

        try {
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

                    //给t对象指定的columnName属性，赋值为columnValue,通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCMysqlUtils.closeResource(null, ps, re);
        }
        return null;
    }

    /**
     * 表的通用的查询操作（获取多个对象）
     * @param clazz 类型
     * @param sql mysql语句
     * @param args 参数
     * @param <T> 泛型
     * @return 返回一个泛型List
     */
    public static <T> List<T> batchQueryData(Class<T> clazz, String sql, Object ... args){
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
            // 创建集合对象
            ArrayList<T> list = new ArrayList<>();
            while (re.next()) {
                T t = clazz.getDeclaredConstructor().newInstance();
                // 处理一行数据中的每一个列:给t对象指定的属性赋值
                for (int i = 0; i < columnCount; i++) {
                    // 获取每个列值：通过ResultSet
                    Object columnValue = re.getObject(i + 1);

                    // 通过ResultSetMetaData
                    // 获取每个列的列名：getColumnName()  // 不推荐使用
                    // 获取每个列的别名：getColumnLabel() // 针对表的名字与java属性的名字不同的情况
                    //String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //给t对象指定的columnName属性，赋值为columnValue,通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCMysqlUtils.closeResource(conn, ps, re);
        }
        return null;
    }

    /**
     * 表的通用的查询操作（获取多个对象）（考虑上事务）
     * @param clazz 类型
     * @param sql mysql语句
     * @param args 参数
     * @param <T> 泛型
     * @return 返回一个泛型List
     */
    public static <T> List<T> batchQueryDataPro(Connection conn, Class<T> clazz, String sql, Object ... args){
        PreparedStatement ps = null;
        ResultSet re = null;

        try {
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
            // 创建集合对象
            ArrayList<T> list = new ArrayList<>();
            while (re.next()) {
                T t = clazz.getDeclaredConstructor().newInstance();
                // 处理一行数据中的每一个列:给t对象指定的属性赋值
                for (int i = 0; i < columnCount; i++) {
                    // 获取每个列值：通过ResultSet
                    Object columnValue = re.getObject(i + 1);

                    // 通过ResultSetMetaData
                    // 获取每个列的列名：getColumnName()  // 不推荐使用
                    // 获取每个列的别名：getColumnLabel() // 针对表的名字与java属性的名字不同的情况
                    //String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //给t对象指定的columnName属性，赋值为columnValue,通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCMysqlUtils.closeResource(null, ps, re);
        }
        return null;
    }



    // 以下是不通用的方法

    /**
     * Blob类型数据的插入
     *
     * @throws Exception 异常
     */
    @Test
    public void insertBlobData() throws Exception {
        Connection connection = JDBCMysqlUtils.getConnection();
        String sql = "insert into student(stuId,name,age,photo) values(?,?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, "2021010001");
        preparedStatement.setObject(2, "美眉");
        preparedStatement.setObject(3, 18);
        FileInputStream is = new FileInputStream(new File("src/1.PNG"));
        preparedStatement.setBlob(4, is);

        preparedStatement.execute();

        JDBCMysqlUtils.closeResource(connection, preparedStatement);
    }

    /**
     * Blob类型数据的查询
     */
    @Test
    public void queryBlobData() throws Exception {
        Connection connection = JDBCMysqlUtils.getConnection();
        String sql = "select stuId,name,age,photo from student where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, 16);
        InputStream is = null;
        FileOutputStream fos = null;

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String stuId = resultSet.getString("stuId");
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            Student student = new Student();
            student.setStuId(stuId);
            student.setName(name);
            student.setAge(age);
            System.out.println(student );
            // 将Blob类型的字段下载下来，以文件的方式保存到本地
            Blob photo = resultSet.getBlob("photo");
            is = photo.getBinaryStream();
            fos = new FileOutputStream("meimei.png");
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }

            is.close();
            fos.close();
            JDBCMysqlUtils.closeResource(connection, preparedStatement, resultSet);
        }

    }

    /**
     * 批量插入数据
     */
    @Test
    public void insertData() throws Exception {
        long start = System.currentTimeMillis();
        Connection connection = JDBCMysqlUtils.getConnection();

        // 设置不允许自动提交数据（优化）
        connection.setAutoCommit(false);

        String sql = "insert into goods(name) values(?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 1; i <= 1000000; i++) {
            preparedStatement.setObject(1,"name_" + i);

            // 提高效率：addBatch()、executeBatch()、clearBatch()
            // mysql服务器默认是关闭批处理的，我们需要通过一个参数，让mysql开启批处理的支持
            // ?rewriteBatchedStatements=true 写在配置文件的url后面（jdbc.properties）如果有多个参数用&连接
            // 使用更新的mysql驱动：mysql-connector-java-5.1.37-bin.jar
            // 1."攒"sql
            preparedStatement.addBatch();
            if (i % 500 == 0){
                // 2.执行batch
                preparedStatement.executeBatch();
                // 3.清空batch
                preparedStatement.clearBatch();
            }
//            preparedStatement.execute();
        }
        // 提交数据
        connection.commit();
        JDBCMysqlUtils.closeResource(connection,preparedStatement);
        long end = System.currentTimeMillis();
        System.out.println((end-start));
    }
}
