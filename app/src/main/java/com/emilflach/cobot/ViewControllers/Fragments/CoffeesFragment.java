package com.emilflach.cobot.ViewControllers.Fragments;

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

import com.emilflach.cobot.CobotMain;
import com.emilflach.cobot.Models.ApiError;
import com.emilflach.cobot.Models.Product;
import com.emilflach.cobot.R;
import com.emilflach.cobot.ViewControllers.Adapters.CoffeeRVAdapter;
import com.emilflach.cobot.api.ErrorUtils;
import com.emilflach.cobot.api.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * cobot
 * by Emil on 2015-11-07.
 */
public class CoffeesFragment extends Fragment {

    ServiceGenerator.UserClient userClient;
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
        userClient = ServiceGenerator.createService(ServiceGenerator.UserClient.class);
        View v = inflater.inflate(R.layout.coffees_fragment, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Set an empty adapter on initialization after which data gets retrieved
        View v = getView();
        if (v != null) {
            rv = (RecyclerView) v.findViewById(R.id.rv);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(llm);
            rv.setHasFixedSize(true);

            List<Product> products = new ArrayList<>();

            CobotMain cobotMain = (CobotMain) getActivity();
            CoffeeRVAdapter adapter = new CoffeeRVAdapter(products, cobotMain);
            rv.setAdapter(adapter);

            initializeData();
        }
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.logoutButton:
//                logout();
//                break;
//        }
//    }


    /**
     * Gets the products attached to user and sets the adapter with this data
     */
    private void initializeData() {
        Call<List<Product>> call = userClient.products(CobotMain.id);
        call.enqueue(new Callback<List<Product>>() {

            private List<Product> products = new ArrayList<>();

            @Override
            public void onResponse(Response<List<Product>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    for (Product product : response.body()) {
                        this.products.add(product);
                    }
                    CobotMain cobotMain = (CobotMain) getActivity();
                    CoffeeRVAdapter adapter = new CoffeeRVAdapter(products, cobotMain);
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




}
