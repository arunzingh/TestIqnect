package org.iqnect.testiqnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.Map;

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
    private ImageView mIntroBg;
    private ImageView mIntroBgBlur;
    private RelativeLayout mPhoneLayout;
    private RelativeLayout mCoverLayout;
    private ImageView mScannerBarUp;
    private ImageView mScannerBarDown;
    private ImageView imgPhoneBg;
    private ImageView imgPayload;

    private float radius = 1.0f;

    private Animation animRotateAndSlideUp;
    private Animation animScannerUp;
    private Animation animScannerDown;
    private Animation animFadeIn;
    private Animation animFadeOut;
    private Animation animPayloadSlideUp;
    private Animation animPayloadSlideDown;
    private Animation animSlideOut;
    private Animation animSlideIn;

    private Bitmap bmpIntroMagazine;

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


        mIntroBg = (ImageView) view.findViewById(R.id.intro_bg);
        mIntroBgBlur = (ImageView) view.findViewById(R.id.intro_bg_blur);
        imgPhoneBg = (ImageView) view.findViewById(R.id.intro_img_phone_overlay);
        mPhoneLayout = (RelativeLayout) view.findViewById(R.id.containter_phone);
        mCoverLayout = (RelativeLayout) view.findViewById(R.id.intro_content);
        mScannerBarUp = (ImageView) view.findViewById(R.id.scanner_bar_up);
        mScannerBarDown = (ImageView) view.findViewById(R.id.scanner_bar_down);
        imgPayload = (ImageView) view.findViewById(R.id.img_intro_payload);

        bmpIntroMagazine = BitmapFactory.decodeResource(getResources(), R.drawable.intro_magazine);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(false, new PageFadeTransformer());
        setupPageIndicator(view);

    }


    private void startScanningAnimation() {
        Log.d(TAG, "startScanningAnimation");
        if (mPager.getCurrentItem() == 0) {
            mScannerBarDown.setVisibility(View.VISIBLE);

            animScannerUp.setAnimationListener(scannerUpListener);
            animScannerDown.setAnimationListener(scannerDownListener);
            mScannerBarDown.startAnimation(animScannerDown);
        }

    }

    private void stopScanningAnimation() {
        mScannerBarUp.setVisibility(View.INVISIBLE);
        mScannerBarDown.setVisibility(View.INVISIBLE);
        mScannerBarUp.clearAnimation();
        mScannerBarDown.clearAnimation();
        animScannerUp.setAnimationListener(null);
        animScannerDown.setAnimationListener(null);
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

        animRotateAndSlideUp = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.phone_rotate_slide_up);

        animScannerUp = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.scanner_move_up);
        animScannerDown = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.scanner_move_down);
        animFadeIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.phone_bg_fade_in);
        animFadeOut = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.phone_bg_fade_out);
        animPayloadSlideUp = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.payload_slide_up);
        animPayloadSlideDown = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.payload_slide_down);
        animSlideOut = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_out);
        animSlideIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_in);

        animSlideOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCoverLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animPayloadSlideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "animPayloadSlideDown onAnimationEnd");
                imgPayload.setVisibility(View.INVISIBLE);
                startScanningAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animRotateAndSlideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "animRotateAndSlideUp onAnimationEnd");
                startScanningAnimation();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPhoneLayout.setVisibility(View.VISIBLE);
                mPhoneLayout.startAnimation(animRotateAndSlideUp);
                mIntroBgBlur.setVisibility(View.VISIBLE);
                mIntroBgBlur.startAnimation(animFadeIn);
            }
        }, 1500);

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
        Log.d(TAG, "onPageScrolled:position= " + position);

        if (position == 0) {
            stopScanningAnimation();
        }
    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected:position= " + position);
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            dots[i].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.normal_dot, null));
        }
        dots[position].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.selected_dot, null));

        if (position == 2) {
            mCoverLayout.startAnimation(animSlideOut);
        } else {
            if (mCoverLayout.getVisibility() != View.VISIBLE || (animSlideOut.hasStarted() && !animSlideOut.hasEnded())) {
                mCoverLayout.startAnimation(animSlideIn);
                mCoverLayout.setVisibility(View.VISIBLE);
            }
        }

        if (position == 1) {
            Log.d(TAG, "started=" + animRotateAndSlideUp.hasStarted() + " ended=" + animRotateAndSlideUp.hasEnded());
            stopScanningAnimation();
            blurPhoneBackground(true);
            mPhoneLayout.animate().scaleX(1.5f).scaleY(1.5f).setDuration(500).start();
            imgPayload.setVisibility(View.VISIBLE);
            imgPayload.startAnimation(animPayloadSlideUp);
        } else if (position == 0) {
            mPhoneLayout.animate().scaleX(1.0f).scaleY(1.0f).setDuration(500).start();
            blurPhoneBackground(false);
            imgPayload.startAnimation(animPayloadSlideDown);
        }
    }

    private void blurPhoneBackground(boolean isBlur) {
        if (isBlur) {
            imgPhoneBg.setVisibility(View.VISIBLE);
            imgPhoneBg.startAnimation(animFadeIn);
        } else {
            imgPhoneBg.startAnimation(animFadeOut);
            imgPhoneBg.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE && mPager.getCurrentItem() == 0) {
            Log.d(TAG, "onPageScrollStateChanged:state= " + state);
            startScanningAnimation();
        }
    }

    public ViewPager getViewPager() {
        return mPager;
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageOneFragment objects, in
     * sequence.
     */
    public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private Map<Integer, Fragment> mPageReferenceMap = new HashMap<Integer, Fragment>();


        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new ScreenSlidePageOneFragment();
                    break;
                case 1:
                    fragment = new ScreenSlidePageTwoFragment();
                    break;
                case 2:
                    fragment = new SignupFragment();
                    break;
            }
            mPageReferenceMap.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
        }

        public Fragment getFragment(int position) {
            return mPageReferenceMap.get(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
