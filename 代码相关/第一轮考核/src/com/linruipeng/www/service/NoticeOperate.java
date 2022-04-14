package com.linruipeng.www.service;

import com.linruipeng.www.dao.Notice;
import com.linruipeng.www.po.User;
import com.linruipeng.www.view.BranchView;

import java.util.Scanner;

/**
 * 这里处理通知相关的操作
 */
public class NoticeOperate {

    /**
     * 这里就是实现对notice表的操作
     * @param user 这个用户两个作用提供一个id和用户名(其实用户名不需要也行的) 方便我去遍历表
     * @param noticeMark 这个就是对应查看已读文件或者未读文件
     * @return 返回true，表明的确有未读信息，返回false，打印一句，抱歉您没有未读邮件
     */
    public static boolean noticeUnread(User user, int noticeMark){

        return Notice.noticeUnreadTable(user, noticeMark);//这里传过去的mark代表了对应的搜寻操作
    }

    /**
     * 这里就是实现对notice表的操作
     * @param user 这个用户两个作用提供一个id和用户名(其实用户名不需要也行的) 方便我去遍历表
     * @param noticeMark 这个就是对应查看已读文件或者未读文件
     * @return 返回true，表明的确已读消息，返回false，打印一句，抱歉您没有已读消息
     */
    public static boolean noticeAlreadyRead(User user, int noticeMark){

        return Notice.noticeAlreadyReadTable(user, noticeMark);//这里传过去的mark代表了对应的搜寻操作
    }


    /**
     * 用来跟view和dao层的过渡
     * @param user 用来传id和地址
     * @param str  对应指令
     */
    public static void noticeOperate(User user, String str){

        switch(str){
            case "1" :
                if (!NoticeOperate.noticeUnread(user, 0)) {//这里实现查看未读文件，查看之后必须把mark改为1
                    System.out.println("您当前没有新邮件哦！");
                } //打印未读文件
                break;
            case "2" :
                if(!NoticeOperate.noticeAlreadyRead(user, 1)) {//打印已读文件
                    System.out.println("您当前没有已读邮件哦！");
                }
                    break;
            case "3" :
                BranchView.deleteView(user);//进入删除指定邮件的界面
                break;

        }

        System.out.println();//换个行
    }


    public static void deleteNotice(User user, String str){
        String judge;
        Scanner s = new Scanner(System.in);
        String[] title;
        String[] applicant;
        int no;//这是等会数组的序号，等会我要转型的

        switch(str){
            case "1" :
                System.out.println("确认删除？(Y/N)");
                do {
                    System.out.print("请输入：");
                    judge = s.nextLine();
                } while (!("Y".equals(judge)) && !("N".equals(judge)));

                if("Y".equals(judge)) {
                    System.out.println(Notice.deleteAllNotice(user, 1) ? "删除成功" : "删除失败");//删除全部文件
                }
                    break;
            case "2" :
                title = Notice.returnTitleAlreadyReadTable(user, 1);//删除指定文件
                applicant = Notice.returnApplicantAlreadyReadTable(user, 1);
                System.out.println("请输入你需要删除的邮件的序号：");
                do {
                    no = s.nextInt();
                    if(no >= 1 && no < Notice.HaveRendNum)
                    {
                        break;
                    }
                    System.out.println("请输入正确的序号");
                }while(true);

                //下面就进入dao层删除操作了
                System.out.println(Notice.deleteOneNotice(user, title[no - 1], applicant[no - 1], 1) ? "删除成功" : "删除失败");

                break;
        }
    }

}
