package com.linruipeng.www.dao;

import com.linruipeng.www.po.User;
import com.linruipeng.www.util.DBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

/**
 * 原来排序的操作
 * 这里再实现打印我的部落成员的信息
 */
public class Sort {

        public static void sortDate(User user) {
            //JDBC相关的代码
            //输入desc就是降序，输入asc就是升序
            Scanner s = new Scanner(System.in);
            String keyWords;
            do {
                System.out.println("请输入desc或asc，输入desc为降序，输入asc为升序");
                System.out.print("请输入：");
                keyWords = s.nextLine();
            } while (!("desc".equals(keyWords)) && !("asc".equals(keyWords)));

            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            String myTribe = "\"" + user.getTribe() + "\"";//这句话无敌好吧

            try {
                //1、2步靠工具类完成了
                conn = DBUtil.getConnection();

                //3、获取数据库操作对象
                stmt = conn.createStatement();
                //4、执行sql语句

                String sql = "select * from user where tribe = " + myTribe + " order by id " + keyWords;//我透，这里的like后面记得接个空格，因为sql语句需要空格啊
                rs = stmt.executeQuery(sql);//类似于迭代器的玩意
                //5、有DQL语句处理结果集
                System.out.println("排序结果如下");
                while(rs.next()){//遍历结果集
                    System.out.print("用户名：" + rs.getString(2) + " ");
                    System.out.print("性别：" + rs.getString(4) + " ");
                    System.out.print("金币：" + rs.getString(7) + " ");
                    System.out.print("点赞：" + rs.getString(8) + " ");
                    System.out.println();//换个行
                }
                System.out.println();//换个行
            } catch (Exception e) {
                e.printStackTrace();
            } finally{
                DBUtil.releaseConnection(conn);//释放连接
                DBUtil.close(stmt, rs);//工具类
            }

        }

    public static void sortMoney(User user) {
        //JDBC相关的代码
        //输入desc就是降序，输入asc就是升序
        Scanner s = new Scanner(System.in);
        String keyWords;
        do {
            System.out.println("请输入desc或asc，输入desc为降序，输入asc为升序");
            System.out.print("请输入：");
            keyWords = s.nextLine();
        } while (!("desc".equals(keyWords)) && !("asc".equals(keyWords)));


        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String myTribe = "\"" + user.getTribe() + "\"";//嘎嘎乱杀

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //3、获取数据库操作对象
            stmt = conn.createStatement();
            //4、执行sql语句

            String sql = "select * from user where tribe = " + myTribe + " order by money " + keyWords;//我透，这里的like后面记得接个空格，因为sql语句需要空格啊
            rs = stmt.executeQuery(sql);//类似于迭代器的玩意
            //5、有DQL语句处理结果集
            System.out.println("排序结果如下");
            while(rs.next()){//遍历结果集
                System.out.print("用户名：" + rs.getString(2) + " ");
                System.out.print("金币：" + rs.getString(7) + " ");
                System.out.print("点赞：" + rs.getString(8) + " ");
                System.out.println();//换个行
            }
            System.out.println();//换行
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(stmt, rs);//工具类
        }


    }

    public static void sortLike(User user) {
        //JDBC相关的代码
        //输入desc就是降序，输入asc就是升序
        Scanner s = new Scanner(System.in);
        String keyWords;
        do {
            System.out.println("请输入desc或asc，输入desc为降序，输入asc为升序");
            System.out.print("请输入：");
            keyWords = s.nextLine();
        } while (!("desc".equals(keyWords)) && !("asc".equals(keyWords)));


        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String myTribe = "\"" + user.getTribe() + "\"";//想不冲都不行啦

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //3、获取数据库操作对象
            stmt = conn.createStatement();
            //4、执行sql语句

            String sql = "select * from user where tribe = " + myTribe + " order by likes " + keyWords;//我透，这里的like后面记得接个空格，因为sql语句需要空格啊

            rs = stmt.executeQuery(sql);//类似于迭代器的玩意
            //5、有DQL语句处理结果集
            System.out.println("排序结果如下");
            while(rs.next()){//遍历结果集
                System.out.print("用户名：" + rs.getString(2) + " ");
                System.out.print("金币：" + rs.getString(7) + " ");
                System.out.print("点赞：" + rs.getString(8) + " ");
                System.out.println();//换个行
            }
            System.out.println();//换个行
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(stmt, rs);//工具类
        }

    }
}
