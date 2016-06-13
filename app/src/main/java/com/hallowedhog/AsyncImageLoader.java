package com.hallowedhog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Jay on 5/5/2016.
 */
public class AsyncImageLoader {
    private HashMap<String, SoftReference<Drawable>> imageCache;
    private static Context context;
    public AsyncImageLoader(Context context){
        this.context = context;
        imageCache = new HashMap<>();
    }

    public Drawable loadDrawable(final String imageURL, final ImageCallBack imageCallBack){
        if(imageCache.containsKey(imageURL)){
            SoftReference<Drawable> softReference = imageCache.get(imageURL);
            Drawable drawable = softReference.get();
            if(drawable != null){
                return drawable;
            }
        }
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message message){
                imageCallBack.imageLoaded((Drawable) message.obj, imageURL);
            }
        };
        new Thread(){
            @Override
            public void run(){
                Drawable drawable = loadImageFromUrl(imageURL);
                imageCache.put(imageURL, new SoftReference<Drawable>(drawable));
                Message message = handler.obtainMessage(0, drawable);
                handler.sendMessage(message);
            }
        }.start();
        return null;
    }

    public static Drawable loadImageFromUrl(String url){
        InputStream inputStream;
        Drawable drawable = null;
        try{
            inputStream = new URL(url).openStream();
        }catch(IOException e){
            throw new RuntimeException(e);
        }

        drawable = Drawable.createFromStream(inputStream, "src");
        Bitmap b = ((BitmapDrawable)drawable).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 700, 700, false);
        return new BitmapDrawable(bitmapResized);

    }

    public static Drawable loadScaledImageFromUrl(String url){
        InputStream inputStream;
        Drawable drawable = null;
        try{
            inputStream = new URL(url).openStream();
        }catch(IOException e){
            throw new RuntimeException(e);
        }

        drawable = Drawable.createFromStream(inputStream, "src");
        return drawable;

    }

    public interface ImageCallBack {
        public void imageLoaded(Drawable imageDrawable, String imageUrl);
    }






}
