package com.linruipeng.www.dao;

import com.linruipeng.www.po.User;
import com.linruipeng.www.util.DBUtil;
import com.linruipeng.www.view.BranchView;
import java.sql.*;


/**
 * 这里放处理apply的同意和申请操作
 */
public class Apply {

    /**
     * 这里是给用户用
     * 用户申请部落就唤起这个操作，然后创建一条记录
     * @param user 申请人
     * @param tribe 申请部落
     * @param founder 申请部落创始人
     */
    public static void startApply(User user, String tribe, String founder){
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

            //下面的操作实现java.util.Date转换为java.sql.Date

            //3、获取数据库操作对象
            stmt = conn.createStatement();

            //下面这行就是实现用户名和接收人一样的话，那么就发送人就改为系统

            //增加
            String sql1 = "select max(id) from apply";
            String sql = "insert into apply values(?, ?, ?, ?, ?)";
            //单纯为了找出最大值id

            rs1 = stmt.executeQuery(sql1);//类似于迭代器的玩意
            rs1.next();//指向第一个数据

            int id = rs1.getInt(1);
            ps = conn.prepareStatement(sql);
            //传过来的user对象进行操作
            ps.setInt(1, id + 1);//把得到的第一行的id放进去
            ps.setString(2, user.getUsername());//注意这里的号码数是不能与原有数据库的重复的
            ps.setString(3, tribe);
            ps.setString(4, founder);
            ps.setInt(5, 0);


            //count用于判断操作影响了几行，相当于判断操作成功没有
            int count = ps.executeUpdate();
            if(1 != count){
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
     * 笑了，不同意不就是不做该改变呗
     * 但这里你得把那个mark改变了，改为2
     * 这里实现修改状态
     * @param status 修改的状态
     * @param applicant 申请人
     * @param user 用来搞一个对象
     */
    public static void changeStatus(User user, String applicant, int status){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //修改
            String sql1 = "update apply set status = ? where applicant = ? and applyltrible = ?";
            ps = conn.prepareStatement(sql1);
            ps.setInt(1, status);//如果通过了上面的检测，就可以使用sql语句操作了，这里的操作是指修改金币数目
            ps.setString(2, applicant);
            ps.setString(3, user.getTribe());

            //count用于判断操作影响了几行，相当于判断操作成功没有
            int count = ps.executeUpdate();
            if(0 == count){
                System.out.println("程序执行异常");
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
     * 我打算在这里打印申请人的信息并且
     * @return 这个的返回值是一个存放了申请人信息的数组
     */
    public static String[] returnApplicant(User user){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int mark = 0;//标记
        String[] applicant = new String[BranchView.notHandleNum + 1];//这里是返回值

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            String sql = "select * from apply where founder = ? and status = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setInt(2, 0);

            rs = ps.executeQuery();

            while(rs.next()) {
                mark++;
                if (1 == BranchView.printApplicantMark) {//这里会实现赋值到数组去
                    applicant[mark - 1] = rs.getString(2);//这里减1是因为坑爹数组下标从0开始
                }else{//这里实现打印申请人的信息
                    //不用担心把删除信息的操作放在这里，因为吧，逻辑上你总是先进入这个else的
                    Select.printApplicantAccu(user, rs.getString(2));//这里打印申请人信息
                }
            }

            return applicant;
        } catch (Exception e) {
            e.printStackTrace();

        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

        return null;
    }


    /**
     * 这个唯一的作用就是告诉部落首领有多少信息没有处理
     * @param user 首领
     * @param status 用这个判断状态，找出多少没有处理的
     */
    public static void notHandleApply(User user, int status){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int mark = 0;//标记

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            String sql = "select * from apply where founder = ? and status = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setInt(2, status);

            rs = ps.executeQuery();

            while(rs.next()) {
                mark++;
            }
            BranchView.notHandleNum = mark;//实现把多少封没读的邮件赋值到这个去，然后传出去

        } catch (Exception e) {
            e.printStackTrace();

        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }
    }

}
