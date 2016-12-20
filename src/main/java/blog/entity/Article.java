package blog.entity;

import org.springframework.beans.factory.annotation.Required;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "articles")
public class Article {

    private Integer id;

    private String title;

    private String content;

    private String mapCoordinates;

    private String picturePath1;

    private User author;

    private Category category;

    private Set<Tag> tags;

    public Article(String title, String content, String mapCoordinates, User author, Category category, HashSet<Tag> tags, String picturePath1) {
        this.title = title;
        this.content = content;
        this.mapCoordinates = mapCoordinates;
        this.author = author;
        this.category = category;
        this.tags = tags;
        this.picturePath1 = picturePath1;
    }

    public Article() {
    }

    @Transient
    public String getSummary() {
        return this.getContent().length() > 200
                ? this.getContent().substring(0, Math.min(this.getContent().length(), 200)) + "..."
                : this.getContent();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(nullable = false)
    @Required
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(columnDefinition = "text", nullable = false)
    @Required
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "map_coordinates")
    public String getMapCoordinates() {
        return mapCoordinates;
    }

    public void setMapCoordinates(String mapCoordinates) {
        this.mapCoordinates = mapCoordinates;
    }

    @ManyToOne
    @JoinColumn(nullable = false, name = "authorId")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @ManyToOne()
    @JoinColumn(nullable = false, name = "categoryId")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @ManyToMany()
    @JoinColumn(table = "articles_tags")
    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public String getPicturePath1() {
        return picturePath1;
    }

    public void setPicturePath1(String picturePath1) {
        this.picturePath1 = picturePath1;
    }
}
