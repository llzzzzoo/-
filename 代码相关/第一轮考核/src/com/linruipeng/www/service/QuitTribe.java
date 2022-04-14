package com.linruipeng.www.service;

import com.linruipeng.www.dao.Update;
import com.linruipeng.www.po.User;

/**
 * 这里是退出部落的相关操作
 */
public class QuitTribe {

    public static boolean quitTribe(User user){

        return Update.quitMyTribe(user);//退出成功返回true
    }
}
