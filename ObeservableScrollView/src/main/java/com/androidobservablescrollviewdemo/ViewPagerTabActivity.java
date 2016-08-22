package com.androidobservablescrollviewdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;

import fragment.ViewPagerFragment;

public class ViewPagerTabActivity extends AppCompatActivity implements ObservableScrollViewCallbacks, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private ImageView mImageView;

    private CustomViewPagerAdapter mAdapter;
    private View mView;

    private int mScrollY;

    private LinearLayout mTab;
    private int mFlexibleSpaceHeight;
    private int mTabHeight;
    private int mToolbarHeight;
    private int mHeaderHeight;
    private int mScrollHeaderHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_tab);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);

        mImageView = (ImageView) findViewById(R.id.imageview);
        mView = findViewById(R.id.toolbar);

        mView.setBackgroundColor(ScrollUtils.getColorWithAlpha(
                0, getResources().getColor(R.color.colorPrimary)));

        mTab = (LinearLayout) findViewById(R.id.tab);

        mFlexibleSpaceHeight = getResources().getDimensionPixelOffset(R.dimen.distance_180_dp);
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.distance_235dp);
        mTabHeight = getResources().getDimensionPixelOffset(R.dimen.distance_48dp);
        mToolbarHeight = getResources().getDimensionPixelOffset(R.dimen.distance_55dp);

        mScrollHeaderHeight = mHeaderHeight - mToolbarHeight - mTabHeight;

        ScrollUtils.addOnGlobalLayoutListener(mTab, new Runnable() {
            @Override
            public void run() {
                translateTab(0);
            }
        });
    }

    private void translateTab(int scrollY) {

        // Tabs will move between the top of the screen to the bottom of the image.
        float translationY = ScrollUtils.getFloat(-scrollY + mFlexibleSpaceHeight, mTabHeight, mFlexibleSpaceHeight);
        // When Fragments' scroll, translate tabs immediately (without animation).
        ViewHelper.setTranslationY(mTab, translationY);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        mScrollY = scrollY;
        mImageView.setTranslationY(-scrollY / 2);

        int baseColor = getResources().getColor(R.color.colorPrimary);
        float alpha = Math.min(1, (float) scrollY / (mImageView.getHeight() - mTabHeight - mToolbarHeight));
        mView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));

        translateTab(scrollY);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        propagateToolbarState(mScrollY);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void propagateToolbarState(int scrollY) {
        int realScrollY = (int) ScrollUtils.getFloat(scrollY, 0, mScrollHeaderHeight);

        // Set scrollY for the fragments that are not created yet
        mAdapter.setScrollY(realScrollY);

        // Set scrollY for the active fragments
        for (int i = 0; i < mAdapter.getCount(); i++) {
            // Skip current item
            if (i == mViewPager.getCurrentItem()) {
                continue;
            }

            // Skip destroyed or not created item
            ViewPagerFragment f =
                    (ViewPagerFragment) mAdapter.getItemAt(i);
            if (f == null) {
                continue;
            }

            View view = f.getView();
            if (view == null) {
                continue;
            }

            ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);
            int scrollYStatus = scrollView.getScrollY();
            if ((scrollYStatus >= 0) && (scrollYStatus <= mScrollHeaderHeight)) {
                scrollView.scrollTo(0, realScrollY);
            }
        }
    }


    public static class CustomViewPagerAdapter extends CacheFragmentStatePagerAdapter {

        private int mScrollY;

        private static String[] TITLE = {"One", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN"};

        public CustomViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setScrollY(int scrollY) {
            mScrollY = scrollY;
        }

        public Fragment newFragment() {
            return new ViewPagerFragment();
        }

        @Override
        protected Fragment createItem(int position) {
            Fragment fragment = newFragment();
            if (mScrollY >= 0) {
                Bundle bundle = new Bundle();
                bundle.putInt(ViewPagerFragment.SCROLL_Y, mScrollY);
                fragment.setArguments(bundle);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return TITLE.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position];
        }
    }
}
