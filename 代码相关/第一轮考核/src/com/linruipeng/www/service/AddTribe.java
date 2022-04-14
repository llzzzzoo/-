package com.linruipeng.www.service;

import com.linruipeng.www.dao.Update;
import com.linruipeng.www.po.User;

/**
 * 这个类实现加入部落的操作
 * 由部落首领来调用的
 */
public class AddTribe {

    public static boolean addTribe(User user, String applicant){

        return Update.addMyTribe(user, applicant);//如果操作成功返回true
    }

}
