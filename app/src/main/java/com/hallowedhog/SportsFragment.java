package com.hallowedhog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


public class SportsFragment extends Fragment {

    private ListView articles;
    private ArticleInformationAdapter articleAdapter;
    private ArrayList<String> articleList;
    private ArrayList<String> articleImages;
    private ArrayList<ArticleInformation> articleInformation;
    private FTPClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_sports, container, false);

        client = new FTPClient();
        articleList = new ArrayList<>();
        articleImages = new ArrayList<>();
        articleInformation = new ArrayList<>();

        articles = (ListView) view.findViewById(R.id.sports_list);

        articleAdapter = new ArticleInformationAdapter(articleInformation, getActivity());

        articles.setAdapter(articleAdapter);

        new SportsAsync(client).execute();

        return view;
    }

    private class SportsAsync extends AsyncTask<Void, Void, ArrayList<ArticleInformation>> {

        FTPClient client;

        public SportsAsync(FTPClient client){
            this.client = client;
        }

        private ProgressDialog pDlg = null;

        private void showProgressDialog() {

            pDlg = new ProgressDialog(getActivity());
            pDlg.setMessage("Retrieving Articles");
            pDlg.setProgressDrawable(getActivity().getWallpaper());
            pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDlg.setCancelable(false);
            pDlg.show();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected ArrayList<ArticleInformation> doInBackground(Void... params) {

            FTPFile[] files = null;

            try {
                client.connect("hallowedhog.com");
                client.enterLocalPassiveMode();
                client.login("hallowedhog", "!AbcD321hog");
                files = client.listFiles("/public_html/archives/Sports");


                for (FTPFile file : files) {
                    Log.d("File", file.getName());
                    String fileName = file.getName(); // .replaceAll("\\+", " ");
                    if((fileName.equals(".") || fileName.equals(".."))) {}
                    else{
                        articleList.add(fileName);
                    }
                }

                for(String article: articleList){
                    files = client.listFiles("/public_html/archives/Sports/" + article);
                    for(FTPFile file: files){
                        if(file.getName().contains("Picture")){
                            articleImages.add("http://hallowedhog.com/archives/Sports/" + article + "/" + file.getName());
                        }
                    }
                }

                for(int i = 0; i < articleList.size(); i++){
                    articleInformation.add(new ArticleInformation(articleImages.get(i), articleList.get(i)));
                }

            }catch(IOException e){
                e.printStackTrace();
            }

            return articleInformation;
        }

        @Override
        protected void onPostExecute(ArrayList<ArticleInformation> result) {
            super.onPostExecute(result);
            pDlg.dismiss();
            articleAdapter.setArticleInformation(result);
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
                client.listFiles("/public_html/archives/Sports/" + url);
                for (FTPFile file : client.listFiles("/public_html/archives/Sports/" + url)) {
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
            pdfViewer.putExtra("url", "Sports/" + url +"/"+result);
            startActivity(pdfViewer);


        }

    }

}
