package probe.com.dal;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;

import probe.com.model.beans.User;

/**
 *
 * @author Yehia Farag
 */
public class DALAuthenticator implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final DataBase db;

    /**
     * constructor 
     * @param url
     * @param dbName
     * @param driver
     * @param userName
     * @param password
     */
    public DALAuthenticator(String url, String dbName, String driver, String userName, String password) {
        db = new DataBase(url, dbName, driver, userName, password);
    }


    /**
     * validate the login username 
     * @param email
     * @return
     */
    
    public boolean validateUsername(String email) {
        boolean test = db.validateUsername(email);
        return test;
    }

    /**
     *
     * @param email
     * @return
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public String authenticate(String email) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        String password = db.authenticate(email);
        return password;
    }

    /**
     * add new user (for future)
     *
     * @param username
     * @param password
     * @param admin
     * @param email
     * @return
     */
    public boolean registerNewUser(String username, String password, boolean admin, String email) {

        boolean test = db.storeNewUser(username, password, admin, email);
        return test;
    }

    /**
     * update login password
     *
     * @param username username
     * @param oldpassword old password
     * @param newpassword new password
     * @param admin is admin user
     * @return
     */
    public boolean changePassword(String oldpassword, String newpassword, String username, boolean admin) {
        //get user password
        String password = db.authenticate(username);
        //compare with old
        if (oldpassword.equals(password)) {
            return db.updateUserPassword(username, newpassword);
        }
        return false;

    }

    /**
     * get user information using his login email
     * @param email
     * @return
     */
    public User getUser(String email) {
        User user = db.getUser(email);
        return user;
    }

    /**
     * get available users registered in the system
     *
     * @return
     */
    public Map<Integer, String> getUsersList() {

        return db.getUsersList();
    }

    /**
     * remove registered users
     *
     * @param user
     * @return
     */
    public boolean removeUser(String user) {
        return db.removeUser(user);
    }

    /**
     * remove identification dataset from the system
     *
     * @param expId
     * @return
     */
    public boolean removeIdentificationDataset(int expId) {
        return db.removeIdentificationDataset(expId);
    }

}
