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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    private ImageView mIntroBg;
    private RelativeLayout mPhoneLayout;
    private RelativeLayout mCoverLayout;
    private ImageView mScannerBarUp;
    private ImageView mScannerBarDown;
    private ImageView imgPhoneBg;
    private ImageView imgPayload;

    private float radius = 1.0f;

    private Handler imgBlurHandler = new Handler();

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

    Animation animScaleDown;

    Runnable blurRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "blurRunnable:radius=" + radius);
            if (radius >= 24) return;

            radius = radius + 2.0f;

            new ImageBlurTask(getActivity(), bmpIntroMagazine).execute(radius);

            imgBlurHandler.postDelayed(blurRunnable, 250);
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


        mIntroBg = (ImageView) view.findViewById(R.id.intro_bg);
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
        animScaleDown = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.scale_down);

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

        EaseSineOutInterpolator easeSineOut = new EaseSineOutInterpolator();

//        animRotateAndSlideUp.setInterpolator(easeSineOut);
//        animPayloadSlideUp.setInterpolator(easeSineOut);

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
                imgBlurHandler.post(blurRunnable);

            }
        }, 1500);

        mPager.addOnPageChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        imgBlurHandler.removeCallbacks(blurRunnable);
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
            Log.d(TAG, "started=" + animRotateAndSlideUp.hasStarted() + " ended="+ animRotateAndSlideUp.hasEnded());
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

    private class ImageBlurTask extends AsyncTask<Float, Void, Bitmap> {

        Context context;
        Bitmap bmpSource;

        public ImageBlurTask(Context context, Bitmap bmp) {
            this.context = context;
            this.bmpSource = bmp;
        }

        @Override
        protected Bitmap doInBackground(Float... floats) {
            Bitmap bitmap = bmpSource.copy(bmpSource.getConfig(), true);

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, bmpSource, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mIntroBg.setImageBitmap(bitmap);
        }
    }

}
