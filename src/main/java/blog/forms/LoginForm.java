package blog.forms;

import javax.validation.constraints.Size;

/**
 * Created by ivanov on 6.12.2016 г..
 */
public class LoginForm {

    @Size(min = 1, max = 30, message = "Please enter a username between 1 and 30 symbols")
    private String username;

    @Size(min = 1, max = 30, message = "Please enter a password between 1 and 30 symbols")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
