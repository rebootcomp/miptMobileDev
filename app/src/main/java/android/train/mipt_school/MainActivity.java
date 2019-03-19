package android.train.mipt_school;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.train.mipt_school.Items.ScheduleItem;
import android.train.mipt_school.Tools.FragmentTransitionCallback;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity
        extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final String SAVED_TAB = "selectedTab";
    private BottomNavigationView bottomNavigationBar;
    private Toolbar toolbar;
    private ArrayList<ScheduleItem> sch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.main_activity_toolbar);
        bottomNavigationBar = findViewById(R.id.bottom_bar);

        setSupportActionBar(toolbar);

        bottomNavigationBar.setOnNavigationItemSelectedListener(this);

        sch = new ArrayList<>();
        sch.add(new ScheduleItem(new Date(319181637), new Date(919182744),
                "Открытая олимпиада школьников по информатике", "Общежитие МФТИ"));

        sch.add(new ScheduleItem(new Date(919181637), new Date(919182744),
                "Открытая олимпиада школьников по информатике", "Общежитие МФТИ"));

        sch.add(new ScheduleItem(new Date(919181637), new Date(919182744),
                "Обед", "ЛК"));

        sch.add(new ScheduleItem(new Date(919181637), new Date(919182744),
                "Контест", "Актовый зал МФТИ"));
        sch.add(new ScheduleItem(new Date(919181637), new Date(919182744),
                "Завтрак", "Столовая МФТИ"));
        sch.add(new ScheduleItem(new Date(919181637), new Date(919182744),
                "Заезд", "Фойе ЛК"));
        sch.add(new ScheduleItem(new Date(919181637), new Date(919191637),
                "Отъезд", "ЛК"));
        sch.add(new ScheduleItem(new Date(919181637), new Date(929182744),
                "ddjdj", "ЛdscsК"));
        sch.add(new ScheduleItem(new Date(919181637), new Date(939182744),
                "sjs", "ЛcdacК"));
        sch.add(new ScheduleItem(new Date(919181637), new Date(919682744),
                "Обед", "ЛscacК"));
        sch.add(new ScheduleItem(new Date(919181637), new Date(919782744),
                "3duxns", "ЛsacК"));
        sch.add(new ScheduleItem(new Date(919181637), new Date(919582744),
                "Обед", "ЛascК"));
        sch.add(new ScheduleItem(new Date(919181637), new Date(919482744),
                "eiisla.", "asdЛК"));
        sch.add(new ScheduleItem(new Date(919181637), new Date(919282744),
                "kjsjjaJDJDJ", "addЛК"));
        sch.add(new ScheduleItem(new Date(919181637), new Date(914182744),
                "djdjdj", "asdadЛК"));
        sch.add(new ScheduleItem(new Date(919181637), new Date(929182744),
                "dieixjxj", "scЛК"));
        sch.add(new ScheduleItem(new Date(919181637), new Date(913182744),
                "Обеtrthshsд", "Лijsak"));
        sch.add(new ScheduleItem(new Date(919181637), null,
                "Обеtrthshsд", "Лijsak"));
        ScheduleFragment.setData(sch);

        bottomNavigationBar.setSelectedItemId(R.id.navigation_main);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            ft.replace(R.id.fragment_container, fragment).commit();


            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        getSupportActionBar().setTitle(item.getTitle());

        switch (item.getItemId()) {
            case R.id.navigation_schedule:
                return loadFragment(ScheduleFragment.newInstance());
            case R.id.navigation_main:
                return loadFragment(MainPageFragment.newInstance());
            case R.id.navigation_info:
                return loadFragment(RestInfoFragment.newInstance(
                        new FragmentTransitionCallback() {
                            @Override
                            public void onTransition(Fragment nextFragment) {
                                loadFragment(nextFragment);
                            }
                        }));
        }

        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bottomNavigationBar.setSelectedItemId(savedInstanceState.getInt(SAVED_TAB));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_TAB, bottomNavigationBar.getSelectedItemId());
    }

    @Override
    public void onBackPressed() {
    }
}
