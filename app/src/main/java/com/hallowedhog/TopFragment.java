package com.hallowedhog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class TopFragment extends Fragment {

    private ImageView topArticle;
    private ListView topList;
    private ArticleAdapter articleAdapter;
    private ArrayList<String> articleList;
    private FTPClient client;
    private ArrayList<Bitmap> articleImages;
    int count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_top, container, false);

        client = new FTPClient();
        articleList = new ArrayList<>();
        articleImages = new ArrayList<>();

        topArticle = (ImageView) view.findViewById(R.id.top_article);
        topList = (ListView) view.findViewById(R.id.top_list);
        articleAdapter = new ArticleAdapter(articleList, articleImages, getActivity());

        topList.setAdapter(articleAdapter);

        new TopAsync(client).execute();



        return view;
    }

    public String getPictureExtension(FTPFile []fileDirectory){
        for(FTPFile file: fileDirectory){
            if(file.getName().contains("Picture")){
                return file.getName();
            }
        }
        return null;
    }
    private class TopAsync extends AsyncTask<Void, Void, ArrayList<String>> {

        FTPClient client;

        public TopAsync(FTPClient client){
            this.client = client;
        }


        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            FTPFile[] files = null;
            String image  = null;

            try {
                client.connect("hallowedhog.com");
                client.enterLocalPassiveMode();
                client.login("hallowedhog", "!AbcD321hog");
                files = client.listFiles("/public_html/archives/Carleton");
                articleList.add(files[files.length - 1].getName());
                image = getPictureExtension(client.listFiles("/public_html/archives/Carleton/" + files[files.length - 1].getName()));
                articleImages.add(BitmapFactory.decodeStream((InputStream) new URL("http://hallowedhog.com/archives/Carleton/" + files[files.length - 1].getName() + "/" + image).getContent()));
                files = client.listFiles("/public_html/archives/Comics");
                articleList.add(files[files.length-1].getName());
                image = getPictureExtension(client.listFiles("/public_html/archives/Comics/" + files[files.length - 1].getName()));
                articleImages.add(BitmapFactory.decodeStream((InputStream) new URL("http://hallowedhog.com/archives/Comics/" + files[files.length - 1].getName() + "/" + image).getContent()));
                files = client.listFiles("/public_html/archives/Politics");
                articleList.add(files[files.length-1].getName());
                image = getPictureExtension(client.listFiles("/public_html/archives/Politics/" + files[files.length - 1].getName()));
                articleImages.add(BitmapFactory.decodeStream((InputStream) new URL("http://hallowedhog.com/archives/Politics/" + files[files.length - 1].getName() + "/" + image).getContent()));
                files = client.listFiles("/public_html/archives/PopCulture");
                articleList.add(files[files.length-1].getName());
                image = getPictureExtension(client.listFiles("/public_html/archives/PopCulture/" + files[files.length - 1].getName()));
                articleImages.add(BitmapFactory.decodeStream((InputStream) new URL("http://hallowedhog.com/archives/PopCulture/" + files[files.length - 1].getName() + "/" + image).getContent()));
                files = client.listFiles("/public_html/archives/Sports");
                articleList.add(files[files.length-1].getName());
                image = getPictureExtension(client.listFiles("/public_html/archives/Sports/" + files[files.length - 1].getName()));
                articleImages.add(BitmapFactory.decodeStream((InputStream) new URL("http://hallowedhog.com/archives/Sports/" + files[files.length - 1].getName() + "/" + image).getContent()));
                files = client.listFiles("/public_html/archives/WorldwideAffairs");
                articleList.add(files[files.length-1].getName());
                image = getPictureExtension(client.listFiles("/public_html/archives/WorldwideAffairs/" + files[files.length - 1].getName()));
                articleImages.add(BitmapFactory.decodeStream((InputStream) new URL("http://hallowedhog.com/archives/WorldwideAffairs/" + files[files.length - 1].getName() + "/" + image).getContent()));
                Log.d("Article Size", "" + articleList.size());
                Log.d("Article Image Size", "" + articleImages.size());


            }catch(IOException e){
                e.printStackTrace();
            }

            return articleList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            articleAdapter.addAll(result);

            topArticle.setImageBitmap(articleImages.get(count));
            topArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    topArticle.setImageBitmap(articleImages.get(count++));
                    if(count == articleImages.size()){
                        count = 0;
                    }
                }
            });

            //articleAdapter.notifyDataSetChanged();

            /*
            articles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("File Clicked", (String)articles.getAdapter().getItem(position));

                    new OpenArticle().execute((String)articles.getAdapter().getItem(position));

                }
            });*/

        }
    }




}
