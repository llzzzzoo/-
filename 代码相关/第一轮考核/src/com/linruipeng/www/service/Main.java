package com.linruipeng.www.service;

import com.linruipeng.www.util.MyException;
import com.linruipeng.www.view.MainView;

/**
 * Hello World!
 */
public class Main {
    public static void main(String[] args) {
        //此处捕获异常
        try {
            MainView.initUI();
        } catch (MyException.IllegalNameException e) {
            e.printStackTrace();
        }
    }
}
