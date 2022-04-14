package com.linruipeng.www.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 打印当前的标准日期
 */
public class NowDate {

    public static String nowDate(){
        Date nowTime = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(nowTime);//返回当前日期的字符串

    }
}
