package com.modulus.ssc.view;

import com.modulus.ssc.R;

import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.view.View;

public class MainActivity extends FragmentActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

            case 0: return FirstFragment.newInstance();
            case 1: return SecondFragment.newInstance();
            case 2: return ThirdFragment.newInstance();
            default: return FirstFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }       
    }
    
    
 
}
