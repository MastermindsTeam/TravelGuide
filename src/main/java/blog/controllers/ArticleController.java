package blog.controllers;

import blog.bindingModel.ArticleBindingModel;
import blog.entity.Article;
import blog.entity.Category;
import blog.entity.Tag;
import blog.entity.User;
import blog.repository.ArticleRepository;
import blog.repository.CategoryRepository;
import blog.repository.TagRepository;
import blog.repository.UserRepository;
import blog.service.NotificationService;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by ivanov on 13.12.2016 г..
 */
@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private Environment env;


    @GetMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model) {
        List<Category> categories = this.categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("view", "article/create");

        return "base-layout";
    }

    @PostMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(ArticleBindingModel articleBindingModel) throws IOException {
        String title = "";
        if (StringUtils.isEmpty(articleBindingModel.getTitle())) {
            notificationService.addErrorMessage("Title is mandatory.");
            return "redirect:/article/create";
        }
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if("".equals(articleBindingModel.getFile().getOriginalFilename())){
            notificationService.addErrorMessage("Picture is mandatory.");
            return "redirect:/article/create";
        }

        this.saveFile(articleBindingModel.getFile());
        User userEntity = this.userRepository.findByEmail(user.getUsername());
        Category category = this.categoryRepository.findOne(articleBindingModel.getCategoryId());
        HashSet<Tag> tags = this.findTagsFromString(articleBindingModel.getTagString());

        for (Tag tag : tags) {
            System.out.println(tag.getId());
        }


        String coordinates = "";

        if (!StringUtils.isEmptyOrWhitespace(articleBindingModel.getMapCoordinates())) {
            coordinates = parseCoordinates(articleBindingModel.getMapCoordinates());

            if (coordinates.equals("ERROR")) {
                throw new IllegalArgumentException("Invalid coordinates entered");
            }
        }

        String picturePath1 = "/img/" + articleBindingModel.getFile().getOriginalFilename();
        Article articleEntity = new Article(
                articleBindingModel.getTitle(),
                articleBindingModel.getContent(),
                coordinates,
                userEntity,
                category,
                tags,
                picturePath1
        );

        this.articleRepository.saveAndFlush(articleEntity);
        return "redirect:/";
    }

    private String parseCoordinates(String mapCoordinates) {
        String[] coordinatesString = mapCoordinates.split(", ");

        double latitude;
        double longitude;

        try {
            latitude = Double.parseDouble(coordinatesString[0]);
            longitude = Double.parseDouble(coordinatesString[1]);
        } catch (NumberFormatException ex) {
            return "ERROR";
        }

        String coordinates = String.format(Locale.ROOT, "%f,%f", latitude, longitude);
        return coordinates;
    }

    @GetMapping("/article/{id}")
    public String details(Model model, @PathVariable Integer id) {
        if (!this.articleRepository.exists(id)) {
            notificationService.addErrorMessage("Cannot find post #" + id);
            return "redirect:/";
        }

        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User entityUser = this.userRepository.findByEmail(principal.getUsername());
            model.addAttribute("user", entityUser);
        }

        Article article = this.articleRepository.findOne(id);

        model.addAttribute("article", article);
        model.addAttribute("view", "article/details");

        return "base-layout";
    }

    @GetMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Integer id, Model model) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }
        Article article = this.articleRepository.findOne(id);

        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article/" + id;
        }

        List<Category> categories = this.categoryRepository.findAll();
        String tagString = article.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.joining(", "));

        model.addAttribute("view", "article/edit");
        model.addAttribute("article", article);
        model.addAttribute("categories", categories);
        model.addAttribute("tags", tagString);

        return "base-layout";
    }

    @PostMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id, ArticleBindingModel articleBindingModel) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }
        Article article = this.articleRepository.findOne(id);

        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article/" + id;
        }

        Category category = this.categoryRepository.findOne(articleBindingModel.getCategoryId());
        HashSet<Tag> tags = this.findTagsFromString(articleBindingModel.getTagString());

        article.setCategory(category);
        article.setContent(articleBindingModel.getContent());
        article.setMapCoordinates(article.getMapCoordinates());
        article.setTitle(articleBindingModel.getTitle());
        article.setTags(tags);

        this.articleRepository.saveAndFlush(article);

        return "redirect:/article/" + article.getId();
    }

    @GetMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable Integer id) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }

        Article article = this.articleRepository.findOne(id);

        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article/" + id;
        }

        model.addAttribute("article", article);
        model.addAttribute("view", "article/delete");

        return "base-layout";
    }

    @PostMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable Integer id) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }
        Article article = this.articleRepository.findOne(id);

        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article/" + id;
        }
        this.articleRepository.delete(article);

        return "redirect:/";
    }

    private boolean isUserAuthorOrAdmin(Article article) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin() || userEntity.isAuthor(article);
    }

    private HashSet<Tag> findTagsFromString(String tagString) {
        HashSet<Tag> tags = new HashSet<>();

        if (tagString.isEmpty()) {
            return tags;
        }

        String[] tagNames = tagString.split(",\\s*");

        for (String tagName : tagNames) {
            Tag currentTag = this.tagRepository.findByName(tagName);

            if (currentTag == null) {
                currentTag = new Tag(tagName);
                this.tagRepository.saveAndFlush(currentTag);
            }

            tags.add(currentTag);
        }

        return tags;
    }

    private void saveFile(MultipartFile file) throws IOException {

        File convFile = new File("src/main/resources/public/img/" + file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
    }
}
