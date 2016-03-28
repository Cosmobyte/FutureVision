package com.example.android.futurevision;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements day_goals.HideBottomBarDay,month_goals.HideBottomBarMonth,year_goals.HideBottomBarYear{
    ViewPagerAdapter adapterViewPager;
    Toolbar toolbarBottom;




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
        toolbarBottom = (Toolbar) findViewById(R.id.toolbar_bottom);
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarTop);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
         adapterViewPager = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.fabid);
        button.setBackgroundTintList(getResources().getColorStateList(R.color.VividBlue));
        final EditText message = (EditText) findViewById(R.id.message);
           alarmMethod(getApplicationContext());





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageX = message.getText().toString();
                if (viewPager.getCurrentItem() == 0) {
                    insertDayGoals(messageX);
                    refreshDayGoals();

                } else if (viewPager.getCurrentItem() == 1) {
                    insertMonthGoals(messageX);
                    refreshMonthGoals();
                } else if (viewPager.getCurrentItem() == 2) {
                    insertYearGoals(messageX);
                    refreshYearGoals();
                }

            }
        });



    }


    public void alarmMethod(Context context) {
        Intent intent = new Intent(MainActivity.this, NotifyService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);


            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            cal.set(Calendar.HOUR_OF_DAY,8);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
        Toast.makeText(MainActivity.this, "Start Alarm", Toast.LENGTH_LONG).show();

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
        db.insert("MONTH_GOALS", null, goalsValues);
    }
    private void insertYearGoals(String goal){
        SQLiteOpenHelper futurevisionDatabaseHelper = new FutureVisionDatabaseHelper(MainActivity.this);
        SQLiteDatabase db = futurevisionDatabaseHelper.getWritableDatabase();
        ContentValues goalsValues = new ContentValues();
        goalsValues.put("GOAL",goal);
        db.insert("YEAR_GOALS", null, goalsValues);
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
            case R.id.delete_rows:
                ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
                SQLiteOpenHelper futurevisionDatabaseHelper = new FutureVisionDatabaseHelper(MainActivity.this);
                SQLiteDatabase db = futurevisionDatabaseHelper.getWritableDatabase();
                if(viewPager.getCurrentItem() == 0) {
                    db.delete("DAY_GOALS", null, null);
                    refreshDayGoals();
                }
                if(viewPager.getCurrentItem()==1) {
                    db.delete("MONTH_GOALS", null, null);
                    refreshMonthGoals();
                }
                if(viewPager.getCurrentItem()==2) {
                    db.delete("YEAR_GOALS", null, null);
                    refreshYearGoals();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
//hiding the bottom bar when the user is scrolling
    @Override
    public void hideViews(RecyclerView recyclerView) {
        recyclerView.setOnScrollListener(new HidingScrollListener() {

            @Override
            public void onShow() {
                toolbarBottom.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
            }

            @Override
            public void onHide() {
                toolbarBottom.animate().translationY(toolbarBottom.getHeight()).setInterpolator(new AccelerateInterpolator(2));
            }
        });
    }

    @Override
    public void hideViewsMonth(RecyclerView recyclerView) {
        recyclerView.setOnScrollListener(new HidingScrollListener() {

            @Override
            public void onShow() {
                toolbarBottom.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
            }

            @Override
            public void onHide() {
                toolbarBottom.animate().translationY(toolbarBottom.getHeight()).setInterpolator(new AccelerateInterpolator(2));
            }
        });

    }

    @Override
    public void hideViewsYear(RecyclerView recyclerView) {
        recyclerView.setOnScrollListener(new HidingScrollListener() {

            @Override
            public void onShow() {

                toolbarBottom.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
            }

            @Override
            public void onHide() {
                toolbarBottom.animate().translationY(toolbarBottom.getHeight()).setInterpolator(new AccelerateInterpolator(2));
            }
        });

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