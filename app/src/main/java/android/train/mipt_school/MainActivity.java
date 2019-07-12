package android.train.mipt_school;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
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
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.train.mipt_school.Items.ScheduleItem;
import android.train.mipt_school.Tools.AsyncLoadCallback;
import android.train.mipt_school.Tools.AsyncLoadingFragment;
import android.train.mipt_school.Tools.DataSavingFragment;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import static android.support.transition.TransitionSet.ORDERING_TOGETHER;

public class MainActivity
        extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final String BUNDLE_TITLE_NAME = "ACTIONBAR_TITLE";
    private final long TRANSITITON_DURATION = 250;

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

    private Fragment getCurrentSceneFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_activity_toolbar);
        bottomNavigationBar = findViewById(R.id.bottom_bar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Fragment currentSceneFragment = getCurrentSceneFragment();

        if (currentSceneFragment != null && currentSceneFragment instanceof SceneFragment) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        bottomNavigationBar.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            loadFragment(MainPageFragment.newInstance());
        }

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null) {
                    Fragment currentSceneFragment = getCurrentSceneFragment();
                    currentSceneFragment.onResume();
                }
            }
        });
    }

    public void openWebLink(String link) {
        CustomTabsIntent intent = new CustomTabsIntent.Builder().build();

        intent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.launchUrl(getApplicationContext(), Uri.parse(link));
    }

    public boolean loadFragment(final Fragment fragment, final View... sharedElements) {
        if (fragment == null) return false;

        final Fragment currentSceneFragment = getCurrentSceneFragment();

        if (currentSceneFragment != null
                && currentSceneFragment.getClass().isInstance(fragment)) {
            return false;
        }

        setLoadingScreenState(true);

        final Handler mHandler = new Handler();
        final Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowCustomEnabled(false);
                getSupportActionBar().setDisplayShowTitleEnabled(true);

                TransitionSet enterTransition = new TransitionSet()
                        .addTransition(new Fade(Fade.IN).setStartDelay(80)
                                .setInterpolator(new FastOutSlowInInterpolator()))
                        .addTransition(new Slide(Gravity.RIGHT))
                        .setDuration(TRANSITITON_DURATION);

                fragment.setEnterTransition(enterTransition); // TODO: починить transitions при перевороте экрана

                ft.setReorderingAllowed(true);
                //ft.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out);
                //ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right);
                //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);


                fragment.setSharedElementEnterTransition(new AutoTransition());

                for (View shared : sharedElements) {
                    ft.addSharedElement(shared, shared.getTransitionName());
                }

                ft.addToBackStack(null);
                ft.replace(R.id.fragment_container, fragment).commit();

                getSupportFragmentManager().executePendingTransactions();

                setLoadingScreenState(false);
            }

        };

        if (fragment instanceof AsyncLoadingFragment) {
            if (((AsyncLoadingFragment) fragment).fragmentDataLoaded()) {
                mHandler.post(mPendingRunnable);
            } else {
                ((AsyncLoadingFragment) fragment).setLoadCallback(new AsyncLoadCallback() {
                    @Override
                    public void onDataLoaded() {
                        mHandler.post(mPendingRunnable);
                    }
                });
            }
        } else {
            mHandler.post(mPendingRunnable);
        }

        return true;
    }


    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public BottomNavigationView getBottomNavigationBar() {
        return bottomNavigationBar;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment currentSceneFragment = getCurrentSceneFragment();

        if (currentSceneFragment instanceof DataSavingFragment &&
                !((DataSavingFragment) currentSceneFragment).canSwitch()) {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);
            builder.setTitle(Html.fromHtml("<b>Вы изменили настройки</b>"))
                    .setMessage("Сначала сохраните изменения")
                    .setPositiveButton("ОК", null);
            builder.show();

            return false;
        }

        switch (item.getItemId()) {
            case R.id.navigation_schedule:
                return loadFragment(SchedulePageFragment.newInstance());
            case R.id.navigation_main:
                return loadFragment(MainPageFragment.newInstance());
            case R.id.navigation_info:
                return loadFragment(RestInfoPageFragment.newInstance());
            case R.id.navigation_group:
                return loadFragment(GroupViewFragment.newInstance(0));
        }

        return false;
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
        Fragment currentSceneFragment = getCurrentSceneFragment();

        if (currentSceneFragment instanceof SceneFragment) {
            ((SceneFragment) currentSceneFragment).onBackButtonPressed();
        }
    }


}
