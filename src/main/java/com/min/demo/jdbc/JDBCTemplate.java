package com.min.demo.jdbc;


import com.mysql.cj.jdbc.Driver;

import java.sql.*;
import java.util.List;
import java.util.Properties;

/**
 *      |- Driver接口： 表示java驱动程序接口。所有的具体的数据库厂商要来实现此接口。
 *              |- connect(url, user, password):  连接数据库的方法。
 *                         url: 连接数据库的URL (URL语法： jdbc协议:数据库子协议://主机:端口/数据库)
 *                         user： 数据库的用户名
 *                         password： 数据库用户密码
 *      |- DriverManager类： 驱动管理器类，用于管理所有注册的驱动程序
 *              |-registerDriver(driver)  : 注册驱动类对象
 *              |-Connection getConnection(url,user,password);  获取连接对象
 *
 *
 *      |- Connection接口： 表示java程序和数据库的连接对象。
 *              |- Statement createStatement() ： 创建Statement对象
 *              |- PreparedStatement prepareStatement(String sql)  创建PreparedStatement对象
 *              |- CallableStatement prepareCall(String sql) 创建CallableStatement对象
 *      |- Statement接口： 用于执行静态的sql语句
 *              |- int executeUpdate(String sql)  ： 执行静态的更新sql语句（DDL，DML）
 *              |- ResultSet executeQuery(String sql)  ：执行的静态的查询sql语句（DQL）
 *      |-PreparedStatement接口：用于执行预编译sql语句
 *              |- int executeUpdate() ： 执行预编译的更新sql语句（DDL，DML）
 *              |-ResultSet executeQuery()  ： 执行预编译的查询sql语句（DQL）
 *      |-CallableStatement接口：用于执行存储过程的sql语句（call xxx）
 *              |-ResultSet executeQuery()  ： 调用存储过程的方法
 *
 *
 *      |- ResultSet接口：用于封装查询出来的数据
 *              |- boolean next() ： 将光标移动到下一行
 *              |-getXX() : 获取列的值
 */


/**
 * @program: data-base-example
 * @description:
 * @author: mcy
 * @create: 2018-10-21 17:14
 **/
public class JDBCTemplate {

    //连接数据库的URL
    //jdbc协议:数据库子协议:主机:端口/连接的数据库
    private String url = "jdbc:mysql://192.168.0.201:3306/logdata_processing_summary";
    private String user = "root";//用户名
    private String password = "123456";//密码

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

    /**
     * 执行DDL语句(创建表)
     */
    public void excuteDDL() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = this.getConnection2();
            statement = connection.createStatement();
            String sql = "CREATE TABLE student(id INT PRIMARY KEY AUTO_INCREMENT,NAME VARCHAR(20),gender VARCHAR(2))";
            //5.发送sql语句，执行sql语句,得到返回结果
            int count = statement.executeUpdate(sql);
            System.out.println("影响了"+count+"行！");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(statement!=null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 执行数操作语句(插入数据)
     */
    public void excuteDML(){
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = this.getConnection2();
            stmt = conn.createStatement();
            String sql = "INSERT INTO student(NAME,gender) VALUES('李四','女')";
            int count = stmt.executeUpdate(sql);
            System.out.println("影响了"+count+"行");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            this.close(conn, stmt);
        }
    }

    /**
     * 执行数据查询语句
     */
    public void excuteDQL(){
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = this.getConnection2();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM student";
            ResultSet resultSet = stmt.executeQuery(sql);
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String gender = resultSet.getString("gender");
                System.out.println(id+","+name+","+gender);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            this.close(conn, stmt);
        }
    }


    /**
     * 使用PreparedStatement执行sql语句
     * PreparedStatement vs Statment
     * 1）语法不同：PreparedStatement可以使用预编译的sql，而Statment只能使用静态的sql
     * 2）效率不同： PreparedStatement可以使用sql缓存区，效率比Statment高
     * 3）安全性不同： PreparedStatement可以有效防止sql注入，而Statment不能防止sql注入。
     *
     * 拓展：con.prepareStatement(sql_dept,Statement.RETURN_GENERATED_KEYS); 需要指定返回自增长标记
     */

    /**
     * 执行数据操作语言
     */
    public void excuteDMLbyPrepared(){
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "INSERT INTO student(NAME,gender) VALUES(?,?)";//?表示一个参数的占位符
        try {
            conn = this.getConnection2();
            stmt = conn.prepareStatement(sql);
            //参数一： 参数位置  从1开始
            stmt.setString(1,"王五");
            stmt.setString(2, "女");
            int count = stmt.executeUpdate();
            System.out.println("影响了"+count+"行");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            this.close(conn, stmt);
        }
    }

    /**
     * 执行存储过程
     */
    public void excuteStored(){
        //调用带有输入参数的存储过程 CALL pro_findById(4);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.getConnection2();
            String sql = "CALL pro_findById(?)"; //可以执行预编译的sql
            stmt = conn.prepareCall(sql);
            stmt.setInt(1, 6);
            rs = stmt.executeQuery(); //注意： 所有调用存储过程的sql语句都是使用executeQuery方法执行！！！
            //遍历结果

            //当执行有输出参数的存储过程
            /**
             * 参数一： 参数位置
             * 参数二： 存储过程中的输出参数的jdbc类型    VARCHAR(20)
             */


            /**
             * 参数一： 参数位置
             * 参数二： 存储过程中的输出参数的jdbc类型    VARCHAR(20)
             * stmt.registerOutParameter(2, java.sql.Types.VARCHAR);
             * stmt.executeQuery(); //结果不是返回到结果集中，而是返回到输出参数中
             * String result = stmt.getString(2); //getXX方法专门用于获取存储过程中的输出参数
             *
             */

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 批处理
     * void addBatch(String sql)     添加批处理
     * void clearBatch()            清空批处理
     * int[] executeBatch()         执行批处理
     */
    public void excuteBatch(List list){
        // SQL
        String sql = "INSERT INTO admin(userName,pwd) values(?,?)";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs;
        try {
            con = this.getConnection2();
            pstmt = con.prepareStatement(sql);

            for (int i=0; i<list.size(); i++) {
                //
                //pstmt.setString(1, admin.getUserName());
                //pstmt.setString(2, admin.getPwd());

                // 添加批处理
                pstmt.addBatch();

                // 测试：每5条执行一次批处理
                if (i % 5 == 0) {
                    // 批量执行
                    pstmt.executeBatch();
                    // 清空批处理
                    pstmt.clearBatch();
                }
            }

            // 批量执行
            pstmt.executeBatch();
            // 清空批处理
            pstmt.clearBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            this.close(con, pstmt);
        }
    }


    /**
     * 事务
     */

    public void transaction(){

        //不使用事务
/*        try {
            con = JdbcUtil.getConnection(); // 默认开启的隐士事务
            con.setAutoCommit(true);

            *//*** 第一次执行SQL ***//*
            pstmt = con.prepareStatement(sql_zs);
            pstmt.executeUpdate();

            *//*** 第二次执行SQL ***//*
            pstmt = con.prepareStatement(sql_ls);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeAll(con, pstmt, null);
        }*/

        //使用事务
/*        try {
            con = JdbcUtil.getConnection(); // 默认开启的隐士事务
            // 一、设置事务为手动提交
            con.setAutoCommit(false);

            *//*** 第一次执行SQL ***//*
            pstmt = con.prepareStatement(sql_zs);
            pstmt.executeUpdate();

            *//*** 第二次执行SQL ***//*
            pstmt = con.prepareStatement(sql_ls);
            pstmt.executeUpdate();

        } catch (Exception e) {
            try {
                // 二、 出现异常，需要回滚事务
                con.rollback();
            } catch (SQLException e1) {
            }
            e.printStackTrace();
        } finally {
            try {
                // 三、所有的操作执行成功, 提交事务
                con.commit();
                JdbcUtil.closeAll(con, pstmt, null);
            } catch (SQLException e) {
            }
        }*/

       //标记位置
        // 3. 转账，使用事务， 回滚到指定的代码段
        /*public void trans() {
            // 定义个标记
            Savepoint sp = null;

            // 第一次转账
            String sql_zs1 = "UPDATE account SET money=money-1000 WHERE accountName='张三';";
            String sql_ls1 = "UPDATE account SET money=money+1000 WHERE accountName='李四';";

            // 第二次转账
            String sql_zs2 = "UPDATE account SET money=money-500 WHERE accountName='张三';";
            String sql_ls2 = "UPDATE1 account SET money=money+500 WHERE accountName='李四';";

            try {
                con = JdbcUtil.getConnection(); // 默认开启的隐士事务
                con.setAutoCommit(false);       // 设置事务手动提交

                *//*** 第一次转账 ***//*
                pstmt = con.prepareStatement(sql_zs1);
                pstmt.executeUpdate();
                pstmt = con.prepareStatement(sql_ls1);
                pstmt.executeUpdate();

                // 回滚到这个位置？
                sp = con.setSavepoint();


                *//*** 第二次转账 ***//*
                pstmt = con.prepareStatement(sql_zs2);
                pstmt.executeUpdate();
                pstmt = con.prepareStatement(sql_ls2);
                pstmt.executeUpdate();


            } catch (Exception e) {
                try {
                    // 回滚 (回滚到指定的代码段)
                    con.rollback(sp);
                } catch (SQLException e1) {
                }
                e.printStackTrace();
            } finally {
                try {
                    // 提交
                    con.commit();
                } catch (SQLException e) {
                }
                JdbcUtil.closeAll(con, pstmt, null);
            } */


    }
    /**
     *关闭资源
     */
    private void close(Connection connection, Statement statement){
        if(statement!=null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        JDBCTemplate jdbcTemplate = new JDBCTemplate();
        jdbcTemplate.excuteDMLbyPrepared();
    }
}
