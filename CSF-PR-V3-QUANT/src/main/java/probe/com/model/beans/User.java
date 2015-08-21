package probe.com.model.beans;

import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public class User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5160857291401579291L;

    private boolean admin;
    private boolean user;
    private String username;
    private String email;

    /**
     *
     * @return
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     *
     * @param admin
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     *
     * @return
     */
    public boolean isUser() {
        return user;
    }

    /**
     *
     * @param user
     */
    public void setUser(boolean user) {
        this.user = user;
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

}
