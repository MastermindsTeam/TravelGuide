package blog.service;

/**
 * Created by ivanov on 18.12.2016 г..
 */
public interface NotificationService {
    void addInfoMessage(String msg);
    void addErrorMessage(String msg);
}
