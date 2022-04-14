package com.linruipeng.www.service;

import com.linruipeng.www.dao.Apply;
import com.linruipeng.www.dao.Notice;
import com.linruipeng.www.po.User;
import com.linruipeng.www.view.BranchView;
import java.util.Scanner;

import static com.linruipeng.www.view.BranchView.notHandleNum;

/**
 * 处理申请部落相关的操作
 */
public class ApplyOperate {

    public static void applyOperate(User user, String str){
        BranchView.printApplicantMark = 1;
        String[] applicant = Apply.returnApplicant(user);
        int no;
        Scanner s = new Scanner(System.in);
        if(0 == notHandleNum){
            System.out.println("当前没有申请哦\n");
            return;//没有的话就返回上一级
        }

        switch (str) {
            case "1":
                System.out.println();//换个行
                //下面就是返回一个数组，用作删除的序号

                System.out.println("请输入你接受申请的序号");
                do {
                    no = s.nextInt();
                    if(no >= 1 && no < notHandleNum + 1)
                    {
                        break;
                    }
                    System.out.println("请输入正确的序号");
                }while(true);

                //下面就进入dao层删除操作了
                //这里需要的参数是申请人，部落名，部落首领名字
                System.out.println(AddTribe.addTribe(user, applicant[no - 1]) ? "成功接受申请" : "接受申请失败");//这一行就是实现加入用户的操作，找到用户，然后赋值给他，我的部落名就算加入了

                //记得改status
                Apply.changeStatus(user, applicant[no - 1], 1);//1表示接受了，本来我想着要不要删除了处理的，但我觉得记录的话还是保留吧

                //下面就是发条消息告诉那家伙恭喜了
                Notice.createNoticeRecord(user, "加入部落成功通知", "恭喜您来到了“" + user.getTribe() + "”这个大家庭", applicant[no - 1]);

                break;
            case "2":
                System.out.println();//换个行
                //下面就是返回一个数组，用作删除的序号

                System.out.println("请输入你拒绝申请的序号");
                do {
                    no = s.nextInt();
                    if(no >= 1 && no < notHandleNum + 1)
                    {
                        break;
                    }
                    System.out.println("请输入正确的序号");
                }while(true);

                //既然不加部落的话，那么就只搞个通知，改个mark咯
                //改个status
                Apply.changeStatus(user, applicant[no - 1], 2);//2表示不接受
                //发条消息
                Notice.createNoticeRecord(user, "拒绝加入部落通知", "很抱歉您不符合加入本部落的需求，祝安好", applicant[no - 1]);
                break;
        }
                 System.out.println();//换个行
    }

}
