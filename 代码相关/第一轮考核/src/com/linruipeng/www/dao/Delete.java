package com.linruipeng.www.dao;

import com.linruipeng.www.po.User;
import com.linruipeng.www.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 管理员在这里实现踢出成员的操作
 * 并且再加上一条解散部落的操作
 * 普通成员在这里可以选择自行退出部落
 */
public class Delete{

    /**
     * 这里是实现踢出成员的操作
     * 但是这里的却是update，因为update代码太多了，干脆放一些带有删除性质的update在这里咯
     * @param username 成员的名字
     */
    public static void deleteUser(User user, String username) {
        //JDBC相关的代码
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        int count1 = 0;
        int count2 = 0;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();
            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物
            //3、获取数据库操作对象

            if("shine".equals(user.getGroup())){
                String sql1 = "update user set tribe = '无1' where username = ? and tribe = ?";
                ps1 = conn.prepareStatement(sql1);
                ps1.setString(1, username);
                ps1.setString(2, user.getTribe());
                count1 = ps1.executeUpdate();
                //下面无法动态修改，但是我有在每次查看部落的时候，重新登录，应该是没问题的吧
                //user.setTribe("无1");//动态修改
            }

            if("dark".equals(user.getGroup())){
                String sql2 = "update user set tribe = '无2' where username = ? and tribe = ?";
                ps2 = conn.prepareStatement(sql2);
                ps2.setString(1, username);
                ps2.setString(2, user.getTribe());
                count2 = ps2.executeUpdate();
                //下面无法动态修改，但是我有在每次查看部落的时候，重新登录，应该是没问题的吧
                //user.setTribe("无2");//动态修改
            }

            //count用于判断操作影响了几行，相当于判断操作成功没有
            if(1 != count1 && 1 != count2){
                System.out.println("程序执行错误");
                return;
            }

            //程序执行到此处说明没有发生异常，那么事务结束，手动提交数据
            conn.commit();//提交事务
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
            DBUtil.close(ps1, rs);//工具类
            DBUtil.close(ps2, rs);//工具类
        }
    }

    /**
     * 这里就一个功能，当一个用户申请了多个部落被其中一个接受了的话
     * 就删除对其他部落的消息
     * @param tribeName 申请的部落
     * @param applicant 申请人
     * @param founder 部落首领
     */
    public static void deleteApplyNotice(String applicant, String tribeName, String founder) {
        //JDBC相关的代码
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();
            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物
            //3、获取数据库操作对象

            String sql = "delete from apply where applicant = ? and applyltrible = ? and founder = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, applicant);
            ps.setString(2, tribeName);
            ps.setString(3, founder);
            count = ps.executeUpdate();
            //下面无法动态修改，但是我有在每次查看部落的时候，重新登录，应该是没问题的吧
            //user.setTribe("无1");//动态修改

            //count用于判断操作影响了几行，相当于判断操作成功没有
            if(0 == count){
                System.out.println("程序执行错误");
                return;
            }

            //程序执行到此处说明没有发生异常，那么事务结束，手动提交数据
            conn.commit();//提交事务
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
        }
    }


    /**
     * 这里实现解散部落的操作
     * @param user 用户
     * @return true代表解散成功
     */
    public static boolean deleteTribe(User user) {
        //JDBC相关的代码
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();
            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物
            //3、获取数据库操作对象

            String sql = "delete from tribe where tribe = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getTribe());//搜寻传过来的参数进行删除


            int count = ps.executeUpdate();//count用于判断操作影响了几行，相当于判断操作成功没有
            if(1 != count){
                System.out.println("程序执行异常");
                return false;
            }
            //程序执行到此处说明没有发生异常，那么事务结束，手动提交数据

            conn.commit();//提交事务

            //判断是哪个阵营的，然后进行动态修改
            if("shine".equals(user.getGroup())){
                user.setTribe("无1");
            }else{
                user.setTribe("无2");
            }

            //由于退出了部落，mark应该是普通人的2
            //同时存入数据库中
            Update.updateMark(user, 2);
            //部落的签到时间也是如此
            //存入数据库中，动态修改
            Update.updateTribeTime(user, 0L);

            return true;//程序执行到这里说明成功了
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
        }

        return false;//执行到这里说明了删除操作失败
    }

}
