package com.hallowedhog;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;


public class ArticleViewActivity extends AppCompatActivity  {

    private WebView pdfViwer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);

        pdfViwer = (WebView) findViewById(R.id.pdf_viewer);
        Bundle extra = getIntent().getExtras();

        Log.d("Here", "Here");
        String url = "";
        if(extra  != null){
            url = extra.getString("url");

        }

        String pdfUrl = "http://docs.google.com/gview?embedded=true&url=" + "http://hallowedhog.com/archives/" + url;
        Log.d("URL", pdfUrl);
        pdfViwer.getSettings().setJavaScriptEnabled(true);
        pdfViwer.loadUrl(pdfUrl);
    }


}
