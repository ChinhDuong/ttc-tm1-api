package com.ttcgroup.tm1.run;

import com.ttcgroup.tm1.bus.IImportBUS;
import com.ttcgroup.tm1.bus.ImportBUSImpl;
import com.ttcgroup.tm1.dao.ITM1DAO;
import com.ttcgroup.tm1.dao.TM1DAOImpl;
import com.ttcgroup.tm1.utils.ProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ImportUser {
    public static void main(String[] args) {
	// write your code here
        Properties prop = new Properties();
        String propFileName = "config.properties";
        Logger logger = LoggerFactory.getLogger(ImportUser.class);
        IImportBUS importBUS = null;
        importBUS = (IImportBUS)ProxyFactory.newInstance(new ImportBUSImpl());
        //IImportBUS importBUS = new ImportBUSImpl();

        try{
            InputStream inputStream = new FileInputStream(propFileName);
            prop.load(inputStream);
            ITM1DAO tm1DAO = null;
            //IFBDAO fbDAO = null;

            tm1DAO = (ITM1DAO) ProxyFactory.newInstance(new TM1DAOImpl(prop.getProperty("tm1_adminhost")
                    , prop.getProperty("tm1_serverName")
                    , prop.getProperty("tm1_user")
                    , prop.getProperty("tm1_password")));

            String fileUsers = prop.getProperty("tm1_userlist");
            String fileGroups = prop.getProperty("tm1_grouplist");

            importBUS.setTm1DAO(tm1DAO);

//            logger.info("Import groups into TM1");
//            importBUS.ImportGroups(fileGroups);

//            logger.info("Import users into TM1");
//            importBUS.ImportUsers(fileUsers);

            logger.info("Import set permission into TM1");
            importBUS.SetUserPermission(fileUsers);

        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());
        }

    }
}
