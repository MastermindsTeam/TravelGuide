package blog.repository;

import blog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ivanov on 15.12.2016 г..
 */
public interface TagRepository extends JpaRepository<Tag, Integer>{
    Tag findByName(String name);
}
