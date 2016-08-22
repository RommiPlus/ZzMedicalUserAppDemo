package com.androidobservablescrollviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

public class ParallexImageActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    private ObservableScrollView mObservableScrollView;
    private ImageView mImageView;
    private Toolbar mToolbarView;

    private int mParallaxImageHeight;

    private static String TAG = ParallexImageActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallex_image);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mObservableScrollView = (ObservableScrollView) findViewById(R.id.observable_scroll_view);
        mObservableScrollView.setScrollViewCallbacks(this);

        mImageView = (ImageView) findViewById(R.id.imageview);

        mParallaxImageHeight = getResources().getDimensionPixelOffset(R.dimen.distance_180_dp);

        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setBackgroundColor(
                ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.colorPrimary)));
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        Log.i(TAG, "onScrollChanged, scrollY: " + scrollY);
        mImageView.setTranslationY(scrollY / 2);

        int baseColor = getResources().getColor(R.color.colorPrimary);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mObservableScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
