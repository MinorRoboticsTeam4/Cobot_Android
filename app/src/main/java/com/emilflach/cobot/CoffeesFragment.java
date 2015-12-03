package com.emilflach.cobot;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Emil on 2015-11-07.
 */
public class CoffeesFragment extends Fragment implements View.OnClickListener{


    private RecyclerView rv;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public CoffeesFragment() {
    }

    public static CoffeesFragment newInstance(int sectionNumber) {
        CoffeesFragment fragment = new CoffeesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.coffees_fragment, container, false);

        //Add click listener to logout button
        ImageButton l = (ImageButton) v.findViewById(R.id.logoutButton);
        l.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Set an empty adapter on initialization after which data gets retrieved
        rv = (RecyclerView) getView().findViewById(R.id.rv);
        if (rv != null) {
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(llm);
            rv.setHasFixedSize(true);

            List<Product> products = new ArrayList<>();

            MainActivity mainActivity = (MainActivity) getActivity();
            RVAdapter adapter = new RVAdapter(products, mainActivity);
            rv.setAdapter(adapter);

            initializeData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logoutButton:
                logout();
                break;
        }
    }


    /**
     * Gets the products attached to user and sets the adapter with this data
     */
    private void initializeData() {
        ServiceGenerator.UserClient userClient = ServiceGenerator.createService(ServiceGenerator.UserClient.class);
        Call<List<Product>> call = userClient.products(MainActivity.id);
        call.enqueue(new Callback<List<Product>>() {

            private List<Product> products = new ArrayList<>();

            @Override
            public void onResponse(Response<List<Product>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    for (Product product : response.body()) {
                        this.products.add(product);
                    }
                    MainActivity mainActivity = (MainActivity) getActivity();
                    RVAdapter adapter = new RVAdapter(products, mainActivity);
                    rv.setAdapter(adapter);
                } else {
                    ApiError error = ErrorUtils.parseError(response, retrofit);
                    Log.d("error message", error.message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }


    /**
     * Logs out user by removing credentials from sharedpreferences
     */
    public void logout() {
        //TODO: Move this away from sharedpreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("id", 0);
        editor.putString("email", null);
        editor.putString("password", null);
        editor.apply();

        MainActivity main = (MainActivity) getActivity();
        main.setAdapter();
    }

}
