package com.example.android.futurevision;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    ViewPagerAdapter adapterViewPager;



        public void refreshDayGoals() {
            day_goals Day_goals = new day_goals();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.day_goals_layout, Day_goals);
            ft.commit();

        }

        public void refreshMonthGoals() {
            month_goals Month_goals = new month_goals();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.month_goals_layout, Month_goals);
            ft.commit();

        }

        public void refreshYearGoals() {
            year_goals Year_goals = new year_goals();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.year_goals_layout, Year_goals);
            ft.commit();

        }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarTop);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
         adapterViewPager = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.fabid);
        final EditText message = (EditText) findViewById(R.id.message);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageX = message.getText().toString();
                if (viewPager.getCurrentItem()==0){
                    insertDayGoals(messageX);
                    refreshDayGoals();

                }
                else if(viewPager.getCurrentItem()==1){
                    insertMonthGoals(messageX);
                    refreshMonthGoals();
                }
                else if(viewPager.getCurrentItem()==2)
                {
                    insertYearGoals(messageX);
                    refreshYearGoals();
                }

            }
        });



    }
    private void insertDayGoals(String goal){
        SQLiteOpenHelper futurevisionDatabaseHelper = new FutureVisionDatabaseHelper(MainActivity.this);
        SQLiteDatabase db = futurevisionDatabaseHelper.getWritableDatabase();
        ContentValues goalsValues = new ContentValues();
        goalsValues.put("GOAL",goal);
        db.insert("DAY_GOALS", null, goalsValues);
    }
    private void insertMonthGoals(String goal){
        SQLiteOpenHelper futurevisionDatabaseHelper = new FutureVisionDatabaseHelper(MainActivity.this);
        SQLiteDatabase db = futurevisionDatabaseHelper.getWritableDatabase();
        ContentValues goalsValues = new ContentValues();
        goalsValues.put("GOAL",goal);
        db.insert("MONTH_GOALS",null,goalsValues);
    }
    private void insertYearGoals(String goal){
        SQLiteOpenHelper futurevisionDatabaseHelper = new FutureVisionDatabaseHelper(MainActivity.this);
        SQLiteDatabase db = futurevisionDatabaseHelper.getWritableDatabase();
        ContentValues goalsValues = new ContentValues();
        goalsValues.put("GOAL",goal);
        db.insert("YEAR_GOALS",null,goalsValues);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id){
            case R.id.action_help:
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


        class ViewPagerAdapter extends SmartFragmentStatePagerAdapter{
            private int NUM_ITEMS = 3;
            public ViewPagerAdapter(FragmentManager fragmentManager) {
                super(fragmentManager);
            }

            // Returns total number of pages
            @Override
            public int getCount() {
                return NUM_ITEMS;
            }
            // Returns the fragment to display for that page
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return day_goals.newInstance();
                    case 1:
                        return month_goals.newInstance();
                    case 2:
                        return year_goals.newInstance();
                    default:
                        return null;
                }
            }

            // Returns the page title for the top indicator
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return "Day goals";
                    case 1:
                        return "Month";
                    case 2:
                        return "Year";
                    default:
                        return null;
                }
            }
            }
    }