package com.ttcgroup.tm1.bus;

import com.applix.tm1.TM1Bean;
import com.applix.tm1.TM1Client;
import com.applix.tm1.TM1Server;
import com.applix.tm1.TM1Val;
import com.sun.rowset.CachedRowSetImpl;
import com.ttcgroup.tm1.dao.IFBDAO;
import com.ttcgroup.tm1.dao.ITM1DAO;
import com.ttcgroup.tm1.dto.GroupInfo;
import com.ttcgroup.tm1.dto.UserInfo;
import com.ttcgroup.tm1.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 4/15/2015.
 */
public class ImportBUSImpl implements IImportBUS {
    private IFBDAO fbDAO;
    private ITM1DAO tm1DAO;
    private Logger logger = LoggerFactory.getLogger(ImportBUSImpl.class);

    public void setFbDAO(IFBDAO fbDAO) {
        this.fbDAO = fbDAO;
    }

    public void setTm1DAO(ITM1DAO tm1DAO) {
        this.tm1DAO = tm1DAO;
    }

    public void ClearData() {
        fbDAO.ExecuteSQLText("EXECUTE PROCEDURE TM1_CLEARTM1;");
    }


    public void CopyFBData() {
        Helper.ExecuteCMD("exe", "run_fbcopy.bat");
    }


    public void UpdateTableImport() {
        try {
            String fileName = "C:/DATA/exe/tm1copy.def";
            List<String> lines;
            lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
            Iterator<String> iter = lines.iterator();
            String pattern = "((?<=#T:)[A-_Z]*)";
            Pattern parse = Pattern.compile(pattern);
            List<String> tblNames = new ArrayList<String>();

            //extract table names
            for (String text : lines) {
                Matcher matcher = parse.matcher(text);
                if (matcher.find()) {
                    tblNames.add(matcher.group(0));
                }
            }
            String sqlText = "DELETE FROM TM1_TABLEIMPORT WHERE CUBENAME='Z'";
            fbDAO.ExecuteSQLText(sqlText);

            sqlText = "INSERT INTO TM1_TABLEIMPORT(TABLENAME,CUBENAME,PROCESSNAME)";
            sqlText = sqlText + "VALUES('?','Z','');";
            String execute = "";
            for (String text2 : tblNames) {
                execute = execute + sqlText.replace("?", text2);
                //fbDAO.ExecuteSQLText(sqlText.replace("?",text2));
            }
            //System.out.println("EXECUTE BLOCK AS BEGIN " + execute + "END");
            fbDAO.ExecuteSQLText("EXECUTE BLOCK AS BEGIN " + execute + "END");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void CallAll(String pYear) {
        fbDAO.ExecuteSQLText("EXECUTE PROCEDURE TM1_CALALL('" + pYear + "')");
    }


    public void ImportTM1(String pYear) {
        String sqlText = "SELECT TABLENAME, CUBENAME, CLEARCONDITION FROM TM1_TABLEIMPORT WHERE CUBENAME != 'Z'";
        CachedRowSetImpl tblImport = fbDAO.ExecuteQueySQLText(sqlText);
        try {
            while (tblImport.next()) {
                sqlText = "SELECT * FROM " + tblImport.getString("TABLENAME");
                String cubeName = tblImport.getString("CUBENAME");

                CachedRowSetImpl data = fbDAO.ExecuteQueySQLText(sqlText);

                String condition = tblImport.getString("CLEARCONDITION");
                if (!condition.isEmpty()) {
                    condition = condition + " & NAM: " + pYear;
                }
//                ResultSetMetaData meta = data.getMetaData();
//                int numberOfCol = meta.getColumnCount();
//                for (int i = 1; i <= numberOfCol; ++i) {
//                    if (meta.getColumnName(i).equalsIgnoreCase("DM_PHIENBAN")) {
//                        condition = condition + " & DM_PhienBan: ThucHien";
//                        break;
//                    }
//                }

                String[] params = {condition, cubeName};
                tm1DAO.RunProcess("ClearActualData", params);

                tm1DAO.ImportIntoCube(data, cubeName);
            }
            //serv.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ImportUsers(String fileName) {
        String COMMA_DELIMITER = ",";

        int USER_NAME = 0;
        int PASSWORD = 1;

        BufferedReader fileReader = null;
        try {

            //Create a new list of student to be filled by CSV file data
            List users = new ArrayList();

            String line;//= "";

            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileName));

            //Read the CSV file header to skip it
            fileReader.readLine();

            //Read the file line by line starting from the second line
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(COMMA_DELIMITER);


                if (tokens.length > 0) {
                    //Create a new student object and fill his  data
                    UserInfo user = new UserInfo(tokens[USER_NAME], tokens[PASSWORD]);
                    users.add(user);
                }
            }

            tm1DAO.ImportUsers(users);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void ImportGroups(String fileName) {
        String COMMA_DELIMITER = ",";
        int GROUP_NAME = 0;
        BufferedReader fileReader = null;
        try {

            //Create a new list of student to be filled by CSV file data
            List groups = new ArrayList();

            String line = "";

            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileName));

            //Read the CSV file header to skip it
            fileReader.readLine();

            //Read the file line by line starting from the second line
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(COMMA_DELIMITER);
                if (tokens.length > 0) {
                    //Create a new student object and fill his  data
                    GroupInfo group = new GroupInfo(tokens[GROUP_NAME]);
                    groups.add(group);
                }
            }

            tm1DAO.ImportGroups(groups);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void SetUserPermission(String fileName) {
        String COMMA_DELIMITER = ",";
        String GROUP_DELIMITER = "%";

        int USER_NAME = 0;
        int PASSWORD = 1;
        int ASSIGNEDGROUP = 2;

        BufferedReader fileReader = null;
        try {

            //Create a new list of student to be filled by CSV file data
            List users = new ArrayList();

            String line = "";

            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileName));

            //Read the CSV file header to skip it
            fileReader.readLine();

            //Read the file line by line starting from the second line
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(COMMA_DELIMITER);
                String[] groups = tokens[ASSIGNEDGROUP].split(GROUP_DELIMITER);

                if (tokens.length > 0) {
                    //Create a new student object and fill his  data
                    UserInfo user = new UserInfo(groups, tokens[USER_NAME], tokens[PASSWORD]);
                    users.add(user);
                }
            }

            tm1DAO.SetUserPermission(users);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void ChangePassword(String adminUser, String adminPassword, String userName, String newPassword, String adminHost) throws Exception {
        TM1Bean tm1_bean = new TM1Bean();
        tm1_bean.setAdminHost(adminHost);
        int serverCounts = tm1_bean.getNumberOfServers();
        for (int i = 0; i < serverCounts; ++i) {
            String serverName = tm1_bean.getServerName(i);
            TM1Server serv = tm1_bean.openConnection(serverName, adminUser, adminPassword);
            TM1Client client = serv.getClient(userName);
            if (!client.isError()) {
                TM1Val result = client.assignPassword(newPassword);
                if (result.isError()) {
                    logger.error("Changing password for " + userName
                            + " is error on server " + serverName + ". " + result.getErrorMessage());
                    throw new Exception("Changing password is error. {Client "
                            + userName + "." + result.getErrorMessage());

                } else
                    logger.info("Changing password for " + userName
                            + " is successful on server " + serverName + ".");
            } else {
                logger.error("Changing password for " + userName
                        + " is error on server " + serverName + ". " + client.getErrorMessage());
            }

        }
    }
}
