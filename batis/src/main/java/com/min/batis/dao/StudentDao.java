package com.min.batis.dao;

import com.min.batis.Utils.MyBatisUtil;
import com.min.batis.bean.Student;
import org.apache.ibatis.session.SqlSession;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * mybatis工作流程
 * 1）通过Reader对象读取src目录下的mybatis.xml配置文件(该文本的位置和名字可任意)
 * 2）通过SqlSessionFactoryBuilder对象创建SqlSessionFactory对象
 * 3）从当前线程中获取SqlSession对象
 * 4）事务开始，在mybatis中默认
 * 5）通过SqlSession对象读取StudentMapper.xml映射文件中的操作编号，从而读取sql语句
 * 6）事务提交，必写
 * 7）关闭SqlSession对象，并且分开当前线程与SqlSession对象，让GC尽早回收
 */
public class StudentDao {
    /**
     * 增加学生（无参）
     */
    public void add1() throws Exception{
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        try{
            sqlSession.insert("mynamespace.add1");
        }catch(Exception e){
            e.printStackTrace();
            sqlSession.rollback();
            throw e;
        }finally{
            sqlSession.commit();
        }
        MyBatisUtil.closeSqlSession();
    }

    /**
     * 增加学生（有参）
     */
    public void add2(Student student) throws Exception{
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        try{
            sqlSession.insert("mynamespace.add2",student);
        }catch(Exception e){
            e.printStackTrace();
            sqlSession.rollback();
            throw e;
        }finally{
            sqlSession.commit();
        }
        MyBatisUtil.closeSqlSession();
    }

    /**
     * 修改学生
     */
    public void update(Student student) throws Exception {
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        try {
            sqlSession.update("mynamespace.update", student);
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
            throw e;
        } finally {
            sqlSession.commit();
            MyBatisUtil.closeSqlSession();
        }
    }

    /**
     * 查询单个学生
     */
    public Student findById(int id) throws Exception{
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        try{
            Student student = sqlSession.selectOne("mynamespace.findById",id);
            return student;
        }catch(Exception e){
            e.printStackTrace();
            sqlSession.rollback();
            throw e;
        }finally{
            sqlSession.commit();
            MyBatisUtil.closeSqlSession();
        }
    }

    /**
     * 查询多个学生
     */
    public List<Student> findAll() throws Exception{
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        try{
            return sqlSession.selectList("mynamespace.findAll");
        }catch(Exception e){
            e.printStackTrace();
            sqlSession.rollback();
            throw e;
        }finally{
            sqlSession.commit();
            MyBatisUtil.closeSqlSession();
        }
    }

    /**
     * 删除学生
     */
    public void delete(Student student) throws Exception{
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        try{
            sqlSession.delete("mynamespace.delete",student);
        }catch(Exception e){
            e.printStackTrace();
            sqlSession.rollback();
            throw e;
        }finally{
            sqlSession.commit();
            MyBatisUtil.closeSqlSession();
        }
    }

    /**
     * 无条件分页查询学生
     */
    public List<Student> findAllWithFy(int start,int size) throws Exception{
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        try{
            Map<String,Integer> map = new LinkedHashMap<String,Integer>();
            map.put("pstart",start);
            map.put("psize",size);
            return sqlSession.selectList("mynamespace.findAllWithFy",map);
        }catch(Exception e){
            e.printStackTrace();
            sqlSession.rollback();
            throw e;
        }finally{
            sqlSession.commit();
            MyBatisUtil.closeSqlSession();
        }
    }

    /**
     * 有条件分页查询学生
     */
    public List<Student> findAllByNameWithFy(String name,int start,int size) throws Exception{
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        try{
            Map<String,Object> map = new LinkedHashMap();
            map.put("pname","%"+name+"%");
            map.put("pstart",start);
            map.put("psize",size);
            return sqlSession.selectList("mynamespace.findAllByNameWithFy",map);
        }catch(Exception e){
            e.printStackTrace();
            sqlSession.rollback();
            throw e;
        }finally{
            sqlSession.commit();
            MyBatisUtil.closeSqlSession();
        }
    }

    /**
     * 动态SQL--查询
     */
    public List<Student> dynaSQLwithSelect(String name,Double sal) throws Exception{
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        try{
            Map<String,Object> map = new LinkedHashMap<String, Object>();
            map.put("pname",name);
            map.put("psal",sal);
            return sqlSession.selectList("mynamespace.dynaSQLwithSelect",map);
        }catch(Exception e){
            e.printStackTrace();
            sqlSession.rollback();
            throw e;
        }finally{
            sqlSession.commit();
            MyBatisUtil.closeSqlSession();
        }
    }

    /**
     * 动态SQL--更新
     */
    public void dynaSQLwithUpdate(Student student) throws Exception{
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        try{
            sqlSession.update("mynamespace.dynaSQLwithUpdate",student);
        }catch(Exception e){
            e.printStackTrace();
            sqlSession.rollback();
            throw e;
        }finally{
            sqlSession.commit();
            MyBatisUtil.closeSqlSession();
        }
    }

    /**
     * 动态SQL--删除
     */
    public void dynaSQLwithDelete(int... ids) throws Exception{
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        try{
            sqlSession.delete("mynamespace.dynaSQLwithDelete",ids);
        }catch(Exception e){
            e.printStackTrace();
            sqlSession.rollback();
            throw e;
        }finally{
            sqlSession.commit();
            MyBatisUtil.closeSqlSession();
        }
    }

    /**
     * 动态SQL--插入
     */
    public void dynaSQLwithInsert(Student student) throws Exception{
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        try{
            sqlSession.insert("mynamespace.dynaSQLwithInsert",student);
        }catch(Exception e){
            e.printStackTrace();
            sqlSession.rollback();
            throw e;
        }finally{
            sqlSession.commit();
            MyBatisUtil.closeSqlSession();
        }
    }

    public static void main(String[] args) throws Exception{
        StudentDao dao = new StudentDao();
        //dao.add1();
        //dao.add2(new Student(2,"呵呵",8000D));
        //List<Student> all = dao.findAll();
        //dao.dynaSQLwithUpdate(new Student(2,"NMB",8000D));
        //dao.dynaSQLwithDelete(1,2);
        dao.dynaSQLwithInsert(new Student(2,"NMB",8000D));
    }


}
