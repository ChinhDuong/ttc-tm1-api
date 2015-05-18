package com.ttcgroup.tm1.utils;

import com.ttcgroup.tm1.bus.IImportBUS;
import com.ttcgroup.tm1.dao.IFBDAO;
import com.ttcgroup.tm1.dao.ITM1DAO;

import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 4/14/2015.
 */
public class ProxyFactory {
    public static Object newInstance(Object ob){
        return Proxy.newProxyInstance(ob.getClass().getClassLoader(),
                new Class<?>[]{IFBDAO.class,ITM1DAO.class, IImportBUS.class}, new TimeExecution(ob));
    }
}
