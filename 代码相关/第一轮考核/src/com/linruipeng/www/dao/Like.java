package com.linruipeng.www.dao;

import com.linruipeng.www.po.User;
import com.linruipeng.www.util.DBUtil;

import java.sql.*;

/**
 * 这个类专门放点赞的方法
 * 注意最多给每个成员点赞一次，并且退出部落也有点赞，说白了就是存到数据库嘛
 */
public class Like {

    /**
     * 这里是实现单独给某个人点赞的功能
     * 我本来还想写给所有人点赞的，但是突然发现最多点赞一次，那多没意思啊，所以我就实现了只能点赞某个人的操作
     * 而底层逻辑就是我会建立一个表，每次谁对谁点赞都会记录到表里面，那么当你想在点赞的时候就先遍历表
     * 发现已经存在了这条记录就不能点赞了
     * @param user 上面这个玩意就是我表里面的sponsor
     * @param username 下面这个玩意就是我表里面的recipient
     */
    public static void likeSomeone(User user, String username) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int likeNum;
        //String likeUsername = "\"" + username + "\"";//这玩意只有在注入现象的条件下才会有用 焯！！！

        //这里的synchroniezd主要是这样理解的，用户a和用户b分别是两个线程，两个线程同时对一个用户发起点赞
        //那么必然有一个抢到锁的进程先点赞，然后另一个进程再点赞
        //这里的理解就是他们共享的是点赞功能中的这一个被点赞的用户名
        synchronized (username) {
            try {
                //1、2步靠工具类完成了
                conn = DBUtil.getConnection();

                //将自动提交机制改为手动提交
                conn.setAutoCommit(false);//开启事物

                //3、获取数据库操作对象
                //这里检测是否点赞过了

                likeNum = Select.getLike(username);//这里实现获取目标用户的总点赞数
                likeNum = likeNum + 1;

                //修改
                String sql = "update user set likes = ? where username = ?";
                ps = conn.prepareStatement(sql);
                ps.setLong(1, likeNum);//如果通过了上面的检测，就可以使用sql语句操作了，这里的操作是指修改点赞数目
                ps.setString(2, username);

                //count用于判断操作影响了几行，相当于判断操作成功没有
                int count = ps.executeUpdate();
                if(1 == count){//如果操作成功那么就把数据存入进去
                    likeRecordStorage(user, username);
                }

                //程序执行到此处说明没有发生异常，那么事务结束，手动提交数据
                conn.commit();//提交事务

            } catch (Exception e) {
                //回滚事物，相当于出现异常把改变了的数据搞回来，雀氏牛批
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                e.printStackTrace();
            } finally {
                DBUtil.releaseConnection(conn);//释放连接
                DBUtil.close(ps, rs);//工具类
            }

        }
    }


    public static boolean likeRecord(User user, String username){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            String sql = "select * from likeRecord where sponsor = ? and recipient = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, username);
            rs = ps.executeQuery();

            if(rs.next()){
                return true;//执行到此处就是查询到了
            }

        } catch (Exception e) {
            e.printStackTrace();

        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

        return false;//说明记录没有找到
    }

    /**
     * 这里实现把点赞的记录存入表里面去
     * @param user 对应表的sponsor
     * @param username 对应表的recipient
     */
    public static void likeRecordStorage(User user, String username){
        Connection conn = null;
        PreparedStatement ps = null;
        Statement stmt;//这玩意的唯一作用就是找到当前目录最大id
        ResultSet rs = null;//没啥用，关闭的时候懒得改罢了
        ResultSet rs1;

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //3、获取数据库操作对象
            stmt = conn.createStatement();

            String sql1 = "select max(id) from likeRecord";
            String sql = "insert into likeRecord values(?, ?, ?)";
            //单纯为了找出最大值id

            rs1 = stmt.executeQuery(sql1);//类似于迭代器的玩意
            rs1.next();//指向第一个数据

            ps = conn.prepareStatement(sql);

            int id = rs1.getInt(1);
            ps.setInt(1, id + 1);
            ps.setString(2, user.getUsername());
            ps.setString(3, username);


            int count = ps.executeUpdate();//判断操作影响了几行
            if(0 == count) {
                System.out.println("执行错误...大的要来啦");
            }

        } catch (Exception e) {
            e.printStackTrace();

        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

    }

}
