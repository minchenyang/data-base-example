package com.min.batis.Utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;

/**
 * @program: data-base-example
 * @description:
 * @author: mcy
 * @create: 2018-11-06 12:21
 **/
public class MyBatisUtil {
    private static ThreadLocal<SqlSession> threadLocal = new ThreadLocal();
    private static SqlSessionFactory sqlSessionFactory;
    static{
        try {
            Reader reader = Resources.getResourceAsReader("mybatis.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static SqlSession getSqlSession(){
        SqlSession sqlSession = threadLocal.get();
        if(sqlSession == null){
            sqlSession = sqlSessionFactory.openSession();
            threadLocal.set(sqlSession);
        }
        return sqlSession;
    }
    public static void closeSqlSession(){
        SqlSession sqlSession = threadLocal.get();
        if(sqlSession != null){
            sqlSession.close();
            threadLocal.remove();
        }
    }
    public static void main(String[] args) {
        try {
            System.out.println(Class.forName("com.mysql.jdbc.Driver"));
        } catch (ClassNotFoundException e) {
            System.out.println("---------------------------------------------");
            e.printStackTrace();
        }
        Connection conn = MyBatisUtil.getSqlSession().getConnection();
        System.out.println(conn!=null?"连接成功":"连接失败");

    }
}
