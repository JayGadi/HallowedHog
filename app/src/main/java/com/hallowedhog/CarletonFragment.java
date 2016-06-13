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
import java.util.HashMap;
import java.util.Iterator;


public class CarletonFragment extends Fragment {

    private ListView articles;
    private ArticleInformationAdapter articleAdapter;
    private ArrayList<String> articleList;
    private FTPClient client;
    private ArrayList<String> articleImages;
    private ArrayList<ArticleInformation> articleInformation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_carleton, container, false);

        client = new FTPClient();
        articleList = new ArrayList<>();
        articleImages = new ArrayList<>();
        articleInformation = new ArrayList<>();

        articles = (ListView) view.findViewById(R.id.carleton_list);
        articleAdapter = new ArticleInformationAdapter(articleInformation, getActivity());

        articles.setAdapter(articleAdapter);

        new CarletonAsync(client).execute();

        return view;
    }

    private class CarletonAsync extends AsyncTask<Void, Void, ArrayList<ArticleInformation>> {

        FTPClient client;

        public CarletonAsync(FTPClient client){
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
                files = client.listFiles("/public_html/archives/Testing");


                for (FTPFile file : files) {
                    Log.d("File", file.getName());
                    String fileName = file.getName();
                    if((fileName.equals(".") || fileName.equals(".."))) {}
                    else{
                        articleList.add(fileName);
                    }
                }
                Log.d("Article List", "" + articleList.size());
                for(String article: articleList){
                    files = client.listFiles("/public_html/archives/Testing/" + article);
                    for(FTPFile file: files){
                        if(file.getName().contains("Picture")){
                            articleImages.add("http://hallowedhog.com/archives/Testing/" + article + "/" + file.getName());
                        }
                    }
                }
                Log.d("Article Images Size", "" + articleImages.size());
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
            Log.d("Result Size", "" + result.size());
            articleAdapter.setArticleInformation(result);
            articleAdapter.notifyDataSetChanged();
            articles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent pdfViewer = new Intent(getActivity(), ArticleViewActivity.class);

                    Log.d("URL", "http://hallowedhog.com/archives/Testing/" + articleList.get(position) + "/" + articleList.get(position).substring(11) + ".txt");
                    pdfViewer.putExtra("url", "http://hallowedhog.com/archives/Testing/" + articleList.get(position) + "/" + articleList.get(position).substring(11) + ".txt");
                    pdfViewer.putExtra("picture", articleImages.get(position));
                    startActivity(pdfViewer);


                }
            });

        }
    }
}
