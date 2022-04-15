package com.linruipeng.www.dao;

import com.linruipeng.www.po.User;
import com.linruipeng.www.service.CreateRandom;
import com.linruipeng.www.service.SignTimeOperate;
import com.linruipeng.www.util.DBUtil;

import java.sql.*;
import java.util.Scanner;

/**
 * 这里
 * 可以实现修改密码
 * 管理员在这里可以实现赋予别人踢人的权限
 * 实现加入部落的操作
 */
public class Update {

    /**
     * 实现修改密码的操作
     * @param user 动态修改密码
     * @return false表示修改失败，true表示修改成功
     */
    public static boolean changePassword(User user){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //3、获取数据库操作对象
            //输入修改后的密码
            Scanner s = new Scanner(System.in);
            String newPassword1;//修改时第一次输的密码
            String newPassword2;//修改时第二次输的密码

            while (true) {
                System.out.println("请输入修改后的密码：");
                newPassword1 = s.nextLine();
                System.out.println("请再次输入修改后的密码：");
                newPassword2 = s.nextLine();
                if (!newPassword1.equals(newPassword2)) {
                    System.out.println("两次密码不重复，请重新输入！\n");
                }else if(newPassword2.equals(LoginVerify.user.getPassword())){
                    System.out.println("不能和修改前的密码重复！\n");
                }else{
                    break;//满足条件就退出循环
                }
            }

            //修改
            String sql = "update user set password = ? where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, newPassword2);
            ps.setString(2, LoginVerify.user.getUsername());

            //count用于判断操作影响了几行，相当于判断操作成功没有
            int count = ps.executeUpdate();
            if(1 != count){
                System.out.println("程序执行异常");
                return false;
            }

            //程序执行到此处说明没有发生异常，那么事务结束，手动提交数据
            user.setPassword(newPassword2);//执行到这里的话肯定要对数据动态修改啊
            conn.commit();//提交事务

            return true;//执行到此处说明修改密码成功
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

           return false;//程序执行到这里说明修改密码失败了
        }


    /**
     * 这里实现把user的每日签到时间传到数据库去
     * @param user 这个就是为了获得时间
     */
    public static void updateMyTime(User user){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //3、获取数据库操作对象


            //修改
            String sql = "update user set dateMine = ? where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, user.getDateTime());//如果通过了上面的检测，就可以使用sql语句操作了，这里的操作是指修改金币数目
            ps.setString(2, user.getUsername());

            //count用于判断操作影响了几行，相当于判断操作成功没有
            int count = ps.executeUpdate();
            if(1 != count){
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
     * 这里实现把user的部落签到时间传到数据库去
     * @param user 这个就是为了获得时间
     */
    public static void updateTribeTime(User user, Long dateTribe){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int money;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //3、获取数据库操作对象

            money = CreateRandom.random(user.getMoney());//这里执行签到的生成随机金币数的操作
            user.setMoney(user.getMoney() + money);//加上去用户获得金币数

            //修改
            String sql = "update user set dateTribe = ? where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, dateTribe);//如果通过了上面的检测，就可以使用sql语句操作了，这里的操作是指修改金币数目
            ps.setString(2, user.getUsername());

            //count用于判断操作影响了几行，相当于判断操作成功没有
            int count = ps.executeUpdate();
            if(1 != count){
                System.out.println("程序执行异常");
                return;
            }
            user.setDateTribeTime(dateTribe);

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
     * 这里实现申请部落接受了，部落签到时间更新，防止提醒活跃度太低
     * @param applicant 这个就是修改申请人的部落签到
     */
    public static void updateApplicantTribeTime(String applicant, Long dateTribe){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int money;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //3、获取数据库操作对象

            //修改
            String sql = "update user set dateTribe = ? where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, dateTribe);//如果通过了上面的检测，就可以使用sql语句操作了，这里的操作是指修改金币数目
            ps.setString(2, applicant);

            //count用于判断操作影响了几行，相当于判断操作成功没有
            int count = ps.executeUpdate();
            if(1 != count){
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
     * 这里实现退出部落的操作，说白了就是把部落修改为"无"罢了
     * @param user 修改的对象
     * @return 修改成功返回true
     */
    public static boolean quitMyTribe(User user){
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3;//这个是把那个数据库中的签到清空的操作
        ResultSet rs = null;
        int count1 = 0;
        int count2 = 0;
        int count3;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //3、获取数据库操作对象
            //在提交事务前进行通知部落长的操作
            Notice.createNoticeRecord(user, "退出部落通知", "祝部落以后越来越好，我先告退了", Select.selectMyTribeBoss(user));

            //修改"1"
            if("shine".equals(user.getGroup())){
                String sql1 = "update user set tribe = '无1' where username = ?";
                ps1 = conn.prepareStatement(sql1);
                ps1.setString(1, user.getUsername());
                count1 = ps1.executeUpdate();
                user.setTribe("无1");//动态修改
            }

            if("dark".equals(user.getGroup())){
                String sql2 = "update user set tribe = '无2' where username = ?";
                ps2 = conn.prepareStatement(sql2);
                ps2.setString(1, user.getUsername());
                count2 = ps2.executeUpdate();
                user.setTribe("无2");//动态修改
            }

            String sql3 = "update user set dateTribe = 0 where username = ?";//这个是实现退出部落，签到清零的操作
            ps3 = conn.prepareStatement(sql3);
            ps3.setString(1, user.getUsername());
            count3 = ps3.executeUpdate();

            //count用于判断操作影响了几行，相当于判断操作成功没有
            if(1 != count1 && 1 != count2 && 1 != count3){
                System.out.println("程序执行异常");
                return false;
            }
            //程序执行到此处说明没有发生异常，那么事务结束，手动提交数据
            conn.commit();//提交事务

            //当前面操作无误后，我就把这个时间存入，不然前面有误后面怎么能存嘛，主要记得嵌套写的话，要先commit再下一个语句嗷
            updateQuitTribeTime(user);//开始操作

            //同时，我也必须把部落签到时间归零
            updateTribeTime(user, SignTimeOperate.nowTime() - 60 * 60 *24);//部落签到时间回到一天前，防止等会新加入部落的家伙被检测到七天没签到

            Update.updateMark(user, 2);//还得修改数据库的mark，退出部落就是普通人员咯
            user.setMark(2);//动态修改

            return true;

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
            DBUtil.close(ps1, rs);//工具类
            DBUtil.close(ps2, rs);//工具类
        }

        return false;//修改失败，就返回false咯
    }


    /**
     * 这里实现退出的时候把退出的时间传到数据库去
     * @param user 修改的对象
     */
    public static void updateQuitTribeTime(User user){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //3、获取数据库操作对象

            //修改
            String sql = "update user set quitTribeDate = ? where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, user.getQuitTribeTime());//如果通过了上面的检测，就可以使用sql语句操作了，这里的操作是指修改金币数目
            ps.setString(2, user.getUsername());

            //count用于判断操作影响了几行，相当于判断操作成功没有
            int count = ps.executeUpdate();
            if(1 != count){
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
     * 这里实现创建部落的操作
     * @param user 修改的对象
     */
    public static void createMyTribe(User user, String tribeName){
        Connection conn = null;
        PreparedStatement ps = null;//这个对应修改部落的sql
        ResultSet rs = null;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //3、获取数据库操作对象

            //这里修改
            String sql = "update user set tribe = ? where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, tribeName);
            ps.setString(2, user.getUsername());
            int count = ps.executeUpdate();
            if(1 != count){
                System.out.println("程序执行异常");
                return;
            }
            conn.commit();//提交事务，提交啊兄弟，你嵌套写的玩意我麻了

            //这里的操作说白了就是把金币给赋值到数据库去，因为我创建部落减少了一千块嘛
           createTribeReduceMoney(user, user.getMoney());//上一个方法已经实现了修改，所以我这里的操作其实是存进去

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
     * 这里实现加入部落的操作，说白了就是把部落名改为输入过来的部落名
     * @param user 修改的对象
     * @return 修改成功返回true
     */
    public static boolean addMyTribe(User user, String applicant){
        Connection conn = null;
        PreparedStatement ps = null;//这个对应修改部落的sql
        ResultSet rs = null;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //3、获取数据库操作对象

            //这里修改
            String sql = "update user set tribe = ? where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getTribe());
            ps.setString(2, applicant);
            int count = ps.executeUpdate();
            if(1 != count){
                System.out.println("程序执行异常");
                return false;
            }
            conn.commit();//提交事务，提交啊兄弟，你嵌套写的玩意我麻了

            //下面左边的参数是实现了把部落的本来的钱加上新加入的人的钱的操作
            applyTribeAddMoney(user, Select.applicantMoney(applicant) + Select.tribeMoney(user));//增加部落的钱钱
            applyTribeChangePeoNum(user, Select.selectTribePeoNumAccu(user, user.getTribe()) + 1);//这里写的加部落人数的操作有点奇怪，但是没有关系，毕竟它不跑起来我就跑了
            //减少用户的钱钱
            joinTribeReduceMoney(applicant, 0);
            //修改部落签到时间为此刻的一天前，防止提醒活跃度太低
            updateApplicantTribeTime(applicant, SignTimeOperate.nowTime() - 60 * 60 * 24);


            return true;
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

        return false;//修改失败，就返回false咯
    }

    /**
     * 这里就实现申请加入部落的金币增加的操作
     * @param user 这个就是为了上交全部金币而设置的
     */
    public static void applyTribeAddMoney(User user, int nowMoney){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //3、获取数据库操作对象

            //修改
            String sql1 = "update tribe set money = ? where tribe = ?";
            ps = conn.prepareStatement(sql1);
            ps.setInt(1, nowMoney);//如果通过了上面的检测，就可以使用sql语句操作了，这里的操作是指修改金币数目
            ps.setString(2, user.getTribe());

            //count用于判断操作影响了几行，相当于判断操作成功没有
           int count = ps.executeUpdate();
           if(1 != count){
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
     * 这里就实现申请加入部落后部落的人数上升的操作
     * @param nowPeoNum 这个就是当前部落的总人数
     * @param user 这个就是为了得到部落的名字
     */
    public static void applyTribeChangePeoNum(User user , int nowPeoNum){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //3、获取数据库操作对象

            //修改
            String sql1 = "update tribe set peonum = ? where tribe = ?";
            ps = conn.prepareStatement(sql1);
            ps.setInt(1, nowPeoNum);//如果通过了上面的检测，就可以使用sql语句操作了，这里的操作是指修改金币数目
            ps.setString(2, user.getTribe());

            //count用于判断操作影响了几行，相当于判断操作成功没有
            int count = ps.executeUpdate();
            if(1 != count){
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
     * 这里就实现创建部落的自己金币减少1000的操作
     * @param user 用户对象
     * @param money 减少的金币
     */
    public static void createTribeReduceMoney(User user, int money){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //3、获取数据库操作对象
            Select.getMyTribeInfo(user);//这行就是为了获取当前部落的信息


            //修改
            String sql1 = "update user set money = ? where username = ?";
            ps = conn.prepareStatement(sql1);
            ps.setInt(1, money);//如果通过了上面的检测，就可以使用sql语句操作了，这里的操作是指修改金币数目
            ps.setString(2, user.getUsername());

            //count用于判断操作影响了几行，相当于判断操作成功没有
            int count = ps.executeUpdate();
            if(1 != count){
                System.out.println("程序执行异常");
                return;
            }
            user.setMoney(money);//执行后就设置自己的金币为0了，动态修改

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
     * 这里就实现加入部落的自己金币全部减少的操作
     * @param applicant 申请人
     * @param money 减少的金币
     */
    public static void joinTribeReduceMoney(String applicant, int money){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //3、获取数据库操作对象

            //修改
            String sql = "update user set money = ? where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, money);//如果通过了上面的检测，就可以使用sql语句操作了，这里的操作是指修改金币数目
            ps.setString(2, applicant);

            //count用于判断操作影响了几行，相当于判断操作成功没有
            int count = ps.executeUpdate();
            if(1 != count){
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
     * 用于修改标记的操作
     * @param user 对象
     * @param mark 修改后的标记
     */
    public static void updateMark(User user, int mark){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //修改
            String sql1 = "update user set mark = ? where username = ?";
            ps = conn.prepareStatement(sql1);
            ps.setInt(1, mark);//如果通过了上面的检测，就可以使用sql语句操作了，这里的操作是指修改金币数目
            ps.setString(2, user.getUsername());

            //count用于判断操作影响了几行，相当于判断操作成功没有
            int count = ps.executeUpdate();
            if(1 != count){
                System.out.println("程序执行异常");
                return;
            }
            user.setMark(mark);//这里是把当前的mark修改了，所以我设想搞那几个操作的时候都得先判断一下mark

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
     * 用于修改标记的操作
     * @param user 对象
     * @param mark 修改后的标记
     * @param username 指定修改的用户名的权限
     */
    public static void updateUserMark(User user, String username, int mark){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //修改
            String sql1 = "update user set mark = ? where username = ? and tribe = ?";
            ps = conn.prepareStatement(sql1);
            ps.setInt(1, mark);//如果通过了上面的检测，就可以使用sql语句操作了，这里的操作是指修改金币数目
            ps.setString(2, username);
            ps.setString(3, user.getTribe());

            //count用于判断操作影响了几行，相当于判断操作成功没有
            int count = ps.executeUpdate();
            if(1 != count){
                System.out.println("程序执行异常");
                return;
            }
            user.setMark(mark);//这里是把当前的mark修改了，所以我设想搞那几个操作的时候都得先判断一下mark

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
     * 这个方法巨牛逼，一个嵌套实现修改一个部落下的所有部落为无
     * @param user 这个玩意就是用来找寻部落在哪的操作的
     * @return 返回true表明修改成功
     */
    public static boolean updateAllTribe(User user){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String myTribe = "\"" + user.getTribe() + "\"";//这句话无敌好吧
        boolean result;
        int mark = 0;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //3、获取数据库操作对象
            stmt = conn.createStatement();
            //4、执行sql语句

            String sql = "select * from user where tribe = " + myTribe + " order by id ";//默认的顺序是升序
            rs = stmt.executeQuery(sql);//类似于迭代器的玩意
            //5、有DQL语句处理结果集
            while(rs.next()){//在这里面我开始了神奇的嵌套
                mark++;
                //这个嵌套仅仅是为了得到用户名罢了
                result = updateOneTribe(user, rs.getString(2));
                if(!result){
                    System.out.println("程序执行错误");
                    return false;
                }

            }

            if(0 != mark){//如果上面进入了循环，那么mark就会自增，那么就可以返回true
                return true;
            }

            System.out.println("程序执行错误");
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(stmt, rs);//工具类
        }

        return false;
    }


    /**
     * 这里实现退出部落的操作，说白了就是把部落修改为"无"罢了
     * @param user 用来发通知和得到阵营的名字
     * @param username 用户的名字
     * @return 修改成功返回true
     */
    public static boolean updateOneTribe(User user, String username){
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3;//这个是把那个数据库中的签到清空的操作
        ResultSet rs = null;
        int count1 = 0;
        int count2 = 0;
        int count3;

        try {
            //1、2步靠工具类完成了
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //3、获取数据库操作对象

            //删除成功就打印删除的信息
            //md一定要在部落被修改为无之前通知啊老弟
            Notice.deleteTribeNotice(user, username);//这里写在前面就是为了让部落长mark变化之前给他通知

            //修改"1"
            if("shine".equals(user.getGroup())){
                String sql1 = "update user set tribe = '无1' where username = ?";
                ps1 = conn.prepareStatement(sql1);
                ps1.setString(1, username);
                count1 = ps1.executeUpdate();
                //下面无法动态修改，但是我有在每次查看部落的时候，重新登录，应该是没问题的吧
                //user.setTribe("无1");//动态修改
            }

            if("dark".equals(user.getGroup())){
                String sql2 = "update user set tribe = '无2' where username = ?";
                ps2 = conn.prepareStatement(sql2);
                ps2.setString(1, username);
                count2 = ps2.executeUpdate();
                //下面无法动态修改，但是我有在每次查看部落的时候，重新登录，应该是没问题的吧
                //user.setTribe("无2");//动态修改
            }

            String sql3 = "update user set dateTribe = ? where username = ?";//这个是实现退出部落，签到转到昨天的操作，就是防止等会新加部落不能签到了
            ps3 = conn.prepareStatement(sql3);
            ps3.setLong(1, SignTimeOperate.nowTime() - 60 * 60 * 24);//签到时间回到一天前，防止用户解散后加入部落被部落首领吊活跃度太低
            ps3.setString(2, username);
            count3 = ps3.executeUpdate();

            //count用于判断操作影响了几行，相当于判断操作成功没有
            if(1 != count1 && 1 != count2 && 1 != count3){
                System.out.println("程序执行异常");
                return false;
            }
            //程序执行到此处说明没有发生异常，那么事务结束，手动提交数据
            conn.commit();//提交事务

            return true;//修改成功
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
            DBUtil.close(ps1, rs);//工具类
            DBUtil.close(ps2, rs);//工具类
        }

        return false;//修改失败，就返回false咯
    }


}
