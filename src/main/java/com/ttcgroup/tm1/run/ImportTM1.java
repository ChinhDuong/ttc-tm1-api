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

public class ImportTM1 {
    public static void main(String[] args) {
	// write your code here
        Properties prop = new Properties();
        String propFileName = "config.properties";
        Logger logger = LoggerFactory.getLogger(ImportTM1.class);
        IImportBUS importBUS = null;
        importBUS = (IImportBUS)ProxyFactory.newInstance(new ImportBUSImpl());
        //IImportBUS importBUS = new ImportBUSImpl();

        try{
            InputStream inputStream = new FileInputStream(propFileName);
            prop.load(inputStream);
            ITM1DAO tm1DAO = null;
            IFBDAO fbDAO = null;
//            String fbCopy_folder = prop.getProperty("fbCopy_folder");
//            String fbCopy_bat = prop.getProperty("fbCopy_bat");

            tm1DAO = (ITM1DAO) ProxyFactory.newInstance(new TM1DAOImpl(prop.getProperty("tm1_adminhost")
                    , prop.getProperty("tm1_serverName")
                    , prop.getProperty("tm1_user")
                    , prop.getProperty("tm1_password")));
//            tm1DAO = new TM1DAOImpl(prop.getProperty("tm1_adminhost")
//                    , prop.getProperty("tm1_serverName")
//                    , prop.getProperty("tm1_user")
//                    , prop.getProperty("tm1_password"));

            fbDAO = (IFBDAO) ProxyFactory.newInstance(new FBDAOImpl(prop.getProperty("fb_address")
                    ,prop.getProperty("fb_user")
                    ,prop.getProperty("fb_password")));
//            fbDAO = new FBDAOImpl(prop.getProperty("fb_address")
//                    ,prop.getProperty("fb_user")
//                    ,prop.getProperty("fb_password"));

            importBUS.setFbDAO(fbDAO);
            importBUS.setTm1DAO(tm1DAO);

            logger.info("Clear all data");
            //importBUS.ClearData();
            logger.info("Copy data from SSP to temp");
            //importBUS.CopyFBData();
            logger.info("Prepare data for import into TM1");
            //importBUS.CallAll("2015");
            logger.info("Import into TM1");
            importBUS.ImportTM1();
//            String[] params = {"2015","KHKD đường mật"};
//
//            tm1DAO.RunProcess("ClearActualData",params);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());
        }

    }
}
