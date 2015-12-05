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

import com.emilflach.cobot.ViewControllers.Adapters.CoffeeRVAdapter;
import com.emilflach.cobot.ViewControllers.Fragments.CoffeesFragment;
import com.emilflach.cobot.ViewControllers.Fragments.LoginFragment;
import com.emilflach.cobot.ViewControllers.Fragments.OrdersFragment;
import com.emilflach.cobot.ViewControllers.Fragments.SettingsFragment;


public class CobotMain extends AppCompatActivity {

    private ViewPager mViewPager;

    public final static int milkScale = 100;
    public static int id = 0; //Current user id
    public static int currentOrderId = 0; //Current order id
    public static String email = null; //Email of logged in user
    public static String password = null; //Email of logged in user
    public static int cardHeight = 0; //Height of card, set at first run
    public static CoffeeRVAdapter.CoffeeViewHolder theCard = null; //Card to get the height from
    public static OrdersFragment ordersFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cobot_main);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(2);
        setAdapter(1);
    }


    /**
     * Sets adapter, can be used to refresh data
     */
    public void setAdapter(int location) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        id = preferences.getInt("id", 0);
        email = preferences.getString("email", null);
        password = preferences.getString("password", null);

        Log.d("id", String.valueOf(id));
        Log.d("email", String.valueOf(email));
        Log.d("password", String.valueOf(password));

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(location, true);
    }

    /**
     * Responds the corresponding integer of a drawable from coffee type
     * @param i type of coffee
     * @return corresponding image
     */
    public static int coffeeImage(int i) {
        switch (i) {
            case 0: //Black
                return R.drawable.black;
//            case 1: //Cappuccino
//                return R.drawable.black;
//            case 2: //Espresso
//                return R.drawable.black;
//            case 3: //Cafe au Lait
//                return R.drawable.black;
//            case 4: //Wiener Melange
//                return R.drawable.black;
//            case 5: //Double Espresso
//                return R.drawable.black;
//            case 6: //Cafe Mocca
//                return R.drawable.black;
//            case 7: //Cafe Macchiato
//                return R.drawable.black;
//            case 8: //Espresso choc
//                return R.drawable.black;
//            case 9://Hot Chocolate
//                return R.drawable.black;
            default:
                return R.drawable.black;
        }
    }

    public static String statusMessage(int i) {
        switch (i) {
            case -1:
                return "No current order";
            case 0: //I have no shit yet for this
                return "Order added to queue";
//            case 1: //Cappuccino
//                return R.drawable.black;
//            case 2: //Espresso
//                return R.drawable.black;
//            case 3: //Cafe au Lait
//                return R.drawable.black;
//            case 4: //Wiener Melange
//                return R.drawable.black;
//            case 5: //Double Espresso
//                return R.drawable.black;
//            case 6: //Cafe Mocca
//                return R.drawable.black;
//            case 7: //Cafe Macchiato
//                return R.drawable.black;
//            case 8: //Espresso choc
//                return R.drawable.black;
//            case 9://Hot Chocolate
//                return R.drawable.black;
            default:
                return "No current order";
        }
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
                        return SettingsFragment.newInstance(position + 1);
                    case 1:
                        return CoffeesFragment.newInstance(position + 1);
                    case 2:
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
                return 3;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(id == 0) {
                return "Login";
            } else {
                switch (position) {
                    case 0:
                        return "Settings";
                    case 1:
                        return "Coffees";
                    case 2:
                        return "Order";
                }
                return null;
            }
        }
    }




}