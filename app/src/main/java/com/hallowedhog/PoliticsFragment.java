package com.hallowedhog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class PoliticsFragment extends Fragment {


    private ListView articles;
    private ArticleAdapter articleAdapter;
    private ArrayList<String> articleList;
    private ArrayList<Bitmap> articleImages;
    private FTPClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_politics, container, false);

        client = new FTPClient();
        articleList = new ArrayList<>();
        articleImages = new ArrayList<>();

        articles = (ListView) view.findViewById(R.id.politics_list);
        articleAdapter = new ArticleAdapter(articleList, articleImages, getActivity());

        articles.setAdapter(articleAdapter);

        new PoliticsAsync(client).execute();

        return view;
    }

    private class PoliticsAsync extends AsyncTask<Void, Void, ArrayList<String>> {

        FTPClient client;

        public PoliticsAsync(FTPClient client){
            this.client = client;
        }


        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            FTPFile[] files = null;

            try {
                client.connect("hallowedhog.com");
                client.enterLocalPassiveMode();
                client.login("hallowedhog", "!AbcD321hog");
                files = client.listFiles("/public_html/archives/Politics");


                for (FTPFile file : files) {
                    Log.d("File", file.getName());
                    String fileName = file.getName(); // .replaceAll("\\+", " ");
                    if((fileName.equals(".") || fileName.equals(".."))) {}
                    else{
                        articleList.add(fileName);
                    }
                }

                for(String article: articleList){
                    files = client.listFiles("/public_html/archives/Politics/" + article);
                    for(FTPFile file: files){
                        if(file.getName().contains("Picture")){
                            articleImages.add(BitmapFactory.decodeStream((InputStream) new URL("http://hallowedhog.com/archives/Politics/" + article + "/" + file.getName()).getContent()));
                        }
                    }
                }

            }catch(IOException e){
                e.printStackTrace();
            }

            return articleList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            articleAdapter.addAll(result);
            articleAdapter.notifyDataSetChanged();
            articles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("File Clicked", (String)articles.getAdapter().getItem(position));

                    new OpenArticle().execute((String)articles.getAdapter().getItem(position));

                }
            });

        }
    }

    private class OpenArticle extends AsyncTask<String, Void, String>{

        String url;
        @Override
        protected String doInBackground(String... params) {

            String article = "";

            url = params[0];
            try {
                client.listFiles("/public_html/archives/Politics/" + url);
                for (FTPFile file : client.listFiles("/public_html/archives/Politics/" + url)) {
                    Log.d("File", file.getName());
                    if(file.getName().contains(".pdf")){
                        article = file.getName();
                    }

                }
            } catch (IOException e) {

            }
            return article;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Intent pdfViewer = new Intent(getActivity(), ArticleViewActivity.class);
            //http://hallowedhog.com/viewPageTest.php?article=archives/
            Log.d("URL", url+"/"+result);
            pdfViewer.putExtra("url", "Politics/" + url +"/"+result);
            startActivity(pdfViewer);


        }

    }

}
