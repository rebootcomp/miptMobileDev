package android.train.mipt_school;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.transition.AutoTransition;
import android.support.transition.ChangeBounds;
import android.support.transition.ChangeImageTransform;
import android.support.transition.ChangeTransform;
import android.support.transition.Fade;
import android.support.transition.Slide;
import android.support.transition.Transition;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.train.mipt_school.Items.ScheduleItem;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import static android.support.transition.TransitionSet.ORDERING_TOGETHER;

public class MainActivity
        extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {
    SharedPreferences mSP;
    private final String BUNDLE_TITLE_NAME = "ACTIONBAR_TITLE";
    private final long TRANSITITON_DURATION = 200;
    private BottomNavigationView bottomNavigationBar;
    private Toolbar toolbar;

    void setLoadingScreenState(boolean state) {
        // включает/выключает загрузочный экран

        View fragmentContainer = findViewById(R.id.fragment_container);
        ProgressBar progressBar = findViewById(R.id.main_activity_progressbar);
        if (state == true) {
            fragmentContainer.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            fragmentContainer.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_activity_toolbar);
        bottomNavigationBar = findViewById(R.id.bottom_bar);

        setSupportActionBar(toolbar);

        bottomNavigationBar.setOnNavigationItemSelectedListener(this);
        mSP = getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = mSP.edit();
        ed.putString("signed", "true");
        ed.commit();
        if (savedInstanceState == null) {
            bottomNavigationBar.setSelectedItemId(R.id.navigation_main);
        }

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null) {
                    Fragment currentSceneFragment = getSupportFragmentManager()
                            .findFragmentById(R.id.fragment_container);
                    currentSceneFragment.onResume();
                }
            }
        });
    }

    public boolean loadFragment(Fragment fragment, View... sharedElements) {
        if (fragment == null) return false;

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        Fragment currentSceneFragment = getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);

        if (currentSceneFragment == null || !currentSceneFragment
                .getClass()
                .isInstance(fragment)) {
            fragment.setEnterTransition(new Slide(Gravity.RIGHT)
                    .setDuration(TRANSITITON_DURATION)
                    .setInterpolator(new LinearOutSlowInInterpolator())); // TODO: починить transitions при перевороте экрана


            ft.setReorderingAllowed(true);
            //ft.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out);
            //ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right);
            //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        }


        fragment.setSharedElementEnterTransition(new AutoTransition());

        for (View shared : sharedElements) {
            ft.addSharedElement(shared, shared.getTransitionName());
        }

        ft.addToBackStack(null);
        ft.replace(R.id.fragment_container, fragment).commit();

        getSupportFragmentManager().executePendingTransactions();

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_schedule:
                return loadFragment(SchedulePageFragment.newInstance());
            case R.id.navigation_main:
                return loadFragment(MainPageFragment.newInstance());
            case R.id.navigation_info:
                return loadFragment(RestInfoPageFragment.newInstance());
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (getSupportActionBar().getTitle() != null) {
            outState.putString(BUNDLE_TITLE_NAME, getSupportActionBar().getTitle().toString());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getSupportActionBar().setTitle(savedInstanceState.getString(BUNDLE_TITLE_NAME));
    }

    @Override
    public void onBackPressed() {
        Fragment currentSceneFragment = getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);


        if (currentSceneFragment instanceof SceneFragment) {
            ((SceneFragment) currentSceneFragment).onBackButtonPressed();
        }
    }
}

