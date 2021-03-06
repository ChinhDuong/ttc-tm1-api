package com.ttcgroup.tm1.run;

import com.ttcgroup.tm1.bus.IImportBUS;
import com.ttcgroup.tm1.bus.ImportBUSImpl;
import com.ttcgroup.tm1.dao.FBDAOImpl;
import com.ttcgroup.tm1.dao.IFBDAO;
import com.ttcgroup.tm1.dao.ITM1DAO;
import com.ttcgroup.tm1.dao.TM1DAOImpl;
import com.ttcgroup.tm1.utils.ProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 7/8/2015.
 */
public class ExportTM1Model {
    public static void main(String[] args) {
        Properties prop = new Properties();
        String propFileName = "config.properties";
        Logger logger = LoggerFactory.getLogger(ExportTM1Model.class);
        IImportBUS importBUS = null;
        importBUS = (IImportBUS) ProxyFactory.newInstance(new ImportBUSImpl());
        //IImportBUS importBUS = new ImportBUSImpl();

        try {
            InputStream inputStream = new FileInputStream(propFileName);
            prop.load(inputStream);
            ITM1DAO tm1DAO = null;
            IFBDAO fbDAO = null;

            tm1DAO = (ITM1DAO) ProxyFactory.newInstance(new TM1DAOImpl(prop.getProperty("tm1_adminhost")
                    , prop.getProperty("tm1_serverName")
                    , prop.getProperty("tm1_user")
                    , prop.getProperty("tm1_password")));


            fbDAO = (IFBDAO) ProxyFactory.newInstance(new FBDAOImpl(prop.getProperty("fb_address")
                    , prop.getProperty("fb_user")
                    , prop.getProperty("fb_password")));


            importBUS.setFbDAO(fbDAO);
            importBUS.setTm1DAO(tm1DAO);

            logger.info("Export tm1 model");
            importBUS.ExportTM1Model2xls();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}
