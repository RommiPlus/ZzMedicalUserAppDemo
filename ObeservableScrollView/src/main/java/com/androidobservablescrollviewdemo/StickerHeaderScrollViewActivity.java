package com.androidobservablescrollviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class StickerHeaderScrollViewActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    private Toolbar mToolbar;
    private LinearLayout mHeaderView;
    private TextView mTextView;
    private TextView mSticker;
    private ObservableScrollView mObservableScrollView;
    private int mBaseTranslationY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_header_scroll_view);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mTextView = (TextView) findViewById(R.id.text_view);
        mSticker = (TextView) findViewById(R.id.sticker);
        mHeaderView = (LinearLayout) findViewById(R.id.header);

        mObservableScrollView = (ObservableScrollView) findViewById(R.id.observable_scroll_view);
        mObservableScrollView.setScrollViewCallbacks(this);

    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (dragging) {
            int toolbarHeight = mToolbar.getHeight();
//            mHeaderView.setTranslationY(Math.max(-scrollY, -toolbarHeight));

            if (firstScroll) {
                float currentHeaderTranslationY = ViewHelper.getTranslationY(mHeaderView);
                if (-toolbarHeight < currentHeaderTranslationY) {
                    mBaseTranslationY = scrollY;
                }
            }
            float headerTranslationY = ScrollUtils.getFloat(-(scrollY - mBaseTranslationY), -toolbarHeight, 0);
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewHelper.setTranslationY(mHeaderView, headerTranslationY);
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        mBaseTranslationY = 0;

        if (scrollState == ScrollState.UP) {
            int toolbarHeight = mToolbar.getHeight();
            int scrollY = mObservableScrollView.getCurrentScrollY();
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        } else if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else {
            if ((!toolbarIsShown()) && (!toobarIsHidden())) {
                showToolbar();
            }
        }
    }

    private boolean toolbarIsShown() {
        return mHeaderView.getTranslationY() == 0;
    }

    private void hideToolbar() {
        moveToolbar(-mToolbar.getHeight());
    }

    private boolean toobarIsHidden() {
        return mHeaderView.getTranslationY() == -mToolbar.getHeight();
    }

    private void showToolbar() {
        moveToolbar(0);
    }

    private void moveToolbar(int translateY) {
        ViewPropertyAnimator.animate(mHeaderView).cancel();
        ViewPropertyAnimator.animate(mHeaderView).translationY(translateY).setDuration(200).start();
    }

    private int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }
}
