package com.hallowedhog;

/**
 * Created by Jay on 5/5/2016.
 */
public class ArticleInformation {

    private String imageUrl;
    private String articleTitle;

    public ArticleInformation(String imageUrl, String articleTitle){
        this.imageUrl = imageUrl;
        this.articleTitle = articleTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }
}
