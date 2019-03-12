package android.train.mipt_school;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.train.mipt_school.Items.ScheduleItem;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity
        extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final String SAVED_TAB = "selectedTab";
    private BottomNavigationView bottomNavigationBar;
    private ArrayList<ScheduleItem> sch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationBar = findViewById(R.id.bottom_bar);

        bottomNavigationBar.setOnNavigationItemSelectedListener(this);

        sch = new ArrayList<>();
        sch.add(new ScheduleItem(new Date(319181637), new Date(919182744),
                "Открытая олимпиада школьников по информатике", "Общежитие МФТИ"));

        sch.add(new ScheduleItem(new Date(919181637), new Date(919182744),
                "Открытая олимпиада школьников по информатике", "Общежитие МФТИ"));

        sch.add(new ScheduleItem(new Date(919181637), new Date(919182744),
                "Обед", "ЛК"));

        sch.add(new ScheduleItem(new Date(919181637), new Date(919182744),
                "sDJDJN DJD DJEJ ", "ЛrК"));
        sch.add(new ScheduleItem(new Date(919181637), new Date(919182744),
                "D DJD JD DJJ D ", "RHRHDhsh"));
        sch.add(new ScheduleItem(new Date(919181637), new Date(919182744),
                "DKDKD djdje ", "did"));
        sch.add(new ScheduleItem(new Date(919181637), new Date(919191637),
                "djdjjd jd dkdjdj", "ЛК"));
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
}
