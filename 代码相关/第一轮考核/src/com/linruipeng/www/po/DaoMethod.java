package com.linruipeng.www.po;

/**
 * 这个接口要给一些家伙去实现，但实际上我只实现了一个
 * 登录的我就不拿接口了，毕竟我已经写好了登录
 */
public interface DaoMethod {

    /**
     * 用来注册学生的信息
     * @param user 操作的对象，把这玩意的信息传到数据库去
     * @return 注册成功返回true，失败返回false
     */
    boolean register(User user);

}
