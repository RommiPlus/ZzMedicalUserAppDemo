package com.androidobservablescrollviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

import java.util.ArrayList;

public class ParallexImageListActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    private ObservableListView mListView;
    private ImageView mImageView;
    private Toolbar mToolbarView;
    private View mView;

    private int mParallaxImageHeight;

    private static String TAG = ParallexImageListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallex_image_list);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mImageView = (ImageView) findViewById(R.id.imageview);

        mParallaxImageHeight = getResources().getDimensionPixelOffset(R.dimen.distance_180_dp);

        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setBackgroundColor(
                ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.colorPrimary)));

        mListView = (ObservableListView) findViewById(R.id.observable_list_view);
        mListView.setScrollViewCallbacks(this);

        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            items.add("Item " + i);
        }
        mListView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items));

        // Set padding view for ListView. This is the flexible space.
        View paddingView = new View(this);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                mParallaxImageHeight);
        paddingView.setLayoutParams(lp);
        paddingView.setClickable(true);

        mListView.addHeaderView(paddingView);

        mView = findViewById(R.id.list_background);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        Log.i(TAG, "onScrollChanged, scrollY: " + scrollY);
        mImageView.setTranslationY(- scrollY / 2);

        int baseColor = getResources().getColor(R.color.colorPrimary);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));

        mView.setTranslationY(Math.max(0, -scrollY + mParallaxImageHeight));
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
