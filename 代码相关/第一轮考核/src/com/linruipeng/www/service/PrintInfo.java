package com.linruipeng.www.service;

import com.linruipeng.www.dao.LoginVerify;
import com.linruipeng.www.dao.Select;
import com.linruipeng.www.po.User;

/**
 * 这里实现
 * 打印我的全部的信息
 * 打印查询到的人的信息，我在查询的方法里面顺便写了
 * 打印我的部落的信息
 * 打印我的阵营，并且同时打印我的阵营的全部部落
 */
public class PrintInfo {

    /**
     * 用于打印自己的全部信息
     */
    public static void printMyInfo(){
        System.out.println("用户名：" + LoginVerify.user.getUsername());
        System.out.println("性别：" + LoginVerify.user.getSex());
        System.out.println("阵营：" + LoginVerify.user.getGroup());
        //下面是打印没有部落的成员部落信息为无，而不是无1、无2
        if(!"无1".equals(LoginVerify.user.getTribe()) && !"无2".equals(LoginVerify.user.getTribe())) {
            System.out.println("部落：" + LoginVerify.user.getTribe());
        }else{
            System.out.println("部落：无");
        }
        System.out.println("金币：" + LoginVerify.user.getMoney());
        System.out.println("点赞：" + LoginVerify.user.getLikes());
        System.out.println();//换个行
    }


    /**
     * 打印我的阵营的所有部落的信息
     * 搞个排序吧
     * @param user 哪个用户查看，就调用他的getGroup方法
     */
    public static void printMyGroupInfo(User user){
        Select.selectGroupAccu(user.getGroup());//打印全部部落
    }

}
