package blog.services;

import blog.models.Post;

import java.util.List;

/**
 * Created by ivanov on 30.11.2016 г..
 */
public interface PostService {
    List<Post> findAll();
    List<Post> findLatest5();
    Post findById(Long id);
    Post create(Post post);
    Post edit(Post post);
    void deleteById(Long id);
}
