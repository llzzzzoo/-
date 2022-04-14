package com.linruipeng.www.view;

import com.linruipeng.www.dao.*;
import com.linruipeng.www.service.*;
import com.linruipeng.www.po.User;

import java.util.Map;
import java.util.Scanner;

import static com.linruipeng.www.dao.Update.applyTribeChangePeoNum;

/**
 * 用以放一些主干上分支的界面
 */
public class BranchView {
        public static int printMark;//这个就是操作打印未读或者已读消息界面遍历但不打印信息的操作，减少代码复用率
        public static int notHandleNum;//这个就是操作打印未读或者已读消息界面遍历但不打印信息的操作，减少代码复用率
        public static int printApplicantMark;

    /**
     * 用于普通用户实现模糊和进准查询
     * 管理员可以实现查询后的删除和任命为管理员操作
     */
    public static void inquiry(User user){
        while(true) {
            System.out.println("====请输入以下指令====");
            System.out.println("====1.精准查询=======");
            System.out.println("====2.模糊查询=======");
            System.out.println("====3.按照性别查询====");
            System.out.println("====4.返回上一级=====");
            Scanner s = new Scanner(System.in);
            String str;
            do {
                System.out.print("请输入：");
                str = s.nextLine();
            } while (!("1".equals(str)) && !("2".equals(str)) && !("3".equals(str)) && !("4".equals(str)));//这么长的判断就是为了防止赋值出现输入字符串的操作

            //当输入4的时候返回上一级，也就是结束这个方法
            if("4".equals(str)){
                System.out.println();//换个行
                return;
            }

            TribeOperate.inquiryUser(user, str);
        }
    }

    /**
     * 打印全部人员之前需要排序
     * @param user 对象的部落
     */
    public static void sort(User user){
        while(true) {
            System.out.println("====请按照以下指令操作====");
            System.out.println("====1.按照注册时间排序====");
            System.out.println("====2.按照金币数排序======");
            System.out.println("====3.按照点赞数排序======");
            System.out.println("====4.返回上一级=========");
            Scanner s = new Scanner(System.in);
            String str;
            do {
                System.out.print("请输入：");
                str = s.nextLine();
            } while (!("1".equals(str)) && !("2".equals(str)) && !("3".equals(str)) && !("4".equals(str)));//这么长的判断就是为了防止赋值出现输入字符串的操作

            //返回上一级就直接结束呗
            if("4".equals(str)){
                System.out.println();//换个行
                return;
            }

            //下面就是进入排序的service层
            TribeOperate.sortOperate(user, str);

            System.out.println();//换个行行，好行行
        }

    }

    /**
     * 处理申请人的界面
     * @param user 一般来说对象是那个谁组长吧
     */
    public static void applyView(User user){
        while(true) {
            Apply.notHandleApply(user, 0);//这里会把那个未处理的消息记录，放在循环里面，这样才能刷新

            printApplicantMark = 0;
            Apply.returnApplicant(user);//打印信息

            System.out.println("====请输入以下指令======");
            System.out.println("====1.接受指定成员申请===");
            System.out.println("====2.拒绝指定成员申请===");
            System.out.println("====3.返回上一级========");
            Scanner s = new Scanner(System.in);
            String str;
            do {
                System.out.print("请输入：");
                str = s.nextLine();
            } while (!("1".equals(str)) && !("2".equals(str)) && !("3".equals(str)));//这么长的
            //返回上一级就直接结束呗
            if ("3".equals(str)) {
                System.out.println();//换个行
                return;
            }

            //下面就是进入service层
            ApplyOperate.applyOperate(user, str);
        }

    }


    /**
     * 缺一个返回上一级的操作
     * @param user 登录用户
     */
   public static void myGroup(User user) {
       System.out.println("我的阵营：" + user.getGroup());
       while (true) {
           System.out.println("====请输入以下指令====");
           System.out.println("====1.查看所有部落====");
           System.out.println("====2.查询指定页码====");
           System.out.println("====3.返回上一级====");
           Scanner s = new Scanner(System.in);
           String str;
           int pageNoMax = (Select.selectLines(user) % 3 == 0 ? (Select.selectLines(user) / 3) : (Select.selectLines(user) / 3 + 1));//这里是用户能输入的最大的页数(一页三条信息)，如果你能看到这里的话我真的十分抱歉，写这种代码~_~
           do {
               System.out.print("请输入：");
               str = s.nextLine();
           } while (!("1".equals(str)) && !("2".equals(str)) && !("3".equals(str)));

           if("3".equals(str)){
               System.out.println();//换个行
               return;//如果输入指定值，结束方法
           }
           //下面就是进入了service层的操作
            TribeOperate.checkTribe(user, str, pageNoMax);

       }

   }

    /**
     * 打印我的部落的界面
     * @param user 传过来也没啥，就是看下id，以及看下部落
     */
   public static void myTribeInfo(User user) {
       Scanner s = new Scanner(System.in);

       while (true) {
           //如果退出了部落，那么每次进入这个玩意就调用那个没部落的方法
           if("无1".equals(user.getTribe()) || "无2".equals(user.getTribe())){
               break;
           }

           System.out.println("====请输入以下指令=======");
           System.out.println("====1.部落签到===========");
           System.out.println("====2.显示部落信息=======");
           System.out.println("====3.显示全部成员信息====");
           System.out.println("====4.查询指定部落人员====");
           System.out.println("====5.自行退出部落=======");
           System.out.println("====6.返回上一级=========");
           String str;
           do {
               System.out.print("请输入：");
               str = s.nextLine();
           } while (!("1".equals(str)) && !("2".equals(str)) && !("3".equals(str)) && !("4".equals(str)) && !("5".equals(str)) && !("6".equals(str)));//这么长的判断就是为了防止赋值出现输入字符串的操作

            if("6".equals(str)){
                System.out.println();//换个行
                return;
            }
            //进入到service层去
           TribeOperate.tribeOperate(user, str);

       }
   }

    /**
     * 打印管理员的部落的界面
     * @param user 传过来也没啥，就是看下id，以及看下部落
     */
    public static void adminTribeInfo(User user) {
        Scanner s = new Scanner(System.in);

        while (true) {
            Apply.notHandleApply(user, 0);//这里会把那个未处理的消息记录，放在循环里面，这样才能刷新
            //如果解散了部落，那么每次进入这个玩意就调用那个没部落的方法
            if("无1".equals(user.getTribe()) || "无2".equals(user.getTribe())){
                break;
            }

            System.out.println("====请输入以下指令============");
            System.out.println("====1.部落签到===============");
            System.out.println("====2.显示部落信息===========");
            System.out.println("====3.显示全部成员信息========");
            System.out.println("====4.查询指定部落人员========");
            System.out.println("====5.踢出的相关功能=========");
            System.out.println("====6.申请部落通知(" + notHandleNum + "条未处理)===");
            System.out.println("====7.解散当前部落===========");
            System.out.println("====8.返回上一级=============");
            String str;
            do {
                System.out.print("请输入：");
                str = s.nextLine();
            } while (!("1".equals(str)) && !("2".equals(str)) && !("3".equals(str)) && !("4".equals(str)) && !("5".equals(str)) && !("6".equals(str)) && !("7".equals(str)) && !("8".equals(str)));//这么长的判断就是为了防止赋值出现输入字符串的操作

            if("8".equals(str)){
                System.out.println();//换个行
                return;
            }
            //进入到service层去
            TribeOperate.adminTribeOperate(user, str);

        }
    }

    /**
     * 这是被赋予了踢人权限的关系户的界面
     * @param user 传过来也没啥，就是看下id，以及看下部落
     */
    public static void relationshipTribeInfo(User user) {
        while (true) {
            Map<String, String> userLoginInfoGroup = LoginOperate.loginAgainOperate(user);//搞一次更新，说白了就是重新登陆一次
            LoginVerify.loginAgain(user, userLoginInfoGroup);//这里实现把最新的值赋进去

            if(2 == user.getMark()){
                BranchView.myTribeInfo(user);//进入普通用户的界面
                return;
            }

            Scanner s = new Scanner(System.in);
            //如果退出了部落，那么每次进入这个玩意就调用那个没部落的方法
            if("无1".equals(user.getTribe()) || "无2".equals(user.getTribe())){
                break;
            }

            System.out.println("====请输入以下指令=======");
            System.out.println("====1.部落签到===========");
            System.out.println("====2.显示部落信息=======");
            System.out.println("====3.显示全部成员信息====");
            System.out.println("====4.查询指定部落人员====");
            System.out.println("====5.踢出指定部落人员====");
            System.out.println("====6.自行退出部落=======");
            System.out.println("====7.返回上一级=========");
            String str;
            do {
                System.out.print("请输入：");
                str = s.nextLine();
            } while (!("1".equals(str)) && !("2".equals(str)) && !("3".equals(str)) && !("4".equals(str)) && !("5".equals(str)) && !("6".equals(str)) && !("7".equals(str)));//这么长的判断就是为了防止赋值出现输入字符串的操作

            if("7".equals(str)){
                System.out.println();//换个行
                return;
            }
            //进入到service层去
            TribeOperate.relationshipTribeOperate(user, str);

        }
    }


    /**
     * 点赞的界面
     * 我打算是放在打印全部信息后面说一下
     */
   public static void likeView(User user){
       while(true) {
           //要在这里刷新一下点赞的记录
           Scanner s = new Scanner(System.in);
           System.out.println("====请输入以下指令====");
           System.out.println("===1.点赞指定人员===");
           System.out.println("===2.返回上一级===");
           String str;
           do {
               System.out.print("请输入：");
               str = s.nextLine();
           } while (!("1".equals(str)) && !("2".equals(str)));//这么长的判断就是为了防止赋值出现输入字符串的操作

            if("2".equals(str)){
                System.out.println();//换个行
                return;//实现返回上一级的操作
            }
            //进入到service的操作
           UserOperate.likeOperate(user);

           System.out.println();//换个行
       }
   }


    /**
     * 当没有部落的时候进入这个界面
     */
    public static void noTribeView(User user){
        //这里还是不要用while了吧，因为这个操作一次就可以了
        Scanner s = new Scanner(System.in);
        System.out.println("您当前未加入部落");
        System.out.println("====请按照下面指令操作====");
        System.out.println("====1.申请加入部落=======");
        System.out.println("====2.创建部落==========");
        System.out.println("====3.返回上一级========");
        String str;
        do {
            System.out.print("请输入：");
            str = s.nextLine();

            if("3".equals(str)){//如果是3的话就直接结束了
                System.out.println();//换个行
                return;
            }
        } while (!("1".equals(str)) && !("2".equals(str)));//这么长的判断就是为了防止赋值出现输入字符串的操作

        //申请加入部落的话就调用service层的方法
        NoTribeOperate.noTribeOperate(user, str);

        System.out.println();//换个行
    }

    /**
     * 管理员的踢人界面
     * 这么点东西就不放到service层了
     */
    public static void adminKickView(User user){
        while(true) {//要用while便于踢出多个人员
            Scanner s = new Scanner(System.in);
            System.out.println("====请输入以下指令=======");
            System.out.println("====1.踢出指定成员=======");
            System.out.println("====2.赋予指定成员踢人权限====");
            System.out.println("====3.返回上一级====");
            String str;
            do {
                System.out.print("请输入：");
                str = s.nextLine();
            } while (!("1".equals(str)) && !("2".equals(str)) && !"3".equals(str));

            if("3".equals(str)){
                System.out.println();//换个行
                return;//实现返回上一级的操作
            }else if("2".equals(str)){
                //这里实现赋予指定人员踢人权限的操作
                KickOperate.kickOutPermission(user);
            }else{
                if(KickOperate.kickOut(user)){
                    System.out.println("成功踢出");
                    applyTribeChangePeoNum(user, Select.selectTribePeoNumAccu(user, user.getTribe()) - 1);//退出部落部落人数减1
                }
            }

            System.out.println();//换个行
        }

    }

    /**
     * 关系户的踢人界面
     */
    public static void relationshipKickView(User user){
            Scanner s = new Scanner(System.in);
            System.out.println("====请输入以下指令=======");
            System.out.println("====1.踢出指定成员=======");
            System.out.println("====2.返回上一级=========");
            String str;
            do {
                System.out.print("请输入：");
                str = s.nextLine();

            } while (!("1".equals(str)) && !("2".equals(str)));

            if("2".equals(str)){
                System.out.println();//换个行
            }else{
                applyTribeChangePeoNum(user, Select.selectTribePeoNumAccu(user, user.getTribe()) - 1);//退出部落部落人数减1
                System.out.println(KickOperate.kickOut(user) ? "成功踢出" : "踢出失败");
            }

    }

    /**
     * 处理邮件的界面
     * @param user 用户
     */
    public static void noticeView(User user) {
        while (true) {
            Scanner s = new Scanner(System.in);
            printMark = 0;//这里为0，遍历的时候不会打印信息
            NoticeOperate.noticeUnread(user, 0);
            NoticeOperate.noticeAlreadyRead(user, 1);

            System.out.println("====请输入以下指令=====");
            System.out.println("====1.查看未读邮件(" + Notice.noRendNum + ")=====");
            System.out.println("====2.查看已读邮件(" + Notice.HaveRendNum + ")=====");
            System.out.println("====3.删除已读邮件(" + Notice.HaveRendNum + ")=====");
            System.out.println("====4.返回上一级======");
            String str;
            do {
                System.out.print("请输入：");
                str = s.nextLine();

            } while (!("1".equals(str)) && !("2".equals(str)) && !"3".equals(str) && !"4".equals(str));

            printMark = 1;//当这里为1的时候就可以打印出来信息了
            if ("4".equals(str)) {
                System.out.println();//换个行
                return;//实现返回上一级的操作
            }
            //进入service层操作
            NoticeOperate.noticeOperate(user, str);

            System.out.println();//换个行
        }
    }

    /**
     * 进行删除的操作的界面
     * @param user 用户
     */
    public static void deleteView(User user){
        Scanner s = new Scanner(System.in);
        String str;
        System.out.println();

        System.out.println();
        Notice.noticeAlreadyReadTable(user, 1);//先打印邮件的信息
        System.out.println("====请输入以下指令===========");
        System.out.println("====1.删除全部邮件===========");
        System.out.println("====2.删除指定邮件(与指定邮件重复的信息将自动删除)====");
        System.out.println("====3.返回上一级=============");
        do {
            System.out.print("请输入：");
            str = s.nextLine();

            if ("3".equals(str)) {//如果是3的话就直接结束了
                System.out.println();//换个行
                return;
            }
        } while (!("1".equals(str)) && !("2".equals(str)));

        //进入service的层操作
        NoticeOperate.deleteNotice(user, str);
        System.out.println();

    }

}