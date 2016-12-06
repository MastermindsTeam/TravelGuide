package blog.services;

/**
 * Created by ivanov on 6.12.2016 г..
 */
public interface LoginService {
    boolean authenticate(String username, String password);
}
