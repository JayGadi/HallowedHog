package com.hallowedhog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 5/1/2016.
 */
public class ArticleInformationAdapter extends ArrayAdapter<ArticleInformation> {

    private List<ArticleInformation> articleInformation;
    private AsyncImageLoader imageLoader;
    private Context context;
    private Drawable cachedImage;

    public ArticleInformationAdapter(List<ArticleInformation> articleInformation, Context context){

        super(context, 0, articleInformation);
        this.context = context;
        this.articleInformation = articleInformation;
        imageLoader = new AsyncImageLoader(context);


    }

    public int getCount(){
        if(articleInformation != null)
            return articleInformation.size();
        return 0;
    }

    public ArticleInformation getArticleInformation(int position){
        if(articleInformation != null){
            return articleInformation.get(position);
        }
        return null;
    }

    public long getItemID(int position){
        if(articleInformation != null)
            return articleInformation.get(position).hashCode();
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if(v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.article_list_layout, null);
        }

        ArticleInformation articleInfo = articleInformation.get(position);

        final ImageView articleImage = (ImageView) v.findViewById(R.id.article_picture);
        TextView articleTitle = (TextView) v.findViewById(R.id.article_title);

        cachedImage = imageLoader.loadDrawable(articleInfo.getImageUrl(), new AsyncImageLoader.ImageCallBack() {
            @Override
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                articleImage.setImageDrawable(imageDrawable);
            }
        });

        articleImage.setImageDrawable(cachedImage);
        articleTitle.setText(articleInfo.getArticleTitle());


        return v;
    }

    private Drawable getCachedImage(){
        return cachedImage;
    }

    public void setArticleInformation(ArrayList<ArticleInformation> articleInformation){
        this.articleInformation = articleInformation;
        for(ArticleInformation info: articleInformation){
            Log.d("Article Title", info.getArticleTitle());
            info.setArticleTitle(info.getArticleTitle().substring(11));
            info.setArticleTitle(info.getArticleTitle().replaceAll("\\+", " "));
        }
    }


}
