package org.iqnect.testiqnect;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class ScreenSlidePageTwoFragment extends Fragment {

    private static final String TAG = ScreenSlidePageTwoFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        if (Build.VERSION.SDK_INT  >= Build.VERSION_CODES.LOLLIPOP) {
//            Log.d(TAG, "onCreateView exit Animation init");
//            TransitionInflater transitionInflater = TransitionInflater.from(getActivity());
//            Transition transition = transitionInflater.inflateTransition(R.transition.transition_boarding_pagetwo);
//            this.setExitTransition(transition);
//        }

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page2, container, false);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}