package com.emilflach.cobot;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;



    public static int id = 0; //Current user id
    public static int currentOrderId = 0; //Current order id
    public static String email = null; //Email of logged in user
    public static String password = null; //Email of logged in user
    public static int cardHeight = 0; //Height of card, set at first run
    public static RVAdapter.CoffeeViewHolder theCard = null; //Card to get the height from

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.container);
        setAdapter();
    }


    /**
     * Sets adapter, can be used to refresh data
     */
    public void setAdapter() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        id = preferences.getInt("id", 0);
        email = preferences.getString("email", null);
        password = preferences.getString("password", null);

        Log.d("id", String.valueOf(id));
        Log.d("email", String.valueOf(email));
        Log.d("password", String.valueOf(password));

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }



    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(id == 0) {
                return LoginFragment.newInstance(position + 1);
            } else {
                switch (position) {
                    case 0:
                        return CoffeesFragment.newInstance(position + 1);
                    case 1:
                        return OrdersFragment.newInstance(position + 1);
                    default:
                        return null;
                }
            }
        }

        @Override
        public int getCount() {
            if(id == 0) {
                return 1;
            } else {
                return 2;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(id == 0) {
                return "Login";
            } else {
                switch (position) {
                    case 0:
                        return "Coffees";
                    case 1:
                        return "Orders";
                }
                return null;
            }
        }
    }




}