package com.linruipeng.www.view;

import com.linruipeng.www.dao.*;
import com.linruipeng.www.po.User;
import com.linruipeng.www.service.*;
import com.linruipeng.www.util.MyException;
import java.util.Map;
import java.util.Scanner;

/**
 * 主要放一些主干上的界面
 */
public class MainView {
    static boolean loginResult = false;//通过这个判断登录成功否，是否new对象

    /**
     * 最初的界面
     * 但是我还是把一些对dao层的操作放到这里了
     */
    public static void initUI() throws MyException.IllegalNameException {

            System.out.println("====请输入以下指令=====");
            System.out.println("====1.登录====");
            System.out.println("====2.注册====");
            Scanner s = new Scanner(System.in);
            String str;
            do {
                System.out.print("请输入：");
                str = s.nextLine();
            }while((!"1".equals(str)) && (!"2".equals(str)));
            if("1".equals(str)){

                //登录会搞一个方法去检测是存在该用户
                //也是去到了service层对应的方法了
                Map<String, String> userLoginInfo = LoginOperate.loginOperate();//返回一个Map集合来比较数据库和输入的匹配

                //验证账号密码的正确性
                loginResult = LoginVerify.loginVerify(userLoginInfo);//验证登录成功了没

                if(loginResult){
                    if(1 == LoginVerify.user.getMark()) {
                        System.out.println("登录成功！欢迎“" + LoginVerify.user.getTribe() + "”的首领“" + LoginVerify.user.getUsername() + "”来到Dream的世界！\n");
                    }else{
                        System.out.println("登录成功！欢迎“"+ LoginVerify.user.getUsername() + "”来到Dream的世界！\n");
                    }
                    //进入下一个界面
                    userUI(LoginVerify.user);
                }else{
                    System.out.println("登录失败！请检查用户名或密码是否正确！");
                }

            }else{
                //注册操作
                //下面就是进入service层的对应操作了
                RegisterOperate.registerOperate();
            }

        }


    /**
     * 用于登录成功后的界面
     */
    public static void userUI(User user) {
        //下面两行就是为了判断签到的时间，然后决定给你发邮件提醒你签到不
        SignTimeOperate.signMyTimeJudge(user, SignTimeOperate.nowTime());
        //有部落的才能这样操作
        if(!"无1".equals(user.getTribe()) && !"无2".equals(user.getTribe())) {
            SignTimeOperate.signTribeTimeJudge(user, SignTimeOperate.nowTime());
        }

        while (true) {
            System.out.println("====请输入以下指令====");
            System.out.println("====1.每日签到========");
            System.out.println("====2.我的信息========");
            System.out.println("====3.我的阵营========");
            System.out.println("====4.我的部落========");
            System.out.println("====5.我的通知========");
            System.out.println("====6.修改密码========");
            System.out.println("====7.退出登录========");
            Scanner s = new Scanner(System.in);
            System.out.print("请输入：");
            String str;
            do {
                str = s.nextLine();
            } while (!("1".equals(str)) && !("2".equals(str)) && !("3".equals(str)) && !("4".equals(str)) && !("5".equals(str)) && !("6".equals(str)) && !("7".equals(str)));//这么长的判断就是为了防止赋值出现输入字符串的操作
            System.out.println();//换个行

            if("7".equals(str)){
                System.out.println("感谢使用！");
                return;
            }

            //下面就是进入service层对应的类的了
            UserOperate.userOperate(user, str);

        }
    }
}
