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
public class ArticleAdapter extends ArrayAdapter<ArticleInformation> {

    private List<ArticleInformation> articleInformation;
    private Context context;

    public ArticleAdapter(List<ArticleInformation> articleInformation, Context context){

        super(context, 0, articleInformation);
        this.context = context;
        this.articleInformation = articleInformation;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.article_list_layout, null);
        }

        ArticleInformation articleInfo = getItem(position);

        ImageView articleImage = (ImageView) v.findViewById(R.id.article_picture);
        



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
