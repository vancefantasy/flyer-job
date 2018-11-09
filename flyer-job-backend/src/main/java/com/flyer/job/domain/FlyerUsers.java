package com.flyer.job.domain;

import java.io.Serializable;

public class FlyerUsers implements Serializable {

    private static final long serialVersionUID = -5607655508643673594L;

    /**
     * 自增id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户密码
     */
    private String userPwd;
    /**
     * 创建时间
     */
    private java.util.Date addTime;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setAddTime(java.util.Date addTime) {
        this.addTime = addTime;
    }

    public java.util.Date getAddTime() {
        return addTime;
    }

}
