package com.linruipeng.www.util;

public class MyException {
    /**
     * 注册的时候输入用户名的错误
     * 一个内部类，专门把一些自定义异常放里面(其实最多写一个
     */
    public static class IllegalNameException extends Exception{

        public IllegalNameException(String s){
            super(s);
        }
    }
}
