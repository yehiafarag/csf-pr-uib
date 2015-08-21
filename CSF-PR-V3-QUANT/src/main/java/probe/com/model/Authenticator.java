package probe.com.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import probe.com.model.beans.User;

/**
 * this class responsible for administrator login authentication
 *
 * @author Yehia Farag
 */
public class Authenticator implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final probe.com.dal.DALAuthenticator daAuthenticator;
    private Pattern pattern;
    private Matcher matcher;

    private final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * constructor
     *
     * @param url
     * @param dbName
     * @param driver
     * @param userName
     * @param password
     */
    public Authenticator(String url, String dbName, String driver, String userName, String password) {
        daAuthenticator = new probe.com.dal.DALAuthenticator(url, dbName, driver, userName, password);
    }

    /**
     * check if the login details (username and password) are correct
     *
     * @param email
     * @param password
     * @return
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public User authenticate(String email, String password) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        String hashedPassword = daAuthenticator.authenticate(email);
        if (hashedPassword == null) {
            return null;
        } else if (hashedPassword.equals(hashPassword(password))) {
            User user = daAuthenticator.getUser(email);
            return user;
        } else {
            return null;
        }
    }

    /**
     * add new user (for future)
     *
     * @param username
     * @param password
     * @param admin
     * @param email
     * @return successful process
     */
    public boolean registerNewUser(String username, String password, boolean admin, String email) {
        String hashedPassword = this.hashPassword(password);
        boolean test = daAuthenticator.registerNewUser(username, hashedPassword, admin, email);
        return test;

    }

    /**
     * encode the password for security
     *
     * @param password
     * @return
     */
    public String hashPassword(String password) {

        String hashword = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes());
            BigInteger hash = new BigInteger(1, md5.digest());
            hashword = hash.toString(16);

        } catch (NoSuchAlgorithmException nsae) {

        }
        return hashword;
    }

    /**
     * get available users registered in the system
     *
     * @return
     */
    public Map<Integer, String> getUsersList() {
        return daAuthenticator.getUsersList();
    }

    /**
     * remove registered users
     *
     * @param user
     * @return
     */
    public boolean removeUser(String user) {
        return daAuthenticator.removeUser(user);
    }

    /**
     * update login password
     *
     * @param name username
     * @param oldPass old password
     * @param newPass new password
     * @return
     */
    public boolean changePassword(String name, String oldPass, String newPass) {

        String oldHashedPassword = this.hashPassword(oldPass);
        String newHashedPassword = this.hashPassword(newPass);
        return daAuthenticator.changePassword(oldHashedPassword, newHashedPassword, name, false);
    }

    /**
     * remove identification dataset from the system
     *
     * @param expId
     * @return
     */
    public boolean removeIdentificationDataset(Integer expId) {
        return daAuthenticator.removeIdentificationDataset(expId);
    }

    /**
     * check the email format
     *
     * @param email
     * @return
     */
    public boolean validEmail(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
