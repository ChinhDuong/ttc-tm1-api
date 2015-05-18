package com.ttcgroup.tm1.run;

import com.ttcgroup.tm1.bus.IImportBUS;
import com.ttcgroup.tm1.bus.ImportBUSImpl;
import com.ttcgroup.tm1.dao.FBDAOImpl;
import com.ttcgroup.tm1.dao.IFBDAO;
import com.ttcgroup.tm1.utils.ProxyFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 4/15/2015.
 */
public class UpdateTableImport {
    public static void main(String[] args) {
        // write your code here
        System.out.println("Update table import: ");
        Properties prop = new Properties();
        String propFileName = "config.properties";
        IFBDAO fbDAO = null;
        IImportBUS importBUS = new ImportBUSImpl();

        try {
            InputStream inputStream = new FileInputStream(propFileName);
            prop.load(inputStream);

            fbDAO = (IFBDAO) ProxyFactory.newInstance(new FBDAOImpl(prop.getProperty("fb_address")
                    , prop.getProperty("fb_user")
                    , prop.getProperty("fb_password")));
            importBUS.setFbDAO(fbDAO);
            importBUS.UpdateTableImport();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
