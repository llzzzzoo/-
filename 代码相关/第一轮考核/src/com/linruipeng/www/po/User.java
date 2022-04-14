package com.linruipeng.www.po;

import java.util.Objects;

public class User {
    /**
     * 用户类
     */
    private int id;//用户id
    private String username;//用户名
    private String password;//密码
    private String sex;//true表示男性，false表示女性
    private String group;//阵营，上面的sex和这行本来算boolean的，但怕影响到数据库
    private String tribe;//部落
    private int money = 0;//钱钱
    private int likes = 0;//点赞数
    private long dateTime = 0;//每日签到的时间，以秒为单位
    private long dateTribeTime = 0;//部落签到的时间，以秒为单位
    private long quitTribeTime = 0;//部落签到的时间，以秒为单位
    private int mark = 2;//区分部落长的操作，注意1为部落长，2为普通成员和没加入部落的人，3为有踢人权限的普通成员

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(int id, String username, String password, String sex, String group, String tribe, int money, int likes, long dateTime, long dateTribeTime, long quitTribeTime, int mark) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.group = group;
        this.tribe = tribe;
        this.money = money;
        this.likes = likes;
        this.dateTime = dateTime;
        this.dateTribeTime = dateTribeTime;
        this.quitTribeTime = quitTribeTime;
        this.mark = mark;
    }

    //修改密码时候的账号和前后密码比较
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    /**
     * @return 这里没有return那个时间嗷
     */
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", group='" + group + '\'' +
                ", tribe='" + tribe + '\'' +
                ", money=" + money +
                ", likes=" + likes +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTribe() {
        return tribe;
    }

    public void setTribe(String tribe) {
        this.tribe = tribe;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public long getDateTribeTime() {
        return dateTribeTime;
    }

    public void setDateTribeTime(long dateTribeTime) {
        this.dateTribeTime = dateTribeTime;
    }

    public long getQuitTribeTime() {
        return quitTribeTime;
    }

    public void setQuitTribeTime(long quitTribeTime) {
        this.quitTribeTime = quitTribeTime;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
