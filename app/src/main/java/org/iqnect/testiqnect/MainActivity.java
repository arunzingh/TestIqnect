package org.iqnect.testiqnect;


import android.animation.Animator;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;


public class MainActivity extends AppCompatActivity implements OnFragmentTouched {

    private static final String TAG = MainActivity.class.getSimpleName();

    MainActivityFragment mMainFragment;


    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.postponeEnterTransition(this);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        findViewById(R.id.drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment, new MoreFragment(), MoreFragment.class.getSimpleName())
                        .addToBackStack(MoreFragment.class.getSimpleName())
                        .commit();
            }
        });

//        toolbar.findViewById(R.id.toolbar_title).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NavigationFragment fragment = new NavigationFragment();
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .add(R.id.fragment, fragment, NavigationFragment.class.getSimpleName())
//                        .addToBackStack(NavigationFragment.class.getSimpleName())
//                        .commit();
//            }
//        });

        mMainFragment = new MainActivityFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment, mMainFragment, MainActivityFragment.class.getSimpleName())
                .commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onFragmentTouched(Fragment fragment, float x, float y) {

        if (fragment instanceof MoreFragment) {
            Log.d(TAG, "onFragmentTouched");
            final MoreFragment theFragment = (MoreFragment) fragment;

            Animator unreveal = theFragment.prepareUnrevealAnimator();

            unreveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // remove the fragment only when the animation finishes
                    getSupportFragmentManager().beginTransaction().remove(theFragment).commit();
                    //to prevent flashing the fragment before removing it, execute pending transactions inmediately
                    getSupportFragmentManager().executePendingTransactions();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            unreveal.start();
        }
    }



}
