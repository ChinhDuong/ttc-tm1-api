package com.ttcgroup.tm1.run;

import com.ttcgroup.tm1.bus.IImportBUS;
import com.ttcgroup.tm1.bus.ImportBUSImpl;
import com.ttcgroup.tm1.utils.ProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ChangePassword {
    public static void main(String[] args) {
	// write your code here
        Properties prop = new Properties();
        String propFileName = "config.properties";
        Logger logger = LoggerFactory.getLogger(ChangePassword.class);
        IImportBUS importBUS = null;
        importBUS = (IImportBUS)ProxyFactory.newInstance(new ImportBUSImpl());
        //IImportBUS importBUS = new ImportBUSImpl();

        try{
            InputStream inputStream = new FileInputStream(propFileName);
            prop.load(inputStream);

            logger.info("Changing password");
            importBUS.ChangePassword(prop.getProperty("tm1_user"),prop.getProperty("tm1_password"),"dungnc", "456","10.33.1.27");

        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());
        }

    }
}
