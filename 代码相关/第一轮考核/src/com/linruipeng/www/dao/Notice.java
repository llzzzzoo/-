package com.linruipeng.www.dao;

import com.linruipeng.www.po.User;
import com.linruipeng.www.service.SignTimeOperate;
import com.linruipeng.www.util.DBUtil;
import com.linruipeng.www.util.NowDate;
import com.linruipeng.www.view.BranchView;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * 实现邮箱与数据库的交互
 */
public class Notice {
    public static int noRendNum = 0;
    public static int HaveRendNum = 0;

    /**
     * 这里的话就是找到没读邮件的底层操作
     * @param user 这个就是传过来id和用户名
     * @return 返回true就是找到了呗
     */
    public static boolean noticeUnreadTable(User user, int noticeMark){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int mark = 0;//标记

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            String sql = "select * from notice where receiver = ? and noticeMark = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setInt(2, noticeMark);

            rs = ps.executeQuery();

            while(rs.next()) {
                mark++;
                if (BranchView.printMark == 1) {//表示要打印信息
                    System.out.println("第" + mark + "封邮件");
                    System.out.println("邮件名：" + rs.getString(2));
                    System.out.println("邮件内容：" + rs.getString(3));
                    System.out.println("日期时间：" + rs.getDate(4));
                    System.out.println("发送人：" + rs.getString(6));
                    System.out.println();//换个行
                    updateNoticeMark(user, rs.getString(2), 1);//调用一个方法把所有的mark修改为1，即已读
                }
            }
            noRendNum = mark;//实现把多少封没读的邮件赋值到这个去，然后传出去
            if(0 == mark){
                return false;//执行到此处就是查询失败了
            }
        } catch (Exception e) {
            e.printStackTrace();

        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

        return true;//执行到这里就是查询成功了
    }

    /**
     * 用以新建一封邮件
     * @param user 看情况来给接收人提供名字
     * @param title 标题
     * @param context 内容
     * @param receiver 接收人
     */
    public static void createNoticeRecord(User user, String title, String context, String receiver){
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

            //下面这行就是实现用户名和接收人一样的话，那么就发送人就改为系统

            //增加
            String sql1 = "select max(id) from notice ";
            String sql = "insert into notice values(?, ?, ?, ?, ?, ?, ?)";
            //单纯为了找出最大值id

            rs1 = stmt.executeQuery(sql1);//类似于迭代器的玩意
            rs1.next();//指向第一个数据

            int id = rs1.getInt(1);
            ps = conn.prepareStatement(sql);
            //传过来的user对象进行操作
            ps.setInt(1, id + 1);//把得到的第一行的id放进去
            ps.setString(2, title);//注意这里的号码数是不能与原有数据库的重复的
            ps.setString(3, context);
            ps.setDate(4, sdHireDate);
            ps.setString(5, receiver);
            //下面就是为了强行让一些信息的发送人为系统
            if(user.getUsername().equals(receiver) || 1 == SignTimeOperate.signMark){
                ps.setString(6, "系统");
                SignTimeOperate.signMark = 0;//每次变化之后都得变回来，以便下一次操作
            }else {
                ps.setString(6, user.getUsername());
            }
            ps.setInt(7, 0);


            //count用于判断操作影响了几行，相当于判断操作成功没有
            int count = ps.executeUpdate();
            if(0 == count){
                System.out.println("程序执行出错");
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
            DBUtil.close(stmt, rs);//工具类
        }

    }

    /**
     * 实现遍历本部落的人
     * @param user 用来得到本部落的人
     */
    public static void deleteTribeNotice(User user, String username) {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String myTribe = "\"" + user.getTribe() + "\"";//这句话无敌好吧
        String noticeUsername = "\"" +username + "\"";//这句话无敌好吧

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //3、获取数据库操作对象
            stmt = conn.createStatement();
            //4、执行sql语句

            String sql = "select * from user where username = " + noticeUsername + " and tribe = " + myTribe;//我透，这里的like后面记得接个空格，因为sql语句需要空格啊
            rs = stmt.executeQuery(sql);//类似于迭代器的玩意
            //5、有DQL语句处理结果集
            while(rs.next()){//遍历结果集
                //当不是部落长的就给他发这条消息
                if(1 != rs.getInt(12)) {
                    createNoticeRecord(user, "部落解散通知", "您当前所在的部落“" + user.getTribe() + "”已解散", rs.getString(2));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(stmt, rs);//工具类
        }

    }


    /**
     * 这里的话就是找到没读邮件的底层操作
     * @param user 这个就是传过来id和用户名
     * @return 返回true表明找到了呗
     */
    public static boolean noticeAlreadyReadTable(User user, int noticeMark){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int mark = 0;//标记

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            String sql = "select * from notice where receiver = ? and noticeMark = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setInt(2, noticeMark);

            rs = ps.executeQuery();

            while(rs.next()) {
                mark++;
                if (BranchView.printMark == 1) {//表示要打印信息
                    System.out.println("第" + mark + "封邮件");
                    System.out.println("邮件名：" + rs.getString(2));
                    System.out.println("邮件内容：" + rs.getString(3));
                    System.out.println("日期时间：" + rs.getDate(4));
                    System.out.println("发送人：" + rs.getString(6));
                    System.out.println();//换个行
                    //updateNoticeMark(user, rs.getString(2), 1);//调用一个方法把所有的mark修改为1，即已读
                }
            }
            HaveRendNum = mark;//实现把多少封读了的邮件赋值到这个去，然后传出去
            if(0 == mark){
                return false;//执行到此处就是查询失败了
            }
        } catch (Exception e) {
            e.printStackTrace();

        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

        return true;//执行到这里就是查询成功了

    }


    /**
     * 用于修改那个邮件标记的操作
     * @param user 对象
     * @param noticeMark 修改后的标记
     */
    public static void updateNoticeMark(User user, String title, int noticeMark){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //修改
            String sql1 = "update notice set noticeMark = ? where receiver = ? and title = ?";
            ps = conn.prepareStatement(sql1);
            ps.setInt(1, noticeMark);//如果通过了上面的检测，就可以使用sql语句操作了，这里的操作是指修改金币数目
            ps.setString(2, user.getUsername());
            ps.setString(3, title);

            //count用于判断操作影响了几行，相当于判断操作成功没有
            int count = ps.executeUpdate();
            if(0 == count){//md这破地方，明明修改了n行，但我写个修改不是一行就会报错，麻了
                System.out.println("修改mark处程序执行异常");
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

    /**
     * 实现删除对应记录的操作
     * @param user 用来用户名和id
     * @param noticeMark 根据已读的标记1来删除
     * @return 删除成功返回true
     */
    public static boolean deleteAllNotice(User user, int noticeMark){
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

            String sql = "delete from notice where receiver = ? and noticeMark = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setInt(2, noticeMark);

            count = ps.executeUpdate();
            //下面无法动态修改，但是我有在每次查看部落的时候，重新登录，应该是没问题的吧
            //user.setTribe("无1");//动态修改

            //count用于判断操作影响了几行，相当于判断操作成功没有
            if(0 == count){
                System.out.println("程序执行错误");
                return false;
            }

            //程序执行到此处说明没有发生异常，那么事务结束，手动提交数据
            conn.commit();//提交事务
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


    /**
     * 这里实现删除指定的文件
     * @param user 用来用户名和id
     * @param noticeMark 根据已读的标记1来删除
     * @return 删除成功返回true
     */
    public static boolean deleteOneNotice(User user, String title, String applicant, int noticeMark){
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

            String sql = "delete from notice where title = ? and receiver = ? and applicant = ? and noticeMark = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, user.getUsername());
            ps.setString(3, applicant);
            ps.setInt(4, noticeMark);

            count = ps.executeUpdate();
            //下面无法动态修改，但是我有在每次查看部落的时候，重新登录，应该是没问题的吧
            //user.setTribe("无1");//动态修改

            //count用于判断操作影响了几行，相当于判断操作成功没有
            if(0 == count){
                System.out.println("程序执行错误");
                return false;
            }

            //程序执行到此处说明没有发生异常，那么事务结束，手动提交数据
            conn.commit();//提交事务
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


    /**
     * 这边我会打印一遍，然后用户根据对于文件的id，即第几篇文件来删除
     * @param user 这个就是传过来id和用户名
     * @return 我打算返回一个数组 这个数组用于存储邮件名
     */
    public static String[] returnTitleAlreadyReadTable(User user, int noticeMark){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int mark = 0;//标记
        String[] title = new String[HaveRendNum + 1];//这里是有多少封已读邮件 + 1的容量

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            String sql = "select * from notice where receiver = ? and noticeMark = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setInt(2, noticeMark);

            rs = ps.executeQuery();

            while(rs.next()) {
                mark++;
                if (BranchView.printMark == 1) {//表示要打印信息
                    System.out.println("第“" + mark + "”封邮件");
                    System.out.println("邮件名：" + rs.getString(2));
                    System.out.println("邮件内容：" + rs.getString(3));
                    System.out.println("日期时间：" + rs.getString(4));
                    System.out.println("发送人：" + rs.getString(6));
                    System.out.println();//换个行
                }
                title[mark - 1] = rs.getString(2);//这里减1是因为坑爹数组下标从0开始
            }
            if(0 == mark){
                System.out.println("程序执行错误");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

        return title;
    }

    /**
     * 用于删除指定邮件存在的玩意
     * @param user 这个就是传过来id和用户名
     * @return 我打算返回一个数组 这个数组用于存储发送人
     */
    public static String[] returnApplicantAlreadyReadTable(User user, int noticeMark){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int mark = 0;//标记
        String[] applicant = new String[HaveRendNum + 1];//这里是有多少封已读邮件 + 1的容量

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            String sql = "select * from notice where receiver = ? and noticeMark = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setInt(2, noticeMark);

            rs = ps.executeQuery();

            while(rs.next()) {
                mark++;
                    //上面的传回标题的玩意，已经打印了，所以我这里就不打印了
                    applicant[mark - 1] = rs.getString(6);//这里减1是因为坑爹数组下标从0开始
            }
            if(0 == mark){
                System.out.println("程序执行错误");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

        return applicant;
    }

}

