package com.ttcgroup.tm1.bus;

import com.ttcgroup.tm1.dao.IFBDAO;
import com.ttcgroup.tm1.dao.ITM1DAO;
import com.ttcgroup.tm1.utils.WriteLog;

/**
 * Created by Administrator on 4/15/2015.
 */
public interface IImportBUS {
    void setFbDAO(IFBDAO fbDAO);

    void setTm1DAO(ITM1DAO tm1DAO);

    void ClearData();

    @WriteLog
    void CopyFBData();
//    void CopyFBData(String folderPath, String batFile);

    @WriteLog
    void UpdateTableImport();

    void CallAll(String pYear);

    void ImportTM1();

    void ImportUsers(String fileName);

    void ImportGroups(String fileName);

    void SetUserPermission(String fileName);

    void ChangePassword(String adminUser, String adminPassword, String userName, String newPassword, String adminHost) throws Exception;
}
