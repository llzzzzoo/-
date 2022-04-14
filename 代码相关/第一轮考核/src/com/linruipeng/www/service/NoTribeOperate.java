package com.linruipeng.www.service;

import com.linruipeng.www.dao.Apply;
import com.linruipeng.www.dao.NewTribe;
import com.linruipeng.www.dao.Notice;
import com.linruipeng.www.dao.Select;
import com.linruipeng.www.po.User;

import java.util.Scanner;

/**
 * 这里放一些没部落的时候的操作
 */
public class NoTribeOperate {

    public static void noTribeOperate(User user, String str){
        boolean judge;
        String tribeName;
        String choice;
        Scanner s = new Scanner(System.in);

        switch (str) {
            case "1":
                //判断是否加入部落超过一天
                judge = SignTimeOperate.signQuitTribeView(user);

                if(judge) {
                    applyTribeOperate(user);//申请加入部落调用这个方法
                }

                break;
            case "2":
                //判断是否有1000金币已经是否真的创建
                System.out.println("创建部落需要花费1000金币");
                System.out.println("是否创建？");
                do {
                    System.out.print("请输入(Y/N)：");
                    choice = s.nextLine();
                }while(!("Y".equals(choice)) && !("N".equals(choice)));

                if("N".equals(choice)){
                    return;//如果选择否的话直接退出呗
                }

                if(user.getMoney() >= 1000) {//检测有没有1000金币的操作
                    user.setMoney(user.getMoney() - 1000);
                    System.out.println("剩余金币：" + user.getMoney());
                }else{
                    System.out.println("抱歉您的金币不足1000，还缺少" + (1000 - user.getMoney()) + "金币");//原来打个括号啊
                    return;
                }
                System.out.println();//换个行
                //判断是否已经存在改部落了
                do {
                    System.out.println("请输入您要创建的部落名");//创建部落的操作
                    System.out.print("请输入：");
                    tribeName = s.nextLine();
                    if(!Select.selectTribeNameRepeatedly(tribeName)){
                        break;
                    }
                    System.out.println("当前部落名已被注册");
                    System.out.println();//换个行
                }while(true);
                //实现创建部落的操作
                System.out.println(NewTribe.createTribe(user, tribeName) ? "创建成功" : "\n创建失败");

                break;
        }

    }


    /**
     * 申请加入部落的操作
     * @param user 用户
     */
    public static void applyTribeOperate(User user){
        Scanner s = new Scanner(System.in);
        String judge;//判断是否加入
        String tribeName;//想加入的部落名

        do {
            //这里加入退出部落的检测
            System.out.println("请输入你要加入的部落：");//加入部落的操作
            tribeName = s.nextLine();
            Select.applyTribeMoney = Select.selectTribeAccu(user, tribeName);//这个地方判断是否输入了对的部落

            if (-1 != Select.applyTribeMoney) {
                break;
            }
            System.out.println("请输入正确的部落！\n");//程序执行失败，喊操作者检查是否输入部落有误
        } while (true);

        System.out.println("加入部落需要上交您的全部金币(Y/N)");
        do {
            System.out.print("请输入：");
            judge = s.nextLine();
        } while (!("Y".equals(judge)) && !("N".equals(judge)));

        if ("Y".equals(judge)) {
            Apply.startApply(user, tribeName, Select.returnBossForNoTribe(user, tribeName));//搞一个新记录进去

            Notice.createNoticeRecord(user, "申请加入部落通知", "您好，我申请加入你的部落", Select.returnBossForNoTribe(user, tribeName));//发一条通知

        } else {
            System.out.println("加入失败\n");
        }

    }

}
