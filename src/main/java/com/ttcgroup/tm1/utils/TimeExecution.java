package com.ttcgroup.tm1.utils;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 4/14/2015.
 */
public class TimeExecution implements InvocationHandler {
    private final Object obj;
    private Logger logger = null;
    public TimeExecution(Object proxy)
    {
        this.obj = proxy;
        logger = LoggerFactory.getLogger(obj.getClass());
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        WriteLog _log = method.getAnnotation(WriteLog.class);
        try
        {
            result = method.invoke(obj, args);
        }
        catch (InvocationTargetException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        String param = "";
        if (args != null) {
            for (Object obj : args) {
                param = param + obj.toString() + ",";
            }
        }
        if (_log != null ) {
            logger.debug(method.getName() + " execution : " + duration + " ms. " + param);
        }
        return result;
    }
}
