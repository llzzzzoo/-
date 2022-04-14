package com.linruipeng.www.dao;

import com.linruipeng.www.po.User;
import com.linruipeng.www.service.CreateRandom;
import com.linruipeng.www.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用于处理签到的类
 * 并且由于签到会修改字段的值，所以在这里也包含一部分update
 */
public class Sign {
    public static int moneyAdd;//这个是部落签到收获的金币
    public static int powerAdd;//这个是部落签到收获的主力

    /**
     * 这个是用于自己签到的操作
     */
    public static int signDay(User user){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int money;
        int preMoney = user.getMoney();//签到之前的金币

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //3、获取数据库操作对象

            money = CreateRandom.random(user.getMoney());//这里执行签到的生成随机金币数的操作
            user.setMoney(preMoney + money);//加上去用户获得金币数

            //修改
            String sql = "update user set money = ? where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, user.getMoney());//如果通过了上面的检测，就可以使用sql语句操作了，这里的操作是指修改金币数目
            ps.setString(2, user.getUsername());

            //count用于判断操作影响了几行，相当于判断操作成功没有
            int count = ps.executeUpdate();
            if(1 != count){
                System.out.println("程序执行错误");
                return -1;
            }

            //程序执行到此处说明没有发生异常，那么事务结束，手动提交数据
            conn.commit();//提交事务

            return money;//执行到此处说明签到成功
        }catch (Exception e) {
            //回滚事物，相当于出现异常把改变了的数据搞回来，雀氏牛批
            if(conn != null){
                try {
                    conn.rollback();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            e.printStackTrace();
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

        return -1;//遇到这种情况肯定是那里写错了，就当mark了
    }

    /**
     * 这个是用于部落签到的操作
     */
    public static void signTribe(User user){
        Connection conn = null;
        PreparedStatement ps1;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //3、获取数据库操作对象
            Select.getMyTribeInfo(user);//这行就是为了获取当前部落的信息

            moneyAdd = CreateRandom.random(user.getMoney());//这里执行签到的生成随机金币数的操作
            Select.moneyTribe += moneyAdd;//加上去用户获得金币数
            powerAdd = CreateRandom.random(Select.powerTribe);//这里执行签到的生成随机战力的操作
            Select.powerTribe += powerAdd;//加上去部落获得战力

            //修改
            String sql1 = "update tribe set money = ?, power = ? where tribe = ?";
            ps1 = conn.prepareStatement(sql1);
            ps1.setInt(1, Select.moneyTribe);//如果通过了上面的检测，就可以使用sql语句操作了，这里的操作是指修改金币数目
            ps1.setInt(2, Select.powerTribe);//如果通过了上面的检测，就可以使用sql语句操作了，这里的操作是指修改金币数目
            ps1.setString(3, user.getTribe());

            //count用于判断操作影响了几行，相当于判断操作成功没有
            int count = ps1.executeUpdate();
            if(1 != count){
                System.out.println("程序执行错误");
                return;
            }


            //程序执行到此处说明没有发生异常，那么事务结束，手动提交数据
            conn.commit();//提交事务

        }catch (Exception e) {
            //回滚事物，相当于出现异常把改变了的数据搞回来，雀氏牛批
            if(conn != null){
                try {
                    conn.rollback();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            e.printStackTrace();
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

    }

}
