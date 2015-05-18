package com.ttcgroup.tm1.dao;

import com.applix.tm1.TM1Server;
import com.sun.rowset.CachedRowSetImpl;
import com.ttcgroup.tm1.dto.GroupInfo;
import com.ttcgroup.tm1.dto.UserInfo;
import com.ttcgroup.tm1.utils.WriteLog;

import java.util.List;

/**
 * Created by Administrator on 4/14/2015.
 */
public interface ITM1DAO {
    @WriteLog
    void ImportIntoCube(CachedRowSetImpl crs, String cubeName) throws  Exception;

    @WriteLog
    void ImportIntoCube(CachedRowSetImpl crs, String cubeName,TM1Server serv) throws  Exception;

    void RunProcess(String procName, String[] params) throws Exception;

    TM1Server OpenConnection();

    @WriteLog
    void ImportUsers(List<UserInfo> users) throws Exception;

    @WriteLog
    void ImportGroups(List<GroupInfo> groups) throws Exception;

    @WriteLog
    void SetUserPermission(List<UserInfo> users) throws Exception;

    @WriteLog
    void ChangePassword(String userName, String password) throws Exception;
}
