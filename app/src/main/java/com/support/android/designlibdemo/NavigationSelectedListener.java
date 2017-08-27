package com.support.android.designlibdemo;


import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.support.android.designlibdemo.SearchTimetable.DatePickerManager;

/**
 * Created by ragro on 2017/08/27.
 */

public class NavigationSelectedListener implements NavigationView.OnNavigationItemSelectedListener {
    private Context mContext;
    private FragmentManager mFragmentManager;
    private ActivityType mActivityType;
    private DrawerLayout mDrawer;
    public NavigationSelectedListener(Context context, FragmentManager fragmentManager, ActivityType type, DrawerLayout drawer){
        mContext = context;
        mFragmentManager = fragmentManager;
        mActivityType = type;
        mDrawer = drawer;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_timetable:
                if (mActivityType != ActivityType.MAIN){
                    Intent intent = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(intent);
                }
                break;
            case R.id.nav_search:
                DatePickerManager datePickerManager = new DatePickerManager(mContext);
                datePickerManager.show(mFragmentManager);
                break;
            case R.id.nav_setting:
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public enum ActivityType{MAIN, SEARCH, SETTING}
}
