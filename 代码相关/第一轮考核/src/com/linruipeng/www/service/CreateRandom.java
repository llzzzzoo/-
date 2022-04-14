package com.linruipeng.www.service;

import java.util.Random;

/**
 * 这个类用于生成一个随机数
 */
public class CreateRandom {

    /**
     * 生成随机数，生成的随机数必须有范围限制，桀桀资本家太多了
     * @param num 输入的数，用作范围
     * @return 返回生成的随机数
     */
    public static int random(int num){
        Random random = new Random();
        int randomResult;

        if(num > 100){
            num /= 10;//如果是10的倍数，那么最大的金币数也就十分之一
        }else{
            return 5;//当金币数少于100的时候每天都会固定返回五个金币
        }

        /*这里在nextInt里面输入了一个数字，表示了一个范围，即[0,bound)，左闭右开哦*/
        randomResult = random.nextInt(num);

        return randomResult;
    }

}
