package com.linruipeng.www.service;

import com.linruipeng.www.dao.Notice;
import com.linruipeng.www.dao.Select;
import com.linruipeng.www.dao.Sign;
import com.linruipeng.www.dao.Update;
import com.linruipeng.www.po.User;

/**
 * 用于判断签到时间是否超过了一天的类
 * 以及退出部落是否超过一天的类
 */
public class SignTimeOperate {
    public static int signMark = 0;//这个就是为了实现参数为user，但是发送人却是系统
    public static int judgeMyTimeMark = 0;//用于是否把当前时间存入属性的判断
    public static int judgeTribeTimeMark = 0;//用于是否把当前时间存入属性的判断
    /**
     * 我现在想的就是这里用来判断是否超过一天了
     * 成功了的话把当前秒数赋值给user的date
     * @return 当返回true的时候表明过了一天了
     */
    public static boolean signMyTimeJudge(User user, long nowTimeSeconds){
        long gapTimeSeconds = nowTimeSeconds - user.getDateTime();//这里的差值应该是秒数

        if(gapTimeSeconds > 60 * 60 * 24){
            //说明过了一天了
            if(1 == judgeMyTimeMark) {//这里是保证不会把当前赋给属性免得签到时候出错
                user.setDateTime(nowTimeSeconds);
            }else {
                Notice.createNoticeRecord(user, "每日签到提醒", "您好，今天您每日签到了吗^_^", user.getUsername());
            }
            return true;
         }

        return false;
    }

    /**
     * 这个是用来判断部落签到的玩意
     * @param user 获取用户上次签到的时间
     * @param nowTimeSeconds 当前签到的时间
     * @return 签到成功返回true咯
     */
    public static boolean signTribeTimeJudge(User user, long nowTimeSeconds){
        long gapTimeSeconds = nowTimeSeconds - user.getDateTribeTime();//这里的差值应该是秒数

        if(gapTimeSeconds > 60 * 60 * 24){
            //说明过了一天了
            if(1 == judgeTribeTimeMark) {//这里是保证不会把当前赋给属性免得签到时候出错
                user.setDateTribeTime(nowTimeSeconds);
            }else {
                //不知道为什么我要写个这玩意,估计就为了排除没部落的家伙吧，其实也不用写，反正没部落也搞不了这个功能
                if (!"无1".equals(user.getTribe()) && !"无2".equals(user.getTribe())) {
                    Notice.createNoticeRecord(user, "部落签到通知", "您好，今天您部落签到了吗^_^", user.getUsername());
                    //这个操作是当用户三天没部落签到就告诉老大，然后老大决定要不要踢出
                    if (gapTimeSeconds > 7 * 60 * 60 * 24 && !user.getUsername().equals(Select.selectMyTribeBoss(user))) {
                        signMark = 1;
                        if (1 != user.getMark()) { //这里是实现解散部落部落长不会被通知
                            Notice.createNoticeRecord(user, "成员活跃度通知", "“" + user.getUsername() + "”该成员七天未签到，活跃度较低", Select.selectMyTribeBoss(user));
                        }
                    }
                }
            }
            return true;
        }

        return false;
    }

    /**
     * 这里是返回当前的秒数
     */
    public static long nowTime(){

        return System.currentTimeMillis() / 1000;//这个是现在的秒数，md这里最开始/100搞得我数据库都是错的
    }

    /**
     * 这里是用来判断退出部落和申请加入部落时间是否超过一天
     * @param user 这个是得到用户上次突出部落的时间
     * @param nowTimeSeconds 这个是现在的时间，也就是申请加入部落的时间
     * @return 返回true表示超过了一天
     */
    public static boolean signQuitTribeTime(User user, long nowTimeSeconds){
        long gapTimeSeconds = nowTimeSeconds - user.getQuitTribeTime();//这里的差值应该是秒数

        if(gapTimeSeconds > 60 * 60 * 24){
            //说明过了一天了
            user.setDateTribeTime(nowTimeSeconds);
            signMark = 1;
            Notice.createNoticeRecord(user, "退出部落通知", "“" + user.getUsername() + "”退出了部落", Select.selectMyTribeBoss(user));
            return true;
        }

        return false;//还没过一天
    }


    /**
     * 这个方法其实也没啥用，就是为了处理一下已经签了到的操作
     * 还得提醒一下获得了多少金币
     * 记得加一个返回上一级的操作
     */
    public static void signDayOperate(User user) {
        judgeMyTimeMark = 1;//这里是使得能把当前时间赋给属性，注意得放在第一行哦
        boolean result = SignTimeOperate.signMyTimeJudge(user, SignTimeOperate.nowTime());//检测一下签了到吗，如果签过了的话，那么就不让他签了提示一下
        judgeMyTimeMark = 0;//用完了还是得归回原位，方便重复操作
        long gapTimeSeconds = SignTimeOperate.nowTime() - user.getDateTime();

        if (result) {
            Update.updateMyTime(user);
            System.out.println("签到成功获得" + Sign.signDay(user) + "枚金币\n");
        } else {
            System.out.println("签到失败，请" + (23 - gapTimeSeconds / 3600) + "时" + (59 - gapTimeSeconds % 3600 / 60) + "分" + (59 - gapTimeSeconds % 3600 % 60) + "秒" + "后再尝试！\n");//这里的话注意是23和59来当被减数
        }
    }

    /**
     * 这个方法其实也没啥用，就是为了处理一下已经签了到的操作
     * 还得提醒一下获得了多少金币
     * 记得加一个返回上一级的操作
     */
    public static void signTribeOperate(User user){
        judgeTribeTimeMark = 1;//这里是使得能把当前时间赋给属性，要放在第一行哦
        boolean result = SignTimeOperate.signTribeTimeJudge(user, SignTimeOperate.nowTime());//检测一下签了到吗，如果签过了的话，那么就不让他签了提示一下
        judgeTribeTimeMark = 0;//用完了还是得归回原位，方便重复操作

        long gapTimeSeconds = SignTimeOperate.nowTime() - user.getDateTribeTime();

        if (result) {
            Sign.signTribe(user);//如果签到成功，提示一下，部落获得了多少金币、多少战力
            Update.updateTribeTime(user, user.getDateTribeTime());
            System.out.println("签到成功部落获得" + Sign.moneyAdd + "枚金币和" + Sign.powerAdd + "点战力");
        } else {
            System.out.println("签到失败，请" + (23 - gapTimeSeconds / 3600) + "时" + (59 - gapTimeSeconds % 3600 / 60) + "分" + (59 - gapTimeSeconds % 3600 % 60) + "秒" + "后再尝试！\n");//这里的话注意是23和59来当被减数
        }

    }

    /**
     * 这个方法就是为了判断是否能创建部落
     * @return 返回true表明可以申请部落
     */
    public static boolean signQuitTribeView(User user){
        boolean result = SignTimeOperate.signQuitTribeTime(user, SignTimeOperate.nowTime());//检测一下签了到吗，如果签过了的话，那么就不让他签了提示一下
        long gapTimeSeconds = SignTimeOperate.nowTime() - user.getQuitTribeTime();//得到还有多久能申请部落的操作

        if (result) {
            return true;//这里表明可以申请部落
        } else {
            System.out.println("申请部落失败，请" + (23 - gapTimeSeconds / 3600) + "时" + (59 - gapTimeSeconds % 3600 / 60) + "分" + (59 - gapTimeSeconds % 3600 % 60) + "秒" + "后再尝试！\n");//这里的话注意是23和59来当被减数
        }
        return false;//执行到这里说明不能申请部落
    }

}
