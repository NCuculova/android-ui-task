package com.ncuculova.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * load image from url
 */
public class DownloadImagesTask extends AsyncTask<Void, Void, Bitmap> {

    OnImageDownload mOnImageDownload;
    String mUrl;
    ImageView mImageView;

    public DownloadImagesTask(String url, ImageView mImageView) {
        this.mImageView = mImageView;
        this.mUrl = url;
    }

    public void setOnImageDownload(OnImageDownload onImageDownload) {
        this.mOnImageDownload = onImageDownload;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap img = null;
        try {
            InputStream is = new URL(mUrl).openStream();
            img = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        mImageView.setImageBitmap(bitmap);
        if (mOnImageDownload != null) {
            mOnImageDownload.onDownload(bitmap, mUrl);
        }
    }
}

interface OnImageDownload {
    void onDownload(Bitmap imgBitmap, String url);
}
