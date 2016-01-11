package com.ncuculova.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ncuculova on 11.1.16.
 */
public class ImageAdapter extends BaseAdapter {

    static final String TAG = "ImageAdapter";

    List<String> imageUrls;
    Context mContext;
    private LayoutInflater mInflater;
    private LruCache<String, Bitmap> mMemoryCache;

    public ImageAdapter(Context context) {
        imageUrls = new ArrayList<>();
        mContext = context;
        mInflater = LayoutInflater.from(context);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void loadBitmap(String url, ImageView imageView) {
        final Bitmap bitmap = getBitmapFromMemCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Log.d(TAG, String.format("Loading %s from cache", url));
        } else {
            Log.d(TAG, String.format("Loading %s from web", url));
            imageView.setImageResource(R.drawable.loading);
            DownloadImagesTask task = new DownloadImagesTask(url, imageView);
            task.setOnImageDownload(mOnImageDownload);
            task.execute();
        }
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ImageHolder {
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String url = imageUrls.get(position);
        ImageHolder imageHolder = null;
        if (convertView == null) {
            imageHolder = new ImageHolder();
            convertView = mInflater.inflate(R.layout.image_list_item, null);
            imageHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(imageHolder);
        } else {
            imageHolder = (ImageHolder) convertView.getTag();
        }
        loadBitmap(url, imageHolder.imageView);
        return convertView;
    }

    OnImageDownload mOnImageDownload = new OnImageDownload() {
        @Override
        public void onDownload(Bitmap imgBitmap, String url) {
            addBitmapToMemoryCache(url, imgBitmap);
        }
    };
}
