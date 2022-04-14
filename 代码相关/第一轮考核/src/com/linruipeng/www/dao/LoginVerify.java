package com.linruipeng.www.dao;

import com.linruipeng.www.po.User;
import com.linruipeng.www.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

/**
 * 登录时候跟数据库的交互
 * 由于这里我之前写好了，就不实现接口了
 */
public class LoginVerify {
    static int id;//用户id，这个id用于我new对象时该传入何值
    public static User user;//这里我这样写，是因为一旦一个用户登录，那么这个属性(即这个对象)就是类级别的了

    public static boolean loginVerify(Map<String, String> userLoginInfo) {
        //JDBC相关的代码
        boolean loginResult = false;//做一个mark
        String loginUsername = userLoginInfo.get("loginUsername");
        String loginPassword = userLoginInfo.get("loginPassword");
        Connection conn = null;
        PreparedStatement ps = null;//搞一个预编译的数据库操作对象
        ResultSet rs = null;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //3、获取预编译的数据库操作对象
            //sql语句的框框，其中的?，表示一个占位符，一个坑用来接收一个“值”，注意，?不能使用''括起来
            String sql = "select * from user where username = ? and password = ?";//编写sql语句

            //程序执行到此处，会发送sql语句框架给DBMS，然后DBMS进行sql语句的编译
            ps = conn.prepareStatement(sql);

            //给占位符?传值，第一个?的下标是1，第二个?的下标是2，JDBC中所有下标从1开始
            ps.setString(1, loginUsername);//不用担心你的输入会影响最终sql语句的编译，因为上面的sql语句已经编译了
            ps.setString(2, loginPassword);

            //4、执行sql语句
            rs = ps.executeQuery();//类似于迭代器的玩意，这里不能写sql了，因为上面已经传过去了

            //5、有DQL语句处理结果集
            if(rs.next()){//只需要一个if就行了，因为上面的sql执行出来最多一个记录，迭代器走一步发现什么都没有为false，发现有数据必然有这个账号
                loginResult = true;

                //执行一个操作，返回一个id，这个id我搞的静态变量，感觉可能后续会有跟这个相关的操作
                id = rs.getInt(1);//读出来的结果就一行，所以第一第一列的值就是id

                 //把数据库的东西读出来，然后放到new的对象里面去
                LoginVerify.user = new User(id, rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getLong(9), rs.getLong(10), rs.getLong(11), rs.getInt(12));//我直接让这个静态变量指向了新new的对象
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

        return loginResult;//返回结果
    }

    public static void loginAgain(User user, Map<String, String> userLoginInfo) {
        //JDBC相关的代码
        //boolean loginResult = false;//做一个mark
        String loginUsername = userLoginInfo.get("loginUsername");
        String loginPassword = userLoginInfo.get("loginPassword");
        Connection conn = null;
        PreparedStatement ps = null;//搞一个预编译的数据库操作对象
        ResultSet rs = null;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //3、获取预编译的数据库操作对象
            //sql语句的框框，其中的?，表示一个占位符，一个坑用来接收一个“值”，注意，?不能使用''括起来
            String sql = "select * from user where username = ? and password = ?";//编写sql语句

            //程序执行到此处，会发送sql语句框架给DBMS，然后DBMS进行sql语句的编译
            ps = conn.prepareStatement(sql);

            //给占位符?传值，第一个?的下标是1，第二个?的下标是2，JDBC中所有下标从1开始
            ps.setString(1, loginUsername);//不用担心你的输入会影响最终sql语句的编译，因为上面的sql语句已经编译了
            ps.setString(2, loginPassword);

            //4、执行sql语句
            rs = ps.executeQuery();//类似于迭代器的玩意，这里不能写sql了，因为上面已经传过去了

            //5、有DQL语句处理结果集
            if(rs.next()){//只需要一个if就行了，因为上面的sql执行出来最多一个记录，迭代器走一步发现什么都没有为false，发现有数据必然有这个账号

                //执行一个操作，返回一个id，这个id我搞的静态变量，感觉可能后续会有跟这个相关的操作
                id = rs.getInt(1);//读出来的结果就一行，所以第一第一列的值就是id

                //把数据库的东西读出来，然后重新放到这个对象里面去
                user.setId(id);
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
                user.setSex(rs.getString(4));
                user.setGroup(rs.getString(5));
                user.setTribe(rs.getString(6));
                user.setMoney(rs.getInt(7));
                user.setLikes(rs.getInt(8));
                user.setDateTime(rs.getLong(9));
                user.setDateTribeTime(rs.getLong(10));
                user.setQuitTribeTime(rs.getLong(11));
                user.setMark(rs.getInt(12));

                LoginVerify.user = user;//这里我就把这个对象的地址给了类级别的user
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

    }

}
