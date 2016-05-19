package org.iqnect.testiqnect;


import android.support.v4.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import org.iqnect.testiqnect.MainActivityFragment.ScreenSlidePagerAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    MainActivityFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.postponeEnterTransition(this);
        setContentView(R.layout.activity_main);

        mMainFragment = new MainActivityFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment, mMainFragment, MainActivityFragment.class.getSimpleName())
                .commit();
    }


    public void onEmailSignUpClick(View vw) {


        ScreenSlidePagerAdapter adapter = (ScreenSlidePagerAdapter) mMainFragment.getViewPager().getAdapter();

        SignupFragment signupFragment = (SignupFragment) adapter.getFragment(2);

        if (signupFragment.checkSignUpFieldsValidity()) {
            SignupFragmentLast signupLast = new SignupFragmentLast();
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right)
                    .add(R.id.fragment, signupLast, MainActivityFragment.class.getSimpleName())
                    .addToBackStack(MainActivityFragment.class.getSimpleName())
                    .commit();
        }
    }
}
