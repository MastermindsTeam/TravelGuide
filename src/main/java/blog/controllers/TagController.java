package blog.controllers;

import blog.entity.Tag;
import blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by ivanov on 15.12.2016 г..
 */
@Controller
public class TagController {
    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/tag/{name}")
    public String articlesWithTag(Model model, @PathVariable String name){
        Tag tag = this.tagRepository.findByName(name);

        if(tag == null){
            return "redirect:/";
        }

        model.addAttribute("view", "tag/articles");
        model.addAttribute("tag", tag);

        return "base-layout";
    }
}
