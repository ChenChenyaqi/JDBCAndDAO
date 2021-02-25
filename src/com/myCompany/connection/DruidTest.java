package com.myCompany.connection;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author chenyaqi
 * @date 2021/2/25 - 17:35
 */
public class DruidTest {
    @Test
    public void getConnection() throws Exception {
        Properties pros = new Properties();

        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");

        pros.load(is);

        DataSource dataSource = DruidDataSourceFactory.createDataSource(pros);

        Connection conn = dataSource.getConnection();
        System.out.println(conn);
    }
}
