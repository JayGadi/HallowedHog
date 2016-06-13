package com.hallowedhog;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class ArticleViewActivity extends AppCompatActivity {

    private TextView articleViewer;
    private ImageView articlePicture;
    private Drawable img;
    private String url = "";
    private String image = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);

        articleViewer = (TextView) findViewById(R.id.article_viewer);
        articlePicture = (ImageView) findViewById(R.id.article_picture);
        Bundle extra = getIntent().getExtras();

        Log.d("Here", "Here");

        if (extra != null) {
            url = extra.getString("url");
            image = extra.getString("picture");
        }

        ViewArticle viewArticle = new ViewArticle();
        viewArticle.execute(new String[]{url});

    }

    private class ViewArticle extends AsyncTask<String, Void, StringBuilder>{

        @Override
        protected StringBuilder doInBackground(String... params) {

            StringBuilder total = new StringBuilder();
            try {
                // Create a URL for the desired page
                URL viewRL = new URL(params[0]);

                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(viewRL.openStream()));
                img = AsyncImageLoader.loadScaledImageFromUrl(image);


                String str;
                while ((str = in.readLine()) != null) {
                    // str is one line of text; readLine() strips the newline character(s)
                    total.append(str + "\n");

                }

                in.close();
            } catch (MalformedURLException e) {
            } catch (IOException e) {

            }

            return total;
        }

        @Override
        protected void onPostExecute(StringBuilder results){
            super.onPostExecute(results);
            articleViewer.setText(results);
            articlePicture.setImageDrawable(img);
        }
    }
}

