package com.ttcgroup.tm1.dao;

import com.sun.rowset.CachedRowSetImpl;
import com.ttcgroup.tm1.utils.WriteLog;

/**
 * Created by Administrator on 4/14/2015.
 */
public interface IFBDAO {
    @WriteLog
    boolean ExecuteSQLText(String pSQLText);
    @WriteLog
    CachedRowSetImpl ExecuteQueySQLText(String pSQLText);
}
