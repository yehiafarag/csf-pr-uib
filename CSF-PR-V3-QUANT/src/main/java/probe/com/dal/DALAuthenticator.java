package probe.com.dal;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;

import probe.com.model.beans.User;

/**
 *
 * @author Yehia Farag
 *
 * This class represents and abstraction for the data access authentication
 * layer
 */
public class DALAuthenticator implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final DataBase db;

    /**
     * Initialize instance of DAL authenticator
     *
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
     * Validate the login username
     *
     * @param email
     * @return
     */
    public boolean validateUsername(String email) {
        boolean test = db.validateUsername(email);
        return test;
    }

    /**
     * Get user password to compare and validate
     *
     * @param email
     * @return user password
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
     * Add new user (for future)
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
     * Update login password
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
     * Get user information using his login email
     *
     * @param email login email
     * @return user object that contains user information
     */
    public User getUser(String email) {
        User user = db.getUser(email);
        return user;
    }

    /**
     * Get available users registered in the system
     *
     * @return LIST OF USERES ID
     */
    public Map<Integer, String> getUsersList() {

        return db.getUsersList();
    }

    /**
     * Remove registered users
     *
     * @param user
     * @return
     */
    public boolean removeUser(String user) {
        return db.removeUser(user);
    }

    /**
     * Remove identification dataset from the system
     *
     * @param expId
     * @return
     */
    public boolean removeIdentificationDataset(int expId) {
        return db.removeIdentificationDataset(expId);
    }

}
