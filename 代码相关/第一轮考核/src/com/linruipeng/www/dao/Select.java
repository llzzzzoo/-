package com.linruipeng.www.dao;

import com.linruipeng.www.po.User;
import com.linruipeng.www.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 普通用户和管理员查询的操作。
 * 记得管理员在查询后加上一条选择删除指定成员的信息
 * 以及赋予其他成员踢人的操作
 */
public class Select {
    public static int moneyTribe;//我这里搞这个公开的静态变量就是为了每次打印部落信息的时候顺便把部落的金币传到签到那里去
    public static int powerTribe;//同上，只是为了传过去战力
    public static int applyTribeMoney;//这个是检测加入部落的时候是否输入正确

    /**
     * 这里实现精准查询
     * 只能实现查询自己部落的啊
     * @param username 是要查询的用户名
     */
    public static boolean selectAccu(User user, String username) {//这是直接到用户名的精准查询
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int mark = 0;//标记，这个标记是为了当
        String myTribe = "\"" + user.getTribe() + "\"";//嘎嘎乱杀

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            //不用担心这个会打印到其他部落的信息，因为即便不同部落的用户名也是不能一样的，所以这个username自带部落属性
            //而且管理员只能看到自己部落的信息，那就更不用担心打印其他部落的了
            String sql = "select * from user where username = ? and tribe = " + myTribe;
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if(rs.next()){
                mark++;
                System.out.println("具体信息如下");
                System.out.println("用户名：" + rs.getString(2));
                System.out.println("性别：" + rs.getString(4));
                System.out.println("金币：" + rs.getInt(7));
                System.out.println("点赞：" + rs.getInt(8));
                System.out.println();//换个行
            }
            if(0 == mark){
                return false;//执行到这个地方就是说明没有找到
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

        return true;//执行到这里就说明找到了
    }

    /**
     * 有个细节，这个方法应该是那个人加入部落前调用的，所以这里直接搜索用户名就行了
     * 打印用户信息
     * @param applicant 申请人
     * @param user 这个的唯一作用就是传过来申请人申请的部落，方便我删除
     */
    public static void printApplicantAccu(User user, String applicant) {//这是直接到用户名的精准查询
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int mark = 0;//标记，这个标记是为了当

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            //不用担心这个会打印到其他部落的信息，因为即便不同部落的用户名也是不能一样的，所以这个username自带部落属性
            //而且管理员只能看到自己部落的信息，那就更不用担心打印其他部落的了
            String sql = "select * from user where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, applicant);
            rs = ps.executeQuery();
            if(rs.next()){
                // 这里放一个删除申请的操作，如果申请多个部落，被其中一个接受了的话，就删除其他部落的申请通知
                //第一步，在打印信息的时候检测，所以就在这里面进行检测了，break一下不打印了呗
                if(!"无1".equals(rs.getString(6)) && !"无2".equals(rs.getString(6))){
                    Delete.deleteApplyNotice(rs.getString(2), user.getTribe(), user.getUsername());
                }

                mark++;
                System.out.println("第“" + mark + "”位申请人");
                System.out.println("具体信息如下");
                System.out.println("用户名：" + rs.getString(2));
                System.out.println("性别：" + rs.getString(4));
                System.out.println("金币：" + rs.getInt(7));
                System.out.println("点赞：" + rs.getInt(8));
                System.out.println();//换个行
            }
            if(0 == mark){
                System.out.println("程序执行出错");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

    }




    /**
     * 用来检测用户名是否注册重复了
     * @param username 用户名
     * @return 返回true表示数据库已经有了该用户
     */
    public static boolean selectUsernameRepeatedly(String username) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            //不用担心这个会打印到其他部落的信息，因为即便不同部落的用户名也是不能一样的，所以这个username自带部落属性
            //而且管理员只能看到自己部落的信息，那就更不用担心打印其他部落的了
            String sql = "select * from user where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if(rs.next()){
                return true;//如果数据库已经有这个用户名就返回true
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

        return false;//执行到此处说明执行失败了
    }

    /**
     * 用来检测一个部落的用户是否存在
     * @param username 用户名
     * @return 返回true表示数据库已经有了该用户
     */
    public static boolean selectUsernameInOneTribeRepeatedly(User user, String username) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            //不用担心这个会打印到其他部落的信息，因为即便不同部落的用户名也是不能一样的，所以这个username自带部落属性
            //而且管理员只能看到自己部落的信息，那就更不用担心打印其他部落的了
            String sql = "select * from user where username = ? and tribe = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, user.getTribe());
            rs = ps.executeQuery();
            if(rs.next()){
                return true;//如果数据库已经有这个用户名就返回true
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

        return false;//执行到此处说明执行失败了
    }

    /**
     * 用来检测部落名是否注册重复了
     * @param tribeName 部落名
     * @return 返回true表示数据库已经有了该用户
     */
    public static boolean selectTribeNameRepeatedly(String tribeName) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            //不用担心这个会打印到其他部落的信息，因为即便不同部落的用户名也是不能一样的，所以这个username自带部落属性
            //而且管理员只能看到自己部落的信息，那就更不用担心打印其他部落的了
            String sql = "select * from tribe where tribe = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, tribeName);
            rs = ps.executeQuery();
            if(rs.next()){
                return true;//如果数据库已经有这个用户名就返回true
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

        return false;//执行到此处说明执行失败了
    }


    /**
     * 这里实现模糊查询
     * 只能打印出自己部落的啊
     */
    public static boolean selectVag(User user, String str){//此处实现模糊查询
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int mark = 0;//标记
        String myTribe = "\"" + user.getTribe() + "\"";//嘎嘎乱杀

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            String sql = "select * from user where username like ? and tribe = " + myTribe;
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%"+ str +"%");
            rs = ps.executeQuery();
            while(rs.next()) {
                mark++;
                if (!rs.getString(2).equals(user.getUsername())) {
                    System.out.println("已为您找到");
                    System.out.println("用户名：" + rs.getString(2));
                    System.out.println("性别：" + rs.getString(4));
                    System.out.println("金币：" + rs.getString(7));
                    System.out.println("点赞：" + rs.getString(8));
                    System.out.println();//换个行
                }
            }
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
     * 按照性别查询，并且只能查询到自己部落的
     * @param sex 性别
     */
    public static boolean selectSex(User user, String sex){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int mark = 0;//标记
        String myTribe = "\"" + user.getTribe() + "\"";//嘎嘎乱杀

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            String sql = "select * from user where sex = ? and tribe = " + myTribe;
            ps = conn.prepareStatement(sql);
            ps.setString(1, sex);
            rs = ps.executeQuery();

            while(rs.next()){
                mark++;
                System.out.println("已为您找到");
                System.out.println("用户名：" + rs.getString(2));
                System.out.println("性别：" + rs.getString(4));
                System.out.println("金币：" + rs.getString(7));
                System.out.println("点赞：" + rs.getString(8));
                System.out.println();//换个行
            }
            if(0 == mark){
                return false;//执行到此处就是查询失败了
            }

        } catch (Exception e) {
            e.printStackTrace();

        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }
        return true;
    }

    /**
     * 这里实现查询我的阵营的部落的信息
     */
    public static void selectGroupAccu(String groupName) {//这里打印全部部落信息
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            //不用担心这个会打印到其他部落的信息，因为即便不同部落的用户名也是不能一样的，所以这个username自带部落属性
            //而且管理员只能看到自己部落的信息，那就更不用担心打印其他部落的了
            String sql = "select * from tribe where groupName = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, groupName);
            rs = ps.executeQuery();
            String tribeName;
            while(rs.next()){
                tribeName = rs.getString(3);
                if(!("无1".equals(tribeName)) && !("无2".equals(tribeName))) {//这里就是避免打印出部落无的家伙
                    System.out.println("已为您找到");
                    System.out.println("部落名：" + tribeName);
                    System.out.println("部落首领：" + rs.getString(4));
                    System.out.println("成员数量：" + rs.getString(5));
                    System.out.println("战力：" + rs.getInt(6));
                    System.out.println("金币数：" + rs.getInt(7));
                    System.out.println("创建日期：" + rs.getString(8));
                    System.out.println();//换个行
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

    }

    /**
     * 这里实现查询我申请的部落的信息
     * @return 返回就是部落当前的金币数目，说实话这里写的有点死
     */
    public static int selectTribeAccu(User user, String tribeName) {//这里打印全部部落信息
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            //不用担心这个会打印到其他部落的信息，因为即便不同部落的用户名也是不能一样的，所以这个username自带部落属性
            //而且管理员只能看到自己部落的信息，那就更不用担心打印其他部落的了
            String sql = "select * from tribe where tribe = ? and groupName = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, tribeName);
            ps.setString(2, user.getGroup());
            rs = ps.executeQuery();
            if(rs.next()){
                if(!"无".equals(tribeName)) {//这里就是避免打印出部落无的家伙
                    System.out.println("已为您找到");
                    System.out.println("部落名：" + tribeName);
                    System.out.println("部落首领：" + rs.getString(4));
                    System.out.println("成员数量：" + rs.getString(5));
                    System.out.println("战力：" + rs.getInt(6));
                    System.out.println("金币数：" + rs.getInt(7));
                    System.out.println("创建日期：" + rs.getString(8));
                    System.out.println();//换个行
                    return rs.getInt(7);//如果找到了就返回个金币数
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }
        return -1;//可以在这个方法的调用的地方，如果返回-1的话就说明输入有误
    }

    /**
     * 这玩意就一个作用，返回部落长的名字，但是你必须加入了部落才行
     * @param user 用来查询部落名字什么的
     * @return 返回部落长的名字
     */
    public static String selectMyTribeBoss(User user) {//这里打印全部部落信息
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            //不用担心这个会打印到其他部落的信息，因为即便不同部落的用户名也是不能一样的，所以这个username自带部落属性
            //而且管理员只能看到自己部落的信息，那就更不用担心打印其他部落的了
            String sql = "select * from tribe where tribe = ? and groupName = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getTribe());
            ps.setString(2, user.getGroup());
            rs = ps.executeQuery();
            if(rs.next()){
                if(!"无".equals(user.getTribe())) {//这里就是避免打印出部落无的家伙
                    return rs.getString(4);//返回部落首领的名字
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }
        return null;//可以在这个方法的调用的地方，如果返回-1的话就说明输入有误
    }

    /**
     * 这玩意就一个作用，返回部落长的名字，这个是针对没加部落的人的操作
     * @param user 用来查询部落名字什么的
     * @return 返回部落长的名字
     */
    public static String returnBossForNoTribe(User user, String tribeName) {//这里打印全部部落信息
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            //不用担心这个会打印到其他部落的信息，因为即便不同部落的用户名也是不能一样的，所以这个username自带部落属性
            //而且管理员只能看到自己部落的信息，那就更不用担心打印其他部落的了
            String sql = "select * from tribe where tribe = ? and groupName = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, tribeName);
            ps.setString(2, user.getGroup());
            rs = ps.executeQuery();
            if(rs.next()){
                if(!"无".equals(user.getTribe())) {//这里就是避免打印出部落无的家伙
                    return rs.getString(4);//返回部落首领的名字
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }
        return null;//可以在这个方法的调用的地方，如果返回-1的话就说明输入有误
    }



    /**
     * 这里实现查询我申请的部落的信息
     * @return 返回就是部落当前的人数
     */
    public static int selectTribePeoNumAccu(User user, String tribeName) {//这里打印全部部落信息
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            //不用担心这个会打印到其他部落的信息，因为即便不同部落的用户名也是不能一样的，所以这个username自带部落属性
            //而且管理员只能看到自己部落的信息，那就更不用担心打印其他部落的了
            String sql = "select * from tribe where tribe = ? and groupName = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, tribeName);
            ps.setString(2, user.getGroup());
            rs = ps.executeQuery();
            if(rs.next()){
                if(!"无".equals(tribeName)) {//这里就是避免打印出部落无的家伙
                    return rs.getInt(5);//如果找到了就返回个金币数
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }
        return -1;//可以在这个方法的调用的地方，如果返回-1的话就说明输入有误
    }

    /**
     * 这里实现分页打印部落信息的操作
     * @param pageNo 页数
     */
    public static void selectGroupDivide(int pageNo) {//这里实现分页打印部落信息的操作
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int startIndex = (pageNo-1) * 4;//这里实现打印指定目录

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            //不用担心这个会打印到其他部落的信息，因为即便不同部落的用户名也是不能一样的，所以这个username自带部落属性
            //而且管理员只能看到自己部落的信息，那就更不用担心打印其他部落的了
            String sql = "select * from tribe where groupName = ? limit ?, 4";//这个4表明是长度，不知道为啥我好像一页打印三个却得输入4
            ps = conn.prepareStatement(sql);
            ps.setString(1, LoginVerify.user.getGroup());//获取登录对象属于的阵营
            ps.setInt(2, startIndex);

            rs = ps.executeQuery();
            String tribeName;
            while(rs.next()){
                tribeName = rs.getString(3);
                if(!("无1".equals(tribeName)) && !("无2".equals(tribeName))) {//这里就是避免打印出部落无的家伙
                    System.out.println("已为您找到");
                    System.out.println("部落名：" + tribeName);
                    System.out.println("部落首领：" + rs.getString(4));
                    System.out.println("成员数量：" + rs.getString(5));
                    System.out.println("战力：" + rs.getString(6));
                    System.out.println("金币数：" + rs.getString(7));
                    System.out.println("创建日期：" + rs.getString(8));
                    System.out.println();//换个行
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

    }

    /**
     * 这里实现打印全部行数的操作
     * @return 返回的就是行数
     */
    public static int selectLines(User user){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int countLine = 0;

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //将自动提交机制改为手动提交
            conn.setAutoCommit(false);//开启事物

            //3、获取数据库操作对象
            String sql = "select count(tribe) from tribe where groupName = ?";//实现返回多少行
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getGroup());
            rs = ps.executeQuery();
            if(rs.next()){
                countLine = rs.getInt(1);//返回行数
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
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

        return countLine;
    }

    /**
     * 打印我的部落的信息
     */
    public static void printMyTribe(User user){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            //不用担心这个会打印到其他部落的信息，因为即便不同部落的用户名也是不能一样的，所以这个username自带部落属性
            //而且管理员只能看到自己部落的信息，那就更不用担心打印其他部落的了
            String sql = "select * from tribe where tribe = ?";//我吐了我tm这里from user找了半天
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getTribe());
            rs = ps.executeQuery();
            if(rs.next()){
                System.out.println("已为您找到");
                System.out.println("阵营：" + rs.getString(2));
                System.out.println("部落：" + rs.getString(3));
                System.out.println("部落首领：" + rs.getString(4));
                System.out.println("部落人数：" + rs.getInt(5));
                System.out.println("战力：" + rs.getInt(6));
                System.out.println("金币数：" + rs.getInt(7));
                System.out.println("创始日期：" + rs.getString(8));
                System.out.println();//换个行
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

    }

    /**
     * 这里我这么写就是方便后面得到我的部落的一些值啊什么的
     * @param user 为了打印我的部落的东西
     */
    public static void getMyTribeInfo(User user){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            //不用担心这个会打印到其他部落的信息，因为即便不同部落的用户名也是不能一样的，所以这个username自带部落属性
            //而且管理员只能看到自己部落的信息，那就更不用担心打印其他部落的了
            String sql = "select * from tribe where tribe = ?";//我吐了我tm这里from user找了半天
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getTribe());
            rs = ps.executeQuery();
            if(rs.next()){
                powerTribe = rs.getInt(6);
                moneyTribe = rs.getInt(7);
                System.out.println();//换个行
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

    }

    public static int getLike(String username){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int likeNum = 0;
        //String likeUsername = "\"" + username + "\"";//没错左边这个玩意感觉好像也就在注入现象的条件下才适用唉浪费我大半天

        try {
            //获取连接
            conn = DBUtil.getConnection();

            //获取预编译的数据库操作对象
            //不用担心这个会打印到其他部落的信息，因为即便不同部落的用户名也是不能一样的，所以这个username自带部落属性
            //而且管理员只能看到自己部落的信息，那就更不用担心打印其他部落的了
            String sql = "select * from user where username = ?";//我吐了我tm这里from user找了半天
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if(rs.next()){
                likeNum = rs.getInt(8);
                System.out.println();//换个行
            }

            return likeNum;//返回的是得到的总赞数
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.releaseConnection(conn);//释放连接
            DBUtil.close(ps, rs);//工具类
        }

        return -1;//返回-1必然是出现错误了
    }


    /**
     * 这里算出加入部落的金币的数目
     * @param user 作用就是获取当前部落的金币数目
     * @return 返回的就是部落金币的数目
     */
    public static int tribeMoney(User user){
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
            String sql = "select * from tribe where tribe = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getTribe());
            rs = ps.executeQuery();

            if(rs.next()){
                return rs.getInt(7);
            }

            System.out.println("程序执行异常");

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
        return -1;//执行到此处说明执行出错了
    }


    /**
     * 这里计算出申请人的金币数量
     * @param applicant 申请人
     * @return 返回的就是金币的数目
     */
    public static int applicantMoney(String applicant){
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
            String sql = "select * from user where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, applicant);
            rs = ps.executeQuery();

            if(rs.next()){
                return rs.getInt(7);
            }

            //count用于判断操作影响了几行，相当于判断操作成功没有
            System.out.println("程序执行异常");

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
        return -1;//执行到此处说明代码错误了
    }

}
