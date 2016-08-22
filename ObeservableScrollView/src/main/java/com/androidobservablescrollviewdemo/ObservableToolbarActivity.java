package com.androidobservablescrollviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;

public class ObservableToolbarActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    private Toolbar mToolbar;
    private ObservableListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observable_toolbar);

        mListView = (ObservableListView) findViewById(R.id.scrollable);
        mListView.setScrollViewCallbacks(this);

        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            items.add("Item " + i);
        }
        mListView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items));

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.UP) {
            if (toolbarIsShown()) { // TODO Not implemented
                hideToolbar(); // TODO Not implemented
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (toolbarIsHidden()) { // TODO Not implemented
                showToolbar(); // TODO Not implemented
            }
        }
    }

    private boolean toolbarIsShown() {
        return mToolbar.getTranslationY() == 0;
    }

    private boolean toolbarIsHidden() {
        return mToolbar.getTranslationY() == - mToolbar.getHeight();
    }

    private void showToolbar() {
        moveToolbar(0);
    }

    private void hideToolbar() {
        moveToolbar(-mToolbar.getHeight());
    }

    private void moveToolbar(float toTranslationY) {
        ValueAnimator animator = ValueAnimator.ofFloat(mToolbar.getTranslationY(), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();

                mToolbar.setTranslationY(translationY);
                mListView.setTranslationY(translationY);

                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ((View) mListView).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                ((View) mListView).requestLayout();
            }
        });
        animator.start();
    }

    private int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }
}
