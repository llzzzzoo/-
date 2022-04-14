package com.linruipeng.www.service;

import com.linruipeng.www.dao.Delete;
import com.linruipeng.www.dao.Notice;
import com.linruipeng.www.dao.Select;
import com.linruipeng.www.dao.Update;
import com.linruipeng.www.po.User;

import java.util.Scanner;

/**
 * 踢人操作相关的类
 * 我把赋予别人踢人权限也放到这里
 */
public class KickOperate {

    /**
     * 踢人service层
     * @param user 这个用来判断踢出后修改部落名为什么
     * @return 踢出成功返回true呗
     */
    public static boolean kickOut(User user){
        Scanner s = new Scanner(System.in);

        String username;
        do {
            System.out.println("请输入需要踢出人员的用户名：");//点赞指定人员
            username = s.nextLine();
            if (username.equals(Select.selectMyTribeBoss(user))) {
                System.out.println("不可踢出自己的部落首领\n");
            }else if(Select.selectUsernameInOneTribeRepeatedly(user, username)){//这里是处理吕布反噬董卓的情节
                break;
            }else{
                System.out.println("用户名不存在！\n");
            }
        }while(true);

        Delete.deleteUser(user, username);

        if(3 == user.getMark()){//如果检查出来是赋予的权限，那么经过这一次踢人之后，修改回mark2
            user.setMark(2);
            Update.updateMark(user, 2);//下面就是发一个邮件提醒一下
        }
        Notice.createNoticeRecord(user, "踢出通知", "您已经被踢出部落", username);

        return true;//执行到这里就返回true呗
    }

    /**
     * 指定谁有踢人权限
     * @param user 不知道参数有什么用，反正传都传了
     */
    public static void kickOutPermission(User user){
        Scanner s = new Scanner(System.in);

        String username;
        do {
            System.out.println("请输入赋予踢人权限的用户名(赋予的成员仅有一次踢人权限)：");//点赞指定人员
            username = s.nextLine();
            if (Select.selectUsernameInOneTribeRepeatedly(user, username)) {
                break;
            }
            System.out.println("用户名不存在！\n");//其实这里就会帮忙排除其他阵营的老六
        }while(true);
        Update.updateUserMark(user, username, 3);
        System.out.println("赋予成功");
    }

}
