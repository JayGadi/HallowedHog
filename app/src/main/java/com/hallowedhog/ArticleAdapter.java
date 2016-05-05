package com.hallowedhog;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 5/1/2016.
 */
public class ArticleAdapter extends ArrayAdapter<String> {

    private List<String> articles;
    private List<Bitmap> articleImages;
    private Context context;

    public ArticleAdapter(List<String> articles, List<Bitmap> articleImages, Context context){

        super(context, android.R.layout.simple_list_item_1, articles);
        this.context = context;
        this.articles = articles;
        this.articleImages = articleImages;

    }

    public int getArticleCount(){
        if(articles != null)
            return articles.size();
        return 0;
    }

    public int getImageCount(){
        if(articleImages != null)
            return articleImages.size();
        return 0;
    }

    public Bitmap getImage(int position){
        if(articleImages != null){
            return articleImages.get(position);
        }
        return null;
    }
    public String getArticle(int position){
        if(articles != null){
            return articles.get(position);
        }
        return null;
    }

    public long getItemID(int position){
        if(articles != null)
            return articles.get(position).hashCode();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.article_list_layout, null);
        }

        List<String> articlesFormat = new ArrayList<>();
        List<Bitmap> imagesFormat = new ArrayList<>();
         for(String articleFormat: articles){
            articleFormat = articleFormat.substring(11);
            articlesFormat.add(articleFormat.replaceAll("\\+", " "));
        }

        String article = articlesFormat.get(position);

        TextView articleTitle = (TextView) v.findViewById(R.id.article_title);


        articleTitle.setText(article);


        for(Bitmap images : articleImages){
            imagesFormat.add(Bitmap.createScaledBitmap(images, 220, 220, false));
        }

        Bitmap image = imagesFormat.get(position);
        ImageView articlePicture = (ImageView) v.findViewById(R.id.article_picture);
        articlePicture.setImageBitmap(image);



        return v;
    }

    public List<String> getArticleList(){
        return articles;
    }
    public List<Bitmap> getImageList() { return articleImages; }

    public void setArticleList(List<String> articles){
        this.articles = articles;
    }
    public void setImageList(List<Bitmap> images){
        this.articleImages = images;
    }
}
