package com.linruipeng.www.service;

import com.linruipeng.www.dao.*;
import com.linruipeng.www.po.User;
import com.linruipeng.www.view.BranchView;

import java.util.Map;
import java.util.Scanner;

/**
 * 这里主要放user方面的操作
 */
public class UserOperate {

    public static void userOperate(User user, String str) {
        Map<String, String> userLoginInfoGroup = LoginOperate.loginAgainOperate(user);//搞一次更新，说白了就是重新登陆一次
        LoginVerify.loginAgain(user, userLoginInfoGroup);//这里实现把最新的值赋进去

        switch (str) {
            case "1":
                System.out.println();//换个行
                SignTimeOperate.signDayOperate(user);//签到操作
                break;

            case "2":
                System.out.println();//换个行
                PrintInfo.printMyInfo();//打印我的信息
                break;

            case "3":
                System.out.println();//换个行
                BranchView.myGroup(user);//我的阵营
                break;

            case "4":
                System.out.println();//换个行
                //如果没部落，提醒一下，并且说一下要不要新建部落
                if ("无1".equals(user.getTribe()) || "无2".equals(user.getTribe())) {
                    BranchView.noTribeView(user);//进入没加入部落的界面
                } else {
                    //有部落就进入下面的操作
                    if (2 == user.getMark()) {//如果为2的话就进入普通成员的界面
                        BranchView.myTribeInfo(user);//普通成员的部落
                    } else if(1 == user.getMark()){
                        BranchView.adminTribeInfo(user);//首领进入的界面
                    }else{
                        BranchView.relationshipTribeInfo(user);//关系户的界面
                    }
                }
                break;

            case "5":
                BranchView.noticeView(user);//我的通知
                break;

            case "6":
                System.out.println();//换个行
                System.out.println(Update.changePassword(user) ? "修改成功\n" : "修改失败\n");//修改密码
                break;

        }
    }

    public static void likeOperate(User user) {
            Scanner s = new Scanner(System.in);
            boolean likeJudge;

            String username;
            System.out.println("请输入需要点赞人员的用户名：");//点赞指定人员
            username = s.nextLine();
            likeJudge = LikeRecord.likeRecord(user, username);
            if (!Select.selectUsernameInOneTribeRepeatedly(user, username)) {
                System.out.println("用户名不存在！");
            } else if (likeJudge) {
                System.out.println("您已经点过赞了！请勿重复点赞！");
            } else if(username.equals(user.getUsername())){
                System.out.println("不能给自己点赞哦");
            } else {
                System.out.println("点赞成功！");
                Like.likeSomeone(user, username);
                Notice.createNoticeRecord(user, "点赞通知", "给你点个赞", username);
            }

    }
}

