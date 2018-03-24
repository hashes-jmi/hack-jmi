package com.abdulwd.lifeline.main;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import com.abdulwd.lifeline.R;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_previous_records:
                RecordsFragment recordsFragment = new RecordsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, recordsFragment).commit();
                return true;
            case R.id.navigation_aadhar:
                PatientFragment patientFragment = new PatientFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, patientFragment).commit();
                return true;
            case R.id.navigation_account:
                ProfileFragment profileFragment = new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, profileFragment).commit();
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_aadhar);
        PatientFragment patientFragment = new PatientFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, patientFragment).commit();
    }

}
