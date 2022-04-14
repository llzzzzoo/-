package com.linruipeng.www.service;

import com.linruipeng.www.dao.Register;
import com.linruipeng.www.dao.Select;
import com.linruipeng.www.po.User;
import com.linruipeng.www.util.MyException;

import java.util.Scanner;

/**
 * 这就是注册的操作
 */
public class RegisterOperate {

    /**
     * 用于注册界面
     *
     */
    public static void registerOperate() throws MyException.IllegalNameException {
        Scanner s = new Scanner(System.in);
        String noTribe;

        System.out.println("请输入用户名：");//这里缺个防止重复的，我后面补一下
        String username = s.nextLine();
        while(Select.selectUsernameRepeatedly(username)) {//如果重复了话就会返回true，然后一直卡在这里
            System.out.println("用户名已被注册，请重新输入：");
            username = s.nextLine();
        }
        System.out.println("请输入密码：");
        String password = s.nextLine();

        RegisterCheck.register(username);//检查用户名是否合法，先是输入长度是否合法
        //出问题直接上抛

        String sex;
        do {
            System.out.println("请输入性别(男/女)：");
            sex = s.nextLine();

        }while(!("男".equals(sex)) && !("女".equals(sex)));


        String group;
        do {
            System.out.println("请输入你选择加入的阵营");
            System.out.println("shine/dark");
            group = s.nextLine();

        }while(!("shine".equals(group)) && !("dark".equals(group)));
        if("shine".equals(group)){
            noTribe = "无1";
        }else{
            noTribe = "无2";
        }

        //执行到这里的话就可以new对象，然后把值传进去了，然后我们可以通过对象把值传给数据库
        //我new对象主要是为了方便把这么一大坨值传过去
        User registerUser = new User(0, username, password, sex, group, noTribe, 0, 0, SignTimeOperate.nowTime() - (60 * 60 * 24), SignTimeOperate.nowTime() - (60 * 60 * 24), 0, 2);//默认的金币和点赞0和0
        //上面我的注册这样写，是为了实现刚注册也能签到的操作
        //这里的退出部落的时间初始化确实该为0
        //这里的mark属性也确实该是2

        Register reg = new Register();//这里很坑爹，只能new一个类来操作里面对数据库的注册方法
        if(!reg.register(registerUser)){
            throw new MyException.IllegalNameException("注册失败");//这里的异常会往上抛，我打算在main方法捕获，并且命名出现不明错误吧
        }else{
            System.out.println("注册成功！欢迎来到Dream的世界");
            System.out.println("请重新登录");
        }

    }
}
