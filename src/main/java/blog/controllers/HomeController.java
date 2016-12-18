package blog.controllers;


import blog.entity.Article;
import blog.entity.Category;
import blog.repository.ArticleRepository;
import blog.repository.CategoryRepository;
import blog.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by ivanov on 15/Dec/2016.
 */
@Controller
public class HomeController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/")
    public String index(Model model) {
        notificationService.addInfoMessage("Welcome to our amazing Travel Guide!");
        List<Category> categories = this.categoryRepository.findAll();
        List<Article> allPosts = articleRepository.findAll();
        List<Article> latest3Articles = allPosts.stream()
                .sorted((a, b) -> b.getId().compareTo(a.getId()))
                .limit(3)
                .collect(Collectors.toList());

        model.addAttribute("latest3articles", latest3Articles);
        model.addAttribute("categories", categories);
        model.addAttribute("view", "home/index");

        return "base-layout";
    }

    @RequestMapping("/error/403")
    public String accessDenied(Model model) {
        model.addAttribute("view", "error/403");

        return "base-layout";
    }

    @GetMapping("/category/{id}")
    public String listArticles(Model model, @PathVariable Integer id){
        if(!this.categoryRepository.exists(id)){
            return "redirect:/";
        }
        Category category = this.categoryRepository.findOne(id);
        Set<Article> articles = category.getArticles();

        model.addAttribute("articles", articles);
        model.addAttribute("category", category);
        model.addAttribute("view", "home/list-articles");

        return "base-layout";
    }
}
