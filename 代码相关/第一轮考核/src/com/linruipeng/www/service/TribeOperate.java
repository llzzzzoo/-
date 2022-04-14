package com.linruipeng.www.service;

import com.linruipeng.www.dao.*;
import com.linruipeng.www.po.User;
import com.linruipeng.www.view.BranchView;

import java.util.Map;
import java.util.Scanner;

import static com.linruipeng.www.dao.Update.applyTribeChangePeoNum;
import static com.linruipeng.www.view.BranchView.*;

/**
 * 这个类用于放一些对部落的操作，好比排序啊，打印信息，查询啊什么的
 */
public class TribeOperate {

    /**
     * 这里统一普通用户调用对部落的操作
     */
    public static void tribeOperate(User user, String str){
        String judge;
        Scanner s = new Scanner(System.in);

        switch (str) {
            case "1":
                System.out.println();//换个行
                SignTimeOperate.signTribeOperate(user);//签到
                break;
            case "2":
                System.out.println();//换个行
                Select.printMyTribe(user);//显示部落信息
                break;
            case "3":
                System.out.println();//换个行
                sort(user);//显示部落成员的信息
                break;
            case "4":
                System.out.println();//换个行
                //查询部落成员
                inquiry(user);
                break;
            case "5":
                    System.out.println();//换个行
                    System.out.println("确认退出？(Y/N)");
                    do {
                        System.out.print("请输入：");
                        judge = s.nextLine();
                    } while (!("Y".equals(judge)) && !("N".equals(judge)));

                    if ("Y".equals(judge)) {
                        user.setQuitTribeTime(SignTimeOperate.nowTime());//细节如我啊，他按下退出的时候算做mark，那么存入数据库的应该是这个时间啊
                        applyTribeChangePeoNum(user, Select.selectTribePeoNumAccu(user, user.getTribe()) - 1);//退出部落部落人数减1
                        System.out.println(QuitTribe.quitTribe(user) ? "成功退出" : "退出失败");//实现退出部落的操作
                    }

                break;
        }
        System.out.println();//换个行
    }

    /**
     * 这里统一管理员调用对部落的操作
     */
    public static void adminTribeOperate(User user, String str){
        String judge;
        Scanner s = new Scanner(System.in);
        boolean changeResult;

        switch (str) {
            case "1":
                System.out.println();//换个行
                SignTimeOperate.signTribeOperate(user);//签到
                break;
            case "2":
                System.out.println();//换个行
                Select.printMyTribe(user);//显示部落信息
                break;
            case "3":
                System.out.println();//换个行
                sort(user);//显示部落成员的信息
                break;
            case "4":
                System.out.println();//换个行
                //查询部落成员
                inquiry(user);
                break;

            case "5" :
                System.out.println();//换个行
                //进入踢出的子界面
                BranchView.adminKickView(user);

                break;
                case "6" :
                System.out.println();//换个行
                //然后进入下一个界面
                BranchView.applyView(user);
                break;

            case "7":
                    System.out.println();//换个行
                    System.out.println("确认解散？(Y/N)");
                    do {
                        System.out.print("请输入：");
                        judge = s.nextLine();
                    } while (!"Y".equals(judge) && !"N".equals(judge));
                    if("Y".equals(judge)) {
                        // 并且部落的成员全部部落变为对应阵营的无
                        changeResult = Update.updateAllTribe(user);
                        if(!changeResult){
                            System.out.println("程序执行错误!");
                            return;
                        }
                        System.out.println(Delete.deleteTribe(user) ? "解散成功" : "解散失败\n");//删除部落
                        //咦我突然发现好像我重新登录的操作解决了无数的动态更新问题哎，焯！为什么之前没想到
                    }

                break;

        }
        System.out.println();//换个行
    }

    /**
     * 这里统一被赋予权限的人调用对部落的操作
     */
    public static void relationshipTribeOperate(User user, String str){
        Map<String, String> userLoginInfoGroup = LoginOperate.loginAgainOperate(user);//搞一次更新，说白了就是重新登陆一次
        LoginVerify.loginAgain(user, userLoginInfoGroup);//这里实现把最新的值赋进去

        Scanner s = new Scanner(System.in);
        String judge;

        switch (str) {
            case "1":
                System.out.println();//换个行
                SignTimeOperate.signTribeOperate(user);//签到
                break;
            case "2":
                System.out.println();//换个行
                Select.printMyTribe(user);//显示部落信息
                break;
            case "3":
                System.out.println();//换个行
                sort(user);//显示部落成员的信息
                break;
            case "4":
                System.out.println();//换个行
                //查询部落成员
                inquiry(user);
                break;
            case "5":
                System.out.println();//换个行
                //进入踢人的界面
                BranchView.relationshipKickView(user);
                break;
            case "6":
                System.out.println();//换个行
                System.out.println("确认退出？(Y/N)");
                do {
                    System.out.print("请输入：");
                    judge = s.nextLine();
                } while (!("Y".equals(judge)) && !("N".equals(judge)));

                if ("Y".equals(judge)) {
                    user.setQuitTribeTime(SignTimeOperate.nowTime());//细节如我啊，他按下退出的时候算做mark，那么存入数据库的应该是这个时间啊
                    applyTribeChangePeoNum(user, Select.selectTribePeoNumAccu(user, user.getTribe()) - 1);//退出部落部落人数减1
                    System.out.println(QuitTribe.quitTribe(user) ? "成功退出" : "退出失败");//实现退出部落的操作
                }
                break;
        }
        System.out.println();//换个行
    }



    /**
     * 这是排序的操作
     * @param user 针对登录用户
     * @param str 输入对应操作
     */
    public static void sortOperate(User user, String str){
        switch (str) {
            case "1":
                System.out.println();//换个行
                Sort.sortDate(user);//实现按照注册时间从早到晚排序
                BranchView.likeView(user);
                break;
            case "2":
                System.out.println();//换个行
                Sort.sortMoney(user);//实现按照金币数升序降序
                BranchView.likeView(user);
                break;
            case "3":
                System.out.println();//换个行
                Sort.sortLike(user);//实现按照点赞数排序
                BranchView.likeView(user);
                break;

        }
    }


    /**
     * 这个方法就是用于查询的
     * @param user 针对用户
     * @param str 所需要查询的字段
     */
    public static void inquiryUser(User user, String str){
        Scanner s = new Scanner(System.in);

        if ("1".equals(str)) {
            System.out.println("请输入您要查询的用户名：");//调用精准查询的方法
            String username = s.nextLine();
            boolean selectResult = Select.selectAccu(user, username);//实现精准查询
            if (!selectResult) {
                System.out.println("查无此用户！\n");
            }

        } else if("2".equals(str)){
            System.out.println("请输入关键字：");//调用模糊查询的方法
            String usernameVag = s.nextLine();
            boolean selectResult1 = Select.selectVag(user, usernameVag);
            if (!selectResult1) {
                System.out.println("查无此用户！\n");
            }
        }else if("3".equals(str)){
            String sex;
            do {
                System.out.println("请输入性别(男/女)：");
                sex = s.nextLine();
            }while(!("男".equals(sex)) && !("女".equals(sex)));
            if(!Select.selectSex(user, sex)){
                System.out.println("查无此类用户！\n");
            }
        }

        System.out.println();//换个行
    }

    public static void checkTribe(User user, String str, int pageNoMax){
        Scanner s = new Scanner(System.in);

        if ("1".equals(str)) {
            PrintInfo.printMyGroupInfo(user);//打印我的阵营的信息
        } else if ("2".equals(str)) {
            int pageNo;
            do {
                System.out.println("请输入[1," + pageNoMax + "]" + "的页数");
                pageNo = s.nextInt();//实现查询指定页码

            } while (pageNo < 1 || pageNo > pageNoMax);
            Select.selectGroupDivide(pageNo);//实现分页操作
        }

    }

}
