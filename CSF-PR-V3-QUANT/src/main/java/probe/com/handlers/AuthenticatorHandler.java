package probe.com.handlers;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;
import probe.com.model.beans.User;

/**
 *
 * @author Yehia Farag
 */
public class AuthenticatorHandler implements Serializable {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private final probe.com.model.Authenticator auth ;

    /**
     *
     * @param url
     * @param dbName
     * @param driver
     * @param userName
     * @param password
     */
    public AuthenticatorHandler(String url,String dbName,String driver,String userName, String password)
	{
		auth = new probe.com.model.Authenticator(url,dbName,driver,userName, password);
	}

    /**
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
		User user = auth.authenticate(email, password);
		return user;
	}

    /**
     *
     * @param username
     * @param password
     * @param admin
     * @param email
     * @return
     */
    public boolean reg(String username, String password, boolean admin,String email) {
		
		return auth.registerNewUser(username, password, admin,email);
	}

    /**
     *
     * @return
     */
    public Map<Integer, String> getUsersList() {
		return auth.getUsersList();
	}

    /**
     *
     * @param user
     * @return
     */
    public boolean removeUser(String user) {
		
		return auth.removeUser(user);
	}

    /**
     *
     * @param name
     * @param oldPass
     * @param newPass
     * @return
     */
    public boolean changePassword(String name, String oldPass,
			String newPass) {
		return auth.changePassword( name, oldPass, newPass);
	}

    /**
     *
     * @param expId
     * @return
     */
    public boolean removeExp(Integer expId) {
		return auth.removeIdentificationDataset(expId); 
	}
	
    /**
     *
     * @param email
     * @return
     */
    public boolean validEmail(String email)
	{
		return auth.validEmail(email);
	}

}
