package blog.controllers;

import blog.models.Post;
import blog.services.NotificationService;
import blog.services.PostService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ivanov on 24.11.2016 г..
 */
@Controller
public class HomeControllerOLD {

    @Autowired
    private PostService postService;

    @Autowired
    private NotificationService notificationService;

//    @RequestMapping("/")
//    public String home(Model model) {
//
//        notificationService.addInfoMessage("Welcome to our amazing blog!");
//
//        List<Post> latest5Posts = postService.findLatest5();
//        model.addAttribute("latest5posts", latest5Posts);
//        List<Post> latest3Posts = latest5Posts.stream()
//                .limit(3).collect(Collectors.toList());
//        model.addAttribute("latest3posts", latest3Posts);
//
//        return "base-layout";
//    }

    @RequestMapping("/posts/view/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        Post post = postService.findById(id);
        if (post == null) {
            notificationService.addErrorMessage("Cannot find post: " + id);
            return "redirect:/";
        }

        model.addAttribute("post", post);
        model.addAttribute("view", "posts/index");

        return "base-layout";
    }


    @GetMapping("/")
    public String index(Model model) {
        notificationService.addInfoMessage("Welcome to our amazing blog!");

        List<Post> latest5Posts = this.postService.findLatest5();

        model.addAttribute("latest5posts", latest5Posts);
        model.addAttribute("view", "home/index");

        return "base-layout";
    }
}
