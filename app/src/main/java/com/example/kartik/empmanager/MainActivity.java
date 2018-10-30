package com.example.kartik.empmanager;

import android.support.v4.app.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new TodayAttendanceFragment1();
                    break;
                case R.id.navigation_dashboard:
                    fragment = new AttendanceFragment();
                    break;
                case R.id.navigation_notifications:
                    fragment = new EmployeeFragment();
                    break;
                case R.id.navigation_addemp:
                    fragment = new AddEmployeeFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , fragment).commit();
            return true;
        }
        return false;
    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setSelectedItemId(R.id.navigation_addemp);

    }
}













