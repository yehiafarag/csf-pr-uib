package probe.com.listeners;

import java.io.Serializable;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import probe.com.model.AuthenticatorLogic;

import probe.com.dal.DataAccess;

/**
 * @author Yehia Farag
 *
 */
public class ContextListener implements ServletContextListener, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String URL, D_B_NAME, DRIVER, USER_NAME, PASSWORD;
    private String adminName, adminPassword, adminEmail;

    /**
     *
     * @param event
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }

    /**
     * this method responsible for initialize database and create the database
     * tables
     *
     * @param event start the context event
     *
     *
     *
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {

        ServletContext scx = event.getServletContext();
        URL = scx.getInitParameter("url");
        D_B_NAME = scx.getInitParameter("dbName");
        DRIVER = scx.getInitParameter("driver");
        USER_NAME = scx.getInitParameter("userName");
        PASSWORD = scx.getInitParameter("password");
        adminName = scx.getInitParameter("adminName");
        adminEmail = scx.getInitParameter("adminEmail");
        adminPassword = scx.getInitParameter("adminPassword");
        DataAccess da = new DataAccess(URL, D_B_NAME, DRIVER, USER_NAME, PASSWORD);

        da.createTable();
        AuthenticatorLogic auth = new AuthenticatorLogic(URL, D_B_NAME, DRIVER, USER_NAME, PASSWORD);
        if (adminName != null && adminPassword != null) {
            adminName = adminName.toUpperCase();
            auth.registerNewUser(adminName, adminPassword, true, adminEmail.toUpperCase());
            auth.changePassword(adminName, "norway", adminPassword);

        }
//		da.runOnceToUpdateDatabase();
    }

}
