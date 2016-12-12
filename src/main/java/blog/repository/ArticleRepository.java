package blog.repository;


import blog.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by PetrovStormwing on 22/Nov/2016.
 */
public interface ArticleRepository extends JpaRepository<Article,Integer> {

}
