package com.ttcgroup.tm1.dao;

import com.applix.tm1.*;
import com.sun.rowset.CachedRowSetImpl;
import com.ttcgroup.tm1.dto.GroupInfo;
import com.ttcgroup.tm1.dto.UserInfo;

import java.util.List;

/**
 * Created by Administrator on 4/2/2015.
 */
public class TM1DAOImpl implements ITM1DAO {
    private String adminHost;
    private String serverName;
    private String userName;
    private String password;

    public TM1DAOImpl(String adminHost, String serverName, String userName, String password) {
        this.adminHost = adminHost;
        this.serverName = serverName;
        this.userName = userName;
        this.password = password;

    }

    /**
     * Import into cube
     *
     * @param crs      : Data
     * @param cubeName : Cube name
     */
    public void ImportIntoCube(CachedRowSetImpl crs, String cubeName) throws Exception {

        TM1Bean tm1_bean = new TM1Bean();
        tm1_bean.setAdminHost(adminHost);
        TM1Server serv = tm1_bean.openConnection(serverName, userName, password);
        TM1Cube cube = serv.getCube(cubeName);

        if (cube.isError())
            throw new Exception("Cube is error.{Cube: " + cubeName);

        while (crs.next()) {// Scan input data

            // input data
            TM1Val elemTitles;
            // get number of dimension
            elemTitles = TM1Val.makeArrayVal(cube.getDimensionCount().getInt());
            boolean isText = false;
            //String condition = "";
            for (int iCTR = 1; iCTR <= cube.getDimensionCount().getInt(); iCTR++) {
                String dimName = cube.getDimension(iCTR).getName().getString();
                String eleName = crs.getString(dimName);
                TM1Element element = cube.getDimension(iCTR).getElement(eleName);

                if (element.isError()) // element error
                    throw new Exception("Element is error.{Dim: " + dimName + ", Ele: " + eleName + "}");

                if (element.getElementType() == TM1ObjectType.ElementString)
                    isText = true;
                elemTitles.addToArray(element);
            }


            TM1Val ret;//= TM1Val.TM1Val_NULL;

            if (isText)
                ret = cube.setCellValue(elemTitles, new TM1Val(crs.getString("TEXT")));
            else
                ret = cube.setCellValue(elemTitles, new TM1Val(crs.getDouble("GIATRI")));

            if (ret.isError())
                throw new Exception("SetCelValue is error \n" + elemTitles.toString()
                        + "-" + crs.getDouble("GIATRI") + ".\n Message: " + ret.toString());
        }

        serv.disconnect();
    }

    public void ImportIntoCube(CachedRowSetImpl crs, String cubeName, TM1Server serv) throws Exception {

        //TM1Bean tm1_bean = new TM1Bean();
        //tm1_bean.setAdminHost(adminHost);
        //TM1Server serv = tm1_bean.openConnection(serverName, userName, password);
        TM1Cube cube = serv.getCube(cubeName);
        if (cube.isError())
            throw new Exception("Cube is error.{Cube: " + cubeName);
        while (crs.next()) {// Scan input data
            TM1Val elemTitles;
            // get number of dimension
            elemTitles = TM1Val.makeArrayVal(cube.getDimensionCount().getInt());
            boolean isText = false;

            for (int iCTR = 1; iCTR <= cube.getDimensionCount().getInt(); iCTR++) {
                String dimName = cube.getDimension(iCTR).getName().getString();
                String eleName = crs.getString(dimName);
                TM1Element element = cube.getDimension(iCTR).getElement(eleName);

                if (element.isError()) // element error
                    throw new Exception("Element is error.{Dim: " + dimName + ", Ele: " + eleName + "}");

                if (element.getElementType() == TM1ObjectType.ElementString)
                    isText = true;
                elemTitles.addToArray(element);
            }
            TM1Val ret;//= TM1Val.TM1Val_NULL;

            if (isText)
                ret = cube.setCellValue(elemTitles, new TM1Val(crs.getString("TEXT")));
            else
                ret = cube.setCellValue(elemTitles, new TM1Val(crs.getDouble("GIATRI")));

            if (ret.isError())
                throw new Exception("SetCelValue is error \n" + elemTitles.toString()
                        + "-" + crs.getDouble("GIATRI") + ".\n Message: " + ret.toString());
        }
    }

    /**
     * Run process
     *
     * @param procName: process Name
     */
    public void RunProcess(String procName, String[] params) throws Exception {
        TM1Bean tm1_bean = new TM1Bean();
        tm1_bean.setAdminHost(adminHost);
        TM1Server serv = tm1_bean.openConnection(serverName, userName, password);
        TM1Process tm1_proc = serv.getProcess(procName);
        if (tm1_proc.isError())
            throw new Exception("Process '" + procName + "' is error " + tm1_proc.getErrorMessage());

        TM1Val arr = TM1Val.makeArrayVal(params.length);

        for (String p : params) {
            arr.addToArray(new TM1Val(p));
        }

        tm1_proc.check();
        TM1Val ret = tm1_proc.executeEx(arr);

        if (ret.isError())
            throw new Exception("Executing process '" + procName + "' is error " + ret.getErrorMessage());
    }

    public TM1Server OpenConnection() {
        TM1Bean tm1_bean = new TM1Bean();
        tm1_bean.setAdminHost(adminHost);
        return tm1_bean.openConnection(serverName, userName, password);
        //return serv;
    }

    public void ImportUsers(List<UserInfo> users) throws Exception {
        TM1Bean tm1_bean = new TM1Bean();
        tm1_bean.setAdminHost(adminHost);
        TM1Server serv = tm1_bean.openConnection(serverName, userName, password);

        for (UserInfo user : users) {
            TM1Val val = serv.addClient(user.getUserName());
            if (val.isError()) {
                throw new Exception("Adding user is error." + val.getErrorMessage());
            }

            TM1Client client = serv.getClient(user.getUserName());
            client.assignPassword(user.getPassword());
            if (val.isError()) {
                throw new Exception("Asigning password is error." + val.getErrorMessage());
            }
            TM1Group group = serv.getGroup("}tp_Everyone");
            if (group.isError()) {
                throw new Exception(" }tp_Everyone is error." + group.getErrorMessage());
            }
            val = client.assignToGroup(group);
            if (val.isError()) {
                throw new Exception("Asigning user to }tp_Everyone is error." + val.getErrorMessage());
            }
        }
    }

    public void ImportGroups(List<GroupInfo> groups) throws Exception {
        TM1Bean tm1_bean = new TM1Bean();
        tm1_bean.setAdminHost(adminHost);
        TM1Server serv = tm1_bean.openConnection(serverName, userName, password);

        for (GroupInfo group : groups) {
            TM1Val val = serv.addGroup(group.getGroupName());
            if (val.isError())
                throw new Exception("Adding group is error." + val.getErrorMessage());
        }
    }

    public void SetUserPermission(List<UserInfo> users) throws Exception {
        TM1Bean tm1_bean = new TM1Bean();
        tm1_bean.setAdminHost(adminHost);
        TM1Server serv = tm1_bean.openConnection(serverName, userName, password);

        for (UserInfo user : users) {
            TM1Client client = serv.getClient(user.getUserName());
            if (client.isError())
                throw new Exception("Client is error. {Client "
                        + user.getUserName() + "." + client.getErrorMessage());

            for (String groupName : user.getAssignedGroup()) {
                TM1Group group = serv.getGroup(groupName);
                if (!group.isError()) {
                    TM1Val val = client.assignToGroup(group);
                    if (val.isError())
                        throw new Exception("Assign to group is error." + val.getErrorMessage());
                } else
                    throw new Exception("Group is error. {Group "
                            + groupName + "." + group.getErrorMessage());
            }
        }
    }

    public void ChangePassword(String userName, String password) throws Exception {
        TM1Bean tm1_bean = new TM1Bean();
        tm1_bean.setAdminHost(adminHost);
        TM1Server serv = tm1_bean.openConnection(serverName, userName, password);
        TM1Client client = serv.getClient(userName);
        if (client.isError())
            throw new Exception("Client is error. {Client "
                    + userName + "." + client.getErrorMessage());
        TM1Val result = client.assignPassword(password);
        if (result.isError())
            throw new Exception("Changing password is error. {Client "
                    + userName + "." + result.getErrorMessage());

    }
}
