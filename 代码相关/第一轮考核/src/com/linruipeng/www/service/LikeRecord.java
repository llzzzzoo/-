package com.linruipeng.www.service;

import com.linruipeng.www.dao.Like;
import com.linruipeng.www.po.User;

/**
 * 这个类用来检测是否已经点赞过了
 */
public class LikeRecord {

    /**
     * 这个就是用来操作是否点过赞的行为
     * 每次点赞前都调用一下这玩意
     * @param user 用户
     * @param username 指定点赞的用户名
     * @return 当我在数据库找到了这两个东西对应的记录的时候，就返回true，表明点过了赞
     */
    public static boolean likeRecord(User user, String username){

        return Like.likeRecord(user, username);//的确存在那么就返回true
    }
}
