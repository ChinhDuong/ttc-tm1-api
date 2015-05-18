package com.ttcgroup.tm1.dao;

import com.sun.rowset.CachedRowSetImpl;

import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Administrator on 4/2/2015.
 */
public class FBDAOImpl implements IFBDAO {

    private String dbAddress = "";
    private String userName = "";
    private String password = "";

    /**
     * Init FBDAOImpl
     *
     * @param dbAddress
     * @param userName
     * @param password
     */
    public FBDAOImpl(String dbAddress, String userName, String password) {
        this.dbAddress = dbAddress;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Execute SQL Text not return
      * @param pSQLText
     * @return
     */

    public boolean ExecuteSQLText(String pSQLText) {
        Statement statement = null;
        Connection connection = null;
        boolean resultSet = false;

        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            connection = DriverManager.getConnection(
                    dbAddress, userName, password);
            statement = connection.createStatement();
            resultSet = statement.execute(pSQLText);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                connection.close();
                statement.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return resultSet;
    }

    /**
     * Execute SQL Text return data
     * @param pSQLText
     * @return CachedRowSetImpl
     */

    public CachedRowSetImpl ExecuteQueySQLText(String pSQLText) {
        Statement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;

        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            connection = DriverManager.getConnection(
                    dbAddress, userName, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(pSQLText);

            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return crs;
    }
}
