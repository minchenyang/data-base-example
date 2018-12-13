package com.min.demo;

/**
 * @program: data-base-example
 * @description:
 * @author: mcy
 * @create: 2018-12-12 19:51
 **/

import com.mysql.cj.jdbc.Driver;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * 表的创建测试  和  表的存在性测试
 */
public class DataBaseTest {

    private String url = "jdbc:mysql://192.168.0.70:3306/springboot_jpa";
    private String user = "root";//用户名
    private String password = "abc123";//密码

    /**
     * 创建数据库连接
     */
    public Connection getConnection() throws SQLException {
        //1.创建驱动程序类对象
        Driver driver = new Driver();
        //设置用户名和密码
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);
        return driver.connect(url, props);
    }

    /**建议使用
     * 创建数据库连接
     */

    public Connection getConnection2() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {


        DataBaseTest dataBaseTest = new DataBaseTest();
        try {
            Connection connection = dataBaseTest.getConnection();
            dataBaseTest.add(connection);



        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void add(Connection connection){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "INSERT INTO HDFS_STORAGE_RECORD (sensor,infoType,fileName,filePath,status,bakPath,fileLength,infoCount,fileCreateTime,fileLastUpdateTime,infoStartTime,infoEndTime " +
                ")  VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            conn = this.getConnection2();
            stmt = conn.prepareStatement(sql);
            //参数一： 参数位置  从1开始
            stmt.setString(1,"1");
            stmt.setString(2, "2");
            stmt.setString(3, "3");
            stmt.setString(4, "4");
            stmt.setString(5, "5");
            stmt.setString(6, "6");
            stmt.setLong(7,17);
            stmt.setLong(8,17);
            stmt.setString(9, dateFormat.format(new java.util.Date(System.currentTimeMillis())));
            stmt.setString(10, dateFormat.format(new java.util.Date(System.currentTimeMillis())));
            stmt.setString(11, dateFormat.format(new java.util.Date(System.currentTimeMillis())));
            stmt.setString(12, dateFormat.format(new java.util.Date(System.currentTimeMillis())));
            int count = stmt.executeUpdate();
            System.out.println("影响了"+count+"行");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {

        }



    }


    public void init(Connection connection)  {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String sql = "CREATE TABLE " + " HDFS_STORAGE_RECORD " +
                    "(HdfsId bigint(64) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'ID自增', " +
                    "sensor varchar(126) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备名', " +
                    "infoType varchar(126) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件类型', " +
                    "fileName varchar(126) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名称', " +
                    "filePath varchar(126) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件路径', " +
                    "status varchar(126) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件状态', " +
                    "bakPath varchar(126) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Nas存储目录', " +
                    "fileLength bigint(64) NULL DEFAULT NULL COMMENT '文件长度', " +
                    "infoCount bigint(64) NULL DEFAULT NULL COMMENT '信息数量', " +
                    "fileCreateTime datetime(0) NULL DEFAULT NULL COMMENT '文件创建时间', " +
                    "fileLastUpdateTime datetime(0) NULL DEFAULT NULL COMMENT '文件最后更新时间', " +
                    "infoStartTime datetime(0) NULL DEFAULT NULL COMMENT '首数据信息时间', " +
                    "infoEndTime datetime(0) NULL DEFAULT NULL COMMENT '尾数据信息时间') ";
            statement.execute(sql);
        } catch (SQLSyntaxErrorException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

        }
    }

}
