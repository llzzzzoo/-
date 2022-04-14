package com.linruipeng.www.util;

import java.sql.*;
import java.util.LinkedList;

/**
 * 这个所谓数据池我还以为很复杂呢(其实真的很复杂，如果放到现实使用状况下的话)
 * 创建一个数据连接池
 * 就是每次我们调用对应方法都得创建一个新的连接，然后close
 * 这个连接池就是放一些连接在池子里面，然后每次我们调用就读一下就行
 * 不要了就放回池子也不关闭
 */
public class DBUtil {
    private static final LinkedList<Connection> dataSource = new LinkedList<>();//这个就是池子，把连接放到这个链表里面，而且这里最好采用这个双向链表，因为我后面会对连接池进行增删来实现获取连接和释放连接，所以链表效率高一丢丢
    //哦哦怪不得编译器提示我搞个final，其实就拿这个datasource引用绑死了这个数组的地址

    /**
     * 工具类的方法都是私有的
     * 因为工具类当中的方法都是静态的，不需要new对象，直接采用类名即可调用
     * 虽然我现在也不知道这个有什么用
     */
    private DBUtil(){

    }

    //静态代码块，实现了只注册一个驱动
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con;
            //用一个for循环来创建二十个连接
            for (int i = 0; i < 20; i++) {
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dream?useUnicode=true&characterEncoding=UTF-8", "root", "123456");//这里的改编码方式很重要啊老表   对不起老表，好像没有这个我也操作的喔
                dataSource.add(con);//并且同时把连接放到链表里面
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 说白了就是读取第一个元素，并且删除，免得下个操作连到同一个连接
     * @return 返回的就是连接
     */
    public static Connection getConnection(){

        return dataSource.removeFirst();//这个方法的意思是返回第一个元素，并且同时去除它，然后后面的元素就会补上来，不用担心会出现空
    }

    /**
     * 此处实现把连接释放
     * 说变了就是把连接加到链表去
     * @param conn 释放连接时传过来的参数
     */
    public static void releaseConnection(Connection conn){

        dataSource.add(conn);//默认加到最后去，双向链表其实也有下标
    }

    /**
     * 关闭资源
     * 本来还有个close连接conn的，但是我搞了个连接池就不close了
     * @param ps 数据库操作对象
     * @param rs 结果集
     */
    public static void close(Statement ps, ResultSet rs){//这里的参数是Statement，不用担心，因为PS那个也是这玩意的子类，会向上转型的
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(ps != null){
            try {
                ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }
}
