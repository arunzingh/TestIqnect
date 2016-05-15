package org.iqnect.testiqnect;

import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private static final String TAG = MainActivityFragment.class.getSimpleName();

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    private LinearLayout mPagerIndicator;

    private ImageView[] dots;

    private BlurImageView mIntroBg;

    private RelativeLayout mPhoneLayout;

    private ImageView mScannerBarUp;

    private ImageView mScannerBarDown;

    private float focus = 0.0f;

    Handler imgBlurHandler = new Handler();

    Animation animScannerUp;
    Animation animScannerDown;
    Animation animScaleup;
    Animation animScaleDown;

    Runnable blurRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "blurRunnable");
            if (focus >= 0.3f) return;

            focus = focus + 0.02f;

            mIntroBg.setFocus(focus);

            imgBlurHandler.postDelayed(blurRunnable, 50);
        }
    };

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mIntroBg = (BlurImageView) view.findViewById(R.id.intro_bg);

        mPhoneLayout = (RelativeLayout) view.findViewById(R.id.containter_phone);

        mScannerBarUp = (ImageView) view.findViewById(R.id.scanner_bar_up);

        mScannerBarDown = (ImageView) view.findViewById(R.id.scanner_bar_down);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        setupPageIndicator(view);

        final Animation animSlideUp = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.slide_up);

        animScannerUp = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.move_up);
        animScannerUp.setAnimationListener(scannerUpListener);

        animScannerDown = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.move_down);
        animScannerDown.setAnimationListener(scannerDownListener);

        animScaleup = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.scale_up);
        animScaleDown = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.scale_down);

        animSlideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "animSlideUp onAnimationEnd");
                mScannerBarDown.startAnimation(animScannerDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPhoneLayout.setVisibility(View.VISIBLE);
                mPhoneLayout.startAnimation(animSlideUp);
                imgBlurHandler.post(blurRunnable);

            }
        }, 1500);

    }

    private Animation.AnimationListener scannerUpListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Log.d(TAG, "scannerUpListener onAnimationEnd");
            mScannerBarUp.setVisibility(View.INVISIBLE);
            mScannerBarDown.startAnimation(animScannerDown);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private Animation.AnimationListener scannerDownListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Log.d(TAG, "scannerDownListener onAnimationEnd");
            mScannerBarDown.setVisibility(View.INVISIBLE);
            mScannerBarUp.startAnimation(animScannerUp);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    @Override
    public void onStart() {
        super.onStart();
        mPager.addOnPageChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPager.removeOnPageChangeListener(this);
    }

    private void setupPageIndicator(View view) {

        mPagerIndicator = (LinearLayout) view.findViewById(R.id.viewPagerIndicator);

        dots = new ImageView[mPagerAdapter.getCount()];

        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.normal_dot, null));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(10, 0, 10, 0);

            mPagerIndicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.selected_dot, null));

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            dots[i].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.normal_dot, null));
        }
        dots[position].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.selected_dot, null));

        if (position == 2) {
            mIntroBg.setVisibility(View.GONE);
        } else {
            mIntroBg.setVisibility(View.VISIBLE);
        }

        if (position == 1) {
            mScannerBarUp.clearAnimation();
            mScannerBarDown.clearAnimation();
            mPhoneLayout.startAnimation(animScaleup);
        } else if (position == 0) {
            mPhoneLayout.startAnimation(animScaleDown);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageOneFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ScreenSlidePageOneFragment();
                case 1:
                    return new ScreenSlidePageTwoFragment();
                case 2:
                    SignupFragment fragment = new SignupFragment();
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        Log.d(TAG, "Setting enter transition");
//                        Slide slide = new Slide();
//                        fragment.setEnterTransition(slide);
//                    }
                    return fragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
