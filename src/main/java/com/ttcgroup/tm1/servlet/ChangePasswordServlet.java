package com.ttcgroup.tm1.servlet;
import com.ibm.cognos.fpmsvc.config.FPMConfig;
import com.ttcgroup.tm1.bus.IImportBUS;
import com.ttcgroup.tm1.bus.ImportBUSImpl;
import com.ttcgroup.tm1.utils.ProxyFactory;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;
import org.slf4j.Logger;

/**
 * Created by Administrator on 5/11/2015.
 */
public class ChangePasswordServlet extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException
    {
        Logger logger = LoggerFactory.getLogger(ChangePasswordServlet.class);
        ServletContext context = getServletContext();
        context.log("Hello ChangePasswordServlet");
        logger.info("Hello ChangePasswordServlet");
        String message = "Hello World";
        // Set response content type
        response.setContentType("text/html");

        // Actual logic goes here.
        PrintWriter out = response.getWriter();
        out.println("<h1>chinhdq:" + getServletContext().getRealPath("/")+ "</h1>");
    }
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Properties prop = new Properties();
        String propFileName = getServletContext().getRealPath("/")+"ttc_tm1.properties";

        IImportBUS importBUS = null;
        importBUS = new ImportBUSImpl();//(IImportBUS) ProxyFactory.newInstance(new ImportBUSImpl());
        String userName = request.getParameter("ttcUser");
        String password = request.getParameter("ttcPassword");
        PrintWriter out = response.getWriter();
        try {
            InputStream inputStream = new FileInputStream(propFileName);
            prop.load(inputStream);
            String adminPass = prop.getProperty("tm1_password");
            String adminUser = prop.getProperty("tm1_user");
            importBUS.ChangePassword(adminUser, adminPass, userName, password, FPMConfig.getLocalHostName());

            response.setContentType("text/html");
//            out.println("<h3>" + userName + ", " + password
//                    + ", " + adminPass +  ", " + adminUser +"</h3>");
            out.println("<h3>" + userName + " is changed successfully. You need to log in again</h3>");
            out.println("<a href='" + request.getScheme() + "://" + request.getServerName() +                     ":" +                           // ":"
                    request.getServerPort() + "/pmpsvc/logoff.jsp'>Continue</a>");
//            out.println("<input type=\"button\" onclick=\""
//                    + getServletContext().getRealPath(\)+"/'logoff.jsp';return false;\" value=\"Continue\"/>");

        } catch (Exception e) {

            out.println("<h3>" + e.getMessage() + "</h3>");
        }
    }
}
