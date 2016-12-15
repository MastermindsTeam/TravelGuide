package blog.bindingModel;

import javax.validation.constraints.NotNull;

/**
 * Created by ivanov on 13.12.2016 г..
 */
public class ArticleBindingModel {
    @NotNull
    private String title;

    @NotNull
    private String content;

    private Integer categoryId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
