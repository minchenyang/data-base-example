package com.min.demo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * 连接池
 */
public class DataBaseApplicationTests {

	private int init_count = 3;		// 初始化连接数目
	private int max_count = 6;		// 最大连接数
	private int current_count = 0;  // 记录当前使用连接数
    private LinkedList<Connection> pool = new LinkedList<Connection>();
	//1.  构造函数中，初始化连接放入连接池
	public DataBaseApplicationTests() {
		for (int i=0; i<init_count; i++){
			// 记录当前连接数目
			current_count++;
			// 创建原始的连接对象
			Connection con = createConnection();
			// 把连接加入连接池
			pool.addLast(con);
		}
	}

	//2. 创建一个新的连接的方法
	private Connection createConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// 原始的目标对象
			 Connection con = DriverManager.getConnection("jdbc:mysql://192.168.0.201:3306/logdata_processing_summary", "root", "123456");

            Object conn = Proxy.newProxyInstance(con.getClass().getClassLoader(), //类加载器
                    //con.getClass().getInterfaces(),                 //目标对象实现接口
                    new Class[]{Connection.class},
                    new InvocationHandler() {                       //调用方法时触发的函数
                        @Override
                        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
                            Object result = null;
                            String name = method.getName();
                            System.out.println("当前调用的方法名字：" + name);
                            if ("close".equals(name)) {
                                System.out.println("执行close方法  连接放入连接池");
                                pool.add(con);
                                System.out.println("连接已经放入连接池");
                            }
                            method.invoke(con, args);
                            return result;
                        }
                    });


            return (Connection) conn;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
            return null;
		}
	}

    //3. 获取连接
    public Connection getConnection(){
        // 3.1 判断连接池中是否有连接, 如果有连接，就直接从连接池取出
        if (pool.size() > 0){
            return pool.removeFirst();
        }

        // 3.2 连接池中没有连接： 判断，如果没有达到最大连接数，创建；
        if (current_count < max_count) {
            // 记录当前使用的连接数
            current_count++;
            // 创建连接
            return createConnection();
        }

        // 3.3 如果当前已经达到最大连接数，抛出异常
        throw new RuntimeException("当前连接已经达到最大连接数目 ！");
    }


    //4. 释放连接
    public void realeaseConnection(Connection con) {
        // 4.1 判断： 池的数目如果小于初始化连接，就放入池中
        if (pool.size() < init_count){
            pool.addLast(con);
        } else {
            try {
                // 4.2 关闭
                current_count--;
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        DataBaseApplicationTests dataBaseApplicationTests = new DataBaseApplicationTests();
        System.out.println(dataBaseApplicationTests);
        Connection connection = dataBaseApplicationTests.createConnection();
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
