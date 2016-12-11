package blog.repository;

import blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ivanov on 11.12.2016 г..
 */
public interface UserRepository extends JpaRepository<User, Integer>{
    User findByEmail(String email);
}
