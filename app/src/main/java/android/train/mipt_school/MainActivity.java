package android.train.mipt_school;

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
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.train.mipt_school.Items.ScheduleItem;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import static android.support.transition.TransitionSet.ORDERING_TOGETHER;

public class MainActivity
        extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final String BUNDLE_TITLE_NAME = "ACTIONBAR_TITLE";
    private final long TRANSITION_DURATION = 250;
    private final long FAST_TRANSITITON_DURATION = 200;
    private BottomNavigationView bottomNavigationBar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_activity_toolbar);
        bottomNavigationBar = findViewById(R.id.bottom_bar);

        setSupportActionBar(toolbar);

        bottomNavigationBar.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            bottomNavigationBar.setSelectedItemId(R.id.navigation_main);
        }
    }

    public boolean loadFragment(Fragment fragment, boolean backPressed, View... sharedElements) {
        if (fragment != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


            Fragment currentSceneFragment = getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_container);

            if (currentSceneFragment == null || !currentSceneFragment
                    .getClass()
                    .isInstance(fragment)) {

                if (backPressed) {
                    if (currentSceneFragment != null) {
                        TransitionSet slideRight = new TransitionSet()
                                .addTransition(new Slide(Gravity.RIGHT)
                                        .setDuration(FAST_TRANSITITON_DURATION))
                                .addTransition(new Fade(Fade.MODE_OUT)
                                        .setDuration(FAST_TRANSITITON_DURATION))
                                .setOrdering(ORDERING_TOGETHER);

                        currentSceneFragment.setExitTransition(slideRight);
                    }
                    fragment.setEnterTransition(new AutoTransition()
                            .setDuration(TRANSITION_DURATION));
                } else {
                    if (currentSceneFragment != null) {
                        currentSceneFragment.setExitTransition(new Fade()
                                .setDuration(FAST_TRANSITITON_DURATION));
                    }
                    fragment.setEnterTransition(new Slide(Gravity.RIGHT));
                }
            }


            fragment.setSharedElementEnterTransition(new AutoTransition());

            for (View shared : sharedElements) {
                ft.addSharedElement(shared, shared.getTransitionName());
            }

            ft.replace(R.id.fragment_container, fragment).commit();

            getSupportFragmentManager().executePendingTransactions();

            if (fragment instanceof SceneFragment) {
                getSupportActionBar().setTitle(((SceneFragment) fragment).getTitle());
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        //getSupportActionBar().setTitle(item.getTitle());

        switch (item.getItemId()) {
            case R.id.navigation_schedule:
                return loadFragment(SchedulePageFragment.newInstance(), false);
            case R.id.navigation_main:
                return loadFragment(MainPageFragment.newInstance(), false);
            case R.id.navigation_info:
                return loadFragment(RestInfoPageFragment.newInstance(), false);
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(BUNDLE_TITLE_NAME, getSupportActionBar().getTitle().toString());
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
