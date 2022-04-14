package com.linruipeng.www.dao;

import com.linruipeng.www.po.DaoMethod;
import com.linruipeng.www.po.User;
import com.linruipeng.www.util.DBUtil;

import java.sql.*;

/**
 * 注册时候跟数据库的交互
 */
public class Register implements DaoMethod {

    /**
     * 这里来操作数据库交互的注册环节
     * @param user 操作的对象，把这玩意的信息传到数据库去
     * @return true注册成功，false失败
     */
    @Override
    public boolean register(User user) {
        //JDBC相关的代码

        Connection conn = null;
        PreparedStatement ps = null;
        Statement stmt = null;//这玩意的唯一作用就是找到当前目录最大id
        ResultSet rs = null;
        ResultSet rs1;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //3、获取数据库操作对象
            stmt = conn.createStatement();
            //增加
            String sql1 = "select max(id) from user ";
            String sql = "insert into user values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            //单纯为了找出最大值id

            rs1 = stmt.executeQuery(sql1);//类似于迭代器的玩意
            rs1.next();//指向第一个数据

            int id = rs1.getInt(1);
            ps = conn.prepareStatement(sql);
            //传过来的user对象进行操作
            ps.setInt(1, id + 1);//把得到的第一行的id放进去
            ps.setString(2, user.getUsername());//注意这里的号码数是不能与原有数据库的重复的
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getSex());
            ps.setString(5, user.getGroup());
            ps.setString(6, user.getTribe());
            ps.setInt(7, user.getMoney());
            ps.setInt(8, user.getLikes());
            ps.setLong(9, user.getDateTime());
            ps.setLong(10, user.getDateTribeTime());
            ps.setLong(11, user.getQuitTribeTime());
            ps.setLong(12, user.getMark());

            //count用于判断操作影响了几行，相当于判断操作成功没有
            int count = ps.executeUpdate();
            if(1 == count) {
                System.out.println("请稍后，正在新建账号......");
                System.out.println("......");
                System.out.println("......");
                System.out.println("......");
            }

            //程序执行到此处说明没有发生异常，那么事务结束，手动提交数据
            conn.commit();//提交事务

            return true;//程序执行成功
        } catch (Exception e) {
            //回滚事物，相当于出现异常把改变了的数据搞回来，雀氏牛批
            if(conn != null){
                try {
                    conn.rollback();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
            DBUtil.close(stmt, rs);//工具类
        }

        return false;//执行到此处说明失败了
    }

}
