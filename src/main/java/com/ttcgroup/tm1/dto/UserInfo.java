package com.ttcgroup.tm1.dto;

/**
 * Created by Administrator on 5/7/2015.
 */
public class UserInfo {
    private String userName;
    private String password;
    private String[] assignedGroup;

    public UserInfo(String[] assignedGroup, String userName, String password) {
        this.assignedGroup = assignedGroup;
        this.password = password;
        this.userName = userName;
    }

    public UserInfo(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String[] getAssignedGroup() {
        return assignedGroup;
    }

    public void setAssignedGroup(String[] assignedGroup) {
        this.assignedGroup = assignedGroup;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
