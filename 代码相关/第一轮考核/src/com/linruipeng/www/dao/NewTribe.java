package com.linruipeng.www.dao;

import com.linruipeng.www.po.User;
import com.linruipeng.www.util.DBUtil;
import com.linruipeng.www.util.NowDate;

import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * 这里是实现与数据库交互来创建一个新的部落
 */
public class NewTribe {

    /**
     * 实现创建部落的操作
     * @param user 对象用初始化阵营和初始化部落长
     * @param tribeName 这玩意就是我创建的部落名的名字
     */
    public static boolean createTribe(User user, String tribeName){
        //JDBC相关的代码

        Connection conn = null;
        PreparedStatement ps = null;
        Statement stmt = null;//这玩意的唯一作用就是找到当前目录最大id
        ResultSet rs = null;
        ResultSet rs1;
        String strDate = NowDate.nowDate();//调用这个方法正好打印现在的时间的标准格式

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //下面的操作实现java.util.Date转换为java.sql.Date
            //1.先实现String转换为java.util.Date
            java.util.Date udDate;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            udDate = sdf.parse(strDate);
            //2.将util转换为sql

            long time = udDate.getTime();//getTime()方法是获取从1970年到现在的毫秒数；
            java.sql.Date sdHireDate = new java.sql.Date(time);//java.sql.Date支持在构造函数中传入一个1970年的毫秒数的

            //3、获取数据库操作对象
            stmt = conn.createStatement();
            //增加
            String sql1 = "select max(id) from tribe ";
            String sql = "insert into tribe values(?, ?, ?, ?, ?, ?, ?, ?)";
            //单纯为了找出最大值id

            rs1 = stmt.executeQuery(sql1);//类似于迭代器的玩意
            rs1.next();//指向第一个数据

            int id = rs1.getInt(1);
            ps = conn.prepareStatement(sql);
            //传过来的user对象进行操作
            ps.setInt(1, id + 1);//把得到的第一行的id放进去
            ps.setString(2, user.getGroup());//注意这里的号码数是不能与原有数据库的重复的
            ps.setString(3, tribeName);
            ps.setString(4, user.getUsername());
            ps.setInt(5, 1);
            ps.setInt(6, 100);//部落初始化战力
            ps.setInt(7, 1000);//部落初始化金币
            ps.setDate(8, sdHireDate);//这个就是当前的日期格式


            //count用于判断操作影响了几行，相当于判断操作成功没有
            int count = ps.executeUpdate();
            if(1 == count) {
                System.out.println("请稍后，正在新建部落......");
                System.out.println("......");
                System.out.println("......");
                System.out.println("......");
            }

            //程序执行到此处说明没有发生异常，那么事务结束，手动提交数据
            conn.commit();//提交事务
            user.setMoney(user.getMoney() - 1000);//这个地方要做到动态修改金币数，以及部落名
            user.setTribe(tribeName);//动态修改

            //还要在数据库修改金币数和部落名
            Update.createMyTribe(user, tribeName);

            Update.updateMark(user, 1);//还得修改数据库的mark，创建部落肯定就是部落首领咯

            //同时，我也必须把部落签到时间归零
            Update.updateTribeTime(user, 0L);//记得参数要带L，毕竟Long嘛//还得修改部落签到时间

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
