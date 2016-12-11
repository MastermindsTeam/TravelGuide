package blog.repository;

import blog.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ivanov on 11.12.2016 г..
 */
public interface RoleRepository extends JpaRepository<Role, Integer>{
    Role findByName(String name);
}
