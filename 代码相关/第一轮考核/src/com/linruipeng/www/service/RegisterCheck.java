package com.linruipeng.www.service;

import com.linruipeng.www.util.MyException;

public class RegisterCheck {

        /**
         * 处理用户注册的操作
         * @param username 用户名
         * @throws MyException.IllegalNameException 当输入的用户名不在3~9之间，报错
         */
        public static void register(String username) throws MyException.IllegalNameException {
            //小于3个字符，大于9个字符，以及没有字符不行
            if(null == username || username.length() < 3 || username.length() > 15){
                throw new MyException.IllegalNameException("用户名不合法，长度只能在[3,15]之间！");
            }
        }
}
