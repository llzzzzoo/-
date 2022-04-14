package com.linruipeng.www.service;

import com.linruipeng.www.po.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 用处处理登录的类
 */
public class LoginOperate {
    /**
     * 用于登录界面
     * 但是我还是把一些对dao层的操作放到这里了
     * @return Map<String, String>
     */
    public static Map<String, String> loginOperate(){
        Scanner s = new Scanner(System.in);

        System.out.println("请输入用户名：");
        String loginUsername = s.nextLine();

        System.out.println("请输入密码：");
        String loginPassword = s.nextLine();

        //下面的代码其实可以放到po层的，但是怎么说呢，懒就一个字，我只说一次
        //把数据存储到Map集合中
        Map<String, String> userLoginInfo;
        userLoginInfo = new HashMap<>();//自动类型推断机制，又称钻石表达式
        userLoginInfo.put("loginUsername", loginUsername);
        userLoginInfo.put("loginPassword", loginPassword);

        return userLoginInfo;//返回一个集合用以对数据库的进行比较
    }

    /**
     * 我写这个的目的就是为了重新登录的操作，为什么要这样呢
     * 这样考虑到用户登录了，但是部落首领把部落解散了
     * 那么用户查看我的部落的时候肯定得表现出来啊
     * 所以我就搞了一次重新登录的操作
     * @return Map<String, String>
     */
    public static Map<String, String> loginAgainOperate(User user){

        String loginUsername = user.getUsername();

        String loginPassword = user.getPassword();

        //下面的代码其实可以放到po层的，但是怎么说呢，懒就一个字，我只说一次
        //把数据存储到Map集合中
        Map<String, String> userLoginInfo;
        userLoginInfo = new HashMap<>();//自动类型推断机制，又称钻石表达式
        userLoginInfo.put("loginUsername", loginUsername);
        userLoginInfo.put("loginPassword", loginPassword);

        return userLoginInfo;//返回一个集合用以对数据库的进行比较
    }
}
