package blog.services;

/**
 * Created by ivanov on 4.12.2016 г..
 */
public interface NotificationService {
    void addInfoMessage(String msg);
    void addErrorMessage(String msg);
}
