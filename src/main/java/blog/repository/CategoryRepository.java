package blog.repository;

import blog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ivanov on 15.12.2016 г..
 */
public interface CategoryRepository extends JpaRepository<Category, Integer>{
}
