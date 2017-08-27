package com.support.android.designlibdemo.SearchTimetable;

import android.content.Intent;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.View;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.SimpleTimetableFragment;
import com.support.android.designlibdemo.TimetableList.model.ScheduleType;

import java.util.ArrayList;
import java.util.List;

public class SimpleTimeTableListActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    public static final String EXTRA_KEY_MONTH = "MONTH";
    public static final String EXTRA_KEY_DAY = "DAY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_time_table_list);

        Intent intent = getIntent();
        final int month = intent.getIntExtra(EXTRA_KEY_MONTH, 0) + 1;
        final int day = intent.getIntExtra(EXTRA_KEY_DAY, 0);
        final ScheduleType type = ScheduleType.A;

        if (month + day < 2){
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.simple_time_table_title_text, month, day, type.toString()));
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new SimpleTimetableFragment(), "浄水発");
        adapter.addFragment(new SimpleTimetableFragment(), "大学発");
        viewPager.setAdapter(adapter);
    }
}
