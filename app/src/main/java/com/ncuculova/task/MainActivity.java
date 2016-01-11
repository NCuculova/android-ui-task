package com.ncuculova.task;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView mLvImages;
    ImageAdapter mImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Mary Johnson, Andy Smith, +12 others");
        getSupportActionBar().setSubtitle("Duration: 02:09");

        mLvImages = (ListView) findViewById(R.id.lv_images);
        mLvImages.setDivider(null);
        mImageAdapter = new ImageAdapter(this);
        List<String> urls = new ArrayList<>();
        for(int i = 0; i < 20; ++i) {
           urls.add(String.format("http://dummyimage.com/150x150/FF4081/FFF/&text=image_%d", i + 1));
        }
        mImageAdapter.setImageUrls(urls);
        mLvImages.setAdapter(mImageAdapter);
    }

    /**
     * Using single event handler for all buttons. Also we can have separate
     * click handlers for all buttons, but for purpose of this demo, using only one.
     * @param btn The reference of the clicked view
     */
    public void onBtnClick(View btn) {
        Toast.makeText(this, String.format("You clicked button %s", btn.getTag()), Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
